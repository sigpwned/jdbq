/*-
 * =================================LICENSE_START==================================
 * jdbq
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.jdbq.mapper.column;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.config.JdbqConfig;
import com.sigpwned.jdbq.generic.GenericType;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.internal.Optionals;
import com.sigpwned.jdbq.mapper.column.factory.ArrayMapperFactory;
import com.sigpwned.jdbq.mapper.column.factory.BoxedMapperFactory;
import com.sigpwned.jdbq.mapper.column.factory.EnumMapperFactory;
import com.sigpwned.jdbq.mapper.column.factory.EssentialsMapperFactory;
import com.sigpwned.jdbq.mapper.column.factory.InternetMapperFactory;
import com.sigpwned.jdbq.mapper.column.factory.JavaTimeMapperFactory;
import com.sigpwned.jdbq.mapper.column.factory.OptionalMapperFactory;
import com.sigpwned.jdbq.mapper.column.factory.PrimitiveMapperFactory;
import com.sigpwned.jdbq.mapper.row.RowMapper;

/**
 * Configuration registry for {@link ColumnMapperFactory} instances.
 */
public class ColumnMappers implements JdbqConfig<ColumnMappers> {
  private final List<ColumnMapperFactory> factories = new CopyOnWriteArrayList<>();
  private final ConcurrentHashMap<Type, Optional<? extends ColumnMapper<?>>> cache =
      new ConcurrentHashMap<>();

  private boolean coalesceNullPrimitivesToDefaults = true;
  private ConfigRegistry registry;

  public ColumnMappers() {
    register(new JavaTimeMapperFactory());
    register(new InternetMapperFactory());
    register(new EssentialsMapperFactory());
    register(new BoxedMapperFactory());
    register(new PrimitiveMapperFactory());
    register(new OptionalMapperFactory());
    register(new EnumMapperFactory());
    register(new ArrayMapperFactory());
  }

  private ColumnMappers(ColumnMappers that) {
    factories.addAll(that.factories);
    cache.putAll(that.cache);
    coalesceNullPrimitivesToDefaults = that.coalesceNullPrimitivesToDefaults;
  }

  @Override
  public void setRegistry(ConfigRegistry registry) {
    this.registry = registry;
  }

  /**
   * Register a column mapper which will have its parameterized type inspected to determine what it
   * maps to. Column mappers may be reused by {@link RowMapper} to map individual columns.
   * <p>
   * The parameter must be concretely parameterized, we use the type argument T to determine if it
   * applies to a given type.
   *
   * @param mapper the column mapper
   * @return this
   * @throws UnsupportedOperationException if the ColumnMapper is not a concretely parameterized
   *         type
   */
  public ColumnMappers register(ColumnMapper<?> mapper) {
    Type type = GenericTypes.findGenericParameter(mapper.getClass(), ColumnMapper.class)
        .orElseThrow(() -> new UnsupportedOperationException(
            "Must use a concretely typed ColumnMapper here"));
    return this.register(ColumnMapperFactory.of(type, mapper));
  }

  /**
   * Register a column mapper for a given explicit {@link GenericType} Column mappers may be reused
   * by {@link RowMapper} to map individual columns.
   *
   * @param <T> the type
   * @param type the generic type to match with equals.
   * @param mapper the column mapper
   * @return this
   */
  public <T> ColumnMappers register(GenericType<T> type, ColumnMapper<T> mapper) {
    return this.register(ColumnMapperFactory.of(type.getType(), mapper));
  }

  /**
   * Register a column mapper for a given explicit {@link Type} Column mappers may be reused by
   * {@link RowMapper} to map individual columns.
   *
   * @param type the type to match with equals.
   * @param mapper the column mapper
   * @return this
   */
  public ColumnMappers register(Type type, ColumnMapper<?> mapper) {
    return this.register(ColumnMapperFactory.of(type, mapper));
  }

  /**
   * Register a column mapper factory.
   * <p>
   * Column mappers may be reused by {@link RowMapper} to map individual columns.
   *
   * @param factory the column mapper factory
   * @return this
   */
  public ColumnMappers register(ColumnMapperFactory factory) {
    factories.add(0, factory);
    cache.clear();
    return this;
  }

  /**
   * Obtain a column mapper for the given type.
   *
   * @param <T> the type to map
   * @param type the target type to map to
   * @return a ColumnMapper for the given type, or empty if no column mapper is registered for the
   *         given type.
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<ColumnMapper<T>> findFor(Class<T> type) {
    ColumnMapper<T> mapper = (ColumnMapper<T>) findFor((Type) type).orElse(null);
    return Optional.ofNullable(mapper);
  }

  /**
   * Obtain a column mapper for the given type.
   *
   * @param <T> the type to map
   * @param type the target type to map to
   * @return a ColumnMapper for the given type, or empty if no column mapper is registered for the
   *         given type.
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<ColumnMapper<T>> findFor(GenericType<T> type) {
    ColumnMapper<T> mapper = (ColumnMapper<T>) findFor(type.getType()).orElse(null);
    return Optional.ofNullable(mapper);
  }

  /**
   * Obtain a column mapper for the given type.
   *
   * @param type the target type to map to
   * @return a ColumnMapper for the given type, or empty if no column mapper is registered for the
   *         given type.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Optional<ColumnMapper<?>> findFor(Type type) {
    // ConcurrentHashMap can enter an infinite loop on nested computeIfAbsent calls.
    // Since column mappers can decorate other column mappers, we have to populate the cache the old
    // fashioned way.
    // See https://bugs.openjdk.java.net/browse/JDK-8062841,
    // https://bugs.openjdk.java.net/browse/JDK-8142175
    Optional<ColumnMapper<?>> cached = (Optional) cache.get(type);

    if (cached != null) {
      return cached;
    }

    Optional<ColumnMapper<?>> mapper = (Optional) factories.stream()
        .flatMap(factory -> Optionals.stream(factory.build(type, registry))).findFirst();

    mapper.ifPresent(m -> m.init(registry));

    cache.put(type, mapper);

    return mapper;
  }

  /**
   * Returns true if database {@code null} values should be transformed to the default value for
   * primitives.
   *
   * @return {@code true} if database {@code null}s should translate to the Java defaults for
   *         primitives, or throw an exception otherwise.
   *
   *         Default value is true: nulls will be coalesced to defaults.
   */
  public boolean getCoalesceNullPrimitivesToDefaults() {
    return coalesceNullPrimitivesToDefaults;
  }

  public void setCoalesceNullPrimitivesToDefaults(boolean coalesceNullPrimitivesToDefaults) {
    this.coalesceNullPrimitivesToDefaults = coalesceNullPrimitivesToDefaults;
  }

  @Override
  public ColumnMappers createCopy() {
    return new ColumnMappers(this);
  }
}

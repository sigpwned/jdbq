/*-
 * =================================LICENSE_START==================================
 * jdbq
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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
/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sigpwned.jdbq.mapper.column.factory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import com.google.cloud.bigquery.FieldValue;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.NoSuchMapperException;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;
import com.sigpwned.jdbq.mapper.column.ColumnMappers;

/**
 * Column mapper factory which knows how to map Optionals:
 * <ul>
 * <li>{@link Optional}</li>
 * <li>{@link OptionalInt}</li>
 * <li>{@link OptionalLong}</li>
 * <li>{@link OptionalDouble}</li>
 * </ul>
 */
public class OptionalMapperFactory implements ColumnMapperFactory {
  private static final Map<Class<?>, BiFunction<Type, ConfigRegistry, ColumnMapper<?>>> STRATEGIES;

  static {
    Map<Class<?>, BiFunction<Type, ConfigRegistry, ColumnMapper<?>>> s = new HashMap<>();

    s.put(Optional.class, OptionalMapperFactory::create);
    s.put(OptionalInt.class,
        singleton(create(v -> (int) v.getLongValue(), OptionalInt::empty, OptionalInt::of)));
    s.put(OptionalLong.class,
        singleton(create(v -> v.getLongValue(), OptionalLong::empty, OptionalLong::of)));
    s.put(OptionalDouble.class,
        singleton(create(v -> v.getDoubleValue(), OptionalDouble::empty, OptionalDouble::of)));

    STRATEGIES = Collections.unmodifiableMap(s);
  }

  @Override
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    return Optional.ofNullable(STRATEGIES.get(GenericTypes.getErasedType(type)))
        .map(strategy -> strategy.apply(type, config));
  }

  static BiFunction<Type, ConfigRegistry, ColumnMapper<?>> singleton(ColumnMapper<?> instance) {
    return (t, c) -> instance;
  }

  static <Opt, Box> ColumnMapper<?> create(Function<FieldValue, Box> getter, Supplier<Opt> empty,
      Function<Box, Opt> present) {
    return (v, ctx) -> v.isNull() ? empty.get() : present.apply(getter.apply(v));
  }

  private static ColumnMapper<?> create(Type type, ConfigRegistry config) {
    final ColumnMapper<?> mapper = config.get(ColumnMappers.class)
        .findFor(GenericTypes.findGenericParameter(type, Optional.class)
            .orElseThrow(() -> new NoSuchMapperException("No mapper for raw Optional type")))
        .orElseThrow(
            () -> new NoSuchMapperException("No mapper for type " + type + " nested in Optional"));
    return (v, ctx) -> (Optional<?>) Optional.ofNullable(mapper.map(v, ctx));
  }
}

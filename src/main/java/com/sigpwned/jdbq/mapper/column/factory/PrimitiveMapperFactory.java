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
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.function.Function;
import com.google.cloud.bigquery.FieldValue;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;
import com.sigpwned.jdbq.mapper.column.ColumnMappers;
import com.sigpwned.jdbq.result.UnableToProduceResultException;

/**
 * Column mapper factory which knows how to map java primitives:
 * <ul>
 * <li>{@code boolean}</li>
 * <li>{@code byte}</li>
 * <li>{@code char}</li>
 * <li>{@code short}</li>
 * <li>{@code int}</li>
 * <li>{@code long}</li>
 * <li>{@code float}</li>
 * <li>{@code double}</li>
 * </ul>
 */
public class PrimitiveMapperFactory implements ColumnMapperFactory {
  private final IdentityHashMap<Class<?>, ColumnMapper<?>> mappers = new IdentityHashMap<>();

  public PrimitiveMapperFactory() {
    mappers.put(boolean.class,
        primitiveMapper(boolean.class, v -> v.isNull() ? false : v.getBooleanValue()));
    mappers.put(byte.class,
        primitiveMapper(byte.class, v -> (byte) (v.isNull() ? 0L : v.getLongValue())));
    mappers.put(char.class, primitiveMapper(char.class,
        v -> (char) Optional.ofNullable(v.getStringValue()).map(s -> s.charAt(0)).orElse('\0')));
    mappers.put(short.class,
        primitiveMapper(short.class, v -> (short) (v.isNull() ? 0L : v.getLongValue())));
    mappers.put(int.class,
        primitiveMapper(int.class, v -> (int) (v.isNull() ? 0L : v.getLongValue())));
    mappers.put(long.class, primitiveMapper(long.class, v -> v.isNull() ? 0L : v.getLongValue()));
    mappers.put(float.class,
        primitiveMapper(float.class, v -> (float) (v.isNull() ? 0.0 : v.getDoubleValue())));
    mappers.put(double.class,
        primitiveMapper(double.class, v -> v.isNull() ? 0.0 : v.getDoubleValue()));
  }

  @Override
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    Class<?> rawType = GenericTypes.getErasedType(type);
    return Optional.ofNullable(mappers.get(rawType));
  }

  private static <T> ColumnMapper<T> primitiveMapper(Class<T> clazz,
      Function<FieldValue, T> getter) {
    return (v, ctx) -> {
      if (v.isNull() && !ctx.getConfig(ColumnMappers.class).getCoalesceNullPrimitivesToDefaults()) {
        String msg = String.format(
            "Database null values are not allowed for Java primitives by the current configuration:"
                + " could not map column of type %s."
                + " Change your result type to a boxed primitive to resolve.",
            clazz.getName());
        throw new UnableToProduceResultException(msg);
      }
      return getter.apply(v);
    };
  }
}

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
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;

/**
 * Column mapper factory which knows how to map boxed java primitives:
 * <ul>
 * <li>{@link Boolean}</li>
 * <li>{@link Byte}</li>
 * <li>{@link Character}</li>
 * <li>{@link Short}</li>
 * <li>{@link Integer}</li>
 * <li>{@link Long}</li>
 * <li>{@link Float}</li>
 * <li>{@link Double}</li>
 * </ul>
 */
public class BoxedMapperFactory implements ColumnMapperFactory {
  private final IdentityHashMap<Class<?>, ColumnMapper<?>> mappers = new IdentityHashMap<>();

  public BoxedMapperFactory() {
    mappers.put(Boolean.class, (v, ctx) -> v.isNull() ? null : v.getBooleanValue());
    mappers.put(Byte.class, (v, ctx) -> v.isNull() ? null : (byte) v.getLongValue());
    mappers.put(Character.class, (v, ctx) -> v.isNull() ? null : v.getStringValue().charAt(0));
    mappers.put(Short.class, (v, ctx) -> v.isNull() ? null : (short) v.getLongValue());
    mappers.put(Integer.class, (v, ctx) -> v.isNull() ? null : (int) v.getLongValue());
    mappers.put(Long.class, (v, ctx) -> v.isNull() ? null : v.getLongValue());
    mappers.put(Float.class, (v, ctx) -> v.isNull() ? null : (float) v.getDoubleValue());
    mappers.put(Double.class, (v, ctx) -> v.isNull() ? null : v.getDoubleValue());
  }

  @Override
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    Class<?> rawType = GenericTypes.getErasedType(type);
    return Optional.ofNullable(mappers.get(rawType));
  }
}

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
import java.math.BigDecimal;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.UUID;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;

/**
 * Column mapper factory which knows how to map high-level essentials like String:
 * <ul>
 * <li>{@link BigDecimal}</li>
 * <li>{@link String}</li>
 * <li>{@code byte[]}</li>
 * <li>{@link UUID}</li>
 * </ul>
 */
public class EssentialsMapperFactory implements ColumnMapperFactory {
  private final IdentityHashMap<Class<?>, ColumnMapper<?>> mappers = new IdentityHashMap<>();

  public EssentialsMapperFactory() {
    mappers.put(BigDecimal.class, (v, ctx) -> v.isNull() ? null : v.getNumericValue());
    mappers.put(String.class, (v, ctx) -> v.isNull() ? null : v.getStringValue());
    mappers.put(byte[].class, (v, ctx) -> v.isNull() ? null : v.getBytesValue());
    mappers.put(UUID.class, (v, ctx) -> Optional.ofNullable(v.isNull() ? null : v.getStringValue())
        .map(UUID::fromString).orElse(null));
  }

  @Override
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    Class<?> rawType = GenericTypes.getErasedType(type);
    return Optional.ofNullable(mappers.get(rawType));
  }
}

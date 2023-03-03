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
import java.util.Optional;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;

/**
 * Produces enum column mappers, which map enums from varchar columns using
 * {@link Enum#valueOf(Class, String)}.
 */
public class EnumMapperFactory implements ColumnMapperFactory {
  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    Class<?> clazz = GenericTypes.getErasedType(type);
    return Optional.ofNullable(
        Enum.class.isAssignableFrom(clazz)
            ? (v, ctx) -> Optional.ofNullable(v.isNull() ? null : v.getStringValue())
                .map(s -> (Enum) Enum.valueOf((Class) clazz, s)).orElse(null)
            : null);
  }
}

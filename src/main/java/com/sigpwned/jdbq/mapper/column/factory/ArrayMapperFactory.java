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
package com.sigpwned.jdbq.mapper.column.factory;

import java.lang.reflect.Type;
import java.util.Optional;
import com.sigpwned.jdbq.collector.JdbqCollectors;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.column.ArrayColumnMapper;
import com.sigpwned.jdbq.mapper.column.CollectorColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;
import com.sigpwned.jdbq.mapper.column.ColumnMappers;

/**
 * Maps SQL array columns into Java arrays or other Java container types. Supports any Java array
 * type for which a {@link ColumnMapper} is registered for the array element type. Supports any
 * other container type for which a {@link org.jdbi.v3.core.collector.CollectorFactory} is
 * registered, and for which a {@link ColumnMapper} is registered for the container element type.
 */
public class ArrayMapperFactory implements ColumnMapperFactory {
  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    final Class<?> erasedType = GenericTypes.getErasedType(type);

    if (erasedType.isArray()) {
      Class<?> elementType = erasedType.getComponentType();
      return elementTypeMapper(elementType, config)
          .map(elementMapper -> new ArrayColumnMapper(elementMapper, elementType));
    }

    JdbqCollectors collectorRegistry = config.get(JdbqCollectors.class);
    return (Optional) collectorRegistry.findFor(type)
        .flatMap(collector -> collectorRegistry.findElementTypeFor(type)
            .flatMap(elementType -> elementTypeMapper(elementType, config))
            .map(elementMapper -> new CollectorColumnMapper(elementMapper, collector)));
  }

  private Optional<ColumnMapper<?>> elementTypeMapper(Type elementType, ConfigRegistry config) {
    return config.get(ColumnMappers.class).findFor(elementType);
  }
}

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
package com.sigpwned.jdbq.mapper.column;

import java.lang.reflect.Array;
import java.util.List;
import com.google.cloud.bigquery.FieldValue;
import com.sigpwned.jdbq.statement.StatementContext;

public class ArrayColumnMapper implements ColumnMapper<Object> {
  private final ColumnMapper<?> elementMapper;
  private final Class<?> componentType;

  public ArrayColumnMapper(ColumnMapper<?> elementMapper, Class<?> componentType) {
    this.elementMapper = elementMapper;
    this.componentType = componentType;
  }

  @Override
  public Object map(FieldValue v, StatementContext ctx) {
    List<FieldValue> array = v.getRepeatedValue();

    if (array == null) {
      return null;
    }

    Object[] result = (Object[]) Array.newInstance(componentType, array.size());
    for (int i = 0; i < array.size(); i++)
      result[i] = elementMapper.map(array.get(i), ctx);

    return result;
  }
}

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
package com.sigpwned.jdbq.argument.factory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import com.google.cloud.bigquery.QueryParameterValue;
import com.google.cloud.bigquery.StandardSQLTypeName;
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.argument.Arguments;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.statement.exception.UnableToCreateStatementException;

public class CollectionArgumentFactory implements ArgumentFactory {
  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (value == null) {
      result = null;
    } else if (GenericTypes.isSuperType(Iterable.class, type)) {
      Iterable<?> collection = (Iterable<?>) value;
      Type elementType = GenericTypes.findGenericParameter(type, Iterable.class)
          .orElseThrow(() -> new UnableToCreateStatementException(
              "Collection has unresolvable element type " + type));

      // TODO Does the collection case need to match the array case for all inputs?
      // TODO If the array is empty, do we need to map this to a SQLStandardTypeName?
      @SuppressWarnings("unused")
      Class elementClass = GenericTypes.getErasedType(elementType);

      Iterator<?> iterator = collection.iterator();
      List<QueryParameterValue> array = new ArrayList<>();
      while (iterator.hasNext())
        array.add(config.get(Arguments.class).map(elementType, iterator.next(), config));

      // TODO Does this handle array of struct properly?
      result = QueryParameterValue.newBuilder().setType(StandardSQLTypeName.ARRAY)
          .setArrayType(array.isEmpty() ? null : array.get(0).getType()).setArrayValues(array)
          .build();
    } else {
      Class arrayClass = GenericTypes.getErasedType(type);
      if (arrayClass.getComponentType() == null)
        throw new IllegalArgumentException("invalid array type: " + arrayClass.getName());
      Class elementClass = arrayClass.getComponentType();

      Object[] array = (Object[]) value;

      result = QueryParameterValue.array(array, elementClass);
    }
    return Optional.ofNullable(result);
  }
}

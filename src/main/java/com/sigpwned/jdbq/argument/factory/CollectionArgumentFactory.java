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
  @SuppressWarnings({"rawtypes"})
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (GenericTypes.isSuperType(Iterable.class, type)) {
      Type elementType = GenericTypes.findGenericParameter(type, Iterable.class)
          .orElseThrow(() -> new UnableToCreateStatementException(
              "Collection has unresolvable element type " + type));

      Iterable<?> collection = (Iterable<?>) value;
      if (collection != null) {
        Iterator<?> iterator = collection.iterator();
        List<QueryParameterValue> array = new ArrayList<>();
        while (iterator.hasNext())
          array.add(config.get(Arguments.class).map(elementType, iterator.next(), config));

        QueryParameterValue example;
        if (array.isEmpty()) {
          example = config.get(Arguments.class).map(elementType, null, config);
        } else {
          example = array.get(0);
        }

        StandardSQLTypeName exampleType = example.getType();

        // TODO Does this handle array of array and array of struct properly?
        result = QueryParameterValue.newBuilder().setType(StandardSQLTypeName.ARRAY)
            .setArrayType(exampleType).setArrayValues(array).build();
      } else {
        QueryParameterValue example = config.get(Arguments.class).map(elementType, null, config);
        StandardSQLTypeName exampleType = example.getType();
        result = QueryParameterValue.array(null, exampleType);
      }
    } else {
      Class arrayClass = GenericTypes.getErasedType(type);
      if (arrayClass.getComponentType() != null) {
        Class elementClass = arrayClass.getComponentType();

        Object[] values = (Object[]) value;
        if (values != null) {
          List<QueryParameterValue> array = new ArrayList<>();
          for (Object element : values)
            array.add(config.get(Arguments.class).map(elementClass, element, config));

          QueryParameterValue example;
          if (array.isEmpty()) {
            example = config.get(Arguments.class).map(elementClass, null, config);
          } else {
            example = array.get(0);
          }

          StandardSQLTypeName exampleType = example.getType();

          // TODO Does this handle array of array and array of struct properly?
          result = QueryParameterValue.newBuilder().setType(StandardSQLTypeName.ARRAY)
              .setArrayType(exampleType).setArrayValues(array).build();
        } else {
          QueryParameterValue example = config.get(Arguments.class).map(elementClass, null, config);
          StandardSQLTypeName exampleType = example.getType();
          result = QueryParameterValue.array(null, exampleType);
        }
      } else {
        // We can't map this
        result = null;
      }
    }
    return Optional.ofNullable(result);
  }
}

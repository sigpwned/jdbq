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

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import com.google.cloud.bigquery.QueryParameterValue;
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
    } else if (GenericTypes.isSuperType(Collection.class, type)) {
      Collection<?> collection = (Collection<?>) value;
      Type elementType = GenericTypes.findGenericParameter(type, Collection.class)
          .orElseThrow(() -> new UnableToCreateStatementException(
              "Collection has unresolvable element type " + type));
      Class elementClass = GenericTypes.getErasedType(elementType);

      Iterator<?> iterator = collection.iterator();
      Object[] array = (Object[]) Array.newInstance(elementClass, collection.size());
      for (int i = 0; i < array.length; i++)
        array[i] = config.get(Arguments.class).map(elementType, iterator.next(), config);

      result = QueryParameterValue.array(array, elementClass);
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

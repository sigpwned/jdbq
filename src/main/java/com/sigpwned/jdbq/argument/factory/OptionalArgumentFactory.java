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
package com.sigpwned.jdbq.argument.factory;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.argument.Arguments;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;

public class OptionalArgumentFactory implements ArgumentFactory {
  @Override
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (value == null) {
      result = null;
    } else if (type.equals(OptionalInt.class)) {
      OptionalInt optional = (OptionalInt) value;
      result = QueryParameterValue.int64(optional.isPresent() ? optional.getAsInt() : null);
    } else if (type.equals(OptionalLong.class)) {
      OptionalLong optional = (OptionalLong) value;
      result = QueryParameterValue.int64(optional.isPresent() ? optional.getAsLong() : null);
    } else if (type.equals(OptionalDouble.class)) {
      OptionalDouble optional = (OptionalDouble) value;
      result = QueryParameterValue.float64(optional.isPresent() ? optional.getAsDouble() : null);
    } else if (GenericTypes.getErasedType(type).equals(Optional.class)) {
      Optional<?> optional = (Optional<?>) value;
      if (optional.isPresent()) {
        // TODO Are we handling all the edge cases properly?
        Type parameterType = GenericTypes.findGenericParameter(type, Optional.class)
            .orElse(value != null ? value.getClass() : Object.class);
        result = config.get(Arguments.class).map(parameterType, optional.get(), config);
      } else {
        result = null;
      }
    } else {
      result = null;
    }
    return Optional.ofNullable(result);
  }
}

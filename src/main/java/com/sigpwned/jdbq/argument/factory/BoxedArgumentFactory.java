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
import com.google.cloud.bigquery.QueryParameterValue;
import com.google.cloud.bigquery.StandardSQLTypeName;
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.config.ConfigRegistry;

public class BoxedArgumentFactory implements ArgumentFactory {
  @Override
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (type.equals(Boolean.class)) {
      result = QueryParameterValue.of((Boolean) value, StandardSQLTypeName.BOOL);
    } else if (type.equals(Byte.class)) {
      result = QueryParameterValue.of((Byte) value, StandardSQLTypeName.INT64);
    } else if (type.equals(Short.class)) {
      result = QueryParameterValue.of((Short) value, StandardSQLTypeName.INT64);
    } else if (type.equals(Integer.class)) {
      result = QueryParameterValue.of((Integer) value, StandardSQLTypeName.INT64);
    } else if (type.equals(Long.class)) {
      result = QueryParameterValue.of((Long) value, StandardSQLTypeName.INT64);
    } else if (type.equals(Float.class)) {
      result = QueryParameterValue.of((Float) value, StandardSQLTypeName.FLOAT64);
    } else if (type.equals(Double.class)) {
      result = QueryParameterValue.of((Double) value, StandardSQLTypeName.FLOAT64);
    } else if (type.equals(Character.class)) {
      result = QueryParameterValue.of((Character) value, StandardSQLTypeName.STRING);
    } else {
      result = null;
    }
    return Optional.ofNullable(result);
  }
}

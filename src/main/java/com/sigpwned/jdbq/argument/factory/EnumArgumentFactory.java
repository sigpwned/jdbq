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
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;

public class EnumArgumentFactory implements ArgumentFactory {
  @Override
  @SuppressWarnings("rawtypes")
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    Class<?> clazz = GenericTypes.getErasedType(type);
    return Optional.ofNullable(
        Enum.class.isAssignableFrom(clazz) ? QueryParameterValue.string(((Enum) value).name())
            : null);
  }
}

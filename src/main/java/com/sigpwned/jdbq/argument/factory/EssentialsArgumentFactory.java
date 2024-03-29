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
package com.sigpwned.jdbq.argument.factory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.config.ConfigRegistry;

public class EssentialsArgumentFactory implements ArgumentFactory {
  @Override
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (value == null) {
      result = null;
    } else if (type.equals(BigDecimal.class)) {
      BigDecimal bigDecimal = (BigDecimal) value;
      result = QueryParameterValue.bigNumeric(bigDecimal);
    } else if (type.equals(byte[].class)) {
      byte[] bytes = (byte[]) value;
      result = QueryParameterValue.bytes(bytes);
    } else if (type.equals(String.class)) {
      String string = (String) value;
      result = QueryParameterValue.string(string);
    } else if (type.equals(UUID.class)) {
      UUID uuid = (UUID) value;
      result = QueryParameterValue.string(uuid.toString());
    } else {
      result = null;
    }
    return Optional.ofNullable(result);
  }
}

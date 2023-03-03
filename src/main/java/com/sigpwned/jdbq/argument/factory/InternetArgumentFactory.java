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
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.config.ConfigRegistry;

public class InternetArgumentFactory implements ArgumentFactory {
  @Override
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (value == null) {
      result = null;
    } else if (type.equals(Inet4Address.class)) {
      Inet4Address address = (Inet4Address) value;
      result = QueryParameterValue.string(address.getHostAddress());
    } else if (type.equals(Inet6Address.class)) {
      Inet6Address address = (Inet6Address) value;
      result = QueryParameterValue.string(address.getHostAddress());
    } else if (type.equals(URL.class)) {
      URL url = (URL) value;
      result = QueryParameterValue.string(url.toString());
    } else if (type.equals(URI.class)) {
      URI uri = (URI) value;
      result = QueryParameterValue.string(uri.toString());
    } else {
      result = null;
    }
    return Optional.ofNullable(result);
  }
}

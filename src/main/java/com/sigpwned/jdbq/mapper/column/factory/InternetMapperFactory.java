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
package com.sigpwned.jdbq.mapper.column.factory;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.IdentityHashMap;
import java.util.Optional;
import com.google.cloud.bigquery.FieldValue;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.MappingException;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Column mapper factory which knows how to map networking related objects:
 * <ul>
 * <li>{@link InetAddress}</li>
 * <li>{@link URL}</li>
 * <li>{@link URI}</li>
 * </ul>
 */
class InternetMapperFactory implements ColumnMapperFactory {
  private final IdentityHashMap<Class<?>, ColumnMapper<?>> mappers = new IdentityHashMap<>();

  InternetMapperFactory() {
    mappers.put(InetAddress.class, InternetMapperFactory::getInetAddress);
    mappers.put(URL.class, InternetMapperFactory::getURL);
    mappers.put(URI.class, InternetMapperFactory::getURI);
  }

  @Override
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    Class<?> rawType = GenericTypes.getErasedType(type);
    return Optional.ofNullable(mappers.get(rawType));
  }

  private static URL getURL(FieldValue v, StatementContext ctx) {
    String s = v.isNull() ? null : v.getStringValue();
    try {
      return s == null ? null : new URL(s);
    } catch (MalformedURLException e) {
      throw new MappingException("Failed to parse URI", e);
    }
  }

  private static URI getURI(FieldValue v, StatementContext ctx) {
    String s = v.isNull() ? null : v.getStringValue();
    try {
      return s == null ? null : new URI(s);
    } catch (URISyntaxException e) {
      throw new MappingException("Failed to parse URI", e);
    }
  }

  private static InetAddress getInetAddress(FieldValue v, StatementContext ctx) {
    String hostname = v.isNull() ? null : v.getStringValue();
    try {
      return hostname == null ? null : InetAddress.getByName(hostname);
    } catch (UnknownHostException e) {
      throw new MappingException("Could not map InetAddress", e);
    }
  }
}

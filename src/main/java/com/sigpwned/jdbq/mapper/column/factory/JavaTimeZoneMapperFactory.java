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
import java.time.ZoneId;
import java.util.Optional;
import com.google.cloud.bigquery.FieldValue;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Column mapper factory which knows how to map JavaTime objects:
 * <ul>
 * <li>{@link ZoneId}</li>
 * </ul>
 */
class JavaTimeZoneMapperFactory implements ColumnMapperFactory {
  @Override
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    Class<?> rawType = GenericTypes.getErasedType(type);
    return Optional
        .ofNullable(rawType.equals(ZoneId.class) ? JavaTimeZoneMapperFactory::map : null);
  }

  private static ZoneId map(FieldValue v, StatementContext ctx) {
    return v.isNull() ? null : ZoneId.of(v.getStringValue());
  }
}

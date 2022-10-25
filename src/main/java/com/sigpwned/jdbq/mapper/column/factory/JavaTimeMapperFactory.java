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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.IdentityHashMap;
import java.util.Optional;
import com.google.cloud.bigquery.FieldValue;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.internal.JavaTimeMapping;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.ColumnMapperFactory;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Column mapper factory which knows how to map JavaTime objects:
 * <ul>
 * <li>{@link Instant}</li>
 * <li>{@link LocalDate}</li>
 * <li>{@link LocalTime}</li>
 * <li>{@link LocalDateTime}</li>
 * <li>{@link OffsetDateTime}</li>
 * <li>{@link ZonedDateTime}</li>
 * </ul>
 */
class JavaTimeMapperFactory implements ColumnMapperFactory {
  private static final DateTimeFormatter TIME_FORMATTER = JavaTimeMapping.TIME_FORMATTER;

  private static final DateTimeFormatter DATE_FORMATTER = JavaTimeMapping.DATE_FORMATTER;

  private static final DateTimeFormatter DATE_TIME_FORMATTER = JavaTimeMapping.DATE_TIME_FORMATTER;

  private final IdentityHashMap<Class<?>, ColumnMapper<?>> mappers = new IdentityHashMap<>();

  JavaTimeMapperFactory() {
    mappers.put(Instant.class, JavaTimeMapperFactory::getInstant);
    mappers.put(LocalDate.class, JavaTimeMapperFactory::getLocalDate);
    mappers.put(LocalTime.class, JavaTimeMapperFactory::getLocalTime);
    mappers.put(LocalDateTime.class, JavaTimeMapperFactory::getLocalDateTime);
    mappers.put(OffsetDateTime.class, JavaTimeMapperFactory::getOffsetDateTime);
    mappers.put(ZonedDateTime.class, JavaTimeMapperFactory::getZonedDateTime);
  }

  @Override
  public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
    Class<?> rawType = GenericTypes.getErasedType(type);
    return Optional.ofNullable(mappers.get(rawType));
  }

  private static Instant getInstant(FieldValue v, StatementContext ctx) {
    return v.isNull() ? null : Instant.parse(v.getStringValue());
  }

  private static LocalDate getLocalDate(FieldValue v, StatementContext ctx) {
    // TODO Is this right?
    return v.isNull() ? null : LocalDate.parse(v.getStringValue(), DATE_FORMATTER);
  }

  private static LocalDateTime getLocalDateTime(FieldValue v, StatementContext ctx) {
    // TODO Is this right?
    return v.isNull() ? null : LocalDateTime.parse(v.getStringValue(), DATE_TIME_FORMATTER);
  }

  private static OffsetDateTime getOffsetDateTime(FieldValue v, StatementContext ctx) {
    return v.isNull() ? null : getInstant(v, ctx).atOffset(ZoneOffset.UTC);
  }

  private static ZonedDateTime getZonedDateTime(FieldValue v, StatementContext ctx) {
    return v.isNull() ? null : getInstant(v, ctx).atZone(ZoneOffset.UTC);
  }

  private static LocalTime getLocalTime(FieldValue v, StatementContext ctx) {
    // TODO Is this right?
    return v.isNull() ? null : LocalTime.parse(v.getStringValue(), TIME_FORMATTER);
  }
}

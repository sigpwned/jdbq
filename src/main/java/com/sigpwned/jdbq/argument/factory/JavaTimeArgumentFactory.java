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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import com.google.cloud.bigquery.QueryParameterValue;
import com.google.protobuf.Duration;
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.internal.JavaTimeMapping;

public class JavaTimeArgumentFactory implements ArgumentFactory {
  private static final DateTimeFormatter INSTANT_FORMATTER = JavaTimeMapping.INSTANT_FORMATTER;

  private static final DateTimeFormatter TIME_FORMATTER = JavaTimeMapping.TIME_FORMATTER;

  private static final DateTimeFormatter DATE_FORMATTER = JavaTimeMapping.DATE_FORMATTER;

  private static final DateTimeFormatter DATE_TIME_FORMATTER = JavaTimeMapping.DATE_TIME_FORMATTER;

  @Override
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (value == null) {
      result = null;
    } else if (type.equals(Instant.class)) {
      Instant instant = (Instant) value;
      result = QueryParameterValue.timestamp(INSTANT_FORMATTER.format(instant));
    } else if (type.equals(LocalDate.class)) {
      LocalDate localDate = (LocalDate) value;
      result = QueryParameterValue.date(DATE_FORMATTER.format(localDate));
    } else if (type.equals(LocalTime.class)) {
      LocalTime localTime = (LocalTime) value;
      result = QueryParameterValue.time(TIME_FORMATTER.format(localTime));
    } else if (type.equals(LocalDateTime.class)) {
      LocalDateTime localDateTime = (LocalDateTime) value;
      result = QueryParameterValue.dateTime(DATE_TIME_FORMATTER.format(localDateTime));
    } else if (type.equals(OffsetDateTime.class)) {
      OffsetDateTime offsetDateTime = (OffsetDateTime) value;
      result = QueryParameterValue.timestamp(INSTANT_FORMATTER.format(offsetDateTime.toInstant()));
    } else if (type.equals(ZonedDateTime.class)) {
      ZonedDateTime zonedDateTime = (ZonedDateTime) value;
      result = QueryParameterValue.timestamp(INSTANT_FORMATTER.format(zonedDateTime.toInstant()));
    } else if (type.equals(Duration.class)) {
      Duration duration = (Duration) value;
      result = QueryParameterValue
          .int64(TimeUnit.SECONDS.toNanos(duration.getSeconds()) + duration.getNanos());
    } else if (type.equals(Period.class)) {
      Period period = (Period) value;
      result = QueryParameterValue.string(period.toString());
    } else {
      result = null;
    }
    return Optional.ofNullable(result);
  }
}

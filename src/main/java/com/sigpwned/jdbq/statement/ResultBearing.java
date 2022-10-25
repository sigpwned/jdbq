/*-
 * =================================LICENSE_START==================================
 * jdbq
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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
package com.sigpwned.jdbq.statement;

import java.lang.reflect.Type;
import java.util.function.Supplier;
import org.apache.http.client.methods.Configurable;
import com.sigpwned.jdbq.generic.GenericType;
import com.sigpwned.jdbq.mapper.NoSuchMapperException;
import com.sigpwned.jdbq.mapper.column.ColumnMapper;
import com.sigpwned.jdbq.mapper.column.SingleColumnMapper;
import com.sigpwned.jdbq.mapper.row.RowMapper;
import com.sigpwned.jdbq.mapper.row.RowMappers;
import com.sigpwned.jdbq.result.ResultIterable;
import com.sigpwned.jdbq.result.ResultSet;
import com.sigpwned.jdbq.result.ResultSetScanner;

/**
 * Provides access to the contents of a {@link ResultSet} by mapping to Java types.
 */
public interface ResultBearing {
  /**
   * Returns a ResultBearing backed by the given result set supplier and context.
   *
   * @param resultSetSupplier result set supplier
   * @param ctx the statement context
   * @return a ResultBearing
   */
  static ResultBearing of(Supplier<ResultSet> resultSetSupplier, StatementContext ctx) {
    return new ResultBearing() {
      @Override
      public <R> R scanResultSet(ResultSetScanner<R> mapper) {
        return mapper.scanResultSet(resultSetSupplier, ctx);
      }
    };
  }

  /**
   * Invokes the mapper with a result set supplier, and returns the value returned by the mapper.
   * 
   * @param mapper result set scanner
   * @param <R> result type returned by the mapper.
   * @return the value returned by the mapper.
   */
  <R> R scanResultSet(ResultSetScanner<R> mapper);

  /**
   * Maps this result set to a {@link ResultIterable} of the given element type.
   *
   * @param type the type to map the result set rows to
   * @param <T> the type to map the result set rows to
   * @return a {@link ResultIterable} of the given type.
   * @see Configurable#registerRowMapper(RowMapper)
   * @see Configurable#registerRowMapper(org.jdbi.v3.core.mapper.RowMapperFactory)
   * @see Configurable#registerColumnMapper(org.jdbi.v3.core.mapper.ColumnMapperFactory)
   * @see Configurable#registerColumnMapper(ColumnMapper)
   */
  @SuppressWarnings("unchecked")
  default <T> ResultIterable<T> mapTo(Class<T> type) {
    return (ResultIterable<T>) mapTo((Type) type);
  }

  /**
   * Maps this result set to a {@link ResultIterable} of the given element type.
   *
   * @param type the type to map the result set rows to
   * @param <T> the type to map the result set rows to
   * @return a {@link ResultIterable} of the given type.
   * @see Configurable#registerRowMapper(RowMapper)
   * @see Configurable#registerRowMapper(org.jdbi.v3.core.mapper.RowMapperFactory)
   * @see Configurable#registerColumnMapper(org.jdbi.v3.core.mapper.ColumnMapperFactory)
   * @see Configurable#registerColumnMapper(ColumnMapper)
   */
  @SuppressWarnings("unchecked")
  default <T> ResultIterable<T> mapTo(GenericType<T> type) {
    return (ResultIterable<T>) mapTo(type.getType());
  }

  /**
   * Maps this result set to a {@link ResultIterable} of the given element type.
   *
   * @param type the type to map the result set rows to
   * @return a {@link ResultIterable} of the given type.
   * @see Configurable#registerRowMapper(RowMapper)
   * @see Configurable#registerRowMapper(org.jdbi.v3.core.mapper.RowMapperFactory)
   * @see Configurable#registerColumnMapper(org.jdbi.v3.core.mapper.ColumnMapperFactory)
   * @see Configurable#registerColumnMapper(ColumnMapper)
   */
  default ResultIterable<?> mapTo(Type type) {
    return scanResultSet((supplier, ctx) -> {
      RowMapper<?> mapper = ctx.getConfig(RowMappers.class).findFor(type)
          .orElseThrow(() -> new NoSuchMapperException("No mapper registered for type " + type));
      return ResultIterable.of(supplier, mapper, ctx);
    });
  }

  /**
   * Maps this result set to a {@link ResultIterable}, using the given row mapper.
   *
   * @param mapper mapper used to map each row
   * @param <T> the type to map the result set rows to
   * @return a {@link ResultIterable} of type {@code <T>}.
   */
  default <T> ResultIterable<T> map(RowMapper<T> mapper) {
    return scanResultSet((supplier, ctx) -> ResultIterable.of(supplier, mapper, ctx));
  }

  /**
   * Maps this result set to a {@link ResultIterable}, using the given row mapper.
   *
   * @param mapper mapper used to map each row
   * @param <T> the type to map the result set rows to
   * @return a {@link ResultIterable} of type {@code <T>}.
   */
  default <T> ResultIterable<T> map(ColumnMapper<T> mapper) {
    return map(new SingleColumnMapper<>(mapper));
  }
}

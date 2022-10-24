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
package com.sigpwned.jdbq.mapper.row;

import java.sql.SQLException;
import com.google.cloud.bigquery.FieldValueList;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Maps result set rows to objects.
 *
 * @param <T> the mapped type.
 * @see RowMappers
 * @see RowMapperFactory
 * @see org.jdbi.v3.core.result.ResultBearing#map(RowMapper)
 * @see org.jdbi.v3.core.config.Configurable#registerRowMapper(RowMapper)
 * @see org.jdbi.v3.core.config.Configurable#registerRowMapper(java.lang.reflect.Type, RowMapper)
 */
@FunctionalInterface
public interface RowMapper<T> {
  /**
   * Map the current row of the result set. This method should not cause the result set to advance;
   * allow Jdbi to do that, please.
   *
   * @param rs the result set being iterated
   * @param ctx the statement context
   * @return the value to produce for this row
   * @throws SQLException if anything goes wrong go ahead and let this percolate; Jdbi will handle
   *         it
   */
  T map(FieldValueList rs, StatementContext ctx);
}

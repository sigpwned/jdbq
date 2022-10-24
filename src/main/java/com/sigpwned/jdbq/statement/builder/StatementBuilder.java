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
package com.sigpwned.jdbq.statement.builder;

import java.sql.SQLException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Used to convert translated SQL into a prepared statement. The default implementation created by
 * {@link DefaultStatementBuilder#FACTORY} creates a new statement on every call.
 *
 * A StatementBuilder is always associated with exactly one Handle instance
 *
 * @see StatementBuilderFactory
 */
@FunctionalInterface
public interface StatementBuilder {
  /**
   * Called each time a prepared statement needs to be created.
   *
   * @param conn the JDBC Connection the statement is being created for
   * @param sql the translated SQL which should be prepared
   * @param ctx Statement context associated with the SqlStatement this is building for
   *
   * @return a PreparedStatement for the given arguments
   *
   * @throws SQLException if anything goes wrong preparing the statement
   */
  QueryJobConfiguration.Builder create(String sql, StatementContext ctx);
}

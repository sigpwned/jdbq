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
package com.sigpwned.jdbq.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.google.cloud.bigquery.QueryJobConfiguration;

/**
 * Allows tweaking of statement behaviour.
 */
public interface StatementCustomizer {
  /**
   * Invoked prior to using the TemplateEngine to render sql from definitions.
   * 
   * @param stmt the statement we are about to render
   * @param ctx the context associated with the statement
   * @throws SQLException go ahead and percolate it for Jdbi to handle
   */
  default void beforeTemplating(QueryJobConfiguration.Builder stmt, StatementContext ctx) {}

  /**
   * Invoked prior to applying bound parameters to the {@link PreparedStatement}.
   *
   * @param stmt Prepared statement being customized
   * @param ctx Statement context associated with the statement being customized
   * @throws SQLException go ahead and percolate it for Jdbi to handle
   */
  default void beforeBinding(QueryJobConfiguration.Builder stmt, StatementContext ctx) {}

  /**
   * Make the changes you need to inside this method. It will be invoked prior to execution of the
   * prepared statement
   *
   * @param stmt Prepared statement being customized
   * @param ctx Statement context associated with the statement being customized
   * @throws SQLException go ahead and percolate it for Jdbi to handle
   */
  default void beforeExecution(QueryJobConfiguration.Builder stmt, StatementContext ctx) {}

  /**
   * This will be invoked after execution of the prepared statement, but before any results are
   * accessed.
   *
   * @param stmt Prepared statement being customized
   * @param ctx Statement context associated with the statement being customized
   * @throws SQLException go ahead and percolate it for Jdbi to handle
   */
  default void afterExecution(QueryJobConfiguration.Builder stmt, StatementContext ctx) {}
}

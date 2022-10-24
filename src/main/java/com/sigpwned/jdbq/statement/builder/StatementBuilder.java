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

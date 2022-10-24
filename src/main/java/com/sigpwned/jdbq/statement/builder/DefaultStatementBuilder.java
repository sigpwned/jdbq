package com.sigpwned.jdbq.statement.builder;

import com.google.cloud.bigquery.QueryJobConfiguration;
import com.sigpwned.jdbq.statement.StatementContext;

public class DefaultStatementBuilder implements StatementBuilder {
  @Override
  public QueryJobConfiguration.Builder create(String sql, StatementContext ctx) {
    return QueryJobConfiguration.newBuilder(sql);
  }
}

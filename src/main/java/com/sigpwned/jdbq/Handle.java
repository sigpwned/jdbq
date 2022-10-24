package com.sigpwned.jdbq;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import com.google.cloud.bigquery.BigQuery;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.statement.Query;

public class Handle implements AutoCloseable {
  private final Jdbq jdbq;
  private final ConfigRegistry config;

  public Handle(Jdbq jdbq, ConfigRegistry config) {
    this.jdbq = requireNonNull(jdbq);
    this.config = requireNonNull(config);
  }

  public Jdbq getJdbq() {
    return jdbq;
  }

  /**
   * @return the client
   */
  public BigQuery getClient() {
    return getJdbq().getClient();
  }

  /**
   * @return the config
   */
  public ConfigRegistry getConfig() {
    return config;
  }

  public Query createQuery(String sql) {
    return new Query(this, sql);
  }

  @Override
  public void close() throws IOException {
    // NOP
  }
}

package com.sigpwned.jdbq;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.sigpwned.jdbq.config.ConfigRegistry;

public class Jdbq {
  private final BigQuery client;
  private final ConfigRegistry config;

  public Jdbq(GoogleCredentialsProvider credentialsProvider) throws IOException {
    this(credentialsProvider.getCredentials());
  }

  public Jdbq(Credentials credentials) {
    this(BigQueryOptions.newBuilder().setCredentials(credentials).build());
  }

  public Jdbq(BigQueryOptions options) {
    this(options.getService());
  }

  public Jdbq(BigQuery client) {
    this.client = requireNonNull(client);
    this.config = new ConfigRegistry();
  }

  public Handle open() {
    return new Handle(this, getConfig());
  }

  /**
   * @return the client
   */
  BigQuery getClient() {
    return client;
  }

  /**
   * @return the config
   */
  ConfigRegistry getConfig() {
    return config;
  }
}

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
  public BigQuery getClient() {
    return client;
  }

  /**
   * @return the config
   */
  public ConfigRegistry getConfig() {
    return config;
  }
}

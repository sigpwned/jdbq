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

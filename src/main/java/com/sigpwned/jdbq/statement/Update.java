/*-
 * =================================LICENSE_START==================================
 * jdbq
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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

import java.util.Optional;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobStatistics;
import com.sigpwned.jdbq.Handle;

/**
 * Used for INSERT, UPDATE, and DELETE statements
 */
public class Update extends SqlStatement<Update> {
  public Update(Handle handle, String sql) {
    super(handle, sql);
  }

  public void one() {
    long count = execute();
    if (count != 1L) {
      throw new IllegalStateException("Expected 1 modified row, got " + count);
    }
  }

  /**
   * Executes the statement, returning the update count.
   *
   * @return the number of rows modified
   */
  public long execute() {
    Job job = internalExecute();
    JobStatistics.QueryStatistics statistics = job.getStatistics();
    // Return 0 if statistics.getNumDmlAffectedRows() is null to prevent a NullPointerException (NPE)
    return Optional.ofNullable(statistics.getNumDmlAffectedRows()).orElse(0L);
  }
}

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
package com.sigpwned.jdbq.statement.exception;

import com.sigpwned.jdbq.statement.StatementContext;
import com.sigpwned.jdbq.statement.StatementException;

/**
 * Thrown when statement customization fails after execution
 */
public class UnableToCleanupStatementException extends StatementException {
  private static final long serialVersionUID = 1L;

  public UnableToCleanupStatementException(String string, Throwable cause, StatementContext ctx) {
    super(string, cause, ctx);
  }

  public UnableToCleanupStatementException(String string, Throwable cause) {
    super(string, cause);
  }

  public UnableToCleanupStatementException(Throwable cause) {
    super(cause);
  }

  public UnableToCleanupStatementException(String string) {
    super(string);
  }

  public UnableToCleanupStatementException(String string, StatementContext ctx) {
    super(string, ctx);
  }

  public UnableToCleanupStatementException(Exception cause, StatementContext ctx) {
    super(cause, ctx);
  }
}

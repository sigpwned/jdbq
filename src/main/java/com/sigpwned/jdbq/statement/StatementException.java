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
package com.sigpwned.jdbq.statement;

import java.util.Optional;
import com.sigpwned.jdbq.JdbqException;

/**
 * Superclass for exceptions thrown while trying to execute a statement.
 */
public abstract class StatementException extends JdbqException {
  private static final long serialVersionUID = 1L;

  private final transient StatementContext statementContext;

  public StatementException(Throwable cause) {
    super(cause);
    statementContext = null;
  }

  public StatementException(String string, Throwable throwable, StatementContext ctx) {
    super(string, throwable);
    this.statementContext = ctx;
  }

  public StatementException(String string, Throwable throwable) {
    super(string, throwable);
    this.statementContext = null;
  }

  public StatementException(Throwable cause, StatementContext ctx) {
    super(cause);
    this.statementContext = ctx;
  }

  public StatementException(String message, StatementContext ctx) {
    super(message);
    this.statementContext = ctx;
  }

  public StatementException(String string) {
    super(string);
    this.statementContext = null;
  }

  public StatementContext getStatementContext() {
    return statementContext;
  }

  public String getShortMessage() {
    return super.getMessage();
  }

  @Override
  public String getMessage() {
    return Optional.ofNullable(getStatementContext())
        .map(c -> c.getConfig(StatementExceptions.class).getMessageRendering())
        .orElse(StatementExceptions.MessageRendering.NONE).apply(this);
  }
}

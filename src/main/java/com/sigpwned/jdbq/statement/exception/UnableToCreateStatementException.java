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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sigpwned.jdbq.statement.exception;

import com.sigpwned.jdbq.statement.StatementContext;
import com.sigpwned.jdbq.statement.StatementException;

/**
 * Thrown when {@code Jdbq} couldn't create a statement.
 */
public class UnableToCreateStatementException extends StatementException {
    private static final long serialVersionUID = 1L;

    public UnableToCreateStatementException(String string, Throwable cause, StatementContext ctx) {
        super(string, cause, ctx);
    }

    public UnableToCreateStatementException(String string, Throwable cause) {
        super(string, cause);
    }

    public UnableToCreateStatementException(Throwable cause) {
        super(cause);
    }

    public UnableToCreateStatementException(String string) {
        super(string);
    }

    public UnableToCreateStatementException(String string, StatementContext ctx) {
        super(string, ctx);
    }

    public UnableToCreateStatementException(Exception cause, StatementContext ctx) {
        super(cause, ctx);
    }
}

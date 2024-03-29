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
package com.sigpwned.jdbq.parser;

import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Parses the named parameters out of an SQL statement, and returns the {@link ParsedSql} containing
 * the JDBC-ready SQL statement, along with the type of parameters used (named or positional), the
 * number, and the parameter name for each position (if applicable).
 */
@FunctionalInterface
public interface SqlParser {
  /**
   * Parses the given SQL statement, and returns the {@link ParsedSql} for the statement.
   *
   * @param sql The SQL statement to parse
   * @param ctx The statement context for the statement being executed
   * @return the parsed SQL representing the SQL statement itself along with information about the
   *         parameters which should be bound (number and names)
   */
  ParsedSql parse(String sql, StatementContext ctx);
}

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

import static com.sigpwned.jdbq.internal.lexer.ColonStatementLexer.COMMENT;
import static com.sigpwned.jdbq.internal.lexer.ColonStatementLexer.DOUBLE_QUOTED_TEXT;
import static com.sigpwned.jdbq.internal.lexer.ColonStatementLexer.ESCAPED_TEXT;
import static com.sigpwned.jdbq.internal.lexer.ColonStatementLexer.LITERAL;
import static com.sigpwned.jdbq.internal.lexer.ColonStatementLexer.NAMED_PARAM;
import static com.sigpwned.jdbq.internal.lexer.ColonStatementLexer.POSITIONAL_PARAM;
import static com.sigpwned.jdbq.internal.lexer.ColonStatementLexer.QUOTED_TEXT;
import static org.antlr.v4.runtime.Recognizer.EOF;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import com.sigpwned.jdbq.internal.antlr4.ErrorListener;
import com.sigpwned.jdbq.internal.lexer.ColonStatementLexer;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * SQL parser which recognizes named parameter tokens of the form <code>:tokenName</code>
 * <p>
 * This is the default SQL parser
 * </p>
 */
public class ColonPrefixSqlParser implements SqlParser {

  public ColonPrefixSqlParser() {}

  @Override
  public ParsedSql parse(String sql, StatementContext ctx) {
    ParsedSql.Builder parsedSql = ParsedSql.builder();
    ColonStatementLexer lexer = new ColonStatementLexer(CharStreams.fromString(sql));
    lexer.addErrorListener(new ErrorListener());
    Token t = lexer.nextToken();
    while (t.getType() != EOF) {
      switch (t.getType()) {
        case COMMENT:
        case LITERAL:
        case QUOTED_TEXT:
        case DOUBLE_QUOTED_TEXT:
          parsedSql.append(t.getText());
          break;
        case NAMED_PARAM:
          parsedSql.appendNamedParameter(t.getText().substring(1));
          break;
        case POSITIONAL_PARAM:
          parsedSql.appendPositionalParameter();
          break;
        case ESCAPED_TEXT:
          parsedSql.append(t.getText().substring(1));
          break;
        default:
          break;
      }
      t = lexer.nextToken();
    }
    return parsedSql.build();
  }
}

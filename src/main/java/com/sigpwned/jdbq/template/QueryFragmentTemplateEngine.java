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
package com.sigpwned.jdbq.template;

import static com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer.COMMENT;
import static com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer.DEFINE;
import static com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer.DOUBLE_QUOTED_TEXT;
import static com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer.ESCAPED_TEXT;
import static com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer.LITERAL;
import static com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer.NAMED_PARAM;
import static com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer.QUOTED_TEXT;
import static org.antlr.v4.runtime.Recognizer.EOF;
import java.util.Map;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import com.google.cloud.bigquery.QueryJobConfiguration.Builder;
import com.sigpwned.jdbq.internal.antlr4.ErrorListener;
import com.sigpwned.jdbq.internal.lexer.QueryFragmentStatementLexer;
import com.sigpwned.jdbq.statement.Argument;
import com.sigpwned.jdbq.statement.SqlStatements;
import com.sigpwned.jdbq.statement.StatementContext;
import com.sigpwned.jdbq.statement.StatementCustomizer;
import com.sigpwned.jdbq.statement.exception.UnableToCreateStatementException;
import com.sigpwned.jdbq.template.fragment.QueryFragment;

/**
 * Template engine which replaces angle-bracketed tokens like <code>&lt;name&gt;</code> with the
 * string value of the named attribute. Attribute names may contain letters (a-z, A-Z), digits
 * (0-9), or underscores (<code>_</code>).
 */
public class QueryFragmentTemplateEngine implements TemplateEngine {
  @Override
  public String render(String template, StatementContext ctx) {
    StringBuilder buf = new StringBuilder();
    QueryFragmentStatementLexer lexer =
        new QueryFragmentStatementLexer(CharStreams.fromString(template));
    lexer.addErrorListener(new ErrorListener());
    Token t = lexer.nextToken();
    while (t.getType() != EOF) {
      switch (t.getType()) {
        case COMMENT:
        case LITERAL:
        case QUOTED_TEXT:
        case DOUBLE_QUOTED_TEXT:
          buf.append(t.getText());
          break;
        case NAMED_PARAM: {
          String text = t.getText();
          String marker = text.substring(0, 1);
          String key = text.substring(1, text.length());
          buf.append("@").append(key);
        }
          break;
        case DEFINE: {
          String text = t.getText();
          String key = text.substring(1, text.length() - 1);
          Object value = ctx.getAttributeBinding().getAttribute(key);
          if (value == null) {
            throw new UnableToCreateStatementException(
                "Undefined attribute for token '" + text + "'", ctx);
          }
          if (value instanceof QueryFragment) {
            buf.append(renderQueryFragment(key, (QueryFragment) value));
          } else {
            buf.append(value);
          }
        }
          break;
        case ESCAPED_TEXT:
          buf.append(t.getText().substring(1));
          break;
        default:
          break;
      }
      t = lexer.nextToken();
    }

    ctx.getConfig(SqlStatements.class).addCustomizer(new StatementCustomizer() {
      @Override
      public void beforeBinding(Builder stmt, StatementContext ctx) {
        for (Map.Entry<String, Object> e : ctx.getAttributeBinding().getAttributes().entrySet()) {
          if (e.getValue() instanceof QueryFragment) {
            walk(ctx, e.getKey(), (QueryFragment) e.getValue());
          }
        }
      }

      public void walk(StatementContext ctx, String prefix, QueryFragment fragment) {
        // had to make named parameters in ArgumentBinding public
        for (Map.Entry<String, Argument> e : fragment.getArguments().entrySet()) {
          ctx.getArgumentBinding().addNamed(prefix + "_" + e.getKey(), e.getValue().getType(),
              e.getValue().getValue());
        }
        for (Map.Entry<String, Object> e : fragment.getAttributes().entrySet()) {
          if (e.getValue() instanceof QueryFragment) {
            walk(ctx, prefix + "_" + e.getKey(), (QueryFragment) e.getValue());
          }
        }
      }
    });

    return buf.toString();
  }

  protected String renderQueryFragment(String prefix, QueryFragment fragment) {
    StringBuilder buf = new StringBuilder();
    QueryFragmentStatementLexer lexer =
        new QueryFragmentStatementLexer(CharStreams.fromString(fragment.getSql()));
    lexer.addErrorListener(new ErrorListener());
    Token t = lexer.nextToken();
    while (t.getType() != EOF) {
      switch (t.getType()) {
        case COMMENT:
        case LITERAL:
        case QUOTED_TEXT:
        case DOUBLE_QUOTED_TEXT:
          buf.append(t.getText());
          break;
        case NAMED_PARAM: {
          String text = t.getText();
          String marker = text.substring(0, 1);
          String key = text.substring(1, text.length());
          buf.append("@").append(prefix).append("_").append(key);
        }
          break;
        case DEFINE: {
          String text = t.getText();
          String key = text.substring(1, text.length() - 1);
          Object value = fragment.getAttributes().get(key);
          if (value == null) {
            throw new UnableToCreateStatementException(
                "Undefined attribute for token '" + key + "' in fragment");
          }
          if (value instanceof QueryFragment) {
            buf.append(renderQueryFragment(prefix + "_" + key, (QueryFragment) value));
          } else {
            buf.append(value);
          }
        }
          break;
        case ESCAPED_TEXT:
          buf.append(t.getText().substring(1));
          break;
        default:
          break;
      }
      t = lexer.nextToken();
    }
    return buf.toString();
  }
}

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

import static java.util.Objects.requireNonNull;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import com.sigpwned.jdbq.config.JdbqConfig;
import com.sigpwned.jdbq.parser.ColonPrefixSqlParser;
import com.sigpwned.jdbq.parser.SqlParser;
import com.sigpwned.jdbq.statement.builder.DefaultStatementBuilder;
import com.sigpwned.jdbq.statement.builder.StatementBuilder;
import com.sigpwned.jdbq.template.DefinedAttributeTemplateEngine;
import com.sigpwned.jdbq.template.TemplateEngine;

/**
 * Configuration holder for {@link SqlStatement}s.
 */
public final class SqlStatements implements JdbqConfig<SqlStatements> {
  private final Collection<StatementCustomizer> customizers;
  private StatementBuilder statementBuilder;
  private TemplateEngine templateEngine;
  private SqlParser sqlParser;
  private boolean allowUnusedBindings;

  public SqlStatements() {
    customizers = new CopyOnWriteArrayList<>();
    statementBuilder = new DefaultStatementBuilder();
    templateEngine = new DefinedAttributeTemplateEngine();
    sqlParser = new ColonPrefixSqlParser();
    allowUnusedBindings = false;
  }

  private SqlStatements(SqlStatements that) {
    this.customizers = new CopyOnWriteArrayList<>(that.customizers);
    this.statementBuilder = that.statementBuilder;
    this.templateEngine = that.templateEngine;
    this.sqlParser = that.sqlParser;
    this.allowUnusedBindings = that.allowUnusedBindings;
  }

  /**
   * Provides a means for custom statement modification. Common customizations have their own
   * methods, such as {@link Query#setMaxRows(int)}
   *
   * @param customizer instance to be used to customize a statement
   * @return this
   */
  public SqlStatements addCustomizer(final StatementCustomizer customizer) {
    this.customizers.add(customizer);
    return this;
  }

  /**
   * @return the statementBuilder
   */
  public StatementBuilder getStatementBuilder() {
    return statementBuilder;
  }

  /**
   * @param statementBuilder the statementBuilder to set
   */
  public void setStatementBuilder(StatementBuilder statementBuilder) {
    this.statementBuilder = requireNonNull(statementBuilder);
  }

  /**
   * Returns the {@link TemplateEngine} which renders the SQL template.
   *
   * @return the template engine which renders the SQL template prior to parsing parameters.
   */
  public TemplateEngine getTemplateEngine() {
    return templateEngine;
  }

  /**
   * Sets the {@link TemplateEngine} used to render SQL for all {@link SqlStatement SQL statements}
   * executed by Jdbi. The default engine replaces <code>&lt;name&gt;</code>-style tokens with
   * attributes {@link StatementContext#define(String, Object) defined} on the statement context.
   *
   * @param templateEngine the new template engine.
   * @return this
   */
  public SqlStatements setTemplateEngine(TemplateEngine templateEngine) {
    this.templateEngine = requireNonNull(templateEngine);
    return this;
  }

  public SqlParser getSqlParser() {
    return sqlParser;
  }

  /**
   * Sets the {@link SqlParser} used to parse parameters in SQL statements executed by Jdbi. The
   * default parses colon-prefixed named parameter tokens, e.g. <code>:name</code>.
   *
   * @param sqlParser the new SQL parser.
   * @return this
   */
  public SqlStatements setSqlParser(SqlParser sqlParser) {
    this.sqlParser = requireNonNull(sqlParser);
    return this;
  }

  public boolean isUnusedBindingAllowed() {
    return allowUnusedBindings;
  }

  /**
   * Sets whether or not an exception should be thrown when any arguments are given to a query but
   * not actually used in it. Unused bindings tend to be bugs or oversights, but are not always.
   * Defaults to false: unused bindings are not allowed.
   *
   * @see org.jdbi.v3.core.argument.Argument
   * @param unusedBindingAllowed the new setting
   * @return this
   */
  public SqlStatements setUnusedBindingAllowed(boolean unusedBindingAllowed) {
    this.allowUnusedBindings = unusedBindingAllowed;
    return this;
  }

  @Override
  public SqlStatements createCopy() {
    return new SqlStatements(this);
  }

  Collection<StatementCustomizer> getCustomizers() {
    return customizers;
  }
}

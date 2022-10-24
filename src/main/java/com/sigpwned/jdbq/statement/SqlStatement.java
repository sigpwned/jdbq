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
package com.sigpwned.jdbq.statement;

import static java.util.Objects.requireNonNull;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.Handle;
import com.sigpwned.jdbq.argument.Arguments;
import com.sigpwned.jdbq.config.JdbqConfig;
import com.sigpwned.jdbq.mapper.NoSuchMapperException;
import com.sigpwned.jdbq.parser.ParsedSql;
import com.sigpwned.jdbq.result.FieldValueListsResultSet;
import com.sigpwned.jdbq.result.ResultSet;
import com.sigpwned.jdbq.statement.exception.UnableToCreateStatementException;
import com.sigpwned.jdbq.statement.exception.UnableToExecuteStatementException;

public abstract class SqlStatement<This extends SqlStatement<This>> extends BaseStatement<This> {
  private final String sql;
  private QueryJobConfiguration.Builder stmt;

  SqlStatement(Handle handle, String sql) {
    super(handle);
    this.sql = requireNonNull(sql);

    getContext().setRawSql(sql);
  }

  @SuppressWarnings("unchecked")
  public This define(String key, Object value) {
    getAttributeBinding().define(key, value);
    return (This) this;
  }

  public This bind(int position, byte value) {
    return bind(position, byte.class, value);
  }

  public This bind(String name, byte value) {
    return bind(name, byte.class, value);
  }

  public This bind(int position, Byte value) {
    return bind(position, Byte.class, value);
  }

  public This bind(String name, Byte value) {
    return bind(name, Byte.class, value);
  }

  public This bind(int position, short value) {
    return bind(position, short.class, value);
  }

  public This bind(String name, short value) {
    return bind(name, short.class, value);
  }

  public This bind(int position, Short value) {
    return bind(position, Short.class, value);
  }

  public This bind(String name, Short value) {
    return bind(name, Short.class, value);
  }

  public This bind(int position, int value) {
    return bind(position, int.class, value);
  }

  public This bind(String name, int value) {
    return bind(name, int.class, value);
  }

  public This bind(int position, Integer value) {
    return bind(position, Integer.class, value);
  }

  public This bind(String name, Integer value) {
    return bind(name, Integer.class, value);
  }

  public This bind(int position, long value) {
    return bind(position, long.class, value);
  }

  public This bind(String name, long value) {
    return bind(name, long.class, value);
  }

  public This bind(int position, Long value) {
    return bind(position, Long.class, value);
  }

  public This bind(String name, Long value) {
    return bind(name, Long.class, value);
  }

  public This bind(int position, float value) {
    return bind(position, float.class, value);
  }

  public This bind(String name, float value) {
    return bind(name, float.class, value);
  }

  public This bind(int position, Float value) {
    return bind(position, Float.class, value);
  }

  public This bind(String name, Float value) {
    return bind(name, Float.class, value);
  }

  public This bind(int position, double value) {
    return bind(position, double.class, value);
  }

  public This bind(String name, double value) {
    return bind(name, double.class, value);
  }

  public This bind(int position, Double value) {
    return bind(position, Double.class, value);
  }

  public This bind(String name, Double value) {
    return bind(name, Double.class, value);
  }

  public This bind(int position, boolean value) {
    return bind(position, boolean.class, value);
  }

  public This bind(String name, boolean value) {
    return bind(name, boolean.class, value);
  }

  public This bind(int position, Boolean value) {
    return bind(position, Boolean.class, value);
  }

  public This bind(String name, Boolean value) {
    return bind(name, Boolean.class, value);
  }

  public This bind(int position, char value) {
    return bind(position, char.class, value);
  }

  public This bind(String name, char value) {
    return bind(name, char.class, value);
  }

  public This bind(int position, Character value) {
    return bind(position, Character.class, value);
  }

  public This bind(String name, Character value) {
    return bind(name, Character.class, value);
  }

  public This bind(int position, String value) {
    return bind(position, String.class, value);
  }

  public This bind(String name, String value) {
    return bind(name, String.class, value);
  }

  public This bind(int position, Date value) {
    return bind(position, Date.class, value);
  }

  public This bind(String name, Date value) {
    return bind(name, Date.class, value);
  }

  public This bind(int position, Time value) {
    return bind(position, Time.class, value);
  }

  public This bind(String name, Time value) {
    return bind(name, Time.class, value);
  }

  public This bind(int position, Timestamp value) {
    return bind(position, Timestamp.class, value);
  }

  public This bind(String name, Timestamp value) {
    return bind(name, Timestamp.class, value);
  }

  public This bind(int position, URI value) {
    return bind(position, URI.class, value);
  }

  public This bind(String name, URI value) {
    return bind(name, URI.class, value);
  }

  public This bind(int position, URL value) {
    return bind(position, URL.class, value);
  }

  public This bind(String name, URL value) {
    return bind(name, URL.class, value);
  }

  public This bind(int position, UUID value) {
    return bind(position, UUID.class, value);
  }

  public This bind(String name, UUID value) {
    return bind(name, UUID.class, value);
  }

  public This bind(int position, Blob value) {
    return bind(position, Blob.class, value);
  }

  public This bind(String name, Blob value) {
    return bind(name, Blob.class, value);
  }

  public This bind(int position, Clob value) {
    return bind(position, Clob.class, value);
  }

  public This bind(String name, Clob value) {
    return bind(name, Clob.class, value);
  }

  public This bind(int position, byte[] value) {
    return bind(position, byte[].class, value);
  }

  public This bind(String name, byte[] value) {
    return bind(name, byte[].class, value);
  }

  public This bind(int position, InputStream value) {
    return bind(position, InputStream.class, value);
  }

  public This bind(String name, InputStream value) {
    return bind(name, InputStream.class, value);
  }

  public This bind(int position, Reader value) {
    return bind(position, Reader.class, value);
  }

  public This bind(String name, Reader value) {
    return bind(name, Reader.class, value);
  }

  private This bind(int position, Class<?> type, Object value) {
    return bindByType(position, type, value);
  }

  private This bind(String name, Class<?> type, Object value) {
    return bindByType(name, type, value);
  }

  @SuppressWarnings("unchecked")
  private This bindByType(int position, Type type, Object value) {
    QueryParameterValue argumentValue = getConfig(Arguments.class).findArgumentFactoryFor(type)
        .orElseThrow(() -> new NoSuchMapperException("No mapper for type " + type))
        .map(type, value, getHandle().getConfig());

    getArgumentBinding().addPositional(position, argumentValue);

    return (This) this;
  }

  @SuppressWarnings("unchecked")
  private This bindByType(String name, Type type, Object value) {
    QueryParameterValue argumentValue = getConfig(Arguments.class).findArgumentFactoryFor(type)
        .orElseThrow(() -> new NoSuchMapperException("No mapper for type " + type))
        .map(type, value, getHandle().getConfig());

    getArgumentBinding().addNamed(name, argumentValue);

    return (This) this;
  }

  public <C extends JdbqConfig<C>> C getConfig(Class<C> configClass) {
    return getContext().getConfig(configClass);
  }

  ResultSet internalExecute() {
    final StatementContext ctx = getContext();

    beforeTemplating();

    ParsedSql parsedSql = parseSql();

    try {
      stmt = createStatement(ctx, parsedSql);
    } catch (Exception e) {
      throw new UnableToCreateStatementException(e, ctx);
    }

    ctx.setStatement(stmt);

    beforeBinding();

    if (getArgumentBinding().isEmpty()) {
      // Well, that wasy easy.
    } else if (getArgumentBinding().isNamed()) {
      for (Map.Entry<String, QueryParameterValue> entry : getArgumentBinding().getNamed()
          .entrySet()) {
        String parameterName = entry.getKey();
        QueryParameterValue parameterValue = entry.getValue();
        stmt.addNamedParameter(parameterName, parameterValue);
      }
    } else if (getArgumentBinding().isPositional()) {
      for (int parameterPosition = 0; parameterPosition < getArgumentBinding().getPositionals()
          .size(); parameterPosition++) {
        QueryParameterValue parameterValue =
            getArgumentBinding().getPositionals().get(parameterPosition);
        if (parameterValue == null)
          throw new UnableToCreateStatementException(
              "No parameter for position " + parameterPosition);
        stmt.addPositionalParameter(parameterValue);
      }
    } else {
      throw new AssertionError("unrecognized binding type " + getArgumentBinding());
    }

    beforeExecution();

    ResultSet result;
    try {
      result = new FieldValueListsResultSet(getHandle().getClient().create(JobInfo.of(stmt.build()))
          .waitFor().getQueryResults().getValues());
    } catch (InterruptedException e) {
      throw new UnableToExecuteStatementException(e, ctx);
    }

    afterExecution();

    return result;
  }

  private QueryJobConfiguration.Builder createStatement(final StatementContext ctx,
      ParsedSql parsedSql) {
    return getConfig(SqlStatements.class).getStatementBuilder().create(parsedSql.getSql(), ctx);
  }

  private ParsedSql parseSql() {
    String renderedSql =
        getConfig(SqlStatements.class).getTemplateEngine().render(sql, getContext());
    getContext().setRenderedSql(renderedSql);

    ParsedSql parsedSql =
        getConfig(SqlStatements.class).getSqlParser().parse(renderedSql, getContext());
    getContext().setParsedSql(parsedSql);

    return parsedSql;
  }

  private Collection<StatementCustomizer> getCustomizers() {
    return this.getConfig(SqlStatements.class).getCustomizers();
  }

  @SuppressWarnings("resource")
  private void callCustomizers(Consumer<StatementCustomizer> invocation) {
    for (StatementCustomizer customizer : getCustomizers()) {
      try {
        invocation.accept(customizer);
      } catch (Exception e) {
        throw new UnableToExecuteStatementException("Exception thrown in statement customization",
            e, getContext());
      }
    }
  }

  private void beforeTemplating() {
    callCustomizers(c -> c.beforeTemplating(stmt, getContext()));
  }

  private void beforeBinding() {
    callCustomizers(c -> c.beforeBinding(stmt, getContext()));
  }

  private void beforeExecution() {
    callCustomizers(c -> c.beforeExecution(stmt, getContext()));
  }

  private void afterExecution() {
    callCustomizers(c -> c.afterExecution(stmt, getContext()));
  }

  private ArgumentBinding getArgumentBinding() {
    return getContext().getArgumentBinding();
  }

  private AttributeBinding getAttributeBinding() {
    return getContext().getAttributeBinding();
  }
}

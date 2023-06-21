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
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.Handle;
import com.sigpwned.jdbq.argument.Arguments;
import com.sigpwned.jdbq.config.JdbqConfig;
import com.sigpwned.jdbq.parser.ParsedSql;
import com.sigpwned.jdbq.statement.exception.UnableToCleanupStatementException;
import com.sigpwned.jdbq.statement.exception.UnableToCreateStatementException;
import com.sigpwned.jdbq.statement.exception.UnableToExecuteStatementException;
import com.sigpwned.jdbq.statement.exception.UnableToSetupStatementException;
import io.leangen.geantyref.TypeFactory;

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
    return bindByType(position, byte.class, value);
  }

  public This bind(String name, byte value) {
    return bindByType(name, byte.class, value);
  }

  public This bind(int position, Byte value) {
    return bindByType(position, Byte.class, value);
  }

  public This bind(String name, Byte value) {
    return bindByType(name, Byte.class, value);
  }

  public This bind(int position, short value) {
    return bindByType(position, short.class, value);
  }

  public This bind(String name, short value) {
    return bindByType(name, short.class, value);
  }

  public This bind(int position, Short value) {
    return bindByType(position, Short.class, value);
  }

  public This bind(String name, Short value) {
    return bindByType(name, Short.class, value);
  }

  public This bind(int position, int value) {
    return bindByType(position, int.class, value);
  }

  public This bind(String name, int value) {
    return bindByType(name, int.class, value);
  }

  public This bind(int position, Integer value) {
    return bindByType(position, Integer.class, value);
  }

  public This bind(String name, Integer value) {
    return bindByType(name, Integer.class, value);
  }

  public This bind(int position, long value) {
    return bindByType(position, long.class, value);
  }

  public This bind(String name, long value) {
    return bindByType(name, long.class, value);
  }

  public This bind(int position, Long value) {
    return bindByType(position, Long.class, value);
  }

  public This bind(String name, Long value) {
    return bindByType(name, Long.class, value);
  }

  public This bind(int position, float value) {
    return bindByType(position, float.class, value);
  }

  public This bind(String name, float value) {
    return bindByType(name, float.class, value);
  }

  public This bind(int position, Float value) {
    return bindByType(position, Float.class, value);
  }

  public This bind(String name, Float value) {
    return bindByType(name, Float.class, value);
  }

  public This bind(int position, double value) {
    return bindByType(position, double.class, value);
  }

  public This bind(String name, double value) {
    return bindByType(name, double.class, value);
  }

  public This bind(int position, Double value) {
    return bindByType(position, Double.class, value);
  }

  public This bind(String name, Double value) {
    return bindByType(name, Double.class, value);
  }

  public This bind(int position, boolean value) {
    return bindByType(position, boolean.class, value);
  }

  public This bind(String name, boolean value) {
    return bindByType(name, boolean.class, value);
  }

  public This bind(int position, Boolean value) {
    return bindByType(position, Boolean.class, value);
  }

  public This bind(String name, Boolean value) {
    return bindByType(name, Boolean.class, value);
  }

  public This bind(int position, char value) {
    return bindByType(position, char.class, value);
  }

  public This bind(String name, char value) {
    return bindByType(name, char.class, value);
  }

  public This bind(int position, Character value) {
    return bindByType(position, Character.class, value);
  }

  public This bind(String name, Character value) {
    return bindByType(name, Character.class, value);
  }

  public This bind(int position, String value) {
    return bindByType(position, String.class, value);
  }

  public This bind(String name, String value) {
    return bindByType(name, String.class, value);
  }

  public This bind(int position, LocalDate value) {
    return bindByType(position, LocalDate.class, value);
  }

  public This bind(String name, LocalDate value) {
    return bindByType(name, LocalDate.class, value);
  }

  public This bind(int position, LocalTime value) {
    return bindByType(position, LocalTime.class, value);
  }

  public This bind(String name, LocalTime value) {
    return bindByType(name, LocalTime.class, value);
  }

  public This bind(int position, Instant value) {
    return bindByType(position, Instant.class, value);
  }

  public This bind(String name, Instant value) {
    return bindByType(name, Instant.class, value);
  }

  public This bind(int position, URI value) {
    return bindByType(position, URI.class, value);
  }

  public This bind(String name, URI value) {
    return bindByType(name, URI.class, value);
  }

  public This bind(int position, URL value) {
    return bindByType(position, URL.class, value);
  }

  public This bind(String name, URL value) {
    return bindByType(name, URL.class, value);
  }

  public This bind(int position, UUID value) {
    return bindByType(position, UUID.class, value);
  }

  public This bind(String name, UUID value) {
    return bindByType(name, UUID.class, value);
  }

  public This bind(int position, byte[] value) {
    return bindByType(position, byte[].class, value);
  }

  public This bind(String name, byte[] value) {
    return bindByType(name, byte[].class, value);
  }

  @SuppressWarnings("unchecked")
  public This bindByType(int position, Type type, Object value) {
    getArgumentBinding().addPositional(position, type, value);

    return (This) this;
  }

  @SuppressWarnings("unchecked")
  public This bindByType(String name, Type type, Object value) {
    getArgumentBinding().addNamed(name, type, value);
    return (This) this;
  }

  @SuppressWarnings("unchecked")
  public <T> This bindArray(int position, T... array) {
    return bindArray(position, array.getClass().getComponentType(), array);
  }

  @SuppressWarnings("unchecked")
  public <T> This bindArray(String name, T... array) {
    return bindArray(name, array.getClass().getComponentType(), array);
  }

  public This bindArray(int position, Type elementType, Object... array) {
    return bindByType(position, TypeFactory.arrayOf(elementType), array);
  }

  public This bindArray(String name, Type elementType, Object... array) {
    return bindByType(name, TypeFactory.arrayOf(elementType), array);
  }

  public This bindArray(int position, Type elementType, Iterable<?> iterable) {
    return bindByType(position, TypeFactory.parameterizedClass(Iterable.class, elementType),
        iterable);
  }

  public This bindArray(String name, Type elementType, Iterable<?> iterable) {
    return bindByType(name, TypeFactory.parameterizedClass(Iterable.class, elementType), iterable);
  }

  public <C extends JdbqConfig<C>> C getConfig(Class<C> configClass) {
    return getContext().getConfig(configClass);
  }

  /**
   * Returns the completed results of the query. If returned, the job completed successfully. No
   * further method calls should throw and InterruptedException.
   * 
   * @throws UnableToExecuteStatementException if a BigQuery exception is thrown
   * @throws UncheckedIOException with cause InterrupedIOException in case interrupted. The current
   *         thread is re-interrupted first.
   */
  Job internalExecute() {
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
      // Well, that was easy.
    } else if (getArgumentBinding().isNamed()) {
      for (Map.Entry<String, Argument> entry : getArgumentBinding().getNamed().entrySet()) {
        String parameterName = entry.getKey();
        Argument argument = entry.getValue();
        QueryParameterValue parameterValue = getConfig(Arguments.class).map(argument.getType(),
            argument.getValue(), ctx.getConfig());
        if (parameterValue == null)
          throw new UnableToCreateStatementException("No parameter for name " + parameterName);
        stmt.addNamedParameter(parameterName, parameterValue);
      }
    } else if (getArgumentBinding().isPositional()) {
      for (int parameterPosition = 0; parameterPosition < getArgumentBinding().getPositionals()
          .size(); parameterPosition++) {
        Argument argument = getArgumentBinding().getPositionals().get(parameterPosition);
        QueryParameterValue parameterValue = getConfig(Arguments.class).map(argument.getType(),
            argument.getValue(), ctx.getConfig());
        if (parameterValue == null)
          throw new UnableToCreateStatementException(
              "No parameter for position " + parameterPosition);
        stmt.addPositionalParameter(parameterValue);
      }
    } else {
      throw new AssertionError("unrecognized binding type " + getArgumentBinding());
    }

    beforeExecution();

    Job result;
    try {
      result = getHandle().getClient().create(JobInfo.of(stmt.build())).waitFor();
    } catch (InterruptedException e) {
      // This is a titch awkward. We don't want to make every caller handle InterruptedException,
      // but we do need to throw something...
      Thread.currentThread().interrupt();
      throw new UncheckedIOException(new InterruptedIOException());
    } catch (BigQueryException e) {
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

  private void callCustomizers(Consumer<StatementCustomizer> invocation) {
    for (StatementCustomizer customizer : getCustomizers()) {
      invocation.accept(customizer);
    }
  }

  private void beforeTemplating() {
    try {
      callCustomizers(c -> c.beforeTemplating(stmt, getContext()));
    } catch (Exception e) {
      throw new UnableToSetupStatementException(e, getContext());
    }
  }

  private void beforeBinding() {
    try {
      callCustomizers(c -> c.beforeBinding(stmt, getContext()));
    } catch (Exception e) {
      throw new UnableToSetupStatementException(e, getContext());
    }
  }

  private void beforeExecution() {
    try {
      callCustomizers(c -> c.beforeExecution(stmt, getContext()));
    } catch (Exception e) {
      throw new UnableToSetupStatementException(e, getContext());
    }
  }

  private void afterExecution() {
    try {
      callCustomizers(c -> c.afterExecution(stmt, getContext()));
    } catch (Exception e) {
      throw new UnableToCleanupStatementException(e, getContext());
    }
  }

  private ArgumentBinding getArgumentBinding() {
    return getContext().getArgumentBinding();
  }

  private AttributeBinding getAttributeBinding() {
    return getContext().getAttributeBinding();
  }
}

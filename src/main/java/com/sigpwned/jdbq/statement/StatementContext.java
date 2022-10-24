package com.sigpwned.jdbq.statement;

import static java.util.Objects.requireNonNull;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.sigpwned.jdbq.CloseException;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.config.JdbqConfig;
import com.sigpwned.jdbq.parser.ParsedSql;

public class StatementContext implements Closeable {
  private final ConfigRegistry config;
  private final Set<Cleanable> cleanables;
  private String rawSql;
  private String renderedSql;
  private ParsedSql parsedSql;
  private ArgumentBinding argumentBinding;
  private AttributeBinding attributeBinding;
  private QueryJobConfiguration.Builder statement;

  public StatementContext(ConfigRegistry config) {
    this.config = requireNonNull(config);
    this.cleanables = new LinkedHashSet<>();
  }

  /**
   * @return the rawSql
   */
  public String getRawSql() {
    return rawSql;
  }

  /**
   * @param rawSql the rawSql to set
   */
  void setRawSql(String rawSql) {
    this.rawSql = rawSql;
  }

  /**
   * @return the renderedSql
   */
  public String getRenderedSql() {
    return renderedSql;
  }

  /**
   * @param renderedSql the renderedSql to set
   */
  void setRenderedSql(String renderedSql) {
    this.renderedSql = renderedSql;
  }

  /**
   * @return the parsedSql
   */
  public ParsedSql getParsedSql() {
    return parsedSql;
  }

  /**
   * @param parsedSql the parsedSql to set
   */
  void setParsedSql(ParsedSql parsedSql) {
    this.parsedSql = parsedSql;
  }

  /**
   * @return the statement
   */
  public QueryJobConfiguration.Builder getStatement() {
    return statement;
  }

  /**
   * @param statement the statement to set
   */
  void setStatement(QueryJobConfiguration.Builder statement) {
    this.statement = statement;
  }

  void addCleanable(Cleanable cleanable) {
    cleanables.add(cleanable);
  }

  /**
   * Gets the configuration object of the given type, associated with this context.
   *
   * @param configClass the configuration type
   * @param <C> the configuration type
   * @return the configuration object of the given type, associated with this context.
   */
  public <C extends JdbqConfig<C>> C getConfig(Class<C> configClass) {
    return config.get(configClass);
  }

  @Override
  public void close() {
    List<Cleanable> cleanablesCopy = new ArrayList<>(cleanables);
    cleanables.clear();
    Collections.reverse(cleanablesCopy);

    RuntimeException exception = null;
    for (Cleanable cleanable : cleanablesCopy) {
      try {
        cleanable.close();
      } catch (RuntimeException e) {
        if (exception == null) {
          exception = e;
        } else {
          exception.addSuppressed(e);
        }
      }
    }

    if (exception != null) {
      throw new CloseException("Exception thrown while cleaning StatementContext", exception);
    }
  }

  /**
   * Returns the {@code ConfigRegistry}.
   *
   * @return the {@code ConfigRegistry} used by this context.
   */
  public ConfigRegistry getConfig() {
    return config;
  }

  public ArgumentBinding getArgumentBinding() {
    return argumentBinding;
  }

  public AttributeBinding getAttributeBinding() {
    return attributeBinding;
  }
}

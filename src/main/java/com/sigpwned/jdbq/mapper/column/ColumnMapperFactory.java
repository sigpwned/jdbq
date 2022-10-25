package com.sigpwned.jdbq.mapper.column;

import java.lang.reflect.Type;
import java.util.Optional;
import com.sigpwned.jdbq.config.ConfigRegistry;

/**
 * Factory interface used to produce column mappers.
 */
@FunctionalInterface
public interface ColumnMapperFactory {
  /**
   * Supplies a column mapper which will map columns to type if the factory supports it; empty
   * otherwise.
   *
   * @param type the target type to map to
   * @param config the config registry, for composition
   * @return a column mapper for the given type if this factory supports it, or
   *         <code>Optional.empty()</code> otherwise.
   * @see ColumnMappers for composition
   */
  Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config);

  /**
   * Create a ColumnMapperFactory from a given {@link ColumnMapper} that matches a single Type
   * exactly.
   *
   * @param type the type to match with equals.
   * @param mapper the mapper to return
   * @return the factory
   */
  static ColumnMapperFactory of(Type type, ColumnMapper<?> mapper) {
    return (t, c) -> t.equals(type) ? Optional.of(mapper) : Optional.empty();
  }
}

package com.sigpwned.jdbq.mapper.row;

import java.lang.reflect.Type;
import java.util.Optional;
import com.sigpwned.jdbq.config.ConfigRegistry;

/**
 * Factory interface used to produce row mappers.
 */
@FunctionalInterface
public interface RowMapperFactory {
  /**
   * Supplies a row mapper which will map result set rows to type if the factory supports it; empty
   * otherwise.
   *
   * @param type the target type to map to
   * @param config the config registry, for composition
   * @return a row mapper for the given type if this factory supports it;
   *         <code>Optional.empty()</code> otherwise.
   * @see RowMappers for composition
   */
  Optional<RowMapper<?>> build(Type type, ConfigRegistry config);

  /**
   * Create a RowMapperFactory from a given {@link RowMapper} that matches a {@link Type} exactly.
   *
   * @param type the type to match with equals.
   * @param mapper the mapper to return
   *
   * @return the factory
   */
  static RowMapperFactory of(Type type, RowMapper<?> mapper) {
    return (t, ctx) -> t.equals(type) ? Optional.of(mapper) : Optional.empty();
  }
}

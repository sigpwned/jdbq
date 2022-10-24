package com.sigpwned.jdbq.mapper.row;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.sigpwned.jdbq.config.JdbqConfig;

public class RowMappers implements JdbqConfig<RowMappers> {
  private final ConcurrentMap<Type, RowMapper<?>> rowMappers;

  public RowMappers() {
    this.rowMappers = new ConcurrentHashMap<>();
  }

  private RowMappers(RowMappers that) {
    this.rowMappers = new ConcurrentHashMap<>(that.rowMappers);
  }

  public <T> void addRowMapper(Type type, RowMapper<T> mapper) {
    getRowMappers().put(type, mapper);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public <T> Optional<RowMapper<T>> findRowMapper(Type type) {
    return Optional.ofNullable((RowMapper) getRowMappers().get(type));
  }

  /**
   * @return the rowMappers
   */
  private ConcurrentMap<Type, RowMapper<?>> getRowMappers() {
    return rowMappers;
  }

  @Override
  public RowMappers createCopy() {
    return new RowMappers(this);
  }
}

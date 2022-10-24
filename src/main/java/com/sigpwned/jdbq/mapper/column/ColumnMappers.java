package com.sigpwned.jdbq.mapper.column;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.sigpwned.jdbq.config.JdbqConfig;

public class ColumnMappers implements JdbqConfig<ColumnMappers> {
  private final ConcurrentMap<Type, ColumnMapper<?>> columnMappers;

  public ColumnMappers() {
    this.columnMappers = new ConcurrentHashMap<>();
  }

  private ColumnMappers(ColumnMappers that) {
    this.columnMappers = new ConcurrentHashMap<>(that.columnMappers);
  }

  public <T> void addColumnMapper(Type type, ColumnMapper<T> mapper) {
    getColumnMappers().put(type, mapper);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public <T> Optional<ColumnMapper<T>> findColumnMapper(Type type) {
    return Optional.ofNullable((ColumnMapper) getColumnMappers().get(type));
  }

  /**
   * @return the rowMappers
   */
  private ConcurrentMap<Type, ColumnMapper<?>> getColumnMappers() {
    return columnMappers;
  }

  @Override
  public ColumnMappers createCopy() {
    return new ColumnMappers(this);
  }
}

package com.sigpwned.jdbq.result;

import static java.util.Objects.requireNonNull;
import com.sigpwned.jdbq.mapper.row.RowMapper;
import com.sigpwned.jdbq.statement.StatementContext;

public class FieldValueListsResultIterable<T> implements ResultIterable<T> {
  private final ResultSet results;
  private final RowMapper<T> mapper;
  private final StatementContext ctx;

  public FieldValueListsResultIterable(ResultSet results, RowMapper<T> mapper,
      StatementContext ctx) {
    this.results = requireNonNull(results);
    this.mapper = requireNonNull(mapper);
    this.ctx = requireNonNull(ctx);
  }

  @Override
  public ResultIterator<T> iterator() {
    return new FieldValueListsResultIterator<>(getResults().iterator(), getMapper(), getContext());
  }

  /**
   * @return the context
   */
  private StatementContext getContext() {
    return ctx;
  }

  /**
   * @return the results
   */
  private ResultSet getResults() {
    return results;
  }

  /**
   * @return the mapper
   */
  private RowMapper<T> getMapper() {
    return mapper;
  }
}

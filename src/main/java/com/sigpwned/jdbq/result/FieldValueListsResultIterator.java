package com.sigpwned.jdbq.result;

import static java.util.Objects.requireNonNull;
import java.util.Iterator;
import com.google.cloud.bigquery.FieldValueList;
import com.sigpwned.jdbq.mapper.row.RowMapper;
import com.sigpwned.jdbq.statement.StatementContext;

public class FieldValueListsResultIterator<T> implements ResultIterator<T> {
  private final Iterator<FieldValueList> delegate;
  private final RowMapper<T> mapper;
  private final StatementContext ctx;

  public FieldValueListsResultIterator(Iterator<FieldValueList> delegate, RowMapper<T> mapper,
      StatementContext ctx) {
    this.delegate = requireNonNull(delegate);
    this.mapper = requireNonNull(mapper);
    this.ctx = requireNonNull(ctx);
  }

  @Override
  public boolean hasNext() {
    return getDelegate().hasNext();
  }

  @Override
  public T next() {
    return getMapper().map(getDelegate().next(), getContext());
  }

  @Override
  public void close() {
    getContext().close();
  }

  /**
   * @return the context
   */
  @Override
  public StatementContext getContext() {
    return ctx;
  }

  /**
   * @return the iterator
   */
  private Iterator<FieldValueList> getDelegate() {
    return delegate;
  }

  /**
   * @return the mapper
   */
  private RowMapper<T> getMapper() {
    return mapper;
  }
}

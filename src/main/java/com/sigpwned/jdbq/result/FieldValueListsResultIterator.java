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

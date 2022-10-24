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
/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sigpwned.jdbq.mapper.column;

import com.google.cloud.bigquery.FieldValueList;
import com.sigpwned.jdbq.mapper.row.RowMapper;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Adapts a {@link ColumnMapper} into a {@link RowMapper} by mapping a single column.
 */
public class SingleColumnMapper<T> implements RowMapper<T> {
  private final RowMapper<T> delegate;

  /**
   * Constructs a row mapper which maps the first column.
   * 
   * @param mapper the column mapper to delegate to for mapping.
   */
  public SingleColumnMapper(ColumnMapper<T> mapper) {
    this(mapper, 0);
  }

  /**
   * Constructs a row mapper which maps the given column number.
   * 
   * @param mapper the column mapper to delegate to for mapping
   * @param columnNumber the column number (0-based) to map
   */
  public SingleColumnMapper(ColumnMapper<T> mapper, int columnNumber) {
    this.delegate = (row, ctx) -> mapper.map(row, columnNumber, ctx);
  }

  /**
   * Constructs a row mapper which maps the column with the given label.
   * 
   * @param mapper the column mapper to delegate to for mapping
   * @param columnLabel the label of the column to map
   */
  public SingleColumnMapper(ColumnMapper<T> mapper, String columnLabel) {
    this.delegate = (row, ctx) -> mapper.map(row, columnLabel, ctx);
  }

  @Override
  public T map(FieldValueList row, StatementContext ctx) {
    return delegate.map(row, ctx);
  }
}

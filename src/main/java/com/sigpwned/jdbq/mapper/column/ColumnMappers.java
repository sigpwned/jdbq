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

/*-
 * =================================LICENSE_START==================================
 * jdbq
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import com.google.cloud.bigquery.FieldValue;
import com.sigpwned.jdbq.statement.StatementContext;

public class CollectorColumnMapper<T, A, R> implements ColumnMapper<R> {
  private final ColumnMapper<T> elementMapper;
  private final Collector<T, A, R> collector;

  public CollectorColumnMapper(ColumnMapper<T> elementMapper, Collector<T, A, R> collector) {
    this.elementMapper = elementMapper;
    this.collector = collector;
  }

  @Override
  public R map(FieldValue value, StatementContext ctx) {
    List<FieldValue> vs = value.getRepeatedValue();

    A result = collector.supplier().get();
    BiConsumer<A, T> accumulator = collector.accumulator();
    for (FieldValue v : vs)
      accumulator.accept(result, elementMapper.map(v, ctx));

    return collector.finisher().apply(result);
  }
}

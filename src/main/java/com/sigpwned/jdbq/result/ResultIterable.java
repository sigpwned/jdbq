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
package com.sigpwned.jdbq.result;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.sigpwned.jdbq.mapper.row.RowMapper;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * An {@link Iterable} of values, usually mapped from a {@link java.sql.ResultSet}. Generally,
 * ResultIterables may only be traversed once.
 *
 * @param <T> iterable element type
 */
@FunctionalInterface
public interface ResultIterable<T> extends Iterable<T> {
  /**
   * Returns a ResultIterable backed by the given result set supplier, mapper, and context.
   *
   * @param supplier result set supplier
   * @param mapper row mapper
   * @param ctx statement context
   * @param <T> the mapped type
   * @return the result iterable
   */
  static <T> ResultIterable<T> of(Supplier<ResultSet> supplier, RowMapper<T> mapper,
      StatementContext ctx) {
    return new FieldValueListsResultIterable<>(supplier.get(), mapper, ctx);
  }

  /**
   * Returns a ResultIterable backed by the given iterator.
   * 
   * @param iterator the result iterator
   * @param <T> iterator element type
   * @return a ResultIterable
   */
  static <T> ResultIterable<T> of(ResultIterator<T> iterator) {
    return () -> iterator;
  }

  /**
   * Stream all the rows of the result set out with an {@code Iterator}. The {@code Iterator} must
   * be closed to release database resources.
   * 
   * @return the results as a streaming Iterator
   */
  @Override
  ResultIterator<T> iterator();

  /**
   * Returns a {@code ResultIterable<U>} derived from this {@code ResultIterable<T>}, by
   * transforming elements using the given mapper function.
   *
   * @param mapper function to apply to elements of this ResultIterable
   * @param <R> Element type of the returned ResultIterable
   * @return the new ResultIterable
   */
  default <R> ResultIterable<R> map(Function<? super T, ? extends R> mapper) {
    return () -> new ResultIteratorDelegate<T, R>(iterator()) {
      @Override
      public R next() {
        return mapper.apply(getDelegate().next());
      }
    };
  }

  @Override
  default void forEach(Consumer<? super T> action) {
    forEachWithCount(action);
  }

  /**
   * Performs the specified action on each remaining element and returns the iteration i.e. record
   * count.<br>
   * It is often useful (e.g. for logging) to know the record count while processing result sets.
   * 
   * <pre>
   * {@code
   * int cnt = h.createQuery("select * from something").mapTo(String.class)
   *     .forEachWithCount(System.out::println);
   * System.out.println(cnt + " records selected");
   * }
   *  </pre>
   *
   * @param action action to apply (required)
   * @return iteration count
   *
   * @since 3.31
   */
  default int forEachWithCount(Consumer<? super T> action) {
    Objects.requireNonNull(action, "Action required");
    try (ResultIterator<T> iter = iterator()) {
      int count = 0;
      while (iter.hasNext()) {
        count++;
        action.accept(iter.next());
      }
      return count;
    }
  }

  /**
   * Returns the only row in the result set. Returns {@code null} if the row itself is {@code null}.
   * 
   * @throws IllegalStateException if the result set contains zero or multiple rows
   * @return the only row in the result set.
   */
  default T one() {
    try (ResultIterator<T> iter = iterator()) {
      if (!iter.hasNext()) {
        throw new IllegalStateException("Expected one element, but found none");
      }

      final T r = iter.next();

      if (iter.hasNext()) {
        throw new IllegalStateException("Expected one element, but found multiple");
      }

      return r;
    }
  }

  /**
   * Returns the only row in the result set, if any. Returns {@code Optional.empty()} if zero rows
   * are returned, or if the row itself is {@code null}.
   * 
   * @throws IllegalStateException if the result set contains multiple rows
   * @return the only row in the result set, if any.
   */
  default Optional<T> findOne() {
    try (ResultIterator<T> iter = iterator()) {
      if (!iter.hasNext()) {
        return Optional.empty();
      }

      final T r = iter.next();

      if (iter.hasNext()) {
        throw new IllegalStateException("Expected zero to one elements, but found multiple");
      }

      return Optional.ofNullable(r);
    }
  }

  /**
   * Get the only row in the result set.
   * 
   * @throws IllegalStateException if zero or multiple rows are returned
   * @return the object mapped from the singular row in the results
   * @deprecated use {@link #one()} or {@link #findOne()} instead.
   */
  @Deprecated
  default T findOnly() {
    return one();
  }

  /**
   * Returns the first row in the result set. Returns {@code null} if the row itself is
   * {@code null}.
   * 
   * @throws IllegalStateException if zero rows are returned
   * @return the first row in the result set.
   */
  default T first() {
    try (ResultIterator<T> iter = iterator()) {
      if (!iter.hasNext()) {
        throw new IllegalStateException("Expected at least one element, but found none");
      }

      return iter.next();
    }
  }

  /**
   * Returns the first row in the result set, if present. Returns {@code Optional.empty()} if zero
   * rows are returned or the first row is {@code null}.
   * 
   * @return the first row in the result set, if present.
   */
  default Optional<T> findFirst() {
    try (ResultIterator<T> iter = iterator()) {
      return iter.hasNext() ? Optional.ofNullable(iter.next()) : Optional.empty();
    }
  }

  /**
   * Returns the stream of results.
   *
   * <p>
   * Note: the returned stream owns database resources, and must be closed via a call to
   * {@link Stream#close()}, or by using the stream in a try-with-resources block:
   * </p>
   *
   * <pre>
   * try (Stream&lt;T&gt; stream = query.stream()) {
   *   // do stuff with stream
   * }
   * </pre>
   *
   * @return the stream of results.
   *
   * @see #useStream(StreamConsumer)
   * @see #withStream(StreamCallback)
   */
  default Stream<T> stream() {
    ResultIterator<T> iterator = iterator();
    return StreamSupport.stream(spliteratorUnknownSize(iterator, 0), false)
        .onClose(iterator::close);
  }

  /**
   * Returns results in a {@link List}.
   *
   * @return results in a {@link List}.
   */
  default List<T> list() {
    return stream().collect(toList());
  }

  /**
   * An implementation of {@link ResultIterator} that delegates calls to the iterator provided in
   * the constructor.
   *
   * @param <T> iterable element type of delegate
   * @param <R> returned iterable element type, may be same as delegate's ({@code T})
   */
  abstract class ResultIteratorDelegate<T, R> implements ResultIterator<R> {
    private final ResultIterator<T> delegate;

    ResultIteratorDelegate(ResultIterator<T> del) {
      delegate = Objects.requireNonNull(del, "Delegate required");
    }

    @Override
    public boolean hasNext() {
      return delegate.hasNext();
    }

    @Override
    public final void close() {
      delegate.close();
    }

    @Override
    public final StatementContext getContext() {
      return delegate.getContext();
    }

    protected final ResultIterator<T> getDelegate() {
      return delegate;
    }
  }
}

package com.sigpwned.jdbq.internal;

import java.util.Optional;
import java.util.stream.Stream;

public final class Optionals {
  private Optionals() {}

  /**
   * Returns a {@link Stream} containing either the contents of the given {@link Optional} if
   * present, or nothing otherwise. This is the same as the method {@code Optional#stream()} in
   * JDK9+.
   */
  public static <T> Stream<T> stream(Optional<T> o) {
    return o.isPresent() ? Stream.of(o.get()) : Stream.empty();
  }
}

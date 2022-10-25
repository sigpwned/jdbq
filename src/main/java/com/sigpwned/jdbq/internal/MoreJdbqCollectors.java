package com.sigpwned.jdbq.internal;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public final class MoreJdbqCollectors {
  private MoreJdbqCollectors() {}

  private static final Supplier<Collector> SET_COLLECTOR = setFactory();

  private static Supplier<Collector> setFactory() {
    return () -> Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet);
  }

  @SuppressWarnings("unchecked")
  public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
    return SET_COLLECTOR.get();
  }

}

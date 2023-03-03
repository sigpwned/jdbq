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

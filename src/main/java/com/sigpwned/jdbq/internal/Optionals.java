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

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
package com.sigpwned.jdbq.mapper.row;

import java.lang.reflect.Type;
import java.util.Optional;
import com.sigpwned.jdbq.config.ConfigRegistry;

/**
 * Factory interface used to produce row mappers.
 */
@FunctionalInterface
public interface RowMapperFactory {
  /**
   * Supplies a row mapper which will map result set rows to type if the factory supports it; empty
   * otherwise.
   *
   * @param type the target type to map to
   * @param config the config registry, for composition
   * @return a row mapper for the given type if this factory supports it;
   *         <code>Optional.empty()</code> otherwise.
   * @see RowMappers for composition
   */
  Optional<RowMapper<?>> build(Type type, ConfigRegistry config);

  /**
   * Create a RowMapperFactory from a given {@link RowMapper} that matches a {@link Type} exactly.
   *
   * @param type the type to match with equals.
   * @param mapper the mapper to return
   *
   * @return the factory
   */
  static RowMapperFactory of(Type type, RowMapper<?> mapper) {
    return (t, ctx) -> t.equals(type) ? Optional.of(mapper) : Optional.empty();
  }
}

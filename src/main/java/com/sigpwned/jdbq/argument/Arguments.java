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
package com.sigpwned.jdbq.argument;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.sigpwned.jdbq.config.JdbqConfig;

public class Arguments implements JdbqConfig<Arguments> {
  private final ConcurrentMap<Type, ArgumentFactory<?>> argumentFactories;

  public Arguments() {
    this.argumentFactories = new ConcurrentHashMap<>();
  }

  private Arguments(Arguments that) {
    this.argumentFactories = new ConcurrentHashMap<>(that.argumentFactories);
  }

  public <T> void addArgumentFactory(Type type, ArgumentFactory<T> argumentFactory) {
    getArgumentFactories().put(type, argumentFactory);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> Optional<ArgumentFactory<T>> findArgumentFactoryFor(Type type) {
    return Optional.ofNullable((ArgumentFactory) getArgumentFactories().get(type));
  }

  /**
   * @return the argumentFactories
   */
  private ConcurrentMap<Type, ArgumentFactory<?>> getArgumentFactories() {
    return argumentFactories;
  }

  @Override
  public Arguments createCopy() {
    return new Arguments(this);
  }
}

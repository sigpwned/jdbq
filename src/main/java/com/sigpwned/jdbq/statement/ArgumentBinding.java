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
package com.sigpwned.jdbq.statement;

import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.Map;
import com.google.cloud.bigquery.QueryParameterValue;

public class ArgumentBinding {
  protected final Map<Integer, QueryParameterValue> positionals;
  protected final Map<String, QueryParameterValue> named;

  public ArgumentBinding() {
    this.positionals = new HashMap<>();
    this.named = new HashMap<>();
  }

  public boolean isPositional() {
    return !positionals.isEmpty();
  }

  /**
   * Bind a positional parameter at the given index (0-based).
   * 
   * @param position binding position
   * @param argument the argument to bind
   */
  public void addPositional(int position, QueryParameterValue argument) {
    if (isNamed())
      throw new IllegalStateException("Cannot add positional argument to named binding");
    positionals.put(position, argument);
  }

  Map<Integer, QueryParameterValue> getPositionals() {
    if (!isPositional())
      throw new IllegalStateException("not positional");
    return unmodifiableMap(positionals);
  }

  public boolean isNamed() {
    return !named.isEmpty();
  }

  /**
   * Bind a named parameter for the given name.
   * 
   * @param name bound argument name
   * @param argument the argument to bind
   */
  public void addNamed(String name, QueryParameterValue argument) {
    if (isPositional())
      throw new IllegalStateException("Cannot add named argument to positional binding");
    named.put(name, argument);
  }

  Map<String, QueryParameterValue> getNamed() {
    if (!isNamed())
      throw new IllegalStateException("not named");
    return unmodifiableMap(named);
  }

  /**
   * Remove all bindings from this Binding.
   */
  public void clear() {
    positionals.clear();
    named.clear();
  }

  /**
   * Returns whether any bindings exist.
   *
   * @return True if there are no bindings.
   */
  public boolean isEmpty() {
    return positionals.isEmpty() && named.isEmpty();
  }

  @Override
  public String toString() {
    return "Binding [positionals=" + positionals + ", named=" + named + "]";
  }
}

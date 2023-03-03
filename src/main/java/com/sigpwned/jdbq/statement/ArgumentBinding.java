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

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArgumentBinding {
  protected Map<Integer, Argument> positionals;
  protected Map<String, Argument> named;

  public ArgumentBinding() {
    // Intentially leave maps empty
  }

  public boolean isPositional() {
    return positionals != null && !positionals.isEmpty();
  }

  /**
   * Bind a positional parameter at the given index (0-based).
   * 
   * @param position binding position
   * @param argument the argument to bind
   */
  public void addPositional(int position, Type type, Object value) {
    if (isNamed())
      throw new IllegalStateException("Cannot add positional argument to named binding");
    if (type == null)
      throw new NullPointerException();
    if (positionals == null)
      positionals = new HashMap<>();
    positionals.put(position, new Argument() {
      @Override
      public Type getType() {
        return type;
      }

      @Override
      public Object getValue() {
        return value;
      }
    });
  }

  public Map<Integer, Argument> getPositionals() {
    if (!isPositional())
      throw new IllegalStateException("not positional");
    return Optional.ofNullable(positionals).map(Collections::unmodifiableMap)
        .orElseGet(Collections::emptyMap);
  }

  public boolean isNamed() {
    return named != null && !named.isEmpty();
  }

  /**
   * Bind a named parameter for the given name.
   * 
   * @param name bound argument name
   * @param argument the argument to bind
   */
  public void addNamed(String name, Type type, Object value) {
    if (isPositional())
      throw new IllegalStateException("Cannot add named argument to positional binding");
    if (type == null)
      throw new NullPointerException();
    if (named == null)
      named = new HashMap<>();
    named.put(name, new Argument() {
      @Override
      public Type getType() {
        return type;
      }

      @Override
      public Object getValue() {
        return value;
      }
    });
  }

  public Map<String, Argument> getNamed() {
    if (!isNamed())
      throw new IllegalStateException("not named");
    return Optional.ofNullable(named).map(Collections::unmodifiableMap)
        .orElseGet(Collections::emptyMap);
  }

  /**
   * Remove all bindings from this Binding.
   */
  public void clear() {
    positionals = null;
    named = null;
  }

  /**
   * Returns whether any bindings exist.
   *
   * @return True if there are no bindings.
   */
  public boolean isEmpty() {
    return (positionals == null || positionals.isEmpty()) && (named == null || named.isEmpty());
  }

  @Override
  public String toString() {
    return "Binding [positionals=" + positionals + ", named=" + named + "]";
  }
}

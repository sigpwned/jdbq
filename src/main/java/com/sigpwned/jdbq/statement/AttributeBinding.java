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

import java.util.HashMap;
import java.util.Map;

public class AttributeBinding {
  private final Map<String, Object> attributes;

  public AttributeBinding() {
    attributes = new HashMap<>();
  }

  /**
   * Define an attribute for {@link StatementContext} for statements executed by Jdbi.
   *
   * @param key the key for the attribute
   * @param value the value for the attribute
   * @return this
   */
  public void define(String key, Object value) {
    attributes.put(key, value);
  }

  /**
   * Obtain the value of an attribute
   *
   * @param key the name of the attribute
   * @return the value of the attribute
   */
  public Object getAttribute(String key) {
    return attributes.get(key);
  }

  /**
   * Returns the attributes which will be applied to {@link SqlStatement SQL statements} created by
   * Jdbi.
   *
   * @return the defined attributes.
   */
  Map<String, Object> getAttributes() {
    return attributes;
  }

  /**
   * Remove all bindings from this Binding.
   */
  public void clear() {
    attributes.clear();
  }

  /**
   * Returns whether any bindings exist.
   *
   * @return True if there are no bindings.
   */
  public boolean isEmpty() {
    return attributes.isEmpty();
  }

  @Override
  public String toString() {
    return "AttributeBinding [attributes=" + attributes + "]";
  }
}

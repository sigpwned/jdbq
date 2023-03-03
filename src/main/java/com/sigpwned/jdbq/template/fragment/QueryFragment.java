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
package com.sigpwned.jdbq.template.fragment;

import static java.util.Objects.requireNonNull;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.sigpwned.jdbq.statement.Argument;
import io.leangen.geantyref.TypeFactory;

public class QueryFragment {
  private final String sql;
  private Map<String, Object> attributes;
  private Map<String, Argument> bindings;

  public QueryFragment(String sql) {
    this.sql = requireNonNull(sql);
    // Intentially leave map values null
  }

  public String getSql() {
    return sql;
  }

  public QueryFragment define(String key, Object value) {
    if (attributes == null)
      attributes = new HashMap<>();

    attributes.put(key, value);

    return this;
  }

  public Map<String, Object> getAttributes() {
    return Optional.ofNullable(attributes).map(Collections::unmodifiableMap)
        .orElseGet(Collections::emptyMap);
  }

  public QueryFragment bind(String name, byte value) {
    return bind(name, byte.class, value);
  }

  public QueryFragment bind(String name, Byte value) {
    return bind(name, Byte.class, value);
  }

  public QueryFragment bind(String name, short value) {
    return bind(name, short.class, value);
  }

  public QueryFragment bind(String name, Short value) {
    return bind(name, Short.class, value);
  }

  public QueryFragment bind(String name, int value) {
    return bind(name, int.class, value);
  }

  public QueryFragment bind(String name, Integer value) {
    return bind(name, Integer.class, value);
  }

  public QueryFragment bind(String name, long value) {
    return bind(name, long.class, value);
  }

  public QueryFragment bind(String name, Long value) {
    return bind(name, Long.class, value);
  }

  public QueryFragment bind(String name, float value) {
    return bind(name, float.class, value);
  }

  public QueryFragment bind(String name, Float value) {
    return bind(name, Float.class, value);
  }

  public QueryFragment bind(String name, double value) {
    return bind(name, double.class, value);
  }

  public QueryFragment bind(String name, Double value) {
    return bind(name, Double.class, value);
  }

  public QueryFragment bind(String name, boolean value) {
    return bind(name, boolean.class, value);
  }

  public QueryFragment bind(String name, Boolean value) {
    return bind(name, Boolean.class, value);
  }

  public QueryFragment bind(String name, char value) {
    return bind(name, char.class, value);
  }

  public QueryFragment bind(String name, Character value) {
    return bind(name, Character.class, value);
  }

  public QueryFragment bind(String name, String value) {
    return bind(name, String.class, value);
  }

  public QueryFragment bind(String name, LocalDate value) {
    return bind(name, LocalDate.class, value);
  }

  public QueryFragment bind(String name, LocalTime value) {
    return bind(name, LocalTime.class, value);
  }

  public QueryFragment bind(String name, Instant value) {
    return bind(name, Instant.class, value);
  }

  public QueryFragment bind(String name, URI value) {
    return bind(name, URI.class, value);
  }

  public QueryFragment bind(String name, URL value) {
    return bind(name, URL.class, value);
  }

  public QueryFragment bind(String name, UUID value) {
    return bind(name, UUID.class, value);
  }

  public QueryFragment bind(String name, byte[] value) {
    return bind(name, byte[].class, value);
  }

  public QueryFragment bind(String name, Class<?> type, Object value) {
    return bindByType(name, type, value);
  }

  public QueryFragment bindByType(String name, Type type, Object value) {
    if (type == null)
      throw new NullPointerException();
    if (bindings == null)
      bindings = new HashMap<>();
    bindings.put(name, new Argument() {
      @Override
      public Type getType() {
        return type;
      }

      @Override
      public Object getValue() {
        return value;
      }
    });
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> QueryFragment bindArray(String name, T... array) {
    return bindArray(name, array.getClass().getComponentType(), array);
  }

  public QueryFragment bindArray(String name, Type elementType, Object... array) {
    return bindByType(name, TypeFactory.arrayOf(elementType), array);
  }

  public QueryFragment bindArray(String name, Type elementType, Iterable<?> iterable) {
    return bindByType(name, TypeFactory.parameterizedClass(Iterable.class, elementType), iterable);
  }


  public Map<String, Argument> getArguments() {
    return Optional.ofNullable(bindings).map(Collections::unmodifiableMap)
        .orElseGet(Collections::emptyMap);
  }
}

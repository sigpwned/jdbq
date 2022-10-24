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

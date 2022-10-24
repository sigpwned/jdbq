package com.sigpwned.jdbq.argument;

import java.lang.reflect.Type;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.config.ConfigRegistry;

@FunctionalInterface
public interface ArgumentFactory<T> {
  public QueryParameterValue map(Type type, Object value, ConfigRegistry config);
}

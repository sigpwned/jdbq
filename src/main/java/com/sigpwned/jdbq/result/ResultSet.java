package com.sigpwned.jdbq.result;

import java.io.Closeable;
import com.google.cloud.bigquery.FieldValueList;

@FunctionalInterface
public interface ResultSet extends Iterable<FieldValueList>, Closeable {
  default void close() {};
}

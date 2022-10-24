package com.sigpwned.jdbq.result;

import java.util.Iterator;
import com.google.cloud.bigquery.FieldValueList;

public class FieldValueListsResultSet implements ResultSet {
  private final Iterable<FieldValueList> values;

  public FieldValueListsResultSet(Iterable<FieldValueList> values) {
    this.values = values;
  }

  @Override
  public Iterator<FieldValueList> iterator() {
    return getValues().iterator();
  }

  /**
   * @return the values
   */
  private Iterable<FieldValueList> getValues() {
    return values;
  }
}

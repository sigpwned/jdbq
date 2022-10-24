package com.sigpwned.jdbq.statement;

import com.sigpwned.jdbq.Handle;

public class Query extends SqlStatement<Query> {
  public Query(Handle handle, String sql) {
    super(handle, sql);
  }

  
}

package com.sigpwned.jdbq.statement;

import static java.util.Objects.requireNonNull;
import java.io.Closeable;
import java.io.IOException;
import com.sigpwned.jdbq.Handle;

public abstract class BaseStatement<This extends BaseStatement<This>> implements Closeable {
  private final Handle handle;
  private final StatementContext context;

  BaseStatement(Handle handle) {
    this.handle = requireNonNull(handle);
    this.context = new StatementContext(handle.getConfig());
  }

  public Handle getHandle() {
    return handle;
  }

  public StatementContext getContext() {
    return context;
  }

  @Override
  public void close() throws IOException {
    getContext().close();
  }
}

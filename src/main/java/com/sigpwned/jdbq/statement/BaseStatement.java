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

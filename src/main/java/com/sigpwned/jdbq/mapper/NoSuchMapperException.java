/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sigpwned.jdbq.mapper;

import com.sigpwned.jdbq.JdbqException;

/**
 * Thrown when you attempt to map a type that {@code Jdbi} doesn't have a registered mapper factory
 * for.
 */
public class NoSuchMapperException extends JdbqException {
  private static final long serialVersionUID = 1L;

  public NoSuchMapperException(String string, Throwable throwable) {
    super(string, throwable);
  }

  public NoSuchMapperException(Throwable cause) {
    super(cause);
  }

  public NoSuchMapperException(String message) {
    super(message);
  }
}

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
package com.sigpwned.jdbq.template;

import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Renders an SQL statement from a template.
 *
 * Note for implementors: define a suitable public constructor for SqlObject's
 * {@code UseTemplateEngine} annotation, and/or create your own custom annotation in case your
 * {@link TemplateEngine} has configuration parameters! Suitable constructors are the no-arg
 * constructor, one that takes a {@link java.lang.Class}, and one that takes both a
 * {@link java.lang.Class} and a {@link java.lang.reflect.Method}.
 *
 * @see DefinedAttributeTemplateEngine
 */
@FunctionalInterface
public interface TemplateEngine {
  /**
   * Convenience constant that returns the input template.
   */
  TemplateEngine NOP = new NoTemplateEngine();

  /**
   * Renders an SQL statement from the given template, using the statement context as needed.
   *
   * @param template The SQL to rewrite
   * @param ctx The statement context for the statement being executed
   * @return something which can provide the actual SQL to prepare a statement from and which can
   *         bind the correct arguments to that prepared statement
   */
  String render(String template, StatementContext ctx);
}

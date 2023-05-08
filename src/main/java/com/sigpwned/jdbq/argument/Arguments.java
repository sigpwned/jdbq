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
package com.sigpwned.jdbq.argument;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.argument.factory.BoxedArgumentFactory;
import com.sigpwned.jdbq.argument.factory.CharSequenceArgumentFactory;
import com.sigpwned.jdbq.argument.factory.CollectionArgumentFactory;
import com.sigpwned.jdbq.argument.factory.EnumArgumentFactory;
import com.sigpwned.jdbq.argument.factory.EssentialsArgumentFactory;
import com.sigpwned.jdbq.argument.factory.InternetArgumentFactory;
import com.sigpwned.jdbq.argument.factory.JavaTimeArgumentFactory;
import com.sigpwned.jdbq.argument.factory.JavaTimeZoneIdArgumentFactory;
import com.sigpwned.jdbq.argument.factory.OptionalArgumentFactory;
import com.sigpwned.jdbq.argument.factory.PrimitivesArgumentFactory;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.config.JdbqConfig;
import com.sigpwned.jdbq.statement.exception.UnableToCreateStatementException;

public class Arguments implements JdbqConfig<Arguments> {
  private final List<ArgumentFactory> argumentFactories;

  public Arguments() {
    this.argumentFactories = new CopyOnWriteArrayList<>();

    // register built-in factories, priority of factories is by reverse registration order
    addArgumentFactory(new PrimitivesArgumentFactory());
    addArgumentFactory(new BoxedArgumentFactory());
    addArgumentFactory(new InternetArgumentFactory());
    addArgumentFactory(new JavaTimeArgumentFactory());
    addArgumentFactory(new CharSequenceArgumentFactory()); // before EssentialsArgumentFactory
    addArgumentFactory(new EssentialsArgumentFactory());
    addArgumentFactory(new JavaTimeZoneIdArgumentFactory());
    addArgumentFactory(new EnumArgumentFactory());
    addArgumentFactory(new CollectionArgumentFactory());
    addArgumentFactory(new OptionalArgumentFactory());
  }

  private Arguments(Arguments that) {
    this.argumentFactories = new CopyOnWriteArrayList<>(that.argumentFactories);
  }

  public void addArgumentFactory(ArgumentFactory argumentFactory) {
    getArgumentFactories().add(argumentFactory);
  }

  public QueryParameterValue map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (value == null) {
      result = null;
    } else {
      result = null;
      for (ArgumentFactory argumentFactory : getArgumentFactories()) {
        result = argumentFactory.map(type, value, config).orElse(null);
        if (result != null)
          break;
      }
      if (result == null)
        throw new UnableToCreateStatementException("Failed to map argument value " + value);
    }
    return result;
  }

  /**
   * @return the argumentFactories
   */
  private List<ArgumentFactory> getArgumentFactories() {
    return argumentFactories;
  }

  @Override
  public Arguments createCopy() {
    return new Arguments(this);
  }
}

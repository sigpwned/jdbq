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
package com.sigpwned.jdbq.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.methods.Configurable;
import com.sigpwned.jdbq.argument.Arguments;
import com.sigpwned.jdbq.mapper.Mappers;
import com.sigpwned.jdbq.mapper.column.ColumnMappers;
import com.sigpwned.jdbq.mapper.row.RowMappers;
import com.sigpwned.jdbq.statement.SqlStatements;

/**
 * A registry of {@link JdbqConfig} instances by type.
 *
 * @see Configurable
 */
public final class ConfigRegistry {
  private final Map<Class<? extends JdbqConfig<?>>, JdbqConfig<?>> configs;

  /**
   * Creates a new config registry.
   */
  public ConfigRegistry() {
    configs = new ConcurrentHashMap<>();
    get(SqlStatements.class);
    get(Arguments.class);
    get(RowMappers.class);
    get(ColumnMappers.class);
    get(Mappers.class);
  }

  private ConfigRegistry(ConfigRegistry that) {
    this.configs = new ConcurrentHashMap<>();
    for (Map.Entry<Class<? extends JdbqConfig<?>>, JdbqConfig<?>> e : that.configs.entrySet())
      configs.put(e.getKey(), e.getValue().createCopy());
    configs.values().forEach(c -> c.setRegistry(this));
  }

  /**
   * Returns this registry's instance of the given config class. Creates an instance on-demand if
   * this registry does not have one of the given type yet.
   *
   * @param configClass the config class type.
   * @param <C> the config class type.
   * @return the given config class instance that belongs to this registry.
   */
  public <C extends JdbqConfig<C>> C get(Class<C> configClass) {
    // we would computeIfAbsent if not for JDK-8062841 >:(
    final JdbqConfig<?> lookup = configs.get(configClass);
    if (lookup != null) {
      return configClass.cast(lookup);
    }
    C config = configClass.cast(newConfigInstance(configClass));
    return Optional.ofNullable(configClass.cast(configs.putIfAbsent(configClass, config)))
        .orElse(config);
  }

  /**
   * Returns a new instance of the given config class
   * 
   * @param <C> the config class type.
   * @param configClass the config class type.
   * @return a new instance of the given type
   */
  private <C extends JdbqConfig<C>> C newConfigInstance(Class<C> configClass) {
    Constructor<C> configConstructor;
    try {
      configConstructor = configClass.getConstructor(ConfigRegistry.class);
    } catch (NoSuchMethodException e) {
      configConstructor = null;
    } catch (SecurityException e) {
      throw new IllegalStateException(
          "Security manager prevents access to constructor for " + configClass.getName(), e);
    }
    if (configConstructor != null) {
      try {
        return configConstructor.newInstance(this);
      } catch (InstantiationException e) {
        throw new IllegalArgumentException(
            "Config class " + configClass.getName() + " cannot be instantiated");
      } catch (IllegalAccessException e) {
        // The constructor isn't visible. That might be OK. Let it go.
      } catch (IllegalArgumentException e) {
        // We just retrieved the constructor. This should be impossible.
        throw new AssertionError(e);
      } catch (InvocationTargetException e) {
        throw new ConfigException(
            "Config class " + configClass.getName() + " failed to instantiate", e.getCause());
      }
    }

    Constructor<C> defaultConstructor;
    try {
      defaultConstructor = configClass.getConstructor();
    } catch (NoSuchMethodException e) {
      defaultConstructor = null;
    } catch (SecurityException e) {
      throw new IllegalStateException(
          "Security manager prevents access to constructor for " + configClass.getName(), e);
    }

    if (defaultConstructor != null) {
      C result;

      try {
        result = defaultConstructor.newInstance();
      } catch (InstantiationException e) {
        throw new IllegalArgumentException(
            "Config class " + configClass.getName() + " cannot be instantiated");
      } catch (IllegalAccessException e) {
        // The constructor isn't visible. That might be OK. Let it go.
        result = null;
      } catch (IllegalArgumentException e) {
        // We just retrieved the constructor. This should be impossible.
        throw new AssertionError(e);
      } catch (InvocationTargetException e) {
        throw new ConfigException(
            "Config class " + configClass.getName() + " failed to instantiate", e.getCause());
      }

      if (result != null) {
        result.setRegistry(this);
        return result;
      }
    }

    throw new IllegalArgumentException("No appropriate constructor for " + configClass.getName());
  }

  /**
   * Returns a copy of this config registry.
   *
   * @return a copy of this config registry
   * @see JdbqConfig#createCopy() config objects in the returned registry are copies of the
   *      corresponding config objects from this registry.
   */
  public ConfigRegistry createCopy() {
    return new ConfigRegistry(this);
  }
}

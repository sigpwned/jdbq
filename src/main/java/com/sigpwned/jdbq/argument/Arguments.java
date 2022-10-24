package com.sigpwned.jdbq.argument;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.sigpwned.jdbq.config.JdbqConfig;

public class Arguments implements JdbqConfig<Arguments> {
  private final ConcurrentMap<Type, ArgumentFactory<?>> argumentFactories;

  public Arguments() {
    this.argumentFactories = new ConcurrentHashMap<>();
  }

  private Arguments(Arguments that) {
    this.argumentFactories = new ConcurrentHashMap<>(that.argumentFactories);
  }

  public <T> void addArgumentFactory(Type type, ArgumentFactory<T> argumentFactory) {
    getArgumentFactories().put(type, argumentFactory);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> Optional<ArgumentFactory<T>> findArgumentFactoryFor(Type type) {
    return Optional.ofNullable((ArgumentFactory) getArgumentFactories().get(type));
  }

  /**
   * @return the argumentFactories
   */
  private ConcurrentMap<Type, ArgumentFactory<?>> getArgumentFactories() {
    return argumentFactories;
  }

  @Override
  public Arguments createCopy() {
    return new Arguments(this);
  }
}

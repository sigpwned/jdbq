package com.sigpwned.jdbq.config;


/**
 * Interface for classes that hold configuration. Implementations of this interface must have a
 * public constructor that optionally takes the {@link ConfigRegistry}.
 *
 * Implementors should ensure that implementations are thread-safe for access and caching purposes,
 * but not necessarily for reconfiguration.
 *
 * @param <This> A "This" type. Should always be the configuration class.
 */
public interface JdbqConfig<This extends JdbqConfig<This>> {
  /**
   * Returns a copy of this configuration object. Changes to the copy should not modify the
   * original, and vice-versa.
   *
   * @return a copy of this configuration object.
   */
  This createCopy();

  /**
   * The registry will inject itself into the configuration object. This can be useful if you need
   * to look up dependencies. You will get a new registry after being copied.
   * 
   * @param registry the registry that owns this configuration object
   */
  default void setRegistry(ConfigRegistry registry) {}
}

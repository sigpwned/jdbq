package com.sigpwned.jdbq.config;

import com.sigpwned.jdbq.JdbqException;

public class ConfigException extends JdbqException {
  private static final long serialVersionUID = 1L;

  public ConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConfigException(String message) {
    super(message);
  }
}

package com.sigpwned.jdbq.internal;

import java.time.format.DateTimeFormatter;

public final class JavaTimeMapping {
  private JavaTimeMapping() {}

  /**
   * BigQuery instant format
   */
  public static final DateTimeFormatter INSTANT_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSZZ");

  /**
   * BigQuery time format
   */
  public static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");

  /**
   * BigQuery date format
   */
  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * BigQuery datetime format
   */
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
}

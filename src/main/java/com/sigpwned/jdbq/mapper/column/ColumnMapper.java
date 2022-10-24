package com.sigpwned.jdbq.mapper.column;

import java.sql.SQLException;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.sigpwned.jdbq.statement.StatementContext;

/**
 * Maps result set columns to objects.
 *
 * @param <T> The mapped type
 * @see ColumnMapperFactory
 * @see ColumnMappers
 * @see org.jdbi.v3.core.result.ResultBearing#map(ColumnMapper)
 * @see org.jdbi.v3.core.config.Configurable#registerColumnMapper(ColumnMapper)
 * @see org.jdbi.v3.core.config.Configurable#registerColumnMapper(java.lang.reflect.Type,
 *      ColumnMapper)
 * @see StatementContext#findColumnMapperFor(java.lang.reflect.Type)
 */
@FunctionalInterface
public interface ColumnMapper<T> {
  /**
   * Map the given column of the current row of the result set to an Object. This method should not
   * cause the result set to advance; allow Jdbi to do that, please.
   *
   * @param r the result set being iterated
   * @param columnNumber the column number to map (starts at 1)
   * @param ctx the statement context
   * @return the value to return for this column
   * @throws SQLException if anything goes wrong go ahead and let this percolate; Jdbi will handle
   *         it
   */
  default T map(FieldValueList r, int columnNumber, StatementContext ctx) {
    return map(r.get(columnNumber), ctx);
  }

  /**
   * Map the given column of the current row of the result set to an Object. This method should not
   * cause the result set to advance; allow Jdbi to do that, please.
   *
   * @param r the result set being iterated
   * @param columnLabel the column label to map
   * @param ctx the statement context
   * @return the value to return for this column
   * @throws SQLException if anything goes wrong go ahead and let this percolate; Jdbi will handle
   *         it
   */
  default T map(FieldValueList r, String columnLabel, StatementContext ctx) {
    return map(r.get(columnLabel), ctx);
  }

  T map(FieldValue value, StatementContext ctx);
}

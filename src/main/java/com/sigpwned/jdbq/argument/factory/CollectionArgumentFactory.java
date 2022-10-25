package com.sigpwned.jdbq.argument.factory;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import com.google.cloud.bigquery.QueryParameterValue;
import com.sigpwned.jdbq.argument.ArgumentFactory;
import com.sigpwned.jdbq.argument.Arguments;
import com.sigpwned.jdbq.config.ConfigRegistry;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.statement.exception.UnableToCreateStatementException;

public class CollectionArgumentFactory implements ArgumentFactory {
  @Override
  public Optional<QueryParameterValue> map(Type type, Object value, ConfigRegistry config) {
    QueryParameterValue result;
    if (value == null) {
      result = null;
    } else if (GenericTypes.isSuperType(Collection.class, type)) {
      Collection<?> collection = (Collection<?>) value;
      Type elementType = GenericTypes.findGenericParameter(type, Collection.class)
          .orElseThrow(() -> new UnableToCreateStatementException(
              "Collection has unresolvable element type " + type));
      Class<?> elementClass = GenericTypes.getErasedType(elementType);

      Iterator<?> iterator = collection.iterator();
      QueryParameterValue[] array = collection.toArray(QueryParameterValue[]::new);
      for (int i = 0; i < array.length; i++)
        array[i] = config.get(Arguments.class).map(elementType, iterator.next(), config);
      
      result = QueryParameterValue.array(null, null);
    } else {
      result = null;
    }
    return result;
  }
}

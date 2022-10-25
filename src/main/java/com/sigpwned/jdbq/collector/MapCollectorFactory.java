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
package com.sigpwned.jdbq.collector;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collector;
import com.sigpwned.jdbq.generic.GenericTypes;

class MapCollectorFactory implements CollectorFactory {
  private final IdentityHashMap<Class<?>, Collector<?, ?, ?>> collectors = new IdentityHashMap<>();

  MapCollectorFactory() {
    collectors.put(Map.class, MapCollectors.toMap(LinkedHashMap::new));
    collectors.put(HashMap.class, MapCollectors.toMap(HashMap::new));
    collectors.put(LinkedHashMap.class, MapCollectors.toMap(LinkedHashMap::new));
    collectors.put(SortedMap.class, MapCollectors.toMap(TreeMap::new));
    collectors.put(TreeMap.class, MapCollectors.toMap(TreeMap::new));
    collectors.put(ConcurrentMap.class, MapCollectors.toMap(ConcurrentHashMap::new));
    collectors.put(ConcurrentHashMap.class, MapCollectors.toMap(ConcurrentHashMap::new));
    collectors.put(WeakHashMap.class, MapCollectors.toMap(WeakHashMap::new));
  }

  @Override
  public boolean accepts(Type containerType) {
    Class<?> erasedType = GenericTypes.getErasedType(containerType);

    return containerType instanceof ParameterizedType && collectors.containsKey(erasedType);
  }

  @Override
  public Optional<Type> elementType(Type containerType) {
    Class<?> erasedType = GenericTypes.getErasedType(containerType);

    return Map.class.isAssignableFrom(erasedType)
        ? Optional.of(GenericTypes.resolveMapEntryType(containerType))
        : Optional.empty();
  }

  @Override
  public Collector<?, ?, ?> build(Type containerType) {
    Class<?> erasedType = GenericTypes.getErasedType(containerType);

    return collectors.get(erasedType);
  }
}

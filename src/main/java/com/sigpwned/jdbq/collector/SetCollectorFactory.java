/*-
 * =================================LICENSE_START==================================
 * jdbq
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
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
package com.sigpwned.jdbq.collector;

import static java.util.stream.Collectors.toCollection;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collector;
import com.sigpwned.jdbq.generic.GenericTypes;
import com.sigpwned.jdbq.internal.MoreJdbqCollectors;

class SetCollectorFactory implements CollectorFactory {
  private final IdentityHashMap<Class<?>, Collector<?, ?, ?>> collectors = new IdentityHashMap<>();

  SetCollectorFactory() {
    collectors.put(Set.class, MoreJdbqCollectors.toUnmodifiableSet());
    collectors.put(HashSet.class, toCollection(HashSet::new));
    collectors.put(LinkedHashSet.class, toCollection(LinkedHashSet::new));
    collectors.put(SortedSet.class, toCollection(TreeSet::new));
    collectors.put(TreeSet.class, toCollection(TreeSet::new));
  }

  @Override
  public boolean accepts(Type containerType) {
    return containerType instanceof ParameterizedType
        && collectors.containsKey(GenericTypes.getErasedType(containerType));
  }

  @Override
  public Optional<Type> elementType(Type containerType) {
    Class<?> erasedType = GenericTypes.getErasedType(containerType);
    return GenericTypes.findGenericParameter(containerType, erasedType);
  }

  @Override
  public Collector<?, ?, ?> build(Type containerType) {
    Class<?> erasedType = GenericTypes.getErasedType(containerType);
    return collectors.get(erasedType);
  }
}

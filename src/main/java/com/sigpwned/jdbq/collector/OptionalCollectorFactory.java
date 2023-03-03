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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.stream.Collector;
import com.sigpwned.jdbq.generic.GenericTypes;

class OptionalCollectorFactory implements CollectorFactory {
  @Override
  public boolean accepts(Type containerType) {
    return containerType instanceof ParameterizedType
        && Optional.class.equals(GenericTypes.getErasedType(containerType));
  }

  @Override
  public Optional<Type> elementType(Type containerType) {
    Class<?> erasedType = GenericTypes.getErasedType(containerType);
    return GenericTypes.findGenericParameter(containerType, erasedType);
  }

  @Override
  public Collector<?, ?, ?> build(Type containerType) {
    return OptionalCollectors.toOptional();
  }
}

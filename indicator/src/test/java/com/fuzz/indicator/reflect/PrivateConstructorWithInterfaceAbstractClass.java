/*
 * Copyright 2016 Philip Cohn-Cort
 *
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
 */
package com.fuzz.indicator.reflect;

import android.content.Context;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *     This class implements {@link ReflectiveInterface}, but it's also abstract so
 *     trying to instantiate it will cause an {@link InstantiationException}.
 * </p>
 * @author Philip Cohn-Cort (Fuzz)
 */
public abstract class PrivateConstructorWithInterfaceAbstractClass implements ReflectiveInterface {

    public PrivateConstructorWithInterfaceAbstractClass(Context context) {
    }
}

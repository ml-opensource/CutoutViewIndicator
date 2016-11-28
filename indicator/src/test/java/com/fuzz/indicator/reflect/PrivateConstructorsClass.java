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
import android.util.AttributeSet;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *    Implements {@link ReflectiveInterface}, unlike
 *    {@link PrivateConstructorWithoutInterfaceClass}
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PrivateConstructorsClass implements ReflectiveInterface {
    private PrivateConstructorsClass() {
    }

    private PrivateConstructorsClass(String ignored) {
    }

    private PrivateConstructorsClass(Context ignored) {
    }

    private PrivateConstructorsClass(Context ignored, AttributeSet alsoIgnored) {
    }
}

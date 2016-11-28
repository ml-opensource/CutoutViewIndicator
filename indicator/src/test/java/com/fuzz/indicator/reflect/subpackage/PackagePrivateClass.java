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
package com.fuzz.indicator.reflect.subpackage;

import android.content.Context;
import android.util.AttributeSet;

import com.fuzz.indicator.reflect.ReConstructorTest;
import com.fuzz.indicator.reflect.ReflectiveInterface;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *     This class is package-private and in a different package than
 *     {@link ReConstructorTest#reconstructClassWithInaccessibleName}.
 *     Trying to instantiate it should rise an {@link IllegalAccessException}
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class PackagePrivateClass implements ReflectiveInterface {

    public PackagePrivateClass(Context context, AttributeSet attrs, int defaultAttr) {
    }
}

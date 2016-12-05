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
package com.fuzz.indicator;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.fuzz.indicator.proxy.StateProxy;

/**
 * Utility methods for making new instances of {@link StateProxy} subclasses.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class Proxies {
    @NonNull
    public static StateProxy proxyForXCells(@IntRange(from = 0) int x) {
        int currentPosition = 0;
        return new ConstantStateProxy(currentPosition, x);
    }
}

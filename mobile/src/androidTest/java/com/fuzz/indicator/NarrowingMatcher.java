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

import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;

import static org.hamcrest.Matchers.sameInstance;

/**
 * The {@link android.support.test.espresso.Espresso#onView(Matcher)} method
 * DOES NOT support any Matcher for an exact instance. This class fixes that
 * omission.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class NarrowingMatcher extends Is<View> {

    /**
     * Create a new, narrower interpretation of {@link Matchers#sameInstance(Object)}.
     * @param desiredView    any view
     */
    public NarrowingMatcher(View desiredView) {
        super(sameInstance(desiredView));
    }

    /**
     * Create a new, narrower interpretation of {@link Matchers#sameInstance(Object)}.
     * @param desiredView    any view
     */
    public static NarrowingMatcher isTheSameAs(View desiredView) {
        return new NarrowingMatcher(desiredView);
    }
}

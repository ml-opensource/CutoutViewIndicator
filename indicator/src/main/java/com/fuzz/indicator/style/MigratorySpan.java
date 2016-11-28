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
package com.fuzz.indicator.style;

import android.support.annotation.NonNull;
import android.text.Spannable;

import com.fuzz.indicator.text.CutoutTextCell;

/**
 * Interface to be implemented by spans used in {@link CutoutTextCell}.
 *
 * These are designed to be translated (in a 2-dimensional plane) above
 * their associated {@link android.text.Spannable Spannables}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface MigratorySpan {

    /**
     * Consider a Spannable sequence which has a defined start and end.
     * The MigratoryRange returned by this method represents some portion
     * of indices within that sequence. Implementations are asked to
     * <i>NOT</i> modify the parameter within this method.
     * <p>
     *     Sample caller code may be as follows:
     *     <pre>
     * MigratorySpan ms;
     * Spannable target;
     *
     * //...
     *
     * MigratoryRange&lt;Integer&gt; bounds = ms.getCoverage(target);
     *
     * int start = bounds.getLower();
     * int end = bounds.getUpper();
     *
     * target.setSpan(ms, start, end, ms.preferredFlags(0));
     *     </pre>
     * </p>
     *
     * @return a range which can be used to figure out what
     * characters in the Spannable are covered.
     * @param enclosingSequence    the sequence of characters wherein this span
     */
    @NonNull
    MigratoryRange<Integer> getCoverage(Spannable enclosingSequence);

    /**
     * Whatever is returned here should be a valid argument into
     * {@link android.text.Spannable#setSpan(Object, int, int, int)}'s
     * {@code flags} argument. Feel free to return the parameter directly
     * if they don't need to change.
     *
     * @return a combination of valid span-laying-out flags
     * @param previousFlags    flags used for the previous layout - will
     *                         be 0 if not currently attached to a
     *                         {@link android.text.Spannable Spannable}
     */
    int preferredFlags(int previousFlags);
}

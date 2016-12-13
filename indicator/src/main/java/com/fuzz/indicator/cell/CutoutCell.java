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
package com.fuzz.indicator.cell;

import android.support.annotation.NonNull;
import android.view.View;

import com.fuzz.indicator.CutoutCellGenerator;
import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.proxy.IndicatorOffsetEvent;

/**
 * Simple interface which views inside the {@link CutoutViewIndicator} should implement.
 * <p>
 *     Views may instead be wrapped in an implementation of this class before being
 *     returned by the CutoutViewIndicator's {@link CutoutCellGenerator} - see
 *     {@link CutoutViewIndicator#createCellFor(int)} and {@link TypicalCutoutCell}
 *     for more details.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface CutoutCell {

    /**
     * Perform some arbitrary action to represent the provided OffsetEvent.
     *
     * @param offsetEvent    whatever just happened
     * @see OffSetters
     */
    void offsetContentBy(@NonNull IndicatorOffsetEvent offsetEvent);

    /**
     * @return the actual view represented by this object
     */
    @NonNull
    View getItemView();
}

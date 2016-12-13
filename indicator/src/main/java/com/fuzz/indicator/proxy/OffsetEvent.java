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
package com.fuzz.indicator.proxy;

import android.graphics.Matrix;
import android.text.Spannable;
import android.widget.ImageView;

import com.fuzz.indicator.CutoutImageCell;
import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.cell.OffSetters;
import com.fuzz.indicator.cell.CutoutTextCell;

/**
 * This represents a single action or {@link android.view.MotionEvent} or whatever
 * that would trigger a call to
 * {@link CutoutViewIndicator#showOffsetIndicator(int, float)}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface OffsetEvent {
    /**
     * If this is being used in a {@link CutoutImageCell}, this fraction is multiplied
     * by the {@link ImageView#getDrawable()} width/height to get a pixel offset - this
     * is then applied directly to the ImageView's Matrix via
     * {@link OffSetters#offsetImageBy(ImageView, int, float, Matrix)}.
     * <p>
     *     {@link CutoutTextCell} gets a similar treatment, except with
     *     spans instead of a Drawable. Details of that can be read from the
     *     javadoc for {@link OffSetters#offsetSpansBy(Spannable, float)}.
     * </p>
     *
     * @return fraction of indicator width/height to offset by - positive means down/right,
     * negative means up/left
     */
    float getFraction();
}

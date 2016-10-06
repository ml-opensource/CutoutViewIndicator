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

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fuzz.indicator.text.MigratoryRange;
import com.fuzz.indicator.text.MigratorySpan;

/**
 * Collection of utility methods for offsetting specific views.
 * <p>
 *     Right now there's just one for {@link ImageView}s, but more
 *     might appear in future.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class OffSetters {
    /**
     * Sets a new image matrix on this view. This method only allows orthogonal offsets
     *
     * @param imageView the view that will be changed
     * @param orientation direction to offset in. Use either {@link LinearLayout#VERTICAL} or {@link LinearLayout#HORIZONTAL}
     * @param percentage  percentage of width/height to offset by - positive means down/right, negative means up/left
     * @see ImageView#setImageMatrix(Matrix)
     */
    public static void offsetImageBy(ImageView imageView, int orientation, float percentage) {
        Matrix mat = new Matrix();
        float offsetX, offsetY;
        if (orientation == LinearLayout.VERTICAL) {
            offsetX = 0;
            offsetY = percentage * imageView.getHeight();
        } else {
            offsetX = percentage * imageView.getWidth();
            offsetY = 0;
        }
        mat.setTranslate(offsetX, offsetY);
        imageView.setImageMatrix(mat);
    }

    public static void offsetSpansBy(@NonNull Spannable spannable, int orientation, float percentage) {
        int length = spannable.length();

        MigratorySpan[] knownSpans = spannable.getSpans(0, length, MigratorySpan.class);
        if (knownSpans.length > 0) {
            MigratoryRange<Float> fullSize = MigratoryRange.from(0, length);
            for (MigratorySpan knownSpan : knownSpans) {
                offsetSpan(spannable, percentage, length, fullSize, knownSpan);
            }
        }
    }

    public static void offsetSpan(@NonNull Spannable spannable, float percentage, int length, MigratoryRange<Float> fullSize, MigratorySpan knownSpan) {
        // TODO: Offset the span
    }
}

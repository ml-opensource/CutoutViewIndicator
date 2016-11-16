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
import android.widget.TextView;

import com.fuzz.indicator.style.MigratoryRange;
import com.fuzz.indicator.style.MigratorySpan;

/**
 * Collection of utility methods for offsetting specific views.
 * <p>
 *     Right now there's just one for {@link ImageView}s and one for
 *     {@link TextView}s, but more
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
     * @param mat         a starting form for the matrix. Callers may choose to supply a new instance of {@link Matrix}
     * @see ImageView#setImageMatrix(Matrix)
     */
    public static void offsetImageBy(@NonNull ImageView imageView, int orientation, float percentage, @NonNull Matrix mat) {
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

    /**
     * Ensures that all spans of type {@link MigratorySpan} within {@code spannable} are
     * translated a proportional quantity of characters from their baseline position. Each
     * span's implementation of {@link MigratorySpan#getCoverage(Spannable)} is responsible
     * for reporting the correct baseline coverage prior to translation.
     *
     * @param spannable      the text to which the spans are attached
     * @param orientation    direction of offset (currently not used)
     * @param fraction       what proportion of the spannable should be considered offset.
     *                       values outside the range of 0..1 will be clamped into that
     *                       range.
     */
    public static void offsetSpansBy(@NonNull Spannable spannable, int orientation, float fraction) {
        int length = spannable.length();
        int offset = (int) (fraction * length);

        MigratorySpan[] knownSpans = spannable.getSpans(0, length, MigratorySpan.class);
        if (knownSpans.length > 0) {
            MigratoryRange<Integer> fullSize = MigratoryRange.from(0, length);
            for (MigratorySpan knownSpan : knownSpans) {
                offsetSpan(spannable, fullSize, knownSpan, offset);
            }
        }
    }

    public static void offsetSpan(@NonNull Spannable spannable, MigratoryRange<Integer> fullSize, MigratorySpan knownSpan, int offset) {
        MigratoryRange<Integer> covered = knownSpan.getCoverage(spannable).translate(offset);
        int spanStart = fullSize.clamp(covered.getLower());
        int spanEnd = fullSize.clamp(covered.getUpper());
        int flags = knownSpan.preferredFlags(spannable.getSpanFlags(knownSpan));
        spannable.setSpan(knownSpan, spanStart, spanEnd, flags);
    }
}

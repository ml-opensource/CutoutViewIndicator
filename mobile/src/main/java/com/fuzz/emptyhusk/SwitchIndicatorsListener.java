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
package com.fuzz.emptyhusk;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.util.CircularArray;
import android.view.View;

import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.CutoutViewLayoutParams;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Little onClickListener for toggling through a range of different
 * indicator styles.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class SwitchIndicatorsListener implements View.OnClickListener {
    @NonNull
    protected final CutoutViewIndicator cvi;

    @NonNull
    CircularArray<CutoutViewLayoutParams> storage = new CircularArray<>();

    public SwitchIndicatorsListener(@NonNull CutoutViewIndicator cvi) {
        this.cvi = cvi;

        CutoutViewLayoutParams circles = cvi.generateDefaultLayoutParams();
        circles.indicatorDrawableId = R.drawable.circle_accent;
        circles.cellBackgroundId = R.drawable.circle_secondary;
        circles.internalSpacing = 0;
        int circleDiameter = cvi.getResources().getDimensionPixelSize(R.dimen.circle_diameter);
        circles.perpendicularLength = circleDiameter;
        circles.cellLength = circleDiameter;
        storage.addFirst(circles);

        CutoutViewLayoutParams stars = cvi.generateDefaultLayoutParams();
        stars.indicatorDrawableId = android.R.drawable.star_on;
        stars.cellBackgroundId = android.R.drawable.star_off;
        stars.internalSpacing = (int) (Math.random() * 20);
        stars.perpendicularLength = WRAP_CONTENT;
        stars.cellLength = WRAP_CONTENT;
        storage.addLast(stars);

        // The rectangle params should be at the end of the array when this constructor returns.
        CutoutViewLayoutParams rectangles = cvi.generateDefaultLayoutParams();
        rectangles.indicatorDrawableId = R.drawable.rectangle_accent;
        rectangles.cellBackgroundId = R.drawable.rectangle_secondary;
        rectangles.internalSpacing = cvi.getResources().getDimensionPixelOffset(R.dimen.rectangle_internal_spacing);
        rectangles.perpendicularLength = cvi.getResources().getDimensionPixelSize(R.dimen.rectangle_transverse);
        rectangles.cellLength = cvi.getResources().getDimensionPixelOffset(R.dimen.rectangle_length);
        storage.addLast(rectangles);
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        cvi.setAllChildParamsTo(nextParams());
    }

    @NonNull
    private CutoutViewLayoutParams nextParams() {
        CutoutViewLayoutParams layoutParams = storage.popFirst();
        storage.addLast(layoutParams);
        return new CutoutViewLayoutParams(layoutParams);
    }
}

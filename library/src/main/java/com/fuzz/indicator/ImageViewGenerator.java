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

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Default implementation of {@link LayeredViewGenerator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ImageViewGenerator implements LayeredViewGenerator {

    public ImageViewGenerator() {
    }

    /**
     * Creates a new {@link LayeredImageViewHolder} by default. Override to change that.
     *
     * @inheritDoc
     */
    @NonNull
    @Override
    public LayeredView createCellFor(ViewGroup parent, int position) {
        CutoutViewLayoutParams lp = ((CutoutViewIndicator) parent).generateDefaultLayoutParams();

        ImageView child = new ImageView(parent.getContext());
        child.setScaleType(ImageView.ScaleType.MATRIX);
        child.setLayoutParams(lp);
        child.setBackgroundResource(lp.cellBackgroundId);
        child.setImageResource(lp.indicatorDrawableId);
        return new LayeredImageViewHolder(child);
    }

    @Override
    public void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp) {
        child.setBackgroundResource(lp.cellBackgroundId);
        if (child instanceof ImageView) {
            ((ImageView) child).setImageResource(lp.indicatorDrawableId);
        }
    }
}

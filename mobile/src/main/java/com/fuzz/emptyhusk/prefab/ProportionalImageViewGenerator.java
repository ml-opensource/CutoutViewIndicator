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
package com.fuzz.emptyhusk.prefab;

import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuzz.emptyhusk.R;
import com.fuzz.indicator.CutoutViewLayoutParams;
import com.fuzz.indicator.clip.ClippedImageViewGenerator;

/**
 * Generator for displaying clipped images which are proportional to
 * a {@link RecyclerView}. Each cell created by {@link #createCellFor(ViewGroup, int)}
 * displays part of the indicator. Intended for use in concert with
 * {@link CVIScrollListener} and {@link RecyclerStateProxy}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ProportionalImageViewGenerator extends ClippedImageViewGenerator {

    protected int rvLength;
    protected int rvChildLength;

    public void setProportionalTo(@NonNull RecyclerView recyclerView) {
        rvLength = recyclerView.getHeight();
        rvChildLength = recyclerView.getChildAt(0).getHeight();
    }

    @Override
    public void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp, @Nullable View originator) {
        child.setBackgroundResource(lp.cellBackgroundId);
        if (child instanceof ImageView) {
            GradientDrawable elongated = new GradientDrawable();
            elongated.setShape(GradientDrawable.RECTANGLE);

            Resources res = child.getResources();

            int accent = ContextCompat.getColor(child.getContext(), R.color.transparentColorAccent);

            float fractionOfParent = rvLength * 1.0f / rvChildLength;

            elongated.setColor(accent);
            elongated.setCornerRadius(res.getDimensionPixelSize(R.dimen.circle_radius));
            float proposedLength = fractionOfParent * lp.perpendicularLength;
            elongated.setSize(lp.perpendicularLength, (int) proposedLength);

            ((ImageView) child).setImageDrawable(elongated);
            ((ImageView) child).getDrawable();
        }
    }
}

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

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

/**
 * Extension of {@link CutoutImageCell} with support for swapping between
 * {@link AnimationDrawable}s and...well, any other sort of Drawable.
 * <p>
 *     If {@link CutoutViewLayoutParams#indicatorDrawableId} refers to an
 *     AnimationDrawable this will treat its individual frames as different
 *     images. Each cell gets a reference
 *     to the {@link #frames complete animation}, but only shows the nth
 *     frame thereof (where n is the current
 *     {@link CutoutViewLayoutParams#position position} of that cell in
 *     the {@link com.fuzz.indicator.CutoutViewIndicator CutoutViewIndicator}).
 *     If a cell is rebound at a different position for whatever reason,
 *     the chosen drawable can be smoothly updated with just one call
 *     to {@link #updateDrawable(CutoutViewLayoutParams)}.
 * </p>
 * If the drawable attached to {@link #itemView} isn't an AnimationDrawable,
 * this acts identically to its superclass.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class FrameAwareCutoutImageCell extends CutoutImageCell {

    @Nullable
    protected AnimationDrawable frames;

    /**
     * Check out the {@link FrameAwareCutoutImageCell class javadoc} for details on how
     * this works.
     *
     * @param child    the backing view for this cell. It can be retrieved later through
     *                 {@link #getItemView()}
     */
    public FrameAwareCutoutImageCell(@NonNull ImageView child) {
        super(child);
        if (itemView.getDrawable() instanceof AnimationDrawable) {
            frames = (AnimationDrawable) itemView.getDrawable();
            latestResourceId = ((CutoutViewLayoutParams) itemView.getLayoutParams()).indicatorDrawableId;
        } else {
            frames = null;
            latestResourceId = 0;
        }
    }

    @Override
    @Nullable
    protected Drawable chooseDrawable(@NonNull CutoutViewLayoutParams lp) {
        Drawable chosen;
        if (lp.position <= -1 || latestResourceId != lp.indicatorDrawableId) {
            latestResourceId = lp.indicatorDrawableId;
            chosen = ContextCompat.getDrawable(itemView.getContext(), lp.indicatorDrawableId);
            if (chosen instanceof AnimationDrawable) {
                frames = (AnimationDrawable) chosen;
            } else {
                frames = null;
            }
        } else {
            chosen = itemView.getDrawable();
        }

        if (frames != null) {
            int frameIndex = lp.position % frames.getNumberOfFrames();
            chosen = frames.getFrame(frameIndex);
        }

        return chosen;
    }
}

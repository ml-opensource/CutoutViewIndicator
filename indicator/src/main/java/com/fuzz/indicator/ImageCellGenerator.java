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
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Default implementation of {@link CutoutCellGenerator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ImageCellGenerator implements CutoutCellGenerator {

    public ImageCellGenerator() {
    }

    /**
     * Creates a new {@link CutoutImageCell} by default. Override to change that.
     *
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public CutoutCell createCellFor(@NonNull ViewGroup parent, int position) {
        CutoutViewLayoutParams lp = ((CutoutViewIndicator) parent).generateDefaultLayoutParams();

        ImageView child = createChildFor(parent, position);
        child.setScaleType(ImageView.ScaleType.MATRIX);
        child.setLayoutParams(lp);
        child.setBackgroundResource(lp.cellBackgroundId);
        child.setImageResource(lp.indicatorDrawableId);

        CutoutCell cutoutCell = createCellForExisting(child);
        lp.setViewHolder(cutoutCell);
        return cutoutCell;
    }

    /**
     * This method is here to allow subclasses to easily generate their
     * own ImageView subclasses, without needing to worry about the
     * details of {@link CutoutViewLayoutParams}.
     * @param parent      the non-null ViewGroup which, at a later point, will
     *                    contain the view returned by this method.
     * @param position    when the returned view is added to {@code parent},
     *                    it will be placed at this index
     * @return a non-null view which fulfills the requirements above. Default
     *         implementation returns a new ImageView.
     */
    @NonNull
    protected ImageView createChildFor(@NonNull ViewGroup parent, int position) {
        return new ImageView(parent.getContext());
    }

    /**
     * This method is here to allow subclasses ease of overriding the exact
     * type of {@link CutoutCell} returned by {@link #createCellFor(ViewGroup, int)}.
     * <p>
     *     Could be handy for tests too.
     * </p>
     * @param child    the {@code View} part of the returned value
     * @return a CutoutCell wrapping {@code child}. If {@code child}
     * implements CutoutCell in some way, it may be returned directly.
     */
    @NonNull
    protected CutoutCell createCellForExisting(@NonNull ImageView child) {
        return new CutoutImageCell(child);
    }

    @Override
    public void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp, @Nullable View originator) {
        child.setBackgroundResource(lp.cellBackgroundId);
        if (child instanceof ImageView) {
            ((ImageView) child).setImageResource(lp.indicatorDrawableId);
        }
    }
}

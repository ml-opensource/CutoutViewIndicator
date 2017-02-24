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

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;

import com.fuzz.indicator.cell.CutoutCell;

import java.lang.ref.WeakReference;

/**
 * Plain Old Java Object representing the configuration of the child views inside
 * {@link CutoutViewIndicator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutViewLayoutParams extends LinearLayout.LayoutParams {
    @DrawableRes
    public int cellBackgroundId;
    /**
     * This is the id of the drawable currently acting as indicator. If 0, no indicator will be shown.
     */
    @DrawableRes
    public int indicatorDrawableId;

    @NonNull
    protected WeakReference<CutoutCell> cutoutCell = new WeakReference<>(null);

    /**
     * Set this to true to prevent the attached view from being
     * removed during the next call to {@link CutoutViewIndicator#rebuildChildViews()}.
     * <p>
     *     Should be false if the attached view was added by a generator.
     * </p>
     */
    public boolean preservedDuringRebuild;

    /**
     * This is the position of the attached View within its CutoutViewIndicator parent.
     * <p>
     *     Defaults to -1. This value is not copied by {@link #setFrom(CutoutViewLayoutParams)}
     *     and should not be persisted outside of this class. It's here mainly so that
     *     {@link CutoutCellGenerator}s can access position info in their
     *     {@link CutoutCellGenerator#onBindChild(View, CutoutViewLayoutParams, View)}
     *     method.
     * </p>
     */
    public int position = -1;

    /**
     * Amount of space between this view and the one before it
     * <p>
     *     Treat this like a conditional margin - it is added to the
     *     {@link #getMarginStart() start/left/top margin} only if it
     *     is <em>not</em> the first View in the CutoutViewIndicator.
     * </p>
     */
    public int internalSpacing;
    /**
     * This is the resolved length of the view (in pixels)
     *
     * @see #perpendicularLength
     */
    public int cellLength;
    /**
     * This is the height or width bounding all child views when
     * {@link CutoutViewIndicator#HORIZONTAL HORIZONTAL} or
     * {@link CutoutViewIndicator#VERTICAL VERTICAL}, respectively.
     * <p/>
     * Typically equal to the height/width of the {@link CutoutViewIndicator}, minus padding.
     */
    public int perpendicularLength;

    /**
     * Utility method for setting height and width in an orientation-independent way
     *
     * @param dimension the new value for either height or width, in pixels
     * @param isHeight true if this is height, false if this is width
     */
    public void setParamDimension(int dimension, boolean isHeight) {
        if (isHeight) {
            height = dimension;
        } else {
            width = dimension;
        }
    }

    /**
     * Utility method for choosing the right copy constructor.
     *
     * @param params           a pre-existing set of LayoutParams (might be null).
     * @param defaultParams    the default set of LayoutParams to use when {@code params}
     *                         is null
     * @return a new set of {@link CutoutViewLayoutParams}.
     */
    @NonNull
    public static CutoutViewLayoutParams createFrom(@Nullable ViewGroup.LayoutParams params, @NonNull CutoutViewLayoutParams defaultParams) {
        if (params == null) {
            return new CutoutViewLayoutParams(defaultParams);
        } else if (params instanceof CutoutViewLayoutParams) {
            return new CutoutViewLayoutParams((CutoutViewLayoutParams) params);
        } else if (params instanceof MarginLayoutParams) {
            return new CutoutViewLayoutParams((MarginLayoutParams) params);
        } else  {
            return new CutoutViewLayoutParams(params);
        }
    }

    /**
     * This constructor is called by the {@link CutoutViewIndicator} when inflating CVI
     * children. It may only be called when that ViewGroup is fully inflated.
     *
     * {@inheritDoc}
     * @param defaultParams the current value of {@link CutoutViewIndicator#defaultChildParams}
     */
    public CutoutViewLayoutParams(Context c, AttributeSet attrs, @NonNull CutoutViewLayoutParams defaultParams) {
        super(c, attrs);
        init(c, attrs, defaultParams);
    }

    public CutoutViewLayoutParams(@NonNull ViewGroup.LayoutParams source) {
        super(source);
        if (source instanceof CutoutViewLayoutParams) {
            CutoutViewLayoutParams cutoutSource = (CutoutViewLayoutParams) source;
            setFrom(cutoutSource);
        }
    }

    public CutoutViewLayoutParams(@NonNull MarginLayoutParams source) {
        super(source);
    }

    public CutoutViewLayoutParams(@NonNull LinearLayout.LayoutParams source) {
        super((MarginLayoutParams) source);
        weight = source.weight;
        gravity = source.gravity;
        if (source instanceof CutoutViewLayoutParams) {
            CutoutViewLayoutParams cutoutSource = (CutoutViewLayoutParams) source;
            setFrom(cutoutSource);
        }
    }

    public CutoutViewLayoutParams(int width, int height) {
        super(width, height);
    }

    @SuppressWarnings("ResourceType")
    protected void init(Context context, AttributeSet attrs, @Nullable CutoutViewLayoutParams defaultParams) {
        if (defaultParams == null) {
            defaultParams = new CutoutViewLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            defaultParams.perpendicularLength = WRAP_CONTENT;
            defaultParams.cellLength = WRAP_CONTENT;
        }

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CutoutViewIndicator_Layout);
            indicatorDrawableId = a.getResourceId(
                    R.styleable.CutoutViewIndicator_Layout_layout_rcv_drawable,
                    defaultParams.indicatorDrawableId
            );

            perpendicularLength = a.getDimensionPixelSize(
                    R.styleable.CutoutViewIndicator_Layout_layout_rcv_perpendicular_length,
                    defaultParams.perpendicularLength
            );
            cellLength = a.getDimensionPixelOffset(
                    R.styleable.CutoutViewIndicator_Layout_layout_rcv_cell_length,
                    defaultParams.cellLength
            );

            cellBackgroundId = a.getResourceId(
                    R.styleable.CutoutViewIndicator_Layout_layout_rcv_drawable_unselected,
                    defaultParams.cellBackgroundId
            );

            a.recycle();
        }
    }

    /**
     * Use this to set the attached {@link CutoutCell} reference. This can
     * be retrieved by {@link CutoutCellGenerator} implementations during
     * {@link CutoutCellGenerator#onBindChild(View, CutoutViewLayoutParams, View)}
     * if they so wish.
     *
     * @param cutoutCell    a CutoutCell that would hopefully be defined
     *                       such that {@link CutoutCell#getItemView()}
     *                       returns the same object that these LayoutParams
     *                       are set on.
     */
    public void setCutoutCell(@Nullable CutoutCell cutoutCell) {
        this.cutoutCell = new WeakReference<>(cutoutCell);
    }

    /**
     * Getter counterpart to {@link #setCutoutCell(CutoutCell)}. In general,
     * it is safe to assume that the value returned here is the same as that
     * most recently set.
     *
     * @return the associated CutoutCell if it has not been garbage-collected.
     */
    @Nullable
    public CutoutCell getCutoutCell() {
        return cutoutCell.get();
    }

    public void setFrom(@NonNull CutoutViewLayoutParams cutoutSource) {
        cellBackgroundId = cutoutSource.cellBackgroundId;
        indicatorDrawableId = cutoutSource.indicatorDrawableId;
        internalSpacing = cutoutSource.internalSpacing;
        cellLength = cutoutSource.cellLength;
        perpendicularLength = cutoutSource.perpendicularLength;
        cutoutCell = cutoutSource.cutoutCell;
    }
}

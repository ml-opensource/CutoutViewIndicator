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

/**
 * Little pluggable interface for creating new cells for a {@link CutoutViewIndicator}.
 * <p>
 *     Note that cells are awfully similar in concept to a RecyclerView's
 *     ViewHolders, so if you're new at this you'll want to make your implementation of this
 *     class a {@code RecyclerView Adapter}. Then you can just delegate
 *     {@link #createCellFor(ViewGroup, int)} over to {@code onCreateViewHolder}.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface LayeredViewGenerator {
    /**
     * The caller will likely throw an Exception if the returned View is attached
     * to a {@link android.view.ViewParent}.
     *
     * @param parent the parent ViewGroup which the CutoutCell will be added to
     * @param position where (sequentially) in the {@link CutoutViewIndicator} this view will
     *                 appear
     * @return a new {@code CutoutCell}, which really shouldn't be null.
     */
    @NonNull
    CutoutCell createCellFor(@NonNull ViewGroup parent, int position);

    /**
     * This will be called during the {@link CutoutViewIndicator}'s measure pass
     * before laying out {@code child}. Use it to make any last-minute alterations
     * to the child's layout. Inspired by RecyclerView.Adapter's onBindViewHolder()
     * method.
     *
     * <p>
     *     When the indicator represents ViewPagers, RecyclerViews, or other
     *     ViewGroups, {@code originator} is the view which corresponds directly to the
     *     child. In all other cases, the originator parameter is null.
     * </p>
     *
     * @param child         a cell view created by {@link #createCellFor(ViewGroup, int)}
     * @param lp            the child's layout parameters, already cast to
     *                      CutoutViewLayoutParams for your convenience
     * @param originator    the view which is currently acting as inspiration for the child.
     *                      May be null.
     */
    void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp, @Nullable View originator);
}

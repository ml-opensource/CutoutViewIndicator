package com.cliabhach.indicator;

import android.support.annotation.NonNull;
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
     * @param parent the parent ViewGroup which the LayeredView will be added to
     * @param position where (sequentially) in the {@link CutoutViewIndicator} this view will
     *                 appear
     * @return a new {@code LayeredView}, which really shouldn't be null.
     */
    @NonNull
    LayeredView createCellFor(ViewGroup parent, int position);

    /**
     * This will be called during the {@link CutoutViewIndicator}'s layout pass
     * before laying out {@code child}. Use it to make any last-minute alterations
     * to the child's layout. Inspired by RecyclerView.Adapter's onBindViewHolder()
     * method.
     *
     * @param child    a cell view created by {@link #createCellFor(ViewGroup, int)}
     * @param lp       the child's layout parameters, already cast to
     *                 CutoutViewLayoutParams for your convenience
     */
    void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp);
}

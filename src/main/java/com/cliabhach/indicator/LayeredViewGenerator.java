package com.cliabhach.indicator;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * Little pluggable interface for creating new cells for a {@link CutoutViewIndicator}.
 * <p>
 *     Note that cells are awfully similar in concept to a RecyclerView's
 *     ViewHolders, so if you're new at this you'll want to make your implementation of this
 *     class a {@code RecyclerView Adapter}. Then you can just delegate
 *     {@link #createIndicatorFor(ViewGroup, int)} over to {@code onCreateViewHolder}.
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
    LayeredView createIndicatorFor(ViewGroup parent, int position);
}

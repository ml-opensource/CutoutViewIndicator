package com.fuzz.emptyhusk.prefab;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.StateProxy;

/**
 * {@link RecyclerView} analog to {@link com.fuzz.indicator.ViewPagerStateProxy}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class RecyclerStateProxy implements StateProxy {
    @Nullable
    private AdapterDataObserver adObserver;
    @NonNull
    private final RecyclerView recyclerView;
    @NonNull
    private CutoutViewIndicator cvi;
    @NonNull
    private CVIScrollListener listener;

    public RecyclerStateProxy(@NonNull RecyclerView recyclerView, @NonNull CutoutViewIndicator cvi, @NonNull CVIScrollListener listener) {
        this.recyclerView = recyclerView;
        this.cvi = cvi;
        this.listener = listener;
    }

    @Override
    public float getCurrentPosition() {
        return getStartOfCoveredPositions();
    }

    /**
     * Assumption: 1 unit = layout.getTotalSpace() = child.getHeight/getWidth = cellLength
     * @return how much space is covered.
     */
    public float getStartOfCoveredPositions() {
        LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
        int first = layout.findFirstVisibleItemPosition();
        int last = layout.findLastVisibleItemPosition();
        int numOfVisibleViews = Math.abs(last - first) + 1;

        ViewHolder firstViewHolder = recyclerView.findViewHolderForLayoutPosition(first);

        /**
         * If there's no first view, then just return -1
         * (no position found, which is what first should equal under those circumstances)
         */
        float startFraction = first;

        if (firstViewHolder != null) {
            int firstLength;
            int totalLength;
            float start;
            if (layout.getOrientation() == LinearLayoutManager.VERTICAL) {
                firstLength = firstViewHolder.itemView.getHeight();
                start = firstViewHolder.itemView.getY();
                totalLength = recyclerView.getHeight();
            } else {
                firstLength = firstViewHolder.itemView.getWidth();
                start = firstViewHolder.itemView.getX();
                totalLength = recyclerView.getWidth();
            }

            startFraction -= start / totalLength;
        }

        return startFraction;
    }

    @Override
    public int getCellCount() {
        return recyclerView.getAdapter().getItemCount();
    }

    @Override
    public void resendPositionInfo(CutoutViewIndicator cvi, float position) {
        listener.centerIndicatorAround(position);
    }

    @Override
    public boolean canObserve(DataSetObserver observer) {
        return recyclerView.getAdapter() != null;
    }

    @Override
    public void associateWith(final DataSetObserver observer) {
        adObserver = new AdapterDataObserver() {
            @Override
            public void onChanged() {
                observer.onChanged();
            }
        };
        recyclerView.getAdapter().registerAdapterDataObserver(adObserver);
    }

    @Override
    public void disassociateFrom(DataSetObserver observer) {
        recyclerView.getAdapter().unregisterAdapterDataObserver(adObserver);
    }
}

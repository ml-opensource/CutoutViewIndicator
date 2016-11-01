package com.fuzz.emptyhusk.prefab;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;

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
        return getCurrentPositionWith(((LinearLayoutManager) recyclerView.getLayoutManager()));
    }

    /**
     * TODO: represent covered area with a {@link com.fuzz.indicator.style.MigratoryRange}.
     * @param layout    layoutManager in use
     * @return how much space is covered.
     */
    public static float getCurrentPositionWith(LinearLayoutManager layout) {
        int first = layout.findFirstVisibleItemPosition();
        int last = layout.findLastVisibleItemPosition();
        return (first + last) / 2;
    }

    @Override
    public int getCellCount() {
        return recyclerView.getAdapter().getItemCount();
    }

    @Override
    public void resendPositionInfo(float position) {
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

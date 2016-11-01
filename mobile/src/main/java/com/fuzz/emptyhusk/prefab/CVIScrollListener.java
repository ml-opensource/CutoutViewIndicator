package com.fuzz.emptyhusk.prefab;

import android.support.v7.widget.RecyclerView;

import com.fuzz.indicator.CutoutViewIndicator;

/**
 * {@link RecyclerView} analog to {@link com.fuzz.indicator.OnViewPagerChangeListener}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class CVIScrollListener extends RecyclerView.OnScrollListener {
    private CutoutViewIndicator cvi;

    private static final String TAG = CVIScrollListener.class.getSimpleName();

    protected int cumulativeScrollDx;
    protected int cumulativeScrollDy;
    private float startPosition;

    CVIScrollListener(CutoutViewIndicator cvi, float startPosition, int initialDx, int initialDy) {
        this.cvi = cvi;
        cumulativeScrollDx = initialDx;
        cumulativeScrollDy = initialDy;
        this.startPosition = startPosition;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    public void centerIndicatorAround(float position) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }
}

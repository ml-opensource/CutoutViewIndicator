package com.fuzz.emptyhusk.prefab;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

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

    CVIScrollListener(CutoutViewIndicator cvi, int initialDx, int initialDy) {
        this.cvi = cvi;
        cumulativeScrollDx = initialDx;
        cumulativeScrollDy = initialDy;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            cvi.ensureOnlyCurrentItemsSelected();
        }
    }

    public void centerIndicatorAround(float position) {
        int integralPos = (int) position;
        cvi.showOffsetIndicator(cvi.fixPosition(integralPos), position - integralPos);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        cumulativeScrollDx += dx;
        cumulativeScrollDy += dy;
        if (cvi.getIndicatorDrawableId() != 0) {

            LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
            int rvFirst = layout.findFirstVisibleItemPosition();
            int rvLast = layout.findLastVisibleItemPosition();

            if (rvFirst > rvLast) {
                int tmp = rvFirst;
                rvFirst = rvLast;
                rvLast = tmp;
            }

            for (int rvPosition = rvFirst; rvPosition <= rvLast; rvPosition++) {
                // This ViewHolder's associated cell will be at least partially covered by indicator
                ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(rvPosition);

                final int cviPosition = cvi.fixPosition(rvPosition);

                /**
                 * If there's no rvFirst view, then just go to the next one.
                 */
                if (holder != null) {
                    View child = holder.itemView;

                    // rvStart is relative to the screen (so like -17 for just off the top/start side)
                    float rvStart;
                    int childLength;

                    if (layout.getOrientation() == LinearLayoutManager.VERTICAL) {
                        rvStart = child.getY();
                        childLength = child.getHeight();
                    } else {
                        rvStart = child.getX();
                        childLength = child.getWidth();
                    }

                    float cviPositionOffset = -(rvStart / childLength);

                    // Cover the provided position...
                    cvi.showOffsetIndicator(cviPosition, cviPositionOffset);
                }
            }
        }
    }
}

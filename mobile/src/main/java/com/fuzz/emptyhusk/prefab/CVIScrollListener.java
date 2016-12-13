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
package com.fuzz.emptyhusk.prefab;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.proxy.IndicatorOffsetEvent;

/**
 * {@link RecyclerView} analog to {@link com.fuzz.indicator.OnViewPagerChangeListener}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class CVIScrollListener extends RecyclerView.OnScrollListener {
    @NonNull
    private CutoutViewIndicator cvi;

    protected int cumulativeScrollDx;
    protected int cumulativeScrollDy;

    CVIScrollListener(@NonNull CutoutViewIndicator cvi, int initialDx, int initialDy) {
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

    public IndicatorOffsetEvent centerIndicatorAround(float position) {
        return IndicatorOffsetEvent.from(cvi, position);
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

                /**
                 * If there's no rvPosition view, then just go to the next one.
                 */
                if (holder != null) {
                    showOffsetFor(holder, layout.getOrientation());
                }
            }
        }
    }

    public void showOffsetFor(@NonNull ViewHolder holder, int rvOrientation) {
        final int cviPosition = cvi.fixPosition(holder.getLayoutPosition());

        View child = holder.itemView;

        // rvStart is relative to the screen (so like -17 for just off the top/start side)
        float rvStart;
        int childLength;

        if (rvOrientation == LinearLayoutManager.VERTICAL) {
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

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

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.ViewProvidingStateProxy;

/**
 * {@link RecyclerView} analog to {@link com.fuzz.indicator.ViewPagerStateProxy}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class RecyclerStateProxy implements ViewProvidingStateProxy {
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

        ViewHolder firstViewHolder = recyclerView.findViewHolderForLayoutPosition(first);

        /**
         * If there's no first view, then just return -1
         * (no position found, which is what first should equal under those circumstances)
         */
        float startFraction = first;

        if (firstViewHolder != null) {
            int childLength;
            float start;
            if (layout.getOrientation() == LinearLayoutManager.VERTICAL) {
                childLength = firstViewHolder.itemView.getHeight();
                start = firstViewHolder.itemView.getY();
            } else {
                childLength = firstViewHolder.itemView.getWidth();
                start = firstViewHolder.itemView.getX();
            }

            startFraction -= start / childLength;
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

    @Nullable
    @Override
    public View getOriginalViewFor(int cviPosition) {
        ViewHolder holder = recyclerView.findViewHolderForLayoutPosition(cviPosition);

        View retVal;
        if (holder != null) {
            retVal = holder.itemView;
        } else {
            retVal = null;
        }
        return retVal;
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

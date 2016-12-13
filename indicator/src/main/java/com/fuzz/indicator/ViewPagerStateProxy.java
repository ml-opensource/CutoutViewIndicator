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

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fuzz.indicator.proxy.IndicatorOffsetEvent;
import com.fuzz.indicator.proxy.ProxyReference;
import com.fuzz.indicator.proxy.StateProxy;
import com.fuzz.indicator.proxy.ViewProvidingStateProxy;

/**
 * {@link StateProxy} wrapper around a {@link ViewPager}. All calls
 * are delegated to this object.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ViewPagerStateProxy implements ViewProvidingStateProxy {

    @NonNull
    private final ViewPager pager;

    @NonNull
    private OnViewPagerChangeListener pageChangeListener;

    public ViewPagerStateProxy(@NonNull ViewPager pager, @NonNull CutoutViewIndicator cvi) {
        this.pager = pager;
        this.pageChangeListener = new OnViewPagerChangeListener(cvi);
    }

    @Override
    public float getCurrentPosition() {
        // Seriously. They called this the 'CurrentItem'. Can you believe it?
        return pager.getCurrentItem();
    }

    @Override
    public int getCellCount() {
        return pager.getAdapter().getCount();
    }

    @Override
    public IndicatorOffsetEvent resendPositionInfo(ProxyReference cvi, float assumedIndicatorPosition) {
        // We blindly trust that the assumed position is accurate.
        return pageChangeListener.createEventFrom(cvi, assumedIndicatorPosition);
    }

    @Nullable
    @Override
    public View getOriginalViewFor(int cviPosition) {
        View found = null;
        PagerAdapter adapter = pager.getAdapter();

        if (cviPosition != -1 && adapter instanceof ViewProvidingAdapter) {
            found = ((ViewProvidingAdapter) adapter).getViewFor(cviPosition);
        }
        return found;
    }

    @Override
    public void associateWith(DataSetObserver observer) {
        pager.removeOnPageChangeListener(pageChangeListener);
        pager.addOnPageChangeListener(pageChangeListener);
        pager.getAdapter().registerDataSetObserver(observer);
    }

    @Override
    public void disassociateFrom(DataSetObserver observer) {
        pager.removeOnPageChangeListener(pageChangeListener);
        if (pager.getAdapter() != null) {
            pager.getAdapter().unregisterDataSetObserver(observer);
        }
    }

    @Override
    public boolean canObserve(DataSetObserver observer) {
        return pager.getAdapter() != null;
    }
}
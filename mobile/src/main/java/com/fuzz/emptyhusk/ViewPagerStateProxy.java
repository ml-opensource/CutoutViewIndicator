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
package com.fuzz.emptyhusk;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.StateProxy;

/**
 * {@link StateProxy} wrapper around a {@link ViewPager}. All calls
 * are delegated to this object.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ViewPagerStateProxy extends StateProxy {

    @NonNull
    private final ViewPager pager;

    @NonNull
    private ViewPager.OnPageChangeListener pageChangeListener;

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
    public void onSelected(float currentIndicatorPosition) {
        pageChangeListener.onPageSelected((int) currentIndicatorPosition);
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
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

import android.support.v4.view.ViewPager;

/**
 * Change listener for easy integration between {@link CutoutViewIndicator}
 * and {@link android.support.v4.view.ViewPager}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class OnViewPagerChangeListener implements ViewPager.OnPageChangeListener {

    private CutoutViewIndicator cvi;

    public OnViewPagerChangeListener(CutoutViewIndicator cutoutViewIndicator) {
        this.cvi = cutoutViewIndicator;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (cvi.getIndicatorDrawableId() != 0) {
            position = cvi.fixPosition(position);

            // Cover the provided position...
            cvi.showOffsetIndicator(position, positionOffset);
            if (positionOffset > 0) {
                // ...and cover the next one too
                int next = position + 1;
                if (next >= cvi.getChildCount()) {
                    next = 0;
                }
                cvi.showOffsetIndicator(next, positionOffset - 1);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        createEventFrom(cvi, position);
    }

    /**
     * Integration implementation of {@link #onPageSelected(int)} for use with
     * {@link ViewPagerStateProxy#resendPositionInfo(ProxyReference, float)}.
     * @param cvi         which CutoutViewIndicator to call methods on.
     * @param position    a specified position within the indicator.
     */
    public IndicatorOffsetEvent createEventFrom(ProxyReference cvi, float position) {
        return IndicatorOffsetEvent.from(cvi, position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (isIdle(state)) {
            // verify that all non-current views are free from indicators
            cvi.ensureOnlyCurrentItemsSelected();
        }
    }

    protected boolean isIdle(int scrollState) {
        return scrollState == ViewPager.SCROLL_STATE_IDLE;
    }
}

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

import android.support.v4.view.ViewPager;

import com.fuzz.indicator.BaseViewPagerChangeListener;
import com.fuzz.indicator.CutoutViewIndicator;

/**
 * Bridging facade between {@link android.support.v4.view.ViewPager.OnPageChangeListener},
 * {@link BaseViewPagerChangeListener}, and {@link com.fuzz.indicator.StateProxy.ProxyListener}.
 * <p>
 *     With this class it is possible for a {@link CutoutViewIndicator} to know
 *     how to treat a {@link ViewPager} without actually depending on that library.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class OnViewPagerChangeListener extends BaseViewPagerChangeListener implements ViewPager.OnPageChangeListener {
    public OnViewPagerChangeListener(CutoutViewIndicator cutoutViewIndicator) {
        super(cutoutViewIndicator);
    }

    @Override
    protected boolean isIdle(int scrollState) {
        return scrollState == ViewPager.SCROLL_STATE_IDLE;
    }
}

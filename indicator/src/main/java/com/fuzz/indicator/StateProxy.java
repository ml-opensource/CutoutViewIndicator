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

/**
 * Wrap an implementation of this class around your state machine.
 * <p>
 *     Typically speaking, a {@link CutoutViewIndicator} indicates the
 *     state of some outside object. This interface lists out all the
 *     methods which are needed to communicate with such an object.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public abstract class StateProxy {

    /**
     * @return the current position of the indicator
     */
    public abstract float getCurrentPosition();

    /**
     * See {@link CutoutViewIndicator#showOffsetIndicator(int, float)}
     * for details.
     *
     * @return how many cells should be shown in the indicator
     */
    public abstract int getCellCount();

    /**
     * This method is called when the {@link CutoutViewIndicator} has just
     * rebuilt its views.
     *
     * @param currentIndicatorPosition    the position which was selected
     */
    public abstract void onSelected(float currentIndicatorPosition);

    /**
     * This method is called when the {@link CutoutViewIndicator} is ready
     * to start listening to events from the Wrapped Object.
     *
     * @param observer    something internal to the {@link CutoutViewIndicator}
     */
    public abstract void associateWith(DataSetObserver observer);

    /**
     * This method is called when the {@link CutoutViewIndicator} is ready
     * to stop listening to events from the Wrapped Object.
     *
     * @param observer    something internal to the {@link CutoutViewIndicator}
     */
    public abstract void disassociateFrom(DataSetObserver observer);

    /**
     * @param observer    a future parameter to {@link #associateWith(DataSetObserver)}
     * @return whether this (or the Wrapped Object) wants to receive a
     * subsequent call to {@link #associateWith(DataSetObserver)} with the
     * same observer parameter.
     */
    public abstract boolean canObserve(DataSetObserver observer);

    /**
     * Helper interface for remote implementations of
     * {@link android.support.v4.view.ViewPager.OnPageChangeListener
     * OnPageChangeListener}.
     * <p>
     *     Basically, this is a 1:1 copy of that interface so that
     *     classes like {@link BaseViewPagerChangeListener} can
     *     ensure that they have all the right methods without
     *     explicitly depending on {@link android.support.v4.view.ViewPager
     *     ViewPager}.
     * </p>
     */
    protected interface ProxyListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int currentPageNumber);

        void onPageScrollStateChanged(int state);
    }
}

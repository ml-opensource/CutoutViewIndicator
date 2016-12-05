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

/**
 * An OffsetEvent that gives precise scroll information in the spirit of
 * {@link android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class IndicatorOffsetEvent implements OffsetEvent {

    protected final int position;

    protected final float fraction;

    protected final int orientation;

    @OffSetFlag
    protected int offSetFlags = OffSetFlag.IMAGE_TRANSLATE;

    public IndicatorOffsetEvent(int position, float fraction, int orientation) {
        this.position = position;
        this.fraction = fraction;
        this.orientation = orientation;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public float getFraction() {
        return fraction;
    }

    /**
     * the (vertical or horizontal) orientation in which this offset event
     * should be judged. If this object was constructed by a {@link CutoutViewIndicator},
     * then the return value can be assumed equal to that CutoutViewIndicator's
     * {@link CutoutViewIndicator#getOrientation() orientation}.
     *
     * @return either {@link CutoutViewIndicator#HORIZONTAL HORIZONTAL} or
     * {@link CutoutViewIndicator#VERTICAL VERTICAL}
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Call this to change the {@link #offSetFlags} attached to
     * this event.
     *
     * @param offSetFlags    a bitwise OR of a couple
     *                       {@link OffSetFlag} constants
     * @see #getOffSetFlags()
     */
    public void setOffSetFlags(@OffSetFlag int offSetFlags) {
        this.offSetFlags = offSetFlags;
    }

    /**
     * If no flags were set manually, this defaults to {@link OffSetFlag#IMAGE_TRANSLATE}.
     *
     * @return the {@link OffSetFlag OffSetFlags} previously set on this IndicatorOffsetEvent.
     * @see #setOffSetFlags(int)
     */
    @OffSetFlag
    public int getOffSetFlags() {
        return offSetFlags;
    }
}

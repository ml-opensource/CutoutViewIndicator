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
public interface StateProxy {

    /**
     * @return the current position of the indicator
     */
    float getCurrentPosition();

    /**
     * See {@link CutoutViewIndicator#showOffsetIndicator(int, float)}
     * for details.
     *
     * @return how many cells should be shown in the indicator
     */
    int getCellCount();

    /**
     * This method is called when the {@link CutoutViewIndicator} has just
     * rebuilt its views.
     *
     * @param cvi                         the CutoutViewIndicator to which
     *                                    new info should be sent
     * @param assumedIndicatorPosition    the position which the indicator
     *                                    thinks is selected, may or may
     *                                    not be the same as
     *                                    {@link #getCurrentPosition()}.
     */
    void resendPositionInfo(CutoutViewIndicator cvi, float assumedIndicatorPosition);

    /**
     * This method is called when the {@link CutoutViewIndicator} is ready
     * to start listening to events from the Wrapped Object.
     *
     * @param observer    something internal to the {@link CutoutViewIndicator}
     */
    void associateWith(DataSetObserver observer);

    /**
     * This method is called when the {@link CutoutViewIndicator} is ready
     * to stop listening to events from the Wrapped Object.
     *
     * @param observer    something internal to the {@link CutoutViewIndicator}
     */
    void disassociateFrom(DataSetObserver observer);

    /**
     * @param observer    a future parameter to {@link #associateWith(DataSetObserver)}
     * @return whether this (or the Wrapped Object) wants to receive a
     * subsequent call to {@link #associateWith(DataSetObserver)} with the
     * same observer parameter.
     */
    boolean canObserve(DataSetObserver observer);
}

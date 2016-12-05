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
package com.fuzz.indicator.proxy;

import android.database.DataSetObserver;

/**
 * Default implementation of {@link StateProxy}
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class UnavailableProxy implements StateProxy {

    @Override
    public float getCurrentPosition() {
        return -1;
    }

    @Override
    public int getCellCount() {
        return 0;
    }

    @Override
    public IndicatorOffsetEvent resendPositionInfo(ProxyReference cvi, float assumedIndicatorPosition) {
        return IndicatorOffsetEvent.from(cvi, assumedIndicatorPosition);
    }

    @Override
    public void associateWith(DataSetObserver observer) {
        // Do nothing
    }

    @Override
    public void disassociateFrom(DataSetObserver observer) {
        // Do nothing
    }

    /**
     * @param observer    the specified observer
     * @return {@code false}, as {@link UnavailableProxy} cannot observe DataSets
     */
    @Override
    public boolean canObserve(DataSetObserver observer) {
        return false;
    }
}

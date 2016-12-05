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

import com.fuzz.indicator.proxy.IndicatorOffsetEvent;
import com.fuzz.indicator.proxy.ProxyReference;
import com.fuzz.indicator.proxy.StateProxy;

/**
 * Specialised StateProxy for testing.
 * <p>
 *     Specify the current position and the maximum value
 *     in the constructor, after which point they will not
 *     change.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ConstantStateProxy implements StateProxy {
    private final int current;
    private final int max;

    public ConstantStateProxy(int current, int max) {
        this.current = current;
        this.max = max;
    }

    @Override
    public float getCurrentPosition() {
        return current;
    }

    @Override
    public int getCellCount() {
        return max;
    }

    @Override
    public IndicatorOffsetEvent resendPositionInfo(ProxyReference cvi, float position) {
        return IndicatorOffsetEvent.from(cvi, position);
    }

    @Override
    public void associateWith(DataSetObserver observer) {
        // Do nothing.
    }

    @Override
    public void disassociateFrom(DataSetObserver observer) {
        // Do nothing.
    }

    @Override
    public boolean canObserve(DataSetObserver observer) {
        return max >= 0;
    }
}

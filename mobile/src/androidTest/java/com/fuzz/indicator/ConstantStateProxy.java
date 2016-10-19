package com.fuzz.indicator;

import android.database.DataSetObserver;

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
public class ConstantStateProxy extends StateProxy {
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
    public void resendPositionInfo(float assumedIndicatorPosition) {
        // TODO: refactor API so that the CutoutViewIndicator itself is guaranteed accessible from this method
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

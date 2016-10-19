package com.fuzz.indicator;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * Utility methods for making new instances of {@link StateProxy} subclasses.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class Proxies {
    @NonNull
    public static StateProxy proxyForXCells(@IntRange(from = 0) int x) {
        int currentPosition = 0;
        return new ConstantStateProxy(currentPosition, x);
    }
}

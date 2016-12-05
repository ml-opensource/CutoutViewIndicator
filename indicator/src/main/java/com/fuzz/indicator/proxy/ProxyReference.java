package com.fuzz.indicator.proxy;

import com.fuzz.indicator.CutoutViewIndicator;

/**
 * Interface containing the methods a {@link StateProxy} often uses while
 * constructing an {@link IndicatorOffsetEvent}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 * @see StateProxy#resendPositionInfo(ProxyReference, float)
 */
public interface ProxyReference {
    /**
     * Modifies the passed-in value to represent a valid child position in this
     * {@link CutoutViewIndicator}.
     * @param proposed    a proposed position index
     * @return the corrected value
     */
    int fixPosition(int proposed);

    /**
     * See {@link CutoutViewIndicator#getOrientation()} for
     * details.
     * @return either horizontal or vertical
     */
    int getOrientation();
}

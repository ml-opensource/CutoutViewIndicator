package com.fuzz.indicator;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Optional extension to {@link StateProxy} to support queries on specific
 * {@link View Views} in the StateProxy backing object.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface ViewProvidingStateProxy extends StateProxy {
    /**
     * In some use cases, it may be handy to have a reference to the original
     * View when binding a representation thereof. See
     * {@link LayeredViewGenerator#onBindChild(View, CutoutViewLayoutParams, View)}
     * for details.
     *
     * @param cviPosition    position of a child within the CutoutViewIndicator
     * @return the view which directly inspired the indicator's child
     */
    @Nullable
    View getOriginalViewFor(int cviPosition);
}

package com.fuzz.indicator;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Optional interface for your ViewPager's {@link android.support.v4.view.PagerAdapter
 * PagerAdapter}.
 * <p>
 *     Implement it to enable support for passing views from
 *     your StateProxy to your generator.
 * </p>
 *
 * @see ViewPagerStateProxy#getOriginalViewFor(int)
 * @see CutoutCellGenerator#onBindChild(View, CutoutViewLayoutParams, View)
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface ViewProvidingAdapter {
    @Nullable
    View getViewFor(int cviPosition);
}

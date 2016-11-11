package com.fuzz.indicator;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Implement this interface on your ViewPager's {@link android.support.v4.view.PagerAdapter
 * PagerAdapter} to enable support for passing views from
 * {@link ViewPagerStateProxy#getOriginalViewFor(int) your StateProxy}
 * to {@link LayeredViewGenerator#onBindChild(View,
 * CutoutViewLayoutParams, View) your generator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface ViewProvidingAdapter {
    @Nullable
    View getViewFor(int cviPosition);
}

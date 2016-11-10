package com.fuzz.indicator;

/**
 * An OffsetEvent that gives precise scroll information in the spirit of
 * {@link android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface ViewPagerOffsetEvent extends OffsetEvent {
    int positionInPager();

    /**
     * @return the (vertical or horizontal) orientation in which this offset event
     * should be judged.
     */
    int orientation();
    float fraction();
    int pixels();
}

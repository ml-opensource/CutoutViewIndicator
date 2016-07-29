package com.fuzz.indicator;

import android.support.v4.view.ViewPager;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
class OnViewPagerChangeListener implements ViewPager.OnPageChangeListener {
    private CutoutViewIndicator cvi;

    public OnViewPagerChangeListener(CutoutViewIndicator cutoutViewIndicator) {
        this.cvi = cutoutViewIndicator;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (cvi.getIndicatorDrawableId() != 0) {
            position = fixPosition(position);

            // Cover the provided position...
            cvi.showOffsetIndicator(position, positionOffset);
            if (positionOffset > 0) {
                // ...and cover the next one too
                int next = position + 1;
                if (next >= cvi.getChildCount()) {
                    next = 0;
                }
                cvi.showOffsetIndicator(next, positionOffset - 1);
            }
        }
    }

    /**
     * @param proposed the value returned by {@link CutoutViewIndicator#viewPager}
     * @return the corrected value.
     * @see CutoutViewIndicator#usePositiveOffset
     */
    public int fixPosition(int proposed) {
        if (cvi.usePositiveOffset) {
            // ViewPagers like SpinningViewPager are always off by one
            proposed--;
            // Ensure that it's positive
            if (proposed < 0) {
                proposed += cvi.getChildCount();
            }
        }
        return proposed;
    }

    @Override
    public void onPageSelected(int position) {
        cvi.showOffsetIndicator(fixPosition(position), 0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            // verify that all non-current views are free from indicators
            cvi.ensureOnlyCurrentItemsSelected();
        }
    }
}

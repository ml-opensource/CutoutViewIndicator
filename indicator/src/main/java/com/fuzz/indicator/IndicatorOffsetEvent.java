package com.fuzz.indicator;

import android.graphics.Matrix;
import android.text.Spannable;
import android.widget.ImageView;

import com.fuzz.indicator.text.LayeredTextViewHolder;

/**
 * An OffsetEvent that gives precise scroll information in the spirit of
 * {@link android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class IndicatorOffsetEvent implements OffsetEvent {

    protected final int position;

    protected final float fraction;

    protected final int orientation;

    public IndicatorOffsetEvent(int position, float fraction, int orientation) {
        this.position = position;
        this.fraction = fraction;
        this.orientation = orientation;
    }

    public int getPosition() {
        return position;
    }

    /**
     * If this is being used in a {@link LayeredImageViewHolder}, this fraction is multiplied
     * by the {@link ImageView#getDrawable()} width/height to get a pixel offset - this
     * is then applied directly to the ImageView's Matrix via
     * {@link OffSetters#offsetImageBy(ImageView, int, float, Matrix)}.
     * <p>
     *     {@link LayeredTextViewHolder} gets a similar treatment, except with
     *     spans instead of a Drawable. Details of that can be read from the
     *     javadoc for {@link OffSetters#offsetSpansBy(Spannable, int, float)}.
     * </p>
     *
     * @return fraction of indicator width/height to offset by - positive means down/right,
     * negative means up/left
     */
    public float getFraction() {
        return fraction;
    }

    /**
     * the (vertical or horizontal) orientation in which this offset event
     * should be judged.
     */
    public int getOrientation() {
        return orientation;
    }
}

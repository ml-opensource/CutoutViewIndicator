package com.cliabhach.indicator;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Like a {@link android.widget.ProgressBar}, only more straightforward. Offset an image over
 * a static background.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class LayeredImageView extends AppCompatImageView implements LayeredView {
    public LayeredImageView(Context context) {
        this(context, null);
    }

    public LayeredImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayeredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets a new image matrix on this view. This method only allows orthogonal offsets
     *
     * @param orientation direction to offset in. Use either {@link LinearLayout#VERTICAL} or {@link LinearLayout#HORIZONTAL}
     * @param percentage  percentage of width/height to offset by - positive means down/right, negative means up/left
     * @see #setImageMatrix(Matrix)
     */
    @Override
    public void offsetImageBy(int orientation, float percentage) {
        Matrix mat = new Matrix();
        float offsetX, offsetY;
        if (orientation == LinearLayout.VERTICAL) {
            offsetX = 0;
            offsetY = percentage * getHeight();
        } else {
            offsetX = percentage * getWidth();
            offsetY = 0;
        }
        mat.setTranslate(offsetX, offsetY);
        setImageMatrix(mat);
    }
}

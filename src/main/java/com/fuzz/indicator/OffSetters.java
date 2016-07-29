package com.fuzz.indicator;

import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Collection of utility methods for offsetting specific views.
 * <p>
 *     Right now there's just one for {@link ImageView}s, but more
 *     might appear in future.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class OffSetters {
    /**
     * Sets a new image matrix on this view. This method only allows orthogonal offsets
     *
     * @param imageView the view that will be changed
     * @param orientation direction to offset in. Use either {@link LinearLayout#VERTICAL} or {@link LinearLayout#HORIZONTAL}
     * @param percentage  percentage of width/height to offset by - positive means down/right, negative means up/left
     * @see ImageView#setImageMatrix(Matrix)
     */
    public static void offsetImageBy(ImageView imageView, int orientation, float percentage) {
        Matrix mat = new Matrix();
        float offsetX, offsetY;
        if (orientation == LinearLayout.VERTICAL) {
            offsetX = 0;
            offsetY = percentage * imageView.getHeight();
        } else {
            offsetX = percentage * imageView.getWidth();
            offsetY = 0;
        }
        mat.setTranslate(offsetX, offsetY);
        imageView.setImageMatrix(mat);
    }
}

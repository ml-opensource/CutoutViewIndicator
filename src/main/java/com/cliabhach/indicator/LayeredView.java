package com.cliabhach.indicator;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Simple interface which views inside the {@link CutoutViewIndicator} should implement.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface LayeredView {
    /**
     * Sets a new image matrix on this view. This method only allows orthogonal offsets
     *
     * @param orientation direction to offset in. Use either {@link LinearLayout#VERTICAL} or {@link LinearLayout#HORIZONTAL}
     * @param percentage  percentage of width/height to offset by - positive means down/right, negative means up/left
     */
    void offsetImageBy(int orientation, float percentage);

    /**
     * @return the actual view represented by this object
     */
    @NonNull
    View getItemView();
}

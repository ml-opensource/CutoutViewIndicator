package com.fuzz.indicator;

import android.widget.ImageView;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class LayeredImageViewHolder extends IndicatorViewHolder<ImageView> {

    public LayeredImageViewHolder(ImageView itemView) {
        super(itemView);
    }

    @Override
    public void offsetImageBy(int orientation, float percentage) {
        OffSetters.offsetImageBy(itemView, orientation, percentage);
    }
}

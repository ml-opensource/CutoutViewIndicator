package com.fuzz.emptyhusk.prefab;

import android.widget.ImageView;

import com.fuzz.indicator.LayeredImageViewHolder;
import com.fuzz.indicator.OffsetEvent;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ProportionalLayeredImageViewHolder extends LayeredImageViewHolder {
    public ProportionalLayeredImageViewHolder(ImageView child) {
        super(child);
    }

    @Override
    public void offsetContentBy(OffsetEvent event) {
        ;
    }
}

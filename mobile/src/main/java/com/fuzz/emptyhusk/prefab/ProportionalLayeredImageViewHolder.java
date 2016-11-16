package com.fuzz.emptyhusk.prefab;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.fuzz.indicator.IndicatorOffsetEvent;
import com.fuzz.indicator.LayeredImageViewHolder;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ProportionalLayeredImageViewHolder extends LayeredImageViewHolder {
    public ProportionalLayeredImageViewHolder(ImageView child) {
        super(child);
    }

    @Override
    public void offsetContentBy(@NonNull IndicatorOffsetEvent event) {
        ;
    }
}

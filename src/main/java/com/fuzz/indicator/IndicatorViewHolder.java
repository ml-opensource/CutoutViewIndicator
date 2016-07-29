package com.fuzz.indicator;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

/**
 * ViewHolder for a {@link CutoutViewIndicator}.
 *
 * Can be used like a {@link android.widget.ProgressBar}, only more straightforward.
 * <br/><br/>
 * Call {@link OffSetters#offsetImageBy(ImageView, int, float)} to offset an image over a static background.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public abstract class IndicatorViewHolder<Root extends View> implements LayeredView {
    @NonNull
    protected final Root itemView;

    public IndicatorViewHolder(@NonNull Root itemView) {
        this.itemView = itemView;
    }

    @Override
    @NonNull
    public Root getItemView() {
        return itemView;
    }
}

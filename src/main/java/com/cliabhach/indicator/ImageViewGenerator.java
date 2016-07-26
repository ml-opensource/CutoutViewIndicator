package com.cliabhach.indicator;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Default implementation of {@link LayeredViewGenerator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ImageViewGenerator implements LayeredViewGenerator {

    public ImageViewGenerator() {
    }

    /**
     * Creates a new {@link LayeredImageViewHolder} by default. Override to change that.
     *
     * @inheritDoc
     */
    @NonNull
    @Override
    public LayeredView createCellFor(ViewGroup parent, int position) {
        CutoutViewLayoutParams lp = ((CutoutViewIndicator) parent).generateDefaultLayoutParams();

        ImageView child = new ImageView(parent.getContext());
        child.setScaleType(ImageView.ScaleType.MATRIX);
        child.setLayoutParams(lp);
        child.setBackgroundResource(lp.cellBackgroundId);
        child.setImageResource(lp.indicatorDrawableId);
        return new LayeredImageViewHolder(child);
    }

    @Override
    public void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp) {
        child.setBackgroundResource(lp.cellBackgroundId);
        if (child instanceof ImageView) {
            ((ImageView) child).setImageResource(lp.indicatorDrawableId);
        }
    }
}

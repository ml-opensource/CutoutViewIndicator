package com.fuzz.indicator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuzz.indicator.widget.TextClippedImageView;

import java.util.Locale;

/**
 * This Generator is capable of building an auto-incrementing sequence of characters.
 *
 * Each cell created by this class will display one such character.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class SequentialCellGenerator extends ImageCellGenerator {

    @NonNull
    @Override
    protected ImageView createChildFor(@NonNull ViewGroup parent, int position) {
        return new TextClippedImageView(parent.getContext());
    }

    @Override
    public void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp, @Nullable View originator) {
        child.setBackgroundResource(lp.cellBackgroundId);
        if (child instanceof TextClippedImageView) {
            String mask = getMaskFor(lp);
            ((TextClippedImageView) child).setTextMaskPath(mask);
            bindImageToChild((ImageView) child, lp);
        } else {
            super.onBindChild(child, lp, originator);
        }
    }

    /**
     * The returned string will be used as a sort of clipping path for child views,
     * provided that the children are instances of {@link TextClippedImageView}.
     *
     * @param lp    the layout params of the associated view
     * @return a string used for masking. return an empty string and
     * the child view will draw absolutely nothing
     * @see TextClippedImageView#setTextMaskPath(String)
     */
    @NonNull
    public String getMaskFor(@NonNull CutoutViewLayoutParams lp) {
        return String.format(Locale.getDefault(), "~|%d|~", lp.position);
    }
}

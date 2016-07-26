package com.cliabhach.emptyhusk;

import android.support.annotation.NonNull;
import android.support.v4.util.CircularArray;
import android.view.View;

import com.cliabhach.indicator.CutoutViewIndicator;
import com.cliabhach.indicator.CutoutViewLayoutParams;

/**
 * Little onClickListener for toggling through a range of different
 * indicator styles.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class SwitchIndicatorsListener implements View.OnClickListener {
    @NonNull
    private final CutoutViewIndicator cvi;

    @NonNull
    CircularArray<CutoutViewLayoutParams> storage = new CircularArray<>();

    public SwitchIndicatorsListener(@NonNull CutoutViewIndicator cvi) {
        this.cvi = cvi;

        CutoutViewLayoutParams rectangles = cvi.generateDefaultLayoutParams();
        rectangles.indicatorDrawableId = R.drawable.rectangle_accent;
        rectangles.cellBackgroundId = R.drawable.rectangle_secondary;
        storage.addFirst(rectangles);

        CutoutViewLayoutParams circles = cvi.generateDefaultLayoutParams();
        circles.indicatorDrawableId = R.drawable.circle_accent;
        circles.cellBackgroundId = R.drawable.circle_secondary;
        storage.addLast(circles);
    }

    @Override
    public void onClick(View v) {
        cvi.setAllChildParamsTo(nextParams());
    }

    @NonNull
    private CutoutViewLayoutParams nextParams() {
        CutoutViewLayoutParams layoutParams = storage.popFirst();
        storage.addLast(layoutParams);
        return new CutoutViewLayoutParams(layoutParams);
    }
}

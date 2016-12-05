package com.fuzz.indicator;

import android.graphics.Matrix;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;

import static com.fuzz.indicator.OffSetHint.ALPHA;
import static com.fuzz.indicator.OffSetHint.IMAGE_ALPHA;
import static com.fuzz.indicator.OffSetHint.IMAGE_SCALE;
import static com.fuzz.indicator.OffSetHint.SCALE;
import static com.fuzz.indicator.OffSetHint.IMAGE_TRANSLATE;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
@IntDef(value =
        {IMAGE_TRANSLATE,
                ALPHA, IMAGE_ALPHA,
                SCALE, IMAGE_SCALE
        },
        flag = true)
public @interface OffSetHint {
    /**
     * Corresponds to {@link OffSetters#offsetImageBy(ImageView, int, float, Matrix)}
     */
    int IMAGE_TRANSLATE = 1;
    /**
     * Corresponds to {@link OffSetters#offsetAlphaBy(View, float)}
     * @see #IMAGE_ALPHA
     */
    int ALPHA               = 1 << 1;
    /**
     * Corresponds to {@link OffSetters#offsetImageAlphaBy(ImageView, float)}
     * @see #ALPHA
     */
    int IMAGE_ALPHA         = 1 << 2;
    /**
     * Corresponds to {@link OffSetters#offsetScaleBy(View, float)}
     * @see #IMAGE_SCALE
     */
    int SCALE               = 1 << 3;
    /**
     * Corresponds to {@link OffSetters#offsetImageScaleBy(ImageView, float)}
     * @see #SCALE
     */
    int IMAGE_SCALE         = 1 << 4;
}

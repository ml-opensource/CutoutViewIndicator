/*
 * Copyright 2016 Philip Cohn-Cort
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuzz.indicator;

import android.graphics.Matrix;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;

import com.fuzz.indicator.cell.OffSetters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.fuzz.indicator.OffSetHint.ALPHA;
import static com.fuzz.indicator.OffSetHint.IMAGE_ALPHA;
import static com.fuzz.indicator.OffSetHint.IMAGE_SCALE;
import static com.fuzz.indicator.OffSetHint.SCALE;
import static com.fuzz.indicator.OffSetHint.IMAGE_TRANSLATE;

/**
 * Like an enum, but not. The int values in this class correspond
 * directly to methods in {@link OffSetters}.
 * <ul>
 *     <li>{@link #IMAGE_TRANSLATE}</li>
 *     <li>{@link #ALPHA}</li>
 *     <li>{@link #IMAGE_ALPHA}</li>
 *     <li>{@link #SCALE}</li>
 *     <li>{@link #IMAGE_SCALE}</li>
 * </ul>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
@IntDef(value =
        {IMAGE_TRANSLATE,
                ALPHA, IMAGE_ALPHA,
                SCALE, IMAGE_SCALE
        },
        flag = true)
@Retention(RetentionPolicy.CLASS)
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

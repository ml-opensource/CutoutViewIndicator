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
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

/**
 * ViewHolder for a {@link CutoutViewIndicator}.
 *
 * Can be used like a {@link android.widget.ProgressBar}, only more straightforward.
 * <br/><br/>
 * Call {@link OffSetters#offsetImageBy(ImageView, int, float, Matrix)} to offset an image over a static background.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public abstract class IndicatorViewHolder<Root extends View> implements CutoutCell {
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

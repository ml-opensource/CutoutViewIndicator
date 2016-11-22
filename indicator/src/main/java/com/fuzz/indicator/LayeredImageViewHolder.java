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
import android.widget.ImageView;

/**
 * A simple LayeredView implementation that performs a 2D-shift of
 * ImageView content over its background.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class LayeredImageViewHolder extends IndicatorViewHolder<ImageView> {

    public LayeredImageViewHolder(ImageView itemView) {
        super(itemView);
    }

    @Override
    public void offsetContentBy(@NonNull IndicatorOffsetEvent event) {
        OffSetters.offsetImageBy(itemView, event.getOrientation(), event.getFraction(), new Matrix());
    }

}

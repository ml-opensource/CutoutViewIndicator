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

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;

/**
 * Extension of {@link ColorDrawable} with positive width and height.
 *
 * @author Philip Cohn-Cort (Fuzz)
 * @see #setIntrinsicHeight(int)
 * @see #setIntrinsicWidth(int)
 */
public class ResizeableColorDrawable extends ColorDrawable {
    private int intrinsicHeight = 1;
    private int intrinsicWidth = 1;

    /**
     * See the {@link ResizeableColorDrawable class javadoc}.
     *
     * @param drawable    use this to provide a preferred color
     */
    public ResizeableColorDrawable(@NonNull ColorDrawable drawable) {
        super(drawable.getColor());
    }

    @Override
    public int getIntrinsicHeight() {
        return intrinsicHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return intrinsicWidth;
    }

    public void setIntrinsicHeight(int intrinsicHeight) {
        this.intrinsicHeight = intrinsicHeight;
    }

    public void setIntrinsicWidth(int intrinsicWidth) {
        this.intrinsicWidth = intrinsicWidth;
    }
}

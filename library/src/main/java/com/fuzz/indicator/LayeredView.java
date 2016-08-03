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

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Simple interface which views inside the {@link CutoutViewIndicator} should implement.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface LayeredView {
    /**
     * Sets a new image matrix on this view. This method only allows orthogonal offsets
     *
     * @param orientation direction to offset in. Use either {@link LinearLayout#VERTICAL} or {@link LinearLayout#HORIZONTAL}
     * @param percentage  percentage of width/height to offset by - positive means down/right, negative means up/left
     */
    void offsetImageBy(int orientation, float percentage);

    /**
     * @return the actual view represented by this object
     */
    @NonNull
    View getItemView();
}

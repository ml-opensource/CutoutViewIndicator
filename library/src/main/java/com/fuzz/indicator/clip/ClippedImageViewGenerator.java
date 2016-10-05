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
package com.fuzz.indicator.clip;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuzz.indicator.ImageViewGenerator;
import com.fuzz.indicator.widget.ClippedImageView;

/**
 * Proxy variant of {@link ImageViewGenerator} that creates
 * ClippedImageViews
 * in {@link #createChildFor(ViewGroup, int)}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ClippedImageViewGenerator extends ImageViewGenerator {
    @NonNull
    @Override
    protected ImageView createChildFor(@NonNull ViewGroup parent, int position) {
        return new ClippedImageView(parent.getContext());
    }
}

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
package com.fuzz.emptyhusk.choosegenerator;

import android.support.annotation.NonNull;

import com.fuzz.indicator.LayeredViewGenerator;

/**
 * Simple object holding model data about a {@link LayeredViewGenerator}.
 * <p>
 *     {@link #type} is a reference to the exact class this encapsulates,
 *     while {@link #description} is a user-friendly description of its
 *     purpose and usage.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class GeneratorChoice {
    @NonNull
    protected final Class<? extends LayeredViewGenerator> type;
    @NonNull
    protected final String description;

    public GeneratorChoice(@NonNull Class<? extends LayeredViewGenerator> type, @NonNull String description) {
        this.type = type;
        this.description = description;
    }
}

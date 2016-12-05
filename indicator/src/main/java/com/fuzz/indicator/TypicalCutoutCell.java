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

import com.fuzz.indicator.proxy.IndicatorOffsetEvent;

/**
 * Superclass for most cells in a {@link CutoutViewIndicator}. Of course,
 * users of the library are welcome to implement their own
 * {@link CutoutCell}s independently if they so desire.
 * <p>
 *     Subclasses should consider delegating their implementation of
 *     {@link #offsetContentBy(IndicatorOffsetEvent)} to one of the
 *     methods in {@link OffSetters}.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public abstract class TypicalCutoutCell<Root extends View> implements CutoutCell {

    /**
     * @see #getItemView()
     */
    @NonNull
    protected final Root itemView;

    public TypicalCutoutCell(@NonNull Root itemView) {
        this.itemView = itemView;
    }

    /**
     * Externally-accessible reference to {@link #itemView}. Subclasses may choose
     * to reference the field directly, but other objects are kindly requested to use
     * this interface-defined method.
     *
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Root getItemView() {
        return itemView;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " backed by " + itemView;
    }
}

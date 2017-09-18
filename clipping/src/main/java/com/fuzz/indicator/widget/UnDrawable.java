/*
 * Copyright 2016-2017 Philip Cohn-Cort
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
package com.fuzz.indicator.widget;

import android.graphics.Canvas;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.StateSet;

/**
 * This class wraps a drawable, exposing (almost) all of its properties.
 * The only practical distinction is that {@link #draw(Canvas)} does nothing.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class UnDrawable extends StateListDrawable {

    /**
     * The parameter to this method may later be retrieved with {@link #getCurrent()}.
     * If you try to pass in null, a bunch of superclass methods will throw
     * {@link NullPointerException}.
     *
     * @param internalDrawable    provider of all sorts of information
     *                            about this drawable (e.g. size, transparency,
     *                            etc.).
     */
    public void setInternalDrawable(@NonNull Drawable internalDrawable) {
        addState(StateSet.WILD_CARD, internalDrawable);

        selectDrawable(0);
    }

    @Nullable
    @Override
    public Region getTransparentRegion() {
        Drawable current = getCurrent();
        if (current != this) {
            return new Region(current.getBounds());
        } else {
            return super.getTransparentRegion();
        }
    }

    /**
     * This override of draw does nothing.
     * {@inheritDoc}
     * @see #reallyDraw(Canvas)
     */
    @Override
    public void draw(Canvas canvas) {
        // Do absolutely nothing.
    }

    /**
     * Utility method that calls through to {@link DrawableContainer#draw(Canvas)},
     * in case you're making a specialised subclass or need to debug something.
     *
     * @param canvas    The canvas to draw into
     */
    @SuppressWarnings("unused")
    public void reallyDraw(Canvas canvas) {
        super.draw(canvas);
    }
}

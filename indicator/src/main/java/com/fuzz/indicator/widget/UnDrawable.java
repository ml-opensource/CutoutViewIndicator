package com.fuzz.indicator.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.LevelListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * This class wraps a drawable, exposing (almost) all of its properties.
 * The only practical distinction is that {@link #draw(Canvas)} does nothing.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class UnDrawable extends DrawableContainer {

    /**
     * Note that the constructor for {@link android.graphics.drawable.DrawableContainer.DrawableContainerState}
     * is not publicly accessible. This field is a copy of the DrawableContainerState
     * most recently set on the anonymous LevelListDrawable in {@link #UnDrawable()}.
     */
    private DrawableContainerState extracted;

    /**
     * Create a new {@link UnDrawable} with a state stolen from {@link LevelListDrawable}.
     */
    @SuppressWarnings("deprecation")
    public UnDrawable() {
        Drawable seed = Resources.getSystem().getDrawable(android.R.drawable.btn_plus);
        new LevelListDrawable() {
            @Override
            protected void setConstantState(@NonNull DrawableContainerState state) {
                super.setConstantState(state);
                extracted = state;
            }
        };
        extracted.addChild(seed);
        setConstantState(extracted);
        setInternalDrawable(seed);
    }

    @Override
    public int getChangingConfigurations() {
        int changingConfigurations = 0;
        // The superclass will query the constant state, so be careful around that.
        if (extracted != null) {
            changingConfigurations = super.getChangingConfigurations();
        }
        return changingConfigurations;
    }

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
        if (extracted == null) {
            throw new IllegalStateException(
                    "\"setInternalDrawable\" may only be called on UnDrawables with non-null resources."
            );
        }
        if (extracted.getChildCount() <= 0) {
            // We need the state to expand its child array for us
            extracted.addChild(internalDrawable);
        } else {
            // We can modify the array directly.
            Drawable[] children = extracted.getChildren();
            children[0] = internalDrawable;
        }

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
     * For some weird reason, this method is normally protected from objects outside
     * of the DrawableContainer (sub)class. So much of the drawable subsystem is just
     * really clandestine.
     *
     * @param state    where the contained drawables are kept.
     */
    @Override
    public void setConstantState(DrawableContainerState state) {
        super.setConstantState(state);
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

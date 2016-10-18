package com.fuzz.emptyhusk;

import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;

import static org.hamcrest.Matchers.sameInstance;

/**
 * The {@link android.support.test.espresso.Espresso#onView(Matcher)} method
 * DOES NOT support any Matcher for an exact instance. This class fixes that
 * omission.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class NarrowingMatcher extends Is<View> {

    /**
     * Create a new, narrower interpretation of {@link Matchers#sameInstance(Object)}.
     * @param desiredView    any view
     */
    public NarrowingMatcher(View desiredView) {
        super(sameInstance(desiredView));
    }

    /**
     * Create a new, narrower interpretation of {@link Matchers#sameInstance(Object)}.
     * @param desiredView    any view
     */
    public static NarrowingMatcher isTheSameAs(View desiredView) {
        return new NarrowingMatcher(desiredView);
    }
}

package com.fuzz.indicator.reflect;

import android.content.Context;

/**
 * This can be instantiated via reflection only with the
 * {@link #PublicConstructorClass()} constructor.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PublicConstructorClass implements ReflectiveInterface {
    public final Context context;

    public PublicConstructorClass() {
        context = null;
    }

    private PublicConstructorClass(Context context) {
        this.context = context;
    }
}

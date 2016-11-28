package com.fuzz.indicator.reflect;

import android.content.Context;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *     This can be instantiated via reflection, but only with
 *     {@link #PublicConstructorClass() one specific constructor}.
 * </p>
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

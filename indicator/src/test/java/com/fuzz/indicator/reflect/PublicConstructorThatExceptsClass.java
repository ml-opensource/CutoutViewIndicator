package com.fuzz.indicator.reflect;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *     This test should throw an exception in the constructor if
 *     either parameter is non-null.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PublicConstructorThatExceptsClass implements ReflectiveInterface {

    public PublicConstructorThatExceptsClass(Context context, AttributeSet attrs) {
        if (attrs != null || context != null) {
            throw new IllegalArgumentException();
        }
    }
}

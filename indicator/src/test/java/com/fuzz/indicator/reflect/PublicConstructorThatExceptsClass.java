package com.fuzz.indicator.reflect;

import android.content.Context;
import android.util.AttributeSet;

/**
 * This test should throw an exception in the constructor if a mocked
 * {@link AttributeSet} is passed in.
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

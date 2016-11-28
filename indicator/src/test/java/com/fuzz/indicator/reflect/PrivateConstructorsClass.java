package com.fuzz.indicator.reflect;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *    Implements {@link ReflectiveInterface}, unlike
 *    {@link PrivateConstructorWithoutInterfaceClass}
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PrivateConstructorsClass implements ReflectiveInterface {
    private PrivateConstructorsClass() {
    }

    private PrivateConstructorsClass(String ignored) {
    }

    private PrivateConstructorsClass(Context ignored) {
    }

    private PrivateConstructorsClass(Context ignored, AttributeSet alsoIgnored) {
    }
}

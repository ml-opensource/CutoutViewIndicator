package com.fuzz.indicator.reflect;

import android.content.Context;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *     This class implements {@link ReflectiveInterface}, but it's also abstract so
 *     trying to instantiate it will cause an {@link InstantiationException}.
 * </p>
 * @author Philip Cohn-Cort (Fuzz)
 */
public abstract class PrivateConstructorWithInterfaceAbstractClass implements ReflectiveInterface {

    public PrivateConstructorWithInterfaceAbstractClass(Context context) {
    }
}

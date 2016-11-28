package com.fuzz.indicator.reflect;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *    Implements {@link ReflectiveInterface}, unlike
 *    {@link PrivateConstructorWithoutInterfaceClass}
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PrivateConstructorClass implements ReflectiveInterface {
    private PrivateConstructorClass() {
    }
}

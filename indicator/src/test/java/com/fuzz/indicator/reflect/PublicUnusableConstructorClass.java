package com.fuzz.indicator.reflect;

/**
 * This is like {@link PublicConstructorClass} in that it is public and it has
 * a public constructor, but its constructor does not have a method signature
 * that {@link ReConstructor} can recognize.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PublicUnusableConstructorClass implements ReflectiveInterface {

    public PublicUnusableConstructorClass(String ignored) {
    }
}

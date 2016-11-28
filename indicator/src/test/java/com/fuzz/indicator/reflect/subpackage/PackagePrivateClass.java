package com.fuzz.indicator.reflect.subpackage;

import android.content.Context;
import android.util.AttributeSet;

import com.fuzz.indicator.reflect.ReConstructorTest;
import com.fuzz.indicator.reflect.ReflectiveInterface;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *     This class is package-private and in a different package than
 *     {@link ReConstructorTest#reconstructClassWithInaccessibleName}.
 *     Trying to instantiate it should rise an {@link IllegalAccessException}
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class PackagePrivateClass implements ReflectiveInterface {

    public PackagePrivateClass(Context context, AttributeSet attrs, int defaultAttr) {
    }
}

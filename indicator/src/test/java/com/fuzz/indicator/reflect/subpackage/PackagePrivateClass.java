package com.fuzz.indicator.reflect.subpackage;

import android.content.Context;
import android.util.AttributeSet;

import com.fuzz.indicator.reflect.ReflectiveInterface;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
class PackagePrivateClass implements ReflectiveInterface {

    public PackagePrivateClass(Context context, AttributeSet attrs, int defaultAttr) {
    }
}

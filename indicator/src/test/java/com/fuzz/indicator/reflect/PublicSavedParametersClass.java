package com.fuzz.indicator.reflect;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Sample class instantiated via reflection by {@link ReConstructorTest}.
 * <p>
 *     This class keeps track of its two constructor parameters so that
 *     they can be verified in the associated test.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PublicSavedParametersClass implements ReflectiveInterface {

    public final Context context;
    public final AttributeSet attrs;

    public PublicSavedParametersClass(Context context, AttributeSet attrs) {
        this.context = context;
        this.attrs = attrs;
    }
}

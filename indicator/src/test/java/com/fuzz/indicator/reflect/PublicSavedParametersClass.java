package com.fuzz.indicator.reflect;

import android.content.Context;
import android.util.AttributeSet;

/**
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

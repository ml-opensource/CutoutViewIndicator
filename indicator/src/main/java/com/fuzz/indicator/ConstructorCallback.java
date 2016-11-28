package com.fuzz.indicator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Interface for performing actions after
 * {@link CCGFactory#constructGeneratorFrom(Context, AttributeSet, int, String, ConstructorCallback)}
 * finishes.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface ConstructorCallback {
    void onGenerated(@NonNull CutoutCellGenerator generator);

    void onFailed(@NonNull String message);
}

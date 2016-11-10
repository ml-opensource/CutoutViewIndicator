package com.fuzz.indicator;

import android.view.View;

/**
 * Represents an {@link OffsetEvent} initiated by a {@link View}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public interface ViewOffsetEvent extends OffsetEvent {
    View getOriginalView();
}

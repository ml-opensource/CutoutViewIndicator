package com.fuzz.indicator;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.util.HumanReadables.describe;
import static org.junit.Assert.assertEquals;

/**
 * Specialized copy of {@link android.support.test.espresso.assertion.LayoutAssertions}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class BetterLayoutAssertions {
    /**
     * Note that unlike
     * {@link android.support.test.espresso.assertion.LayoutAssertions#noOverlaps(Matcher)}
     * this method will throw an {@link AssertionError} if any {@link android.widget.ImageView}s
     * overlap.
     * @param viewGroup    a group whose direct descendants should not overlap. Does
     *                     <i>not</i> traverse to an arbitrary depth.
     * @throws Exception
     */
    public static void verifyNoOverlaps(@NonNull ViewGroup viewGroup) throws Exception {
        List<View> prevViews = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View selectedView = viewGroup.getChildAt(i);
            Rect selectedBounds = getBounds(selectedView);
            if (couldOverlap(selectedView, selectedBounds)) {
                detectIntersections(selectedView, selectedBounds, prevViews, errorMessage);
                prevViews.add(selectedView);
            }
        }

        assertEquals("", errorMessage.toString());
    }

    private static void detectIntersections(View selectedView, Rect selectedBounds, List<View> prevViews, StringBuilder errorMessage) {
        for (View prevView : prevViews) {
            Rect prevBounds = getBounds(prevView);
            if (Rect.intersects(selectedBounds, prevBounds)) {
                // Overlap detected, add to the error message
                if (errorMessage.length() > 0) {
                    errorMessage.append(",\n\n");
                }
                errorMessage.append(
                        String.format("%s overlaps\n%s", describe(selectedView), describe(prevView)));
                break;
            }
        }
    }

    private static boolean couldOverlap(View selectedView, Rect viewRect) {
        return !viewRect.isEmpty()
                && !(selectedView instanceof TextView
                && ((TextView) selectedView).getText().length() == 0);
    }

    /**
     * Copy of private static method in {@link android.support.test.espresso.assertion.LayoutAssertions}.
     * @param view    the view whose bounds are desired
     * @return the bounds of said view
     */
    private static Rect getBounds(@NonNull View view) {
        int coordinates[] = {0, 0};
        view.getLocationOnScreen(coordinates);
        return new Rect(
                coordinates[0],
                coordinates[1],
                coordinates[0] + view.getWidth() - 1,
                coordinates[1] + view.getHeight() - 1
        );
    }
}

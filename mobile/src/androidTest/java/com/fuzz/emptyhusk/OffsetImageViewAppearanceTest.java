/*
 * Copyright 2016 Philip Cohn-Cort
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuzz.emptyhusk;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.fuzz.indicator.OffSetters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.widget.ImageView.ScaleType.MATRIX;
import static com.fuzz.emptyhusk.BitmapMatcher.sameBitmapAs;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests logical assumptions in
 * {@link OffSetters#offsetImageBy(ImageView, int, float, Matrix)}.
 * Unlike {@code OffsetImageViewTest}, this is focused almost
 * entirely on visual regressions.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
@RunWith(Parameterized.class)
public class OffsetImageViewAppearanceTest {

    @Rule
    public ActivityTestRule<InstrumentationAwareActivity> actRule
            = new ActivityTestRule<>(InstrumentationAwareActivity.class);

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private LinearLayout testLinearLayout;

    @Parameters(name = "IntegrationTest{index} : {0}x{1} at {3}")
    public static Iterable<? extends Object[]> data() {
        return Arrays.asList(new Number[][]{
                {5, 5, HORIZONTAL, -.77f},
                {180, 180, HORIZONTAL, -.77f},
                {5, 5, HORIZONTAL, -1.77f},
                {180, 180, HORIZONTAL, -1.77f},
                {5, 5, HORIZONTAL, -2.77f},
                {100, 100, HORIZONTAL, 0},
                {100, 100, HORIZONTAL, 0.01f},
                {50, 3, HORIZONTAL, .999f},
                {50, 3, HORIZONTAL, 1.999f},
                {50, 3, HORIZONTAL, 2.999f},
                {3, 50, HORIZONTAL, 0},
                {25, 75, HORIZONTAL, 0.3f},
                {183, 129, HORIZONTAL, 0},
                {7, 7, VERTICAL, 0},
                {30, 3, VERTICAL, 0},
                {60, 60, VERTICAL, -1f},
                {100, 100, VERTICAL, -.2f},
                {100, 100, VERTICAL, -.7f},
                {100, 100, VERTICAL, -1.2f},
                {100, 100, VERTICAL, -1.7f},
                {100, 100, VERTICAL, -2.2f},
                {100, 100, VERTICAL, -2.7f},
                {100, 100, VERTICAL, -3.2f}
        });
    }

    private final int width;
    private final int height;
    private final int orientation;
    private final float fraction;

    public OffsetImageViewAppearanceTest(int width, int height, int orientation, float fraction) {
        this.width = width;
        this.height = height;
        this.orientation = orientation;
        this.fraction = fraction;
    }

    @Before
    public void prepareFrame() throws Exception {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                testLinearLayout = actRule.getActivity().inflateLayout(R.layout.test_alt_frame, LinearLayout.class);
                testLinearLayout.setOrientation(orientation);
                actRule.getActivity().inflationFrame.removeAllViews();
                actRule.getActivity().inflationFrame.addView(testLinearLayout);
            }
        });

        CountDownLatch latch = new CountDownLatch(1);
        testLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(listenForGlobalLayout(testLinearLayout, latch));
        testLinearLayout.addOnLayoutChangeListener(listenForLayout(testLinearLayout, latch));
        latch.await();

        assertThat(testLinearLayout.getWidth(), greaterThan(0));
        assertThat(testLinearLayout.getHeight(), greaterThan(0));
    }

    @Test
    public void offsetImageBy() throws Throwable {

        // 1: Create correctly-sized ImageViews

        final ImageView expected = new ImageView(actRule.getActivity());
        final ImageView experimental = new ImageView(actRule.getActivity());

        int accent = ContextCompat.getColor(actRule.getActivity(), R.color.colorAccent);
        int secondary = ContextCompat.getColor(actRule.getActivity(), R.color.colorSecondary);

        sortOutImageView(expected, accent, secondary);
        sortOutImageView(experimental, accent, secondary);

        assertEquals(width, expected.getWidth());
        assertEquals(height, expected.getHeight());
        assertEquals(width, experimental.getWidth());
        assertEquals(height, experimental.getHeight());



        // 2: Offset the experimental ImageView

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                OffSetters.offsetImageBy(experimental, orientation, fraction, new Matrix());
            }
        });


        // 3: Build the perfect 'expected' bitmap

        final Bitmap expectedBacking = Bitmap.createBitmap(width, height, ARGB_8888);
        Canvas expectedTarget = new Canvas(expectedBacking);
        // Draw background...
        expected.getBackground().draw(expectedTarget);
        // Draw foreground...
        adjustExpectations(expected, expectedTarget);
        if (expected.getDrawable() != null) {
            expected.getDrawable().draw(expectedTarget);
        }

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                expected.setImageBitmap(expectedBacking);
            }
        });



        // 4: Build the experimental bitmap

        // Bitmaps cannot have widths or heights <= 0.
        Bitmap experimentalBacking = Bitmap.createBitmap(width, height, ARGB_8888);

        Canvas experimentalTarget = new Canvas(experimentalBacking);
        experimental.draw(experimentalTarget);

        // 5: Build a baseline 'control' bitmap
        Bitmap control = Bitmap.createBitmap(width, height, ARGB_8888);

        // Both bitmaps should have something actually drawn on them
        assertThat(expectedBacking, not(sameBitmapAs(control)));
        assertThat(experimentalBacking, not(sameBitmapAs(control)));

        // 6: Verify that the expected and experimental bitmaps match
        assertThat(experimentalBacking, sameBitmapAs(expectedBacking));
    }

    /**
     * Utility method for {@link #offsetImageBy()}. This modifies the canvas parameter
     * to prepare it for having the ImageView's drawable drawn on it.
     *
     * @param expected          the ImageView which is being adjusted
     * @param expectedTarget    the Canvas on which the ImageView's drawable will be drawn
     */
    private void adjustExpectations(final ImageView expected, Canvas expectedTarget) {
        if (fraction != 0) {
            // Something happened in the offsetImageBy method. Account for it!
            float borderlineFraction = deriveBorderlineFraction(expected);
            if (Math.abs(fraction) >= Math.abs(borderlineFraction)) {
                // expect no foreground to be drawn
                getInstrumentation().runOnMainSync(new Runnable() {
                    @Override
                    public void run() {
                        expected.setImageDrawable(null);
                    }
                });
            } else {
                // expect part (but only part!) of drawable to remain on screen
                float fractionalWidth;
                float fractionalHeight;
                if (orientation == HORIZONTAL) {
                    fractionalWidth = fraction * width;
                    fractionalHeight = 0;
                } else {
                    fractionalWidth = 0;
                    fractionalHeight = fraction * height;
                }

                if ((fraction > 0) ^ (ViewCompat.getLayoutDirection(testLinearLayout) == ViewCompat.LAYOUT_DIRECTION_RTL)) {
                    // Draw towards the end!
                    fractionalHeight = -fractionalHeight;
                    fractionalWidth = -fractionalWidth;
                }
                expectedTarget.translate(fractionalWidth, fractionalHeight);
            }
        }
    }

    /**
     * Get the ratio of drawable size to ImageView size. This is the upper
     * bound on the magnitude of {@link #fraction}.
     *
     * @param imageView    any ImageView
     * @return the ratio between the imageView's drawable width and {@link #width}
     * or between the imageView's drawable height and {@link #height}, depending
     * on {@link #orientation}.
     */
    private float deriveBorderlineFraction(@NonNull ImageView imageView) {
        Drawable d = imageView.getDrawable();
        float borderlineFraction;
        if (orientation == HORIZONTAL) {
            borderlineFraction = d.getIntrinsicWidth() / width;
        } else {
            borderlineFraction = d.getIntrinsicHeight() / height;
        }
        return borderlineFraction;
    }

    private void sortOutImageView(final ImageView view, @ColorInt int accent, @ColorInt int secondary) throws Exception {
        int margin = 30;

        final LayoutParams lp = new LayoutParams(width, height);
        lp.gravity = Gravity.CENTER;
        lp.setMargins(margin, margin, margin, margin);

        final CountDownLatch latch = new CountDownLatch(1);

        view.setScaleType(MATRIX);
        view.setImageDrawable(obtainDrawable(accent));
        view.setBackgroundColor(secondary);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                view.getViewTreeObserver().addOnGlobalLayoutListener(
                        listenForGlobalLayout(view, latch)
                );
                view.addOnLayoutChangeListener(listenForLayout(view, latch));
                testLinearLayout.addView(view, lp);
            }
        });

        latch.await();
    }

    @NonNull
    public Drawable obtainDrawable(@ColorInt int accent) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setSize(width, height);
        drawable.setColor(accent);
        return drawable;
    }

    @NonNull
    private static OnGlobalLayoutListener listenForGlobalLayout(final View view, final CountDownLatch latch) {
        return new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view != null && view.getWidth() >= 1 && view.getHeight() >= 1) {
                    //noinspection deprecation
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    latch.countDown();
                }
            }
        };
    }

    @NonNull
    private static OnLayoutChangeListener listenForLayout(final View view, final CountDownLatch latch) {
        return new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (v == view && (top - bottom >= 1) && (right - left >= 1) ) {
                    v.removeOnLayoutChangeListener(this);
                    latch.countDown();
                }
            }
        };
    }
}
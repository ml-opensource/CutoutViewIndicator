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
package com.fuzz.indicator;

import android.support.test.rule.ActivityTestRule;
import android.widget.FrameLayout;

import com.fuzz.emptyhusk.InstrumentationAwareActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Tests to make sure that the {@link com.fuzz.indicator.CutoutViewIndicator}
 * is inflated correctly.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutViewIndicatorAttributesTest {

    @Rule
    public ActivityTestRule<InstrumentationAwareActivity> actRule
            = new ActivityTestRule<>(InstrumentationAwareActivity.class);

    /**
     * This rule will interrupt {@link #forUI} if that is waiting.
     */
    @Rule
    public Timeout timeout = new Timeout(1500, TimeUnit.MILLISECONDS);

    private CutoutViewIndicator indicatorUnderTest;

    /**
     * This is always initialised in {@link #setUp()} - tests should count down
     * whenever they finish their ui-specific code (which by nature of the indicator
     * is on the main thread). If there is no ui-specific code, then you can safely
     * ignore this field.
     * <p>
     *     The test thread should await that count down, and then run assertions
     *     or whatever. Do not run assertions on the ui thread.
     * </p>
     */
    private CountDownLatch forUI;

    @Before
    public void setUp() {
        forUI = new CountDownLatch(1);
    }

    /**
     * Very simple check here: we try to inflate a CutoutViewIndicator into
     * {@link InstrumentationAwareActivity#inflationFrame a very simple FrameLayout}.
     * @throws Exception
     */
    @Test
    public void cviShouldInflateWithCorrectDefaults() throws Exception {
        actRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                indicatorUnderTest = actRule.getActivity().inflateLayout(com.fuzz.emptyhusk.R.layout.indicator_a, CutoutViewIndicator.class);
                forUI.countDown();
            }
        });
        try {
            forUI.await();
        } catch (InterruptedException ignored) {
        } finally {
            assertNotNull(indicatorUnderTest);
            assertFalse(indicatorUnderTest.isInEditMode());

            // Inflated attributes
            assertEquals(0, indicatorUnderTest.getIndicatorDrawableId());
            assertEquals(0, indicatorUnderTest.getInternalSpacing());
            assertEquals(WRAP_CONTENT, indicatorUnderTest.getCellLength());
            assertEquals(WRAP_CONTENT, indicatorUnderTest.getPerpendicularLength());
            assertEquals(0, indicatorUnderTest.getCellBackgroundId());

            assertThat(indicatorUnderTest.getLayoutParams(), instanceOf(FrameLayout.LayoutParams.class));

            // Consequences of using an UnavailableProxy
            assertEquals(0, indicatorUnderTest.getPageCount());
            assertNotNull(indicatorUnderTest.getGenerator());
            assertNull(indicatorUnderTest.getLayoutParamsForCurrentItem());

            // Technically, this is a consequence of using UnavailableProxy PLUS no views in layout
            assertEquals(0, indicatorUnderTest.getChildCount());
        }
    }
}
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

import android.app.KeyguardManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.fuzz.emptyhusk.InstrumentationAwareActivity;
import com.fuzz.emptyhusk.MainViewBinding;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.LayoutAssertions.noOverlaps;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.view.WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
import static com.fuzz.indicator.NarrowingMatcher.isTheSameAs;
import static com.fuzz.indicator.Proxies.proxyForXCells;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Test to make sure that the {@link com.fuzz.indicator.CutoutViewIndicator}
 * doesn't let any of its children overlap. The test runner will instantiate
 * a new instance of this test for every array returned by {@link #data()}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
@RunWith(Parameterized.class)
public class OverlapTest {

    protected static int maxCellCount = 30;

    @Parameterized.Parameters(name = "With {0} cells")
    public static Iterable<? extends Number[]> data() {
        List<Integer[]> retVal = new ArrayList<>(maxCellCount);
        for (int i = 0; i < maxCellCount; i++) {
            retVal.add(new Integer[]{i});
        }
        return retVal;
    }

    @Rule
    public ActivityTestRule<InstrumentationAwareActivity> actRule
            = new ActivityTestRule<>(InstrumentationAwareActivity.class);

    @Rule
    public Timeout timeout = new Timeout(1, TimeUnit.MINUTES);

    private MainViewBinding binding;

    @Parameterized.Parameter
    public int cellCount;

    @Before
    public void setUp() throws Throwable {
        binding = actRule.getActivity().binding;
        actRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyguardManager keyguardManager = (KeyguardManager) actRule.getActivity().getSystemService(KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
                lock.disableKeyguard();

                //Forcibly ignore any active keyguard
                actRule.getActivity().getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED
                        | FLAG_DISMISS_KEYGUARD
                        | FLAG_KEEP_SCREEN_ON
                        | FLAG_TURN_SCREEN_ON
                        | FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
            }
        });
    }

    /**
     * Makes sure that the children are laid out separately.
     * @throws Exception
     */
    @Test
    public void cviChildrenShouldNotOverlap() throws Exception {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                StateProxy proxy = proxyForXCells(cellCount);
                binding.cvi.setStateProxy(proxy);
                binding.cvi.ensureOnlyCurrentItemsSelected();
            }
        });
        View decorView = binding.root.getRootView();
        try {
            onView(
                    isTheSameAs(binding.cvi)
            ).inRoot(
                    withDecorView(
                            isTheSameAs(decorView)
                    )
            ).check(
                    noOverlaps(allOf(
                            withEffectiveVisibility(VISIBLE), not(isTheSameAs(binding.cvi))
                    ))
            );
        } catch (RuntimeException ignored) {
            // Inspired by http://stackoverflow.com/a/19776489/3934789
            Class globalClass = Class.forName("android.view.WindowManagerGlobal");
            Object globalInstance = globalClass.getMethod("getInstance").invoke(null, (Object[])null);

            Method getViewRootNames = globalClass.getMethod("getViewRootNames");
            Method getRootView = globalClass.getMethod("getRootView", String.class);
            String[] rootViewNames = (String[])getViewRootNames.invoke(globalInstance, (Object[])null);

            StringBuilder foundRootViews = new StringBuilder();
            for(String viewName : rootViewNames) {
                View rootView = (View)getRootView.invoke(globalInstance, viewName);
                foundRootViews
                        .append('\n')
                        .append("Found root view: ")
                        .append(viewName)
                        .append(" with classname ")
                        .append(rootView.getClass());
            }
            throw new RuntimeException(
                    "Known Windows: " + foundRootViews + "; Caused by a DecorView with "
                            + "application-window-token=" + decorView.getApplicationWindowToken()
                            + ", window-token=" + decorView.getWindowToken()
                            + ", has-window-focus=" + decorView.hasWindowFocus()
            );
        }
        assertEquals(cellCount, binding.cvi.getChildCount());
    }
}

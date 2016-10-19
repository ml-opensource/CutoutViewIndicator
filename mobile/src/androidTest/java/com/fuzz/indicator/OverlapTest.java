package com.fuzz.indicator;

import android.support.test.rule.ActivityTestRule;

import com.fuzz.emptyhusk.InstrumentationAwareActivity;
import com.fuzz.emptyhusk.MainViewBinding;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.LayoutAssertions.noOverlaps;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
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

    /**
     * This rule will interrupt {@link #forUI} if that is waiting.
     */
    @Rule
    public Timeout timeout = new Timeout(1500, TimeUnit.MILLISECONDS);

    private MainViewBinding binding;
    /**
     * This is always initialised in {@link #setUp()} - tests should count down
     * whenever they finish their ui-specific code (which by nature of the indicator
     * is on the main thread). If there is no ui-specific code, then you can safely
     * ignore this field.
     * <p>
     * The test thread should await that count down, and then run assertions
     * or whatever. Do not run assertions on the ui thread.
     * </p>
     */
    private CountDownLatch forUI;

    @Parameterized.Parameter
    public int cellCount;

    @Before
    public void setUp() {
        binding = actRule.getActivity().binding;
        forUI = new CountDownLatch(1);
    }

    /**
     * Makes sure that the children are laid out separately.
     * @throws Exception
     */
    @Test
    public void cviChildrenShouldNotOverlap() throws Exception {
        actRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StateProxy proxy = proxyForXCells(cellCount);
                binding.cvi.setStateProxy(proxy);
                binding.cvi.ensureOnlyCurrentItemsSelected();
                forUI.countDown();
            }
        });
        try {
            forUI.await();
        } catch (InterruptedException ignored) {
        } finally {
            onView(
                    isTheSameAs(binding.cvi)
            ).check(
                    noOverlaps(allOf(
                            withEffectiveVisibility(VISIBLE), not(isTheSameAs(binding.cvi))
                    ))
            );
            assertEquals(cellCount, binding.cvi.getChildCount());
        }
    }
}

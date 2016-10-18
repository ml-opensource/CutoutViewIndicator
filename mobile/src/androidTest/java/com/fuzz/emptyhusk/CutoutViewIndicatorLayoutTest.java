package com.fuzz.emptyhusk;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.LayoutAssertions.noOverlaps;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static com.fuzz.emptyhusk.NarrowingMatcher.isTheSameAs;
import static org.junit.Assert.fail;

/**
 * Tests to make sure that the {@link com.fuzz.indicator.CutoutViewIndicator}
 * is laid out correctly.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutViewIndicatorLayoutTest {

    @Rule
    public ActivityTestRule<InstrumentationAwareActivity> actRule
            = new ActivityTestRule<>(InstrumentationAwareActivity.class);

    private MainViewBinding binding;

    @Before
    public void setUp() {
        binding = actRule.getActivity().binding;
    }

    @Test
    public void cviShouldHaveCorrectNumberOfChildren() throws Exception {
        // TODO: write the actual test
        fail();
    }

    @Test
    public void cviChildrenShouldNotOverlap() throws Exception {
        onView(
                isTheSameAs(binding.cvi)
        ).check(
                noOverlaps(withEffectiveVisibility(VISIBLE))
        );
    }

}
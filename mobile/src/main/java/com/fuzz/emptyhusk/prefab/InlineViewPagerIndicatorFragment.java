package com.fuzz.emptyhusk.prefab;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuzz.emptyhusk.R;
import com.fuzz.indicator.CutoutViewIndicator;

/**
 * This fragment offers a couple different inline {@link CutoutViewIndicator}
 * instances, for sampling purposes.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class InlineViewPagerIndicatorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_horizontal_inline, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            viewPager.setAdapter(new InlineAdapter());

            final CutoutViewIndicator cviA = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorA);
            final CutoutViewIndicator cviB = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorB);
            final CutoutViewIndicator cviC = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorC);
            cviA.setViewPager(viewPager);
            cviB.setViewPager(viewPager);
            cviC.setViewPager(viewPager);
        }
    }

}

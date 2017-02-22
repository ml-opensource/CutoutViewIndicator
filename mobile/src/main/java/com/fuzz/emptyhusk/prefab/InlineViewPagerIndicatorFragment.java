/*
 * Copyright 2016-2017 Philip Cohn-Cort
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
import com.fuzz.indicator.OffSetHint;

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
            final ViewPager viewPagerA = (ViewPager) view.findViewById(R.id.viewPagerA);
            viewPagerA.setAdapter(new InlineAdapter(7));

            final CutoutViewIndicator cviA = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorA);
            final CutoutViewIndicator cviB = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorB);
            final CutoutViewIndicator cviC = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorC);
            cviA.setViewPager(viewPagerA);
            cviB.setViewPager(viewPagerA);
            cviC.setViewPager(viewPagerA);

            final ViewPager viewPagerB = (ViewPager) view.findViewById(R.id.viewPagerB);
            viewPagerB.setAdapter(new InlineAdapter(9));

            final CutoutViewIndicator cviD = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorD);
            final CutoutViewIndicator cviE = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorE);
            final CutoutViewIndicator cviF = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorF);
            cviD.setViewPager(viewPagerB);
            cviE.setViewPager(viewPagerB);
            cviE.setOffsetHints(OffSetHint.IMAGE_ALPHA);
            cviF.setViewPager(viewPagerB);
        }
    }

}

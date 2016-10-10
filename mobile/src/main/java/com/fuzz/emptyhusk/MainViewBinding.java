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

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.NumberPicker;

import com.fuzz.indicator.CutoutViewIndicator;

/**
 * Like a DataBinding-generated file, only without any observable inheritance or
 * any Annotation processing.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class MainViewBinding {
    public final Toolbar toolbar;
    public final ViewPager mainViewPager;
    public final FloatingActionButton fab;

    public final NumberPicker spacing;
    public final NumberPicker width;
    public final NumberPicker height;
    public final GridLayout gridLayout;

    public final CutoutViewIndicator cvi;
    public final CompoundButton unifiedButton;
    public final CompoundButton orientationButton;

    public MainViewBinding(View root) {
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mainViewPager = (ViewPager) root.findViewById(R.id.mainViewPager);
        fab = (FloatingActionButton) root.findViewById(R.id.fab);

        cvi = (CutoutViewIndicator) root.findViewById(R.id.cutoutViewIndicator);

        spacing = (NumberPicker) root.findViewById(R.id.spacingPicker);
        width = (NumberPicker) root.findViewById(R.id.widthPicker);
        height = (NumberPicker) root.findViewById(R.id.heightPicker);

        gridLayout = (GridLayout) root.findViewById(R.id.gridLayout);

        unifiedButton = (CompoundButton) root.findViewById(R.id.unifiedSwitch);
        orientationButton = (CompoundButton) root.findViewById(R.id.orientationSwitch);
    }
}

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

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.fuzz.indicator.CutoutViewIndicator;

public class MainActivity extends AppCompatActivity {

    private static int deriveDPFrom(Context context, int pixelCount) {
        float retVal = pixelCount / context.getResources().getDisplayMetrics().density;

        return ((int) retVal);
    }

    private static int derivePXFrom(Context context, int dpCount) {
        float retVal = dpCount * context.getResources().getDisplayMetrics().density;

        return ((int) retVal);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        SimplePagerAdapter adapter = defineAdapter(getResources());
        initPager(viewPager, adapter);

        CutoutViewIndicator cvi = (CutoutViewIndicator) findViewById(R.id.cutoutViewIndicator);
        initIndicator(cvi, viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        CompoundButton unifiedButton = (CompoundButton) findViewById(R.id.unifiedSwitch);
        CompoundButton orientationButton = (CompoundButton) findViewById(R.id.orientationSwitch);
        View gridLayout = findViewById(R.id.gridLayout);
        initButtons(fab, unifiedButton, orientationButton, gridLayout, cvi);

        NumberPicker spacing = (NumberPicker) findViewById(R.id.spacingPicker);
        NumberPicker width = (NumberPicker) findViewById(R.id.widthPicker);
        NumberPicker height = (NumberPicker) findViewById(R.id.heightPicker);
        initPickers(spacing, width, height, cvi);

        if (viewPager != null) {
            viewPager.setCurrentItem(1);
        }
    }

    private void initButtons(
            FloatingActionButton fab,
            CompoundButton unified,
            CompoundButton orientationButton,
            final View anchor,
            final CutoutViewIndicator cvi
    ) {
        unified.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                cvi.cascadeParamChanges(isChecked);
            }
        });
        orientationButton.setOnCheckedChangeListener(new ToggleAlignmentListener(cvi, anchor));
        fab.setOnClickListener(new SwitchIndicatorsListener(cvi) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                setPickerValuesFrom(cvi);
            }
        });
    }

    private void setPickerValuesFrom(@NonNull CutoutViewIndicator cvi) {
        NumberPicker spacing = (NumberPicker) findViewById(R.id.spacingPicker);
        NumberPicker width = (NumberPicker) findViewById(R.id.widthPicker);
        NumberPicker height = (NumberPicker) findViewById(R.id.heightPicker);

        if (spacing != null) {
            spacing.setValue(deriveDPFrom(this, cvi.getInternalSpacing()));
        }
        if (width != null) {
            width.setValue(deriveDPFrom(this, cvi.getCellLength()));
        }
        if (height != null) {
            height.setValue(deriveDPFrom(this, cvi.getPerpendicularLength()));
        }
    }

    @NonNull
    private SimplePagerAdapter defineAdapter(@NonNull Resources resources) {
        String[] pages = resources.getStringArray(R.array.introductory_messages);
        return new SimplePagerAdapter(pages);
    }

    private void initPager(ViewPager viewPager, SimplePagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    private void initIndicator(CutoutViewIndicator cvi, ViewPager viewPager) {
        cvi.cascadeParamChanges(true);
        cvi.setViewPager(viewPager);
    }

    private void initPickers(
            NumberPicker spacing, NumberPicker width, NumberPicker height,
            final CutoutViewIndicator cvi
    ) {
        setRangeOn(spacing);
        spacing.setOnValueChangedListener(new BoundValueListener() {
            @Override
            protected void onNewValue(int px) {
                cvi.setInternalSpacing(px);
            }
        });
        setRangeOn(width);
        width.setOnValueChangedListener(new BoundValueListener() {
            @Override
            protected void onNewValue(int px) {
                cvi.setCellLength(px);
            }
        });
        setRangeOn(height);
        height.setOnValueChangedListener(new BoundValueListener() {
            protected void onNewValue(int px) {
                cvi.setPerpendicularLength(px);
            }
        });
        // Initial values can only be set after the ranges have been defined
        setPickerValuesFrom(cvi);
    }

    private void setRangeOn(NumberPicker picker) {
        picker.setMinValue(0);
        picker.setMaxValue(200);
    }

    private abstract static class BoundValueListener implements OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (newVal >= 0) {
                int px = derivePXFrom(picker.getContext(), newVal);
                onNewValue(px);
            }
        }

        protected abstract void onNewValue(int px);
    }
}

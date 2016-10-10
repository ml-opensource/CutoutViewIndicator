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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.fuzz.indicator.CutoutViewIndicator;

/**
 * Entry point into the sample application. This is designed to cover all the basic
 * functionality of {@link CutoutViewIndicator}, without being too big or complex.
 */
public class MainActivity extends AppCompatActivity {

    public static int deriveDPFrom(Context context, int pixelCount) {
        float retVal = pixelCount / context.getResources().getDisplayMetrics().density;

        return ((int) retVal);
    }

    public static int derivePXFrom(Context context, int dpCount) {
        float retVal = dpCount * context.getResources().getDisplayMetrics().density;

        return ((int) retVal);
    }

    protected MainViewBinding binding;

    protected PickerDelegate pickerDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = new MainViewBinding(findViewById(R.id.root));
        pickerDelegate = new PickerDelegate(binding);

        setSupportActionBar(binding.toolbar);

        SimplePagerAdapter adapter = defineAdapter(getResources());
        initPager(binding.mainViewPager, adapter);

        initIndicator(binding.cvi, binding.mainViewPager);

        initButtons();

        pickerDelegate.initPickers();

        if (binding.mainViewPager != null) {
            binding.mainViewPager.setCurrentItem(1);
        }
    }


    private void initButtons() {
        binding.unifiedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                binding.cvi.cascadeParamChanges(isChecked);
            }
        });
        binding.orientationButton.setOnCheckedChangeListener(new ToggleAlignmentListener(binding.cvi, binding.gridLayout));
        binding.fab.setOnClickListener(new SwitchIndicatorsListener(binding.cvi) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                pickerDelegate.setPickerValuesFrom(binding.cvi);
            }
        });
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

}

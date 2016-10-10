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

import android.support.annotation.NonNull;
import android.widget.NumberPicker;

import com.fuzz.indicator.CutoutViewIndicator;

import static com.fuzz.emptyhusk.MainActivity.deriveDPFrom;

/**
 * Utility class designed solely to handle state changes on the NumberPickers shown
 * in {@link MainActivity}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PickerDelegate {

    @NonNull
    protected MainViewBinding binding;

    public PickerDelegate(@NonNull MainViewBinding binding) {
        this.binding = binding;
    }

    public void initPickers() {
        setRangeOn(binding.spacing);
        binding.spacing.setOnValueChangedListener(new BoundValueListener() {
            @Override
            protected void onNewValue(int px) {
                binding.cvi.setInternalSpacing(px);
            }
        });
        setRangeOn(binding.width);
        binding.width.setOnValueChangedListener(new BoundValueListener() {
            @Override
            protected void onNewValue(int px) {
                binding.cvi.setCellLength(px);
            }
        });
        setRangeOn(binding.height);
        binding.height.setOnValueChangedListener(new BoundValueListener() {
            protected void onNewValue(int px) {
                binding.cvi.setPerpendicularLength(px);
            }
        });
        // Initial values can only be set after the ranges have been defined
        setPickerValuesFrom(binding.cvi);
    }

    protected void setRangeOn(NumberPicker picker) {
        picker.setWrapSelectorWheel(false);
        picker.setMinValue(0);
        picker.setMaxValue(200);
    }

    void setPickerValuesFrom(@NonNull CutoutViewIndicator cvi) {
        NumberPicker spacing = binding.spacing;
        NumberPicker width = binding.width;
        NumberPicker height = binding.height;

        if (spacing != null) {
            spacing.setValue(deriveDPFrom(cvi.getContext(), cvi.getInternalSpacing()));
        }
        if (width != null) {
            width.setValue(deriveDPFrom(cvi.getContext(), cvi.getCellLength()));
        }
        if (height != null) {
            height.setValue(deriveDPFrom(cvi.getContext(), cvi.getPerpendicularLength()));
        }
    }
}

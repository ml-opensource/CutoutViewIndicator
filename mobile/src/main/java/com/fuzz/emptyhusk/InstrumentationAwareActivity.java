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

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Variant of {@link MainActivity} explicitly for instrumentation testing.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class InstrumentationAwareActivity extends AppCompatActivity {

    public MainViewBinding binding;
    public PickerDelegate pickerDelegate;

    public ViewGroup inflationFrame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = new MainViewBinding(findViewById(R.id.root));
        pickerDelegate = new PickerDelegate(binding);

        setSupportActionBar(binding.toolbar);

        inflationFrame = (ViewGroup) getLayoutInflater().inflate(R.layout.test_frame, binding.root, false);
        binding.root.addView(inflationFrame);
    }

    public <T extends View> T inflateLayout(@LayoutRes int layoutId, Class<T> clazz) {
        View inflated = getLayoutInflater().inflate(layoutId, inflationFrame, false);
        return clazz.cast(inflated);
    }
}

package com.fuzz.emptyhusk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Variant of {@link MainActivity} explicitly for instrumentation testing.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class InstrumentationAwareActivity extends AppCompatActivity {

    protected MainViewBinding binding;
    protected PickerDelegate pickerDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = new MainViewBinding(findViewById(R.id.root));
        pickerDelegate = new PickerDelegate(binding);

        setSupportActionBar(binding.toolbar);
    }
}

package com.cliabhach.emptyhusk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.cliabhach.indicator.CutoutViewIndicator;

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
        SimplePagerAdapter adapter = defineAdapter();
        initPager(viewPager, adapter);

        CutoutViewIndicator cvi = (CutoutViewIndicator) findViewById(R.id.cutoutViewIndicator);
        initIndicator(cvi, viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        CompoundButton button = (CompoundButton) findViewById(R.id.unifiedSwitch);
        initButtons(fab, button, cvi);

        NumberPicker spacing = (NumberPicker) findViewById(R.id.spacingPicker);
        NumberPicker width = (NumberPicker) findViewById(R.id.widthPicker);
        NumberPicker height = (NumberPicker) findViewById(R.id.heightPicker);
        initPickers(spacing, width, height, cvi);
    }

    private void initButtons(FloatingActionButton fab, CompoundButton button, final CutoutViewIndicator cvi) {
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                cvi.cascadeParamChanges(isChecked);
            }
        });
        fab.setOnClickListener(new SwitchIndicatorsListener(cvi));
    }

    @NonNull
    private SimplePagerAdapter defineAdapter() {
        String[] pages = {"1", "2", "3", "4", "5", "6"};
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
        spacing.setValue(deriveDPFrom(this, cvi.getInternalSpacing()));
        spacing.setOnValueChangedListener(new BoundValueListener() {
            @Override
            protected void onNewValue(int px) {
                cvi.setInternalSpacing(px);
            }
        });
        setRangeOn(width);
        width.setValue(deriveDPFrom(this, cvi.getCellLength()));
        width.setOnValueChangedListener(new BoundValueListener() {
            @Override
            protected void onNewValue(int px) {
                cvi.setCellLength(px);
            }
        });
        setRangeOn(height);
        height.setValue(deriveDPFrom(this, cvi.getPerpendicularLength()));
        height.setOnValueChangedListener(new BoundValueListener() {
            protected void onNewValue(int px) {
                cvi.setPerpendicularLength(px);
            }
        });
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

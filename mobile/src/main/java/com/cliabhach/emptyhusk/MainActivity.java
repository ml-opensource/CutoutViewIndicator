package com.cliabhach.emptyhusk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.cliabhach.indicator.CutoutViewIndicator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        SimplePagerAdapter adapter = defineAdapter();
        initPager(viewPager, adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        initButtons(fab, adapter);

        CutoutViewIndicator cvi = (CutoutViewIndicator) findViewById(R.id.cutoutViewIndicator);
        initIndicator(cvi, viewPager);

        NumberPicker spacing = (NumberPicker) findViewById(R.id.spacingPicker);
        NumberPicker width = (NumberPicker) findViewById(R.id.widthPicker);
        NumberPicker height = (NumberPicker) findViewById(R.id.heightPicker);
        initPickers(spacing, width, height, cvi, adapter);
    }

    private void initButtons(FloatingActionButton fab, final @NonNull PagerAdapter adapter) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });
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
        cvi.setViewPager(viewPager);
    }

    private void initPickers(
            NumberPicker spacing, NumberPicker width, NumberPicker height,
            final CutoutViewIndicator cvi,
            final PagerAdapter adapter
    ) {
        // TODO: throttle calls to notifyDataSetChanged
        setRangeOn(spacing);
        spacing.setValue(cvi.getInternalSpacing());
        spacing.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal >= 0) {
                    cvi.setInternalSpacing(newVal);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        setRangeOn(width);
        width.setValue(cvi.getCellLength());
        width.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal >= 0) {
                    cvi.setCellLength(newVal);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        setRangeOn(height);
        height.setValue(cvi.getPerpendicularLength());
        height.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal >= 0) {
                    cvi.setPerpendicularLength(newVal);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setRangeOn(NumberPicker picker) {
        picker.setMinValue(0);
        picker.setMaxValue(200);
    }

}

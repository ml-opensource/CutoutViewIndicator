package com.cliabhach.emptyhusk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        initButtons(fab);

        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        initPager(viewPager);

        CutoutViewIndicator cvi = (CutoutViewIndicator) findViewById(R.id.cutoutViewIndicator);
        initIndicator(cvi, viewPager);

        NumberPicker spacing = (NumberPicker) findViewById(R.id.spacingPicker);
        NumberPicker width = (NumberPicker) findViewById(R.id.widthPicker);
        NumberPicker height = (NumberPicker) findViewById(R.id.heightPicker);
        initPickers(spacing, width, height, cvi);
    }

    private void initButtons(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initPager(ViewPager viewPager) {
        String[] pages = new String[]{"1", "2", "3", "4", "5", "6"};
        viewPager.setAdapter(new SimplePagerAdapter(pages));
    }

    private void initIndicator(CutoutViewIndicator cvi, ViewPager viewPager) {
        cvi.setViewPager(viewPager);
    }

    private void initPickers(NumberPicker spacing, NumberPicker width, NumberPicker height, final CutoutViewIndicator cvi) {
        setRangeOn(spacing);
        spacing.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal >= 0) {
                    cvi.setMarginLength(newVal);
                }
            }
        });
        setRangeOn(width);
        width.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal >= 0) {
                    cvi.setCellLength(newVal);
                }
            }
        });
        setRangeOn(height);
        height.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal >= 0) {
                    cvi.setPerpendicularLength(newVal);
                }
            }
        });
    }

    private void setRangeOn(NumberPicker picker) {
        picker.setMinValue(0);
        picker.setMaxValue(100);
    }

}

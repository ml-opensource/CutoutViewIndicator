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
package com.fuzz.emptyhusk.prefab;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuzz.emptyhusk.R;
import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.clip.ClippedImageViewGenerator;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This fragment does not expect any arguments to be passed in.
 * All configuration and appearance logic is self-contained.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class VerticalDotsFragment extends Fragment {

    public int getChildQuantity() {
        return 8;
    }

    public int getChildLayoutId() {
        return R.layout.cell_color_spacer;
    }

    @DrawableRes
    public int getDrawableId() {
        return android.R.drawable.star_on;
    }

    @DrawableRes
    public int getBackgroundDrawableId() {
        return android.R.drawable.star_off;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vertical_dots, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        initRecycler(recyclerView);

        CutoutViewIndicator cvi = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicator);
        initIndicator(recyclerView, cvi);
    }

    private void initRecycler(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MultiColoredAdapter(getChildQuantity(), getChildLayoutId()));

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
    }

    /**
     * Precondition: at least one call to {@link #initRecycler(RecyclerView)} with the same parameter.
     * @param recyclerView    an initialised RecyclerView
     * @param cvi             a new CutoutViewIndicator
     */
    private void initIndicator(@NonNull RecyclerView recyclerView, @NonNull CutoutViewIndicator cvi) {
        cvi.setGenerator(new ClippedImageViewGenerator());
        cvi.setBackgroundColor(Color.BLACK);
        cvi.setCellLength(WRAP_CONTENT);
        cvi.setPerpendicularLength(WRAP_CONTENT);
        cvi.setInternalSpacing(40);
        cvi.setIndicatorDrawableId(getDrawableId());
        cvi.setCellBackgroundId(getBackgroundDrawableId());

        int initialDx = recyclerView.getScrollX();
        int initialDy = recyclerView.getScrollY();

        CVIScrollListener listener = new CVIScrollListener(cvi, initialDx, initialDy);
        RecyclerStateProxy proxy = new RecyclerStateProxy(recyclerView, cvi, listener);
        cvi.setStateProxy(proxy);
        recyclerView.addOnScrollListener(listener);
    }
}

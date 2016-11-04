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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.fuzz.emptyhusk.R;
import com.fuzz.indicator.CutoutViewIndicator;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * This fragment does not expect any arguments to be passed in.
 * All configuration and appearance logic is self-contained.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class NarrowerSegmentsFragment extends Fragment {

    public int getChildQuantity() {
        return 23;
    }

    public int getChildLayoutId() {
        return R.layout.cell_narrow_color_spacer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vertical_dots, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            initRecycler(recyclerView);

            final CutoutViewIndicator cvi = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicator);
            initIndicator(recyclerView, cvi);

            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        //noinspection deprecation
                        recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    ProportionalImageViewGenerator gen = new ProportionalImageViewGenerator();
                    gen.setProportionalTo(recyclerView);
                    cvi.setGenerator(gen);
                }
            });
        }
    }

    private void initRecycler(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MultiColoredAdapter(getChildQuantity(), getChildLayoutId()));
    }

    /**
     * Precondition: at least one call to {@link #initRecycler(RecyclerView)} with the same parameter.
     * @param recyclerView    an initialised RecyclerView
     * @param cvi             a new CutoutViewIndicator
     */
    private void initIndicator(@NonNull final RecyclerView recyclerView, @NonNull final CutoutViewIndicator cvi) {
        cvi.setIndicatorDrawableId(0);

        int initialDx = recyclerView.getScrollX();
        int initialDy = recyclerView.getScrollY();

        CVIScrollListener listener = new CVIScrollListener(cvi, initialDx, initialDy);
        RecyclerStateProxy proxy = new RecyclerStateProxy(recyclerView, cvi, listener);
        cvi.setStateProxy(proxy);
        recyclerView.addOnScrollListener(listener);
    }
}

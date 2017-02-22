/*
 * Copyright 2016-2017 Philip Cohn-Cort
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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuzz.emptyhusk.R;
import com.fuzz.indicator.ViewProvidingAdapter;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class InlineAdapter extends PagerAdapter implements ViewProvidingAdapter {

    @NonNull
    private final LruCache<Integer, View> viewCache;
    private final int count;

    public InlineAdapter(int count) {
        this.count = count;
        viewCache = new LruCache<>(4);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int resId = (position % 3 == 0) ? R.drawable.rectangle_wide_secondary : R.drawable.rectangle_wide_tertiary;

        ImageView inflated = new ImageView(container.getContext());
        inflated.setImageResource(resId);
        container.addView(inflated);

        viewCache.put(position, inflated);

        return inflated;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public View getViewFor(int cviPosition) {
        return viewCache.get(cviPosition);
    }
}

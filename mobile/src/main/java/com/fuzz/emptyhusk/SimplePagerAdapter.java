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
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuzz.indicator.ViewProvidingAdapter;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class SimplePagerAdapter extends PagerAdapter implements ViewProvidingAdapter {

    @NonNull
    private final String[] values;

    @NonNull
    private final LruCache<String, View> viewCache;

    public SimplePagerAdapter(@NonNull String[] values) {
        this.values = values;
        viewCache = new LruCache<>(values.length);
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        TextView textView = (TextView) view.findViewById(R.id.cell_text);
        CharSequence text = null;

        if (textView != null) {
            text = textView.getText();
        }

        return text != null && text.equals(object);
    }

    @Nullable
    @Override
    public View getViewFor(int cviPosition) {
        return viewCache.get(values[cviPosition]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String value = values[position];

        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View inflated = inflater.inflate(R.layout.pager_cell_text, container, false);
        TextView item = (TextView) inflated.findViewById(R.id.cell_text);

        item.setText(value);

        viewCache.put(value, inflated);

        container.addView(inflated);

        return value;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = viewCache.get(object.toString());
        if (view != null) {
            container.removeView(view);
        }
    }
}

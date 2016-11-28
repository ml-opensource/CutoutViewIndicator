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
package com.fuzz.indicator.text;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.CutoutViewLayoutParams;
import com.fuzz.indicator.CutoutCell;
import com.fuzz.indicator.CutoutCellGenerator;

/**
 * This generator is intended to animate stuff on TextViews
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public abstract class TextViewGenerator implements CutoutCellGenerator {
    @NonNull
    @Override
    public CutoutCell createCellFor(@NonNull ViewGroup parent, int position) {
        CutoutViewLayoutParams lp = ((CutoutViewIndicator) parent).generateDefaultLayoutParams();

        TextView child = new TextView(parent.getContext());
        child.setText(getTextFor(parent.getContext(), position));
        child.setLayoutParams(lp);

        return new CutoutTextCell(child);
    }

    @NonNull
    protected abstract Spannable getTextFor(@NonNull Context context, int position);

    @Override
    public void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp, @Nullable View originator) {
        child.setBackgroundResource(lp.cellBackgroundId);
    }
}

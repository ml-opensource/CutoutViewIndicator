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
package com.fuzz.indicator.clip;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuzz.indicator.CutoutViewLayoutParams;
import com.fuzz.indicator.ImageCellGenerator;
import com.fuzz.indicator.R;
import com.fuzz.indicator.widget.TextClippedImageView;

import java.util.Locale;

/**
 * This Generator is capable of building an auto-incrementing sequence of characters.
 *
 * Each cell created by this class will display one such character.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class SequentialCellGenerator extends ImageCellGenerator {

    @NonNull
    private String textMask = "~|%d|~";

    public SequentialCellGenerator() {
    }

    public SequentialCellGenerator(@NonNull Context context, @NonNull AttributeSet attrs, int defAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CutoutViewIndicator, defAttr, 0);
        String textMask = a.getString(R.styleable.CutoutViewIndicator_rcv_generator_text_mask);
        if (textMask != null) {
            setTextMask(textMask);
        }
        a.recycle();
    }

    @NonNull
    @Override
    protected ImageView createChildFor(@NonNull ViewGroup parent, int position) {
        return new TextClippedImageView(parent.getContext());
    }

    @Override
    public void onBindChild(@NonNull View child, @NonNull CutoutViewLayoutParams lp, @Nullable View originator) {
        child.setBackgroundResource(lp.cellBackgroundId);
        if (child instanceof TextClippedImageView) {
            if (!((TextClippedImageView)child).hasTextMask()) {
                // Child mask is not set - propagate our default value
                String mask = getMaskFor(lp);
                ((TextClippedImageView) child).setTextMaskPath(mask);
            }
            bindImageToChild((ImageView) child, lp);
        } else {
            super.onBindChild(child, lp, originator);
        }
    }

    /**
     * Set a new string to be used as base mask by {@link #getMaskFor(CutoutViewLayoutParams)}.
     *
     * @param textMask    a string, with at most one {@code %d} where the position should go.
     */
    public void setTextMask(@NonNull String textMask) {
        this.textMask = textMask;
    }

    /**
     * The returned string will be used as a sort of clipping path for child views,
     * provided that the children are instances of {@link TextClippedImageView}.
     *
     * @param lp    the layout params of the associated view
     * @return a string used for masking. return an empty string and
     * the child view will draw absolutely nothing
     * @see TextClippedImageView#setTextMaskPath(String)
     */
    @NonNull
    public String getMaskFor(@NonNull CutoutViewLayoutParams lp) {
        return String.format(Locale.getDefault(), textMask, lp.position);
    }
}

/*
 * Copyright 2017 Philip Cohn-Cort
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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuzz.emptyhusk.R;
import com.fuzz.emptyhusk.looping.LoopingStateProxy;
import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.proxy.StateProxy;
import com.fuzz.indicator.widget.TextClippedImageView;

import static com.fuzz.indicator.widget.TextClippedImageView.NOT_CLIPPED;

/**
 * This fragment shows an auto-animating version of the library's logo (subject to change).
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class AutomaticLogoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_automatic_logo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            StateProxy proxy = new LoopingStateProxy();

            final CutoutViewIndicator cviA = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicatorA);
            cviA.setStateProxy(proxy);

            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(90);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setAntiAlias(true);
            textPaint.setColor(NOT_CLIPPED);

            for (int i = 0; i < cviA.getChildCount(); i++) {
                TextClippedImageView child = (TextClippedImageView) cviA.getChildAt(i);
                child.copyTextPaintPropertiesFrom(textPaint);
            }
        }
    }

}

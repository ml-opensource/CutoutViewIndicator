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

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.TextView;

import com.fuzz.indicator.cell.TypicalCutoutCell;
import com.fuzz.indicator.OffSetters;
import com.fuzz.indicator.proxy.IndicatorOffsetEvent;

/**
 * Simple indicator implementation for TextViews.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutTextCell extends TypicalCutoutCell<TextView> {

    public CutoutTextCell(@NonNull TextView child) {
        super(child);
    }

    @Override
    public void offsetContentBy(@NonNull IndicatorOffsetEvent event) {
        CharSequence text = itemView.getText();
        if (text instanceof Spanned) {
            Spannable spannable;
            if (text instanceof Spannable) {
                spannable = (Spannable) text;
            } else {
                // The SpannableString constructor will copy over pre-existing spans,
                // so no need to worry about losing state.
                spannable = new SpannableString(text);
            }
            OffSetters.offsetSpansBy(spannable, event.getFraction());
            itemView.setText(spannable);
        }
    }
}

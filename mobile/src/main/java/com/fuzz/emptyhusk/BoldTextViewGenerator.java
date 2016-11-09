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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.fuzz.indicator.style.MigratoryRange;
import com.fuzz.indicator.style.MigratoryStyleSpan;
import com.fuzz.indicator.text.TextViewGenerator;

import static android.graphics.Typeface.BOLD;

/**
 * Simple TextViewGenerator that styles text in a bold sort of way.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class BoldTextViewGenerator extends TextViewGenerator {
    @NonNull
    @Override
    protected Spannable getTextFor(@NonNull Context context, int position) {
        String[] introStrings = context.getResources().getStringArray(R.array.introductory_messages);

        SpannableString ssb = new SpannableString(introStrings[position]);
        MigratoryStyleSpan span = new MigratoryStyleSpan(BOLD);

        // Order matters when setting spans. The base color must be in place first

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorSecondary));
        ssb.setSpan(colorSpan, 0, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        // Only then should custom MigratorySpans be set.

        MigratoryRange<Integer> coverage = span.getCoverage(ssb);
        int start = coverage.getLower();
        int end = coverage.getUpper();
        ssb.setSpan(span, start, end, span.preferredFlags(0));

        MigratoryForegroundColorSpan boldColorSpan = new MigratoryForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent));
        ssb.setSpan(boldColorSpan, start, end, boldColorSpan.preferredFlags(0));

        return ssb;
    }

}

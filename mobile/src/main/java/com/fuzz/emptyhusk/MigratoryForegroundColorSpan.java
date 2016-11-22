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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import com.fuzz.indicator.style.MigratoryRange;
import com.fuzz.indicator.style.MigratorySpan;

/**
 * Migratory variant of {@link ForegroundColorSpan}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
class MigratoryForegroundColorSpan extends ForegroundColorSpan implements MigratorySpan, Parcelable {
    public MigratoryForegroundColorSpan(int color) {
        super(color);
    }

    public MigratoryForegroundColorSpan(Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    public MigratoryRange<Integer> getCoverage(@NonNull Spannable enclosingSequence) {
        int length = enclosingSequence.length();
        return MigratoryRange.from((int)(.2f * length), (int)(.8f * length));
    }

    @Override
    public int preferredFlags(int previousFlags) {
        return Spannable.SPAN_INCLUSIVE_INCLUSIVE;
    }

    public static final Creator<MigratoryForegroundColorSpan> CREATOR = new Creator<MigratoryForegroundColorSpan>() {
        @Override
        public MigratoryForegroundColorSpan createFromParcel(Parcel in) {
            return new MigratoryForegroundColorSpan(in);
        }

        @Override
        public MigratoryForegroundColorSpan[] newArray(int size) {
            return new MigratoryForegroundColorSpan[size];
        }
    };
}

package com.fuzz.emptyhusk;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import com.fuzz.indicator.text.MigratoryRange;
import com.fuzz.indicator.text.MigratorySpan;

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
    public MigratoryRange<Float> getCoverage() {
        return MigratoryRange.from(.2f, .8f);
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

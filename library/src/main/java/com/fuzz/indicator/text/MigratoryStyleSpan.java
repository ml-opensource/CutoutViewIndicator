package com.fuzz.indicator.text;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.style.StyleSpan;

/**
 * Warning: this class's ancestry is not final and may change in a future commit.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class MigratoryStyleSpan extends StyleSpan implements MigratorySpan {

    public static final Creator<MigratoryStyleSpan> CREATOR = new Creator<MigratoryStyleSpan>() {
        @Override
        public MigratoryStyleSpan createFromParcel(Parcel in) {
            return new MigratoryStyleSpan(in);
        }

        @Override
        public MigratoryStyleSpan[] newArray(int size) {
            return new MigratoryStyleSpan[size];
        }
    };

    public MigratoryStyleSpan(int style) {
        super(style);
    }

    private MigratoryStyleSpan(Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    public MigratoryRange<Float> getCoverage() {
        return MigratoryRange.from(.2f, .8f);
    }

    @Override
    public int preferredFlags(int previousFlags) {
        return previousFlags;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }
}

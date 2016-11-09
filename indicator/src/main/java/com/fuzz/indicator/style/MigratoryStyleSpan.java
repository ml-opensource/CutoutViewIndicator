package com.fuzz.indicator.style;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.Spannable;
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
    public MigratoryRange<Integer> getCoverage(@NonNull Spannable enclosingSequence) {
        int length = enclosingSequence.length();
        return MigratoryRange.from((int)(.2f * length), (int)(.8f * length));
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

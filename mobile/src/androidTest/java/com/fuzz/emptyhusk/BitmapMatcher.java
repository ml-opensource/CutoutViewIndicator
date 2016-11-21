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

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.ByteArrayOutputStream;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.util.Base64.DEFAULT;
import static android.util.Base64.encodeToString;

/**
 * Dedicated matcher to wrap functionality of {@link Bitmap#sameAs(Bitmap)}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 * @see #sameBitmapAs(Bitmap)
 */
public class BitmapMatcher extends BaseMatcher<Bitmap> {
    private final Bitmap expectedBacking;

    public BitmapMatcher(Bitmap expectedBacking) {
        this.expectedBacking = expectedBacking;
    }

    /**
     * Use this to instantiate and return a new {@link BitmapMatcher}.
     *
     * @param expectedBacking    the Bitmap all values will be matched against
     * @return a new {@link BitmapMatcher}
     */
    @NonNull
    static Matcher<? super Bitmap> sameBitmapAs(@NonNull final Bitmap expectedBacking) {
        return new BitmapMatcher(expectedBacking);
    }

    @Override
    public boolean matches(Object item) {
        return item instanceof Bitmap && expectedBacking.sameAs((Bitmap) item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("same bitmap as " + bitmapToBase64(expectedBacking));
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("was ");
        if (item instanceof Bitmap) {
            description.appendText(bitmapToBase64((Bitmap) item));
        } else {
            description.appendValue(item);
        }
    }

    /**
     * Converts a Bitmap to a Base64-encoded PNG representation of same.
     * <p>
     *     Credit to https://www.thepolyglotdeveloper.com/2015/06/from-bitmap-to-base64-and-back-with-native-android/
     *     for the implementation.
     * </p>
     *
     * @param bitmap    any {@link Bitmap}
     * @return a non-null string, suitable for displaying on failed tests
     */
    @NonNull
    private static String bitmapToBase64(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return String.format("data:image/png;base64,%s", encodeToString(byteArray, DEFAULT));
    }
}

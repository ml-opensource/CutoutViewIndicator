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
package com.fuzz.indicator.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Custom variant of {@link ClippedImageView} that supports an additional mask,
 * drawn before the background.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class TextClippedImageView extends ClippedImageView {

    /**
     * Since the paint {@link Paint#getXfermode()} used for clipping only clips out
     * content colored fully {@link Color#TRANSPARENT}, this is a suitable
     * background color for non-clipped areas that should appear transparent.
     */
    @ColorInt
    public static final int NOT_CLIPPED = Color.BLACK;

    /**
     * Set this to some sort of bounded region in order to restrict background
     * painting to that region. The foreground will then be masked by the background,
     * ultimately meaning that all drawn content will fit within this path.
     */
    @Nullable
    protected Path backgroundMaskPath;

    /**
     * A string whose bounds define {@link #backgroundMaskPath}.
     */
    @Nullable
    protected String backgroundMaskText;

    /**
     * A paint object for drawing barely-visible text.
     */
    @NonNull
    protected TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public TextClippedImageView(Context context) {
        super(context);
        init(context);
    }

    public TextClippedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextClippedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextClippedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(@NonNull Context context) {
        resetTextPaint();
    }

    /**
     * This sets the text to be
     * <ul>
     *     <li>size: 30</li>
     *     <li>Typeface: bold</li>
     *     <li>align: center</li>
     *     <li>color: black</li>
     * </ul>
     * @see #copyTextPaintPropertiesFrom(TextPaint)
     * @see #textPaint
     */
    public void resetTextPaint() {
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(NOT_CLIPPED);
    }

    /**
     * Call this to modify the TextPaint used for drawing the text clip path.
     *
     * @param sample    a source of TextPaint styling.
     * @see TextPaint#set(TextPaint)
     * @see #resetTextPaint()
     */
    public void copyTextPaintPropertiesFrom(@NonNull TextPaint sample) {
        textPaint.set(sample);
        setTextMaskPath(backgroundMaskText);
    }

    @NonNull
    @Override
    protected Bitmap clipImageInto(int viewWidth, int viewHeight) {

        intermediary = ensureBitmapIsUsable(intermediary, viewWidth, viewHeight);
        intermediary.eraseColor(Color.TRANSPARENT);

        Canvas canvas = new Canvas(intermediary);

        paint.setXfermode(null);
        textPaint.setXfermode(null);

        if (backgroundMaskPath != null) {
            canvas.drawPath(backgroundMaskPath, textPaint);
            paint.setXfermode(overlayTransferMode);
        } else if (backgroundMaskText != null) {
            canvas.drawText(backgroundMaskText, getWidth() / 2, getHeight() / 2, textPaint);
            paint.setXfermode(overlayTransferMode);
        }

        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

        paint.setXfermode(overlayTransferMode);
        int count = canvas.save();
        canvas.concat(getImageMatrix());
        canvas.drawBitmap(primaryBitmap, 0, 0, paint);
        canvas.restoreToCount(count);


        return intermediary;
    }

    /**
     * The passed string will be used to mask the background (and therefore the foreground as well).
     * <br/><br/>
     * Pass in null to disable this feature.
     * @param mask    the text with which this view will be masked.
     */
    public void setTextMaskPath(@Nullable String mask) {
        backgroundMaskText = mask;
        if (mask != null && !isInEditMode()) {
            if (backgroundMaskPath == null) {
                backgroundMaskPath = new Path();
            }
            textPaint.getTextPath(mask, 0, mask.length(), getWidth() / 2, getHeight() / 2, backgroundMaskPath);
        } else {
            backgroundMaskPath = null;
        }
    }

    @Override
    public void setBackground(Drawable background) {
        if (background != null && !(background instanceof UnDrawable)) {
            UnDrawable unDrawable = new UnDrawable();
            unDrawable.setInternalDrawable(background);
            background = unDrawable;
        }
        super.setBackground(background);
    }

    @Override
    public Drawable getBackingBackground() {
        Drawable background = super.getBackingBackground();
        if (background instanceof UnDrawable) {
            background = background.getCurrent();
        }
        return background;
    }
}

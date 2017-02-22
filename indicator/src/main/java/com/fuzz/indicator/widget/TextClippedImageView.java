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
package com.fuzz.indicator.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.fuzz.indicator.R;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.getMode;
import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;

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

    @NonNull
    protected final RectF pathBounds = new RectF();

    /**
     * The number of custom paths in use. For now, this is 0 by default, 1 if either
     * {@link #backgroundMaskPath} or {@link #backgroundMaskText} is set.
     */
    protected int customPathCount;

    /**
     * Flag indicating whether the current {@link #backgroundMaskPath} has been translated
     * into the center of this view.
     *
     * @see #clipImageInto(int, int)
     * @see #extensibleOnMeasure(int, int)
     */
    protected boolean hasOffset;

    public TextClippedImageView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public TextClippedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public TextClippedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextClippedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        resetTextPaint();

        String textMask = null;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextClippedImageView, defStyleAttr, defStyleRes);

            textMask = a.getString(R.styleable.TextClippedImageView_clip_text_mask);

            a.recycle();
        }
        setTextMaskPath(textMask);
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

    @Override
    protected void extensibleOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = getMode(widthMeasureSpec);
        int heightMode = getMode(heightMeasureSpec);

        int currentWidth = getSize(widthMeasureSpec);
        int currentHeight = getSize(heightMeasureSpec);

        if (backgroundMaskPath != null) {
            hasOffset = false;
            // Unconditionally compute path bounds - we'll want them in ::clipImageInto
            backgroundMaskPath.computeBounds(pathBounds, false); // second param is irrelevant

            if (customPathCount > 0) {
                // Path size can take precedence
                float proposedWidth = pathBounds.width() - pathBounds.left;
                float proposedHeight = pathBounds.height() - pathBounds.top;
                if (isReasonableProposal(heightMode, currentHeight, proposedHeight)) {
                    heightMeasureSpec = makeMeasureSpec((int) proposedHeight, heightMode);
                }
                if (isReasonableProposal(widthMode, currentWidth, proposedWidth)) {
                    widthMeasureSpec = makeMeasureSpec((int) proposedWidth, widthMode);
                }
            }
        }
        super.extensibleOnMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Sanity check to be used right before modifying width or height.
     *
     * @param specMode        a mode returned by {@link MeasureSpec#getMode}
     * @param specSize        a size returned by {@link MeasureSpec#getMode}
     * @param proposedSize    a new value to replace {@code specSize}
     * @return true if {@code proposedSize} could be used, false otherwise.
     * @see #extensibleOnMeasure(int, int)
     */
    private boolean isReasonableProposal(int specMode, int specSize, float proposedSize) {
        return proposedSize >= 1 &&
                (
                    specMode != AT_MOST
                            ||
                    proposedSize < specSize
                );
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
            if (!hasOffset && (pathBounds.left != 0 || pathBounds.top != 0)) {
                hasOffset = true;
                backgroundMaskPath.offset(-pathBounds.left, -pathBounds.top);
            }
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
            // Create the path using a 0,0 origin (top-left) - it'll be centred later
            textPaint.getTextPath(mask, 0, mask.length(), 0, 0, backgroundMaskPath);
            customPathCount = 1;
        } else {
            backgroundMaskPath = null;
        }
    }

    public boolean hasTextMask() {
        return backgroundMaskText != null;
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

/*
 * Copyright 2016 fanrunqi
 *
 * Modifications Copyright 2016 Philip Cohn-Cort (Fuzz)
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
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Custom ImageView subclass which ensures that the primary drawable is
 * {@link PorterDuff.Mode#SRC_IN only drawn on top of opaque pixels}.
 * This works by intercepting all calls that may set the image drawable.
 *
 * <p>
 *     Then, when the superclass calls {@link android.view.View#onDraw(Canvas) onDraw},
 *     the provided canvas will already have a visible {@link #getBackingBackground() background}.
 * </p>
 *
 * @author fanrunqi
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ClippedImageView extends ImageView {

    protected Bitmap backgroundBitmap;
    protected Bitmap primaryBitmap;

    @NonNull
    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected PorterDuffXfermode overlayTransferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    /**
     * A cached bitmap used in {@link #onDraw(Canvas)}
     * @see #ensureBitmapIsUsable(Bitmap, int, int)
     */
    protected Bitmap intermediary;

    /**
     * A reference to the most recent value to be passed into
     * {@link #setBackgroundResource(int)}.
     */
    protected int backgroundResourceId;

    /**
     * A reference to the most recent value to be passed into
     * {@link #setImageResource(int)}.
     */
    protected int primaryResourceId;

    public ClippedImageView(Context context) {
        super(context);
    }

    public ClippedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClippedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClippedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int prevHeight = getMeasuredHeight();
        int prevWidth = getMeasuredWidth();

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int postHeight = getMeasuredHeight();
        int postWidth = getMeasuredWidth();

        if (prevHeight != postHeight || prevWidth != postWidth) {
            // Dimensions have changed.
            if (getBackingBackground() != null ) {
                backgroundBitmap = extractBitmapFrom(getBackingBackground(), postWidth, postHeight);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (primaryBitmap != null && backgroundBitmap != null) {

            // Ensure background and foreground are scaled to the desired size
            int viewWidth = getMeasuredWidth();
            int viewHeight = getMeasuredHeight();

            canvas.drawBitmap(clipImageInto(viewWidth, viewHeight), 0, 0, null);
        }
    }

    @NonNull
    protected Bitmap clipImageInto(int viewWidth, int viewHeight) {

        intermediary = ensureBitmapIsUsable(intermediary, viewWidth, viewHeight);
        intermediary.eraseColor(Color.TRANSPARENT);

        Canvas canvas = new Canvas(intermediary);

        paint.setXfermode(null);
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

        paint.setXfermode(overlayTransferMode);
        int count = canvas.save();
        canvas.concat(getImageMatrix());
        canvas.drawBitmap(primaryBitmap, 0, 0, paint);
        canvas.restoreToCount(count);


        return intermediary;
    }

    /**
     * Utility method for ensuring that a provided bitmap is of the specified
     * dimensions.
     * @param target        the bitmap in question
     * @param viewWidth     how wide the target bitmap should be
     * @param viewHeight    how tall the target bitmap should be
     * @return the original bitmap if it already fit those dimensions,
     * a subset of that if it was larger, or else a new instance of that
     * class with the specified size.
     */
    @NonNull
    protected static Bitmap ensureBitmapIsUsable(@Nullable Bitmap target, int viewWidth, int viewHeight) {
        if (target != null) {
            if (target.getWidth() > viewWidth && target.getHeight() > viewHeight) {
                // Recreate target from subset of original Bitmap
                target = Bitmap.createBitmap(target, 0, 0, viewWidth, viewHeight);
            } else if (target.getWidth() != viewWidth || target.getHeight() != viewHeight) {
                // The bitmap is too small in at least one dimension. It is of no further use.
                target.recycle();
                target = null;
            }
        }

        if (target == null) {
            target = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        }

        return target;
    }

    /**
     * Sets a field for use during the {@link #onDraw(Canvas)} step. Other
     * classes are recommended to use standard ImageView methods
     * {@link #setImageBitmap(Bitmap)}, {@link #setImageDrawable(Drawable)}, etc.
     * instead of calling this directly.
     *
     * @param bitmap    this is the content that will be clipped
     * @see #setBackgroundBitmapFrom(Drawable)
     */
    protected void setPrimaryBitmap(@Nullable Bitmap bitmap) {
        primaryBitmap = bitmap;
    }

    /**
     * Overloaded form of {@link #setPrimaryBitmap(Bitmap)}
     *
     * @param drawable    a drawable, from which {@link #extractBitmapFrom(Drawable)}
     *                    will pull a {@link Bitmap}
     */
    protected void setPrimaryBitmapFrom(Drawable drawable) {
        setPrimaryBitmap(extractBitmapFrom(drawable));
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        setPrimaryBitmap(bitmap);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setPrimaryBitmapFrom(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (primaryResourceId == 0 || primaryResourceId != resId) {
            primaryResourceId = resId;
            setPrimaryBitmapFrom(getDrawable());
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setPrimaryBitmapFrom(getDrawable());
    }

    /**
     * Sets a field for use during the {@link #onDraw(Canvas)} step. Other
     * classes are recommended to use standard ImageView methods
     * {@link #setBackground(Drawable)}, {@link #setBackgroundDrawable(Drawable)}, etc.
     * instead of calling this directly.
     * <p>
     *     Unlike {@link #setPrimaryBitmapFrom(Drawable)} and {@link #setPrimaryBitmap(Bitmap)},
     *     there is no corresponding way to set a Bitmap directly as
     *     the background. Note that this limitation derives from the non-overridden
     *     background-drawing logic in {@link #draw(Canvas)}.
     * </p>
     * Long-term the goal of this class is to clip based on the actual
     * drawn position of the background, preserving all features in the
     * ImageView superclass that can be preserved and replicating those
     * that cannot.
     *
     * @param background    this is the background which clips the content.
     */
    protected void setBackgroundBitmapFrom(@Nullable Drawable background) {
        backgroundBitmap = extractBitmapFrom(background);
    }

    /**
     * Some subclasses may need to inject their own background drawables into
     * the clipping process. This offers them a chance to do that without affecting
     * any other drawing code here.
     * @return the view's background, as it should be perceived by this class.
     */
    public Drawable getBackingBackground() {
        return super.getBackground();
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        setBackgroundBitmapFrom(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        setBackgroundBitmapFrom(getBackingBackground());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        setBackgroundBitmapFrom(background);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (backgroundResourceId == 0 || backgroundResourceId != resid) {
            backgroundResourceId = resid;
            setBackgroundBitmapFrom(getBackingBackground());
        }
    }

    @Override
    public void setBackgroundTintList(ColorStateList tint) {
        super.setBackgroundTintList(tint);
        setBackgroundBitmapFrom(getBackingBackground());
    }

    @Override
    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        super.setBackgroundTintMode(tintMode);
        setBackgroundBitmapFrom(getBackingBackground());
    }

    private Bitmap extractBitmapFrom(@Nullable Drawable drawable) {
        Bitmap retVal;
        if (drawable == null) {
            retVal = null;
        } else if (drawable instanceof BitmapDrawable) {
            retVal = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            if (width <= 0) {
                width = Math.max(getMeasuredWidth(), 1);
            }
            int height = drawable.getIntrinsicHeight();
            if (height <= 0) {
                height = Math.max(getMeasuredHeight(), 1);
            }
            retVal = extractBitmapFrom(drawable, width, height);
        }
        return retVal;
    }

    @Nullable
    private Bitmap extractBitmapFrom(@NonNull Drawable drawable, int width, int height) {
        Bitmap retVal;
        try {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            retVal = bitmap;
        } catch (OutOfMemoryError oom) {
            retVal = null;
        }
        return retVal;
    }

}

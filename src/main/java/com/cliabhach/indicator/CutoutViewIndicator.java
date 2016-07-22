package com.cliabhach.indicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * There's a nice monospace line drawing in the javadoc for {@link #showOffsetIndicator(int, float)} that basically sums up
 * this ViewGroup.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutViewIndicator extends LinearLayout {

    private static final String TAG = CutoutViewIndicator.class.getSimpleName();

    protected int internalSpacing;
    /**
     * This is the height or width bounding all child views when {@link #HORIZONTAL} or {@link #VERTICAL}, respectively.
     * <p/>
     * Typically equal to the height/width of the {@link CutoutViewIndicator}, minus padding.
     */
    protected int perpendicularLength;

    /**
     * This is the id of the drawable currently acting as indicator. If 0, no indicator will be shown.
     */
    @DrawableRes
    protected int indicatorDrawableId;

    @DrawableRes
    protected int cellBackgroundId;

    /**
     * This is the resolved dimension (in pixels)
     */
    protected int cellLength;

    protected ViewPager viewPager;

    /**
     * {@link com.eccyan.widget.SpinningViewPager} reports positions as one greater than other ViewPagers. When this variable
     * is true, CutoutViewIndicator will correct for the discrepancy.
     */
    protected boolean usePositiveOffset;

    protected ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (indicatorDrawableId != 0) {
                position = fixPosition(position);

                // Cover the provided position...
                showOffsetIndicator(position, positionOffset);
                if (positionOffset > 0) {
                    // ...and cover the next one too
                    int next = position + 1;
                    if (next >= getChildCount()) {
                        next = 0;
                    }
                    showOffsetIndicator(next, positionOffset - 1);
                }
            }
        }

        /**
         * @see CutoutViewIndicator#usePositiveOffset
         * @param proposed the value returned by {@link CutoutViewIndicator#viewPager}
         * @return the corrected value.
         */
        public int fixPosition(int proposed) {
            if (usePositiveOffset) {
                // ViewPagers like SpinningViewPager are always off by one
                proposed--;
                // Ensure that it's positive
                if (proposed < 0) {
                    proposed += getChildCount();
                }
            }
            return proposed;
        }

        @Override
        public void onPageSelected(int position) {
            showOffsetIndicator(fixPosition(position), 0);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                // verify that all non-current views are free from indicators
                ensureOnlyOneItemIsSelected();
            }
        }
    };
    protected DataSetObserver dataSetObserver = new DataSetObserver() {
        /**
         * This method is called when the entire data set has changed,
         * most likely through a call to {@link Cursor#requery()} on a {@link Cursor}.
         *
         * In our case, this is most commonly triggered by {@link android.support.v4.view.PagerAdapter#notifyDataSetChanged()}
         */
        @Override
        public void onChanged() {
            super.onChanged();
            removeAllViews();

            int pageCount = viewPager.getAdapter().getCount();
            for (int i = 0; i < pageCount; i++) {
                addProgressChild(i);
            }

            Log.i(TAG, "onChanged: count=" + pageCount + ", child count=" + getChildCount());

            // Seriously. They called this the 'CurrentItem'. Can you believe it?
            int currentPageNumber = viewPager.getCurrentItem();
            // Anyway, we need to ensure that item is selected.
            pageChangeListener.onPageSelected(currentPageNumber);
        }

        /**
         * This method is called when the entire data becomes invalid,
         * most likely through a call to {@link Cursor#deactivate()} or {@link Cursor#close()} on a
         * {@link Cursor}.
         */
        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    public CutoutViewIndicator(Context context) {
        this(context, null);
    }

    public CutoutViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutoutViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CutoutViewIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CutoutViewIndicator);
            indicatorDrawableId = a.getResourceId(R.styleable.CutoutViewIndicator_rcv_drawable, 0);
            internalSpacing = a.getDimensionPixelOffset(R.styleable.CutoutViewIndicator_rcv_internal_margin, 0);

            // The superclass will have resolved orientation by now.
            if (getOrientation() == HORIZONTAL) {
                perpendicularLength = a.getDimensionPixelSize(R.styleable.CutoutViewIndicator_rcv_height, 0);
                cellLength = a.getDimensionPixelOffset(R.styleable.CutoutViewIndicator_rcv_width, 0);
            } else {
                perpendicularLength = a.getDimensionPixelSize(R.styleable.CutoutViewIndicator_rcv_width, 0);
                cellLength = a.getDimensionPixelOffset(R.styleable.CutoutViewIndicator_rcv_height, 0);
            }

            cellBackgroundId = a.getResourceId(R.styleable.CutoutViewIndicator_rcv_drawable_unselected, 0);
            a.recycle();
        }
    }

    /**
     * @param position used as 'index' parameter to {@link #addView(View, int)}
     */
    protected void addProgressChild(int position) {
        LinearLayout.LayoutParams lp;
        final int left, top;
        if (getOrientation() == HORIZONTAL) {
            lp = new LayoutParams(cellLength, perpendicularLength);
            left = (position == 0) ? 0 : internalSpacing;
            top = 0;
        } else {
            lp = new LayoutParams(perpendicularLength, cellLength);
            left = 0;
            top = (position == 0) ? 0 : internalSpacing;
        }
        lp.setMargins(left, top, 0, 0);
        lp.gravity = Gravity.CENTER;

        LayeredView child = new LayeredView(getContext()); // inflater.inflate(R.layout.cell_layered, this, false);
        child.setScaleType(ImageView.ScaleType.MATRIX);
        child.setLayoutParams(lp);
        child.setBackgroundResource(cellBackgroundId);
        child.setImageResource(indicatorDrawableId);
        addView(child, position);
    }

    /**
     * The caller is responsible for ensuring the parameters are within bounds.
     * <p>
     * In the below line drawing of a horizontal CutoutViewIndicator, we have 4 cells (a.k.a. child views).
     * <br/>
     * ▓ is the indicator and ░ is the background color of each cell.
     * <br/>
     * Note that the percentageOffset is a percentage of each <i>cell</i> that is drawn.
     * This class does not require that each child view is the same length, but it probably
     * looks better that way.
     * </p>
     * <p>
     * <pre>
     *      position=0         position=1         position=2         position=3
     *     ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
     *     │░░░░░░░░▓▓▓▓▓▓│   │▓▓▓▓▓▓▓▓▓░░░░░│   │░░░░░░░░░░░░░░│   │░░░░░░░░░░░░░░│
     *     │░░░░░░░░▓▓▓▓▓▓│   │▓▓▓▓▓▓▓▓▓░░░░░│   │░░░░░░░░░░░░░░│   │░░░░░░░░░░░░░░│
     *     └──────────────┘   └──────────────┘   └──────────────┘   └──────────────┘
     *      offset=8/14        offset=-8/14       offset=-22/14      offset=-36/14
     * </pre>
     * <p>
     * </p>
     *
     * @param position         corresponds to the view where an indicator should be shown. Must be less than {@link #getChildCount()}
     * @param percentageOffset how much of the indicator to draw (given as a value between -1 and 1). If out of range, no
     *                         indicator will be drawn
     */
    protected void showOffsetIndicator(int position, float percentageOffset) {
        View child = getChildAt(position);
        if (Math.abs(percentageOffset) < 1) {
            // We have something to draw
            if (child instanceof LayeredView) {
                LayeredView layeredView = (LayeredView) child;
                layeredView.offsetImageBy(getOrientation(), percentageOffset);
            }
        }
    }

    public void enablePositiveOffset(boolean usePositiveOffset) {
        this.usePositiveOffset = usePositiveOffset;
    }

    public void setCellBackgroundId(@DrawableRes int cellBackgroundId) {
        this.cellBackgroundId = cellBackgroundId;
    }

    public void setIndicatorDrawableId(@DrawableRes int indicatorDrawableId) {
        this.indicatorDrawableId = indicatorDrawableId;
    }

    /**
     * This is the width of a cell when {@link #getOrientation() horizontal},
     * but the height of a cell when {@link #getOrientation() vertical}.
     * <p>
     *     All cells are the same proportions by default.
     * </p>
     *
     * @param cellLength any positive number of pixels
     * @see #setPerpendicularLength(int)
     */
    public void setCellLength(int cellLength) {
        this.cellLength = cellLength;
        requestLayout();
    }

    /**
     * This is the space between cells. It is not added as padding to either end of
     * the {@code CutoutViewIndicator}. This view does not draw anything in these
     * spaces (except the background, if present).
     *
     * @param internalSpacing any positive number of pixels
     */
    public void setInternalSpacing(int internalSpacing) {
        this.internalSpacing = internalSpacing;
        requestLayout();
    }

    /**
     * This is the height of a cell when {@link #getOrientation() horizontal},
     * but the width of a cell when {@link #getOrientation() vertical}.
     * <p>
     *     All cells are the same proportions by default.
     * </p>
     *
     * @param perpendicularLength any positive number of pixels
     * @see #setCellLength(int)
     */
    public void setPerpendicularLength(int perpendicularLength) {
        this.perpendicularLength = perpendicularLength;
        requestLayout();
    }

    /**
     * @see #setCellLength(int)
     *
     * @return current length of one cell in pixels
     */
    public int getCellLength() {
        return cellLength;
    }

    /**
     * @see #setInternalSpacing(int)
     *
     * @return current space between cells in pixels
     */
    public int getInternalSpacing() {
        return internalSpacing;
    }

    /**
     * @see #setPerpendicularLength(int)
     *
     * @return current perpendicular length of one cell in pixels
     */
    public int getPerpendicularLength() {
        return perpendicularLength;
    }

    /**
     * Call this after setting the other custom parameters ({@link #setIndicatorDrawableId(int)},
     * {@link #setCellLength(int)}, {@link #setInternalSpacing(int)}, {@link #setPerpendicularLength(int)},
     * {@link #setCellBackgroundId(int)})
     * to avoid redrawing or extra layout stuff.
     *
     * @param newPager the new ViewPager that this'll sync with. Pass null to disable.
     */
    public void setViewPager(@Nullable ViewPager newPager) {
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.removeOnPageChangeListener(pageChangeListener);
            viewPager.getAdapter().unregisterDataSetObserver(dataSetObserver);
        }
        viewPager = newPager;
        if (newPager != null && newPager.getAdapter() != null) {
            newPager.removeOnPageChangeListener(pageChangeListener);
            newPager.addOnPageChangeListener(pageChangeListener);
            newPager.getAdapter().registerDataSetObserver(dataSetObserver);
            dataSetObserver.onChanged();
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    ensureOnlyOneItemIsSelected();
                }
            });
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ensureOnlyOneItemIsSelected();
    }

    /**
     * Call this to hide the indicator on all views except for the one corresponding to the currently displaying item.
     */
    public void ensureOnlyOneItemIsSelected() {
        if (viewPager != null) {
            int current = viewPager.getCurrentItem();
            for (int i = 0; i < getChildCount(); i++) {
                if (i != current) {
                    View child = getChildAt(i);
                    if (child instanceof LayeredView) {
                        // offset by 1 puts it just off-view (i.e. hiding it)
                        ((LayeredView) child).offsetImageBy(getOrientation(), 1);
                    }
                }
            }
        }
    }
}

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
package com.fuzz.indicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuzz.indicator.cell.CutoutCell;
import com.fuzz.indicator.proxy.IndicatorOffsetEvent;
import com.fuzz.indicator.proxy.ProxyReference;
import com.fuzz.indicator.proxy.StateProxy;
import com.fuzz.indicator.proxy.UnavailableProxy;
import com.fuzz.indicator.proxy.ViewProvidingStateProxy;
import com.fuzz.indicator.cell.CutoutTextCell;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A RecyclerView-inspired ViewGroup for showing an indicator traversing multiple child Views ('cells').
 * <p>
 * There's a nice monospace line drawing in the javadoc for {@link #showOffsetIndicator(int, float)} that basically sums up
 * its appearance when operating under an {@link ImageCellGenerator}.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class CutoutViewIndicator extends LinearLayout implements ProxyReference {

    private static final String TAG = CutoutViewIndicator.class.getSimpleName();

    /**
     * Specialised implementation of {@link UnavailableProxy} for use with
     * {@linkplain #isInEditMode() the preview tools}.
     */
    protected static final UnavailableProxy EDIT_MODE_PROXY = new UnavailableProxy() {
        @Override
        public IndicatorOffsetEvent resendPositionInfo(ProxyReference cvi, float pos) {
            return IndicatorOffsetEvent.from(cvi, pos);
        }
    };

    /**
     * This holds onto the views that may be attached to this ViewGroup. It's initialised
     * with space for 5 values because practical experience says that space for 10 would
     * be excessive.
     * <p>
     *     This is kinda micro-optimising since it can expand automatically later.
     * </p>
     */
    @NonNull
    protected SparseArray<CutoutCell> cells = new SparseArray<>(5);

    /**
     * Default configuration used as a base for {@link #generateDefaultLayoutParams()}
     * and {@link #bindChild(int, View)}
     */
    @NonNull
    protected CutoutViewLayoutParams defaultChildParams;

    /**
     * A wrapper for the current state - see {@link StateProxy} javadoc for details.
     *
     * @see #setStateProxy(StateProxy)
     * @see #setViewPager(ViewPager)
     */
    @NonNull
    protected StateProxy stateProxy = new UnavailableProxy();

    /**
     * @see #cascadeParamChanges(boolean)
     */
    protected boolean cascadeChanges;
    /**
     * {@code com.eccyan.widget.SpinningViewPager} reports positions as one greater than other ViewPagers. When this variable
     * is true, CutoutViewIndicator will correct for the discrepancy.
     */
    protected boolean usePositiveOffset;

    /**
     * This value is strictly for showing a good tools-style preview of this view. If
     * {@link #isInEditMode()} is false, this value should never be used.
     */
    private int editModePageCount;

    /**
     * This object is responsible for creating new cells
     * @see #showOffsetIndicator(int, float)
     */
    @NonNull
    protected CutoutCellGenerator generator;

    /**
     * This value is set by {@link #showOffsetIndicator(int, IndicatorOffsetEvent)} into
     * {@link IndicatorOffsetEvent#setOffSetHints(int)}.
     */
    @OffSetHint
    protected int offsetHints = OffSetHint.IMAGE_TRANSLATE;

    @NonNull
    protected LayoutLogger logger;

    /**
     * An observer which gets notified whenever the data backing {@link #stateProxy} changes.
     *
     * By default, this is an implementation of {@link IntegratedDataSetObserver}.
     */
    protected DataSetObserver dataSetObserver = new IntegratedDataSetObserver();

    public CutoutViewIndicator(Context context) {
        this(context, null);
    }

    public CutoutViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutoutViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        logger = LayoutLogger.getPreferred(isInEditMode());
        generator = new ImageCellGenerator();
        defaultChildParams = new CutoutViewLayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CutoutViewIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        logger = LayoutLogger.getPreferred(isInEditMode());
        generator = new ImageCellGenerator();
        defaultChildParams = new CutoutViewLayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("ResourceType")
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CutoutViewIndicator);
            setIndicatorDrawableId(a.getResourceId(R.styleable.CutoutViewIndicator_rcv_drawable, 0));
            setInternalSpacing(a.getDimensionPixelOffset(R.styleable.CutoutViewIndicator_rcv_internal_margin, 0));

            // The superclass will have resolved orientation by now.
            if (getOrientation() == HORIZONTAL) {
                setPerpendicularLength(a.getDimensionPixelSize(R.styleable.CutoutViewIndicator_rcv_height, WRAP_CONTENT));
                setCellLength(a.getDimensionPixelOffset(R.styleable.CutoutViewIndicator_rcv_width, WRAP_CONTENT));
            } else {
                setPerpendicularLength(a.getDimensionPixelSize(R.styleable.CutoutViewIndicator_rcv_width, WRAP_CONTENT));
                setCellLength(a.getDimensionPixelOffset(R.styleable.CutoutViewIndicator_rcv_height, WRAP_CONTENT));
            }

            if (isInEditMode()) {
                editModePageCount = a.getInteger(R.styleable.CutoutViewIndicator_rcv_tools_indicator_count, 2);
            }

            String generatorName = a.getString(R.styleable.CutoutViewIndicator_rcv_generator_class_name);
            if (generatorName != null) {
                CCGFactory.constructGeneratorFrom(context, attrs, defStyleAttr, generatorName, new ConstructorCallback() {
                    @Override
                    public void onGenerated(@NonNull CutoutCellGenerator generated) {
                        CutoutViewIndicator.this.generator = generated;
                    }

                    @Override
                    public void onFailed(@NonNull String message) {
                        if (!message.isEmpty()) {
                            Log.e(TAG, message);
                            if (isInEditMode()) {
                                String tag = "resources.invalid";
                                logger.logToLayoutLib(tag, message);
                            }
                        }
                    }
                });

            }

            setCellBackgroundId(a.getResourceId(R.styleable.CutoutViewIndicator_rcv_drawable_unselected, 0));
            a.recycle();
        }
        if (isInEditMode()) {
            setStateProxy(EDIT_MODE_PROXY);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Most child views this ViewGroup will see are created by
     * {@link #createCellFor(int)} and added to this by the
     * {@link #dataSetObserver}. These objects all have non-null
     * CutoutCells associated with them.
     * <p>
     *     Note that all other addView methods will ultimately call into
     *     this one. Any child view that has not already been associated
     *     with a CutoutCell via {@link #cells} will be forcibly
     *     associated with one here (assuming a suitable implementation
     *     can be obtained).
     * </p>
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        // A CutoutCell's Views are always added at exact indices. If there is no value
        // in this.cells for the given index, we need to set that right away.
        int realIndex = indexOfChild(child);

        CutoutViewLayoutParams lp = (CutoutViewLayoutParams) child.getLayoutParams();

        if (cells.get(realIndex) == null) {
            // This was NOT added via dataSetObserver! Flip a flag to ensure future recognition.
            lp.preservedDuringRebuild = true;
            CutoutCell cell = lp.getCutoutCell();
            if (cell == null) {
                if (child instanceof ImageView) {
                    cell = new CutoutImageCell((ImageView) child);
                    warnAboutMisuseOf((ImageView) child, cell);
                } else if (child instanceof TextView) {
                    cell = new CutoutTextCell((TextView) child);
                } else if (child instanceof CutoutCell) {
                    cell = (CutoutCell) child;
                }
                lp.setCutoutCell(cell);
            }
            cells.put(realIndex, cell);
        }
    }

    /**
     * Call this when a View has been added to this (via {@link #addView}), to display any
     * warnings or errors due to misuse of the {@link CutoutCell}.....contract.
     *
     * @param child    the child view in question
     * @param cell     that child's CutoutCell (usually accessible through its LayoutParams)
     */
    public void warnAboutMisuseOf(ImageView child, CutoutCell cell) {
        if (cell.getClass() == CutoutImageCell.class
                && child.getScaleType() != ImageView.ScaleType.MATRIX) {
            String tag = "resources.insufficient";
            String message = "Only child ImageViews with a MATRIX scaleType are respected by" +
                    " your choice of CutoutCell. Offset effects will not appear properly on" +
                    " ScaleType " + child.getScaleType() + ".";
            logger.logToLayoutLib(tag, message);
        }
    }

    /**
     * Binds the special properties of {@link CutoutViewLayoutParams} to the
     * child in question. The child was previously added to this via some {@link #addView}
     * overload, so it can be safely assumed that its layout params fulfill all requirements
     * of {@link #checkLayoutParams(ViewGroup.LayoutParams)}.
     *
     * @param position    what position this child possesses in terms of {@link #getChildAt(int)}
     * @param child       a view that has just been added to this CutoutViewIndicator
     */
    protected void bindChild(int position, View child) {
        CutoutViewLayoutParams lp = (CutoutViewLayoutParams) child.getLayoutParams();
        lp.position = position;

        int cellLength;
        int perpendicularLength;
        int internalSpacing;
        if (cascadeChanges) {
            cellLength = getCellLength();
            perpendicularLength = getPerpendicularLength();
            internalSpacing = getInternalSpacing();
        } else {
            cellLength = lp.cellLength;
            perpendicularLength = lp.perpendicularLength;
            internalSpacing = lp.internalSpacing;
        }

        final int left, top;
        if (getOrientation() == HORIZONTAL) {
            lp.width = cellLength;
            lp.height = perpendicularLength;
            left = (position == 0) ? 0 : internalSpacing;
            top = 0;
        } else {
            lp.width = perpendicularLength;
            lp.height = cellLength;
            left = 0;
            top = (position == 0) ? 0 : internalSpacing;
        }
        lp.setMargins(left, top, 0, 0);
        lp.gravity = Gravity.CENTER;

        if (isInEditMode() && lp.cellBackgroundId <= 0 && lp.indicatorDrawableId <= 0) {
            String tag = "resources.unusual";
            String message = "Note that CutoutViewIndicator's generated views will not appear" +
                    " unless you provide it with a positive drawable id" +
                    " (i.e. for the attribute rcv_drawable).";
            logger.logToLayoutLib(tag, message);
        }

        View originator;
        if (stateProxy instanceof ViewProvidingStateProxy) {
            originator = ((ViewProvidingStateProxy) stateProxy).getOriginalViewFor(position);
        } else {
            originator = null;
        }
        generator.onBindChild(child, lp, originator);
    }

    /**
     * Unbinding counterpart to {@link #bindChild(int, View)}.
     *
     * @param cell    a {@link CutoutCell} originally returned
     *                by {@link #createCellFor(int)}.
     */
    public void unbindChild(CutoutCell cell) {
        ViewGroup.LayoutParams lp = cell.getItemView().getLayoutParams();
        if (checkLayoutParams(lp)) {
            ((CutoutViewLayoutParams) lp).setCutoutCell(null);
            ((CutoutViewLayoutParams) lp).position = -1;
        }
    }

    /**
     * Set a new generator that will be called upon when a new cell
     * is needed. More or less the same as RecyclerView's Adapter.
     * <p>
     *     Defaults to being a {@link ImageCellGenerator}, which
     *     should be good enough for most purposes.
     * </p>
     * This method ends by calling {@link #rebuildChildViews()}.
     *
     * @param generator the new generator. May not be null.
     * @see #showOffsetIndicator(int, float)
     */
    public void setGenerator(@NonNull CutoutCellGenerator generator) {
        this.generator = generator;
        rebuildChildViews();
    }

    /**
     * Utility method for invalidating the {@link #generator} (so to speak).
     * <p>
     *     It'll ensure that the child views match both what the current
     *     {@link CutoutCellGenerator} creates AND the params defined by
     *     {@link #defaultChildParams}.
     * </p>
     * Views which have been marked for
     * {@link CutoutViewLayoutParams#preservedDuringRebuild preservation} will
     * not be replaced.
     */
    public void rebuildChildViews() {
        SparseArray<CutoutCell> preserved = cells.clone();
        cells.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (((CutoutViewLayoutParams)child.getLayoutParams()).preservedDuringRebuild) {
                cells.put(i, preserved.get(i));
            }
        }
        preserved.clear();
        removeAllViews();
        dataSetObserver.onChanged();
    }

    /**
     * Returns a reference to the generator used to create and bind
     * cells. Default value is an instance of {@link ImageCellGenerator}.
     *
     * @return the generator currently in use
     * @see #setGenerator(CutoutCellGenerator)
     */
    @NonNull
    public CutoutCellGenerator getGenerator() {
        return generator;
    }

    /**
     * Asks the {@link #generator} to create a new cell.
     *
     * @param position used as 'index' parameter to {@link #addView(View, int)}
     * @return a new cell, appropriate for that position
     * @see CutoutCellGenerator#createCellFor(ViewGroup, int)
     * @see #showOffsetIndicator(int, float)
     */
    @NonNull
    protected CutoutCell createCellFor(int position) {
        CutoutCell cell = generator.createCellFor(this, position);
        CutoutViewLayoutParams lp = (CutoutViewLayoutParams) cell.getItemView().getLayoutParams();
        lp.setCutoutCell(cell);
        return cell;
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
     *             width of {@link CutoutViewLayoutParams#indicatorDrawableId the indicator}
     *              ┏━━━━━━━━━┻━━━━━━━━━┓
     *      position=0         position=1         position=2         position=3
     *     ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
     *     │░░░░░░░░▓▓▓▓▓▓│   │▓▓▓▓▓▓▓▓▓░░░░░│   │░░░░░░░░░░░░░░│   │░░░░░░░░░░░░░░│
     *     │░░░░░░░░▓▓▓▓▓▓│   │▓▓▓▓▓▓▓▓▓░░░░░│   │░░░░░░░░░░░░░░│   │░░░░░░░░░░░░░░│
     *     └──────────────┘   └──────────────┘   └──────────────┘   └──────────────┘
     *      offset=8/14        offset=-8/14       offset=-22/14      offset=-36/14
     *              ┗━━━━━{@link #getCurrentIndicatorPosition() start}
     * </pre>
     * <p>
     *     Additional information that must be passed to the CutoutCell but <i>can not</i>
     *     be expressed solely with these two parameters may be sent through an overload
     *     of this method. See {@link #showOffsetIndicator(int, IndicatorOffsetEvent)}.
     * </p>
     *
     * @param position         corresponds to the view where an indicator should be shown. Must be less than {@link #getChildCount()}
     * @param percentageOffset how much of the indicator to draw (given as a value between -1 and 1). If out of range, no
     *                         indicator will be drawn
     */
    public void showOffsetIndicator(int position, float percentageOffset) {
        showOffsetIndicator(position, new IndicatorOffsetEvent(position, percentageOffset));
    }

    /**
     * Implementation-agnostic overload of {@link #showOffsetIndicator(int, float)}. Please
     * refer to that method's javadoc (with its line drawing) to understand precisely what
     * this method does.
     *
     * @param position       corresponds to the view where an indicator should be shown. Must be less than {@link #getChildCount()}
     * @param offsetEvent    encapsulates offset and orientation information.
     */
    public void showOffsetIndicator(int position, @NonNull IndicatorOffsetEvent offsetEvent) {
        offsetEvent.setOrientation(getOrientation());
        offsetEvent.setOffSetHints(offsetHints);
        CutoutCell child = getCutoutCellAt(position);
        // We have something to draw
        if (child != null) {
            child.offsetContentBy(offsetEvent);
        }
    }

    /**
     * Sets the {@link OffSetHint}s to be picked up by {@link IndicatorOffsetEvent#getOffSetHints()}.
     *
     * @param offsetHints    the offsets that this CutoutViewIndicator should request
     */
    public void setOffsetHints(@OffSetHint int offsetHints) {
        this.offsetHints = offsetHints;
    }

    /**
     * Setter for {@link #defaultChildParams}
     *
     * @param defaultChildParams sets default for all newly-created cells
     */
    public void setDefaultChildParams(@NonNull CutoutViewLayoutParams defaultChildParams) {
        this.defaultChildParams = defaultChildParams;
    }

    /**
     * Use this to change whether the setters and getters apply to all views in this layout,
     * or just to the currently selected one.
     *
     * @param cascade true to enable cascading changes, false to disable
     */
    public void cascadeParamChanges(boolean cascade) {
        this.cascadeChanges = cascade;
        requestLayout();
    }

    /**
     * Wrapper around {@link #setDefaultChildParams(CutoutViewLayoutParams)}
     * that also applies these params to each of the child views.
     *
     * @param params the new parameters that all child views should use
     */
    public void setAllChildParamsTo(@NonNull CutoutViewLayoutParams params) {
        boolean wasNotCascading = !cascadeChanges;
        if (wasNotCascading) {
            cascadeChanges = true;
        }

        setDefaultChildParams(params);
        for (int i = 0; i < cells.size(); i++) {
            CutoutCell cutoutCell = cells.valueAt(i);
            if (cutoutCell != null) {
                CutoutViewLayoutParams childParams = (CutoutViewLayoutParams) cutoutCell.getItemView().getLayoutParams();
                childParams.setFrom(params);
                childParams.setCutoutCell(cutoutCell);
            }
        }

        if (wasNotCascading) {
            cascadeChanges = false;
        }

        requestLayout();
    }

    /**
     * Gets the index of the page that's currently selected.
     * The value returned is guaranteed to be in the range <b>{@code 0<=position<pageCount}</b>
     * <p>
     *     The floating point value returned here represents the exact start of the
     *     current indicator, which in turn may be partially on two child views at the
     *     same time. Callers should use their best judgement to determine whether to
     *     consider both views current if it falls between integers, or do so for just
     *     one of them.
     * </p>
     * <p>
     *     If this view {@link #isInEditMode() is being edited}, the method
     *     will return a random float in the range from 0 to {@link #getPageCount()}.
     * </p>
     *
     * @return the {@link StateProxy#getCurrentPosition()} if {@link #stateProxy} is non-null,
     * 0 otherwise
     */
    private float getCurrentIndicatorPosition() {
        if (isInEditMode()) {
            return getPageCount() * (float) Math.random();
        }
        return stateProxy.getCurrentPosition();
    }

    /**
     * Gets the total number of pages. In {@link #isInEditMode() edit mode}
     * this will return 1 or {@link #editModePageCount}, whichever is higher.
     *
     * @return the number of pages found.
     */
    protected int getPageCount() {
        if (isInEditMode()) {
            return Math.max(1, editModePageCount);
        }
        return stateProxy.getCellCount();
    }

    /**
     * @param usePositiveOffset    a new value for {@link #usePositiveOffset the associated field}
     */
    public void enablePositiveOffset(boolean usePositiveOffset) {
        this.usePositiveOffset = usePositiveOffset;
    }

    /**
     * @param proposed the value returned by {@link CutoutViewIndicator#stateProxy}
     * @return the corrected value.
     * @see CutoutViewIndicator#usePositiveOffset
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

    public void setCellBackgroundId(@DrawableRes int cellBackgroundId) {
        defaultChildParams.cellBackgroundId = cellBackgroundId;

        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                params.cellBackgroundId = cellBackgroundId;
            }
        }
    }

    public int getCellBackgroundId() {
        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                return params.cellBackgroundId;
            }
        }
        return defaultChildParams.cellBackgroundId;
    }

    public void setIndicatorDrawableId(@DrawableRes int indicatorDrawableId) {
        defaultChildParams.indicatorDrawableId = indicatorDrawableId;

        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                params.indicatorDrawableId = indicatorDrawableId;
            }
        }
    }

    /**
     * This is the id of the drawable currently acting as indicator. If 0, no indicator will be shown.
     *
     * @return that id, or 0 if this was not previously {@link #setIndicatorDrawableId(int) set}
     */
    public int getIndicatorDrawableId() {
        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                return params.indicatorDrawableId;
            }
        }
        return defaultChildParams.indicatorDrawableId;
    }

    /**
     * This is the width of a cell when {@link #getOrientation() horizontal},
     * but the height of a cell when {@link #getOrientation() vertical}.
     * <p>
     *     All cells are the same proportions by default.
     * </p>
     *
     * @param cellLength any positive number of pixels,
     *                   {@link ViewGroup.LayoutParams#WRAP_CONTENT WRAP_CONTENT},
     *                   or {@link ViewGroup.LayoutParams#MATCH_PARENT MATCH_PARENT}
     * @see #setPerpendicularLength(int)
     */
    public void setCellLength(int cellLength) {
        defaultChildParams.cellLength = cellLength;

        boolean isHeight = getOrientation() == VERTICAL;
        defaultChildParams.setParamDimension(cellLength, isHeight);

        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                params.cellLength = cellLength;
                params.setParamDimension(cellLength, isHeight);
            }
        }

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
        defaultChildParams.internalSpacing = internalSpacing;

        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                params.internalSpacing = internalSpacing;
            }
        }

        requestLayout();
    }

    /**
     * This is the height of a cell when {@link #getOrientation() horizontal},
     * but the width of a cell when {@link #getOrientation() vertical}.
     * <p>
     *     All cells are the same proportions by default.
     * </p>
     *
     * @param perpendicularLength any positive number of pixels,
     *                            {@link ViewGroup.LayoutParams#WRAP_CONTENT WRAP_CONTENT},
     *                            or {@link ViewGroup.LayoutParams#MATCH_PARENT MATCH_PARENT}
     * @see #setCellLength(int)
     */
    public void setPerpendicularLength(int perpendicularLength) {
        defaultChildParams.perpendicularLength = perpendicularLength;

        boolean isHeight = getOrientation() == HORIZONTAL;
        defaultChildParams.setParamDimension(perpendicularLength, isHeight);

        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                params.perpendicularLength = perpendicularLength;
                params.setParamDimension(perpendicularLength, isHeight);
            }
        }

        requestLayout();
    }

    /**
     * @see #setCellLength(int)
     *
     * @return current length of one cell in pixels
     */
    public int getCellLength() {
        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                return params.cellLength;
            }
        }
        return defaultChildParams.cellLength;
    }

    /**
     * @see #setInternalSpacing(int)
     *
     * @return current space between cells in pixels
     */
    public int getInternalSpacing() {
        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                return params.internalSpacing;
            }
        }
        return defaultChildParams.internalSpacing;
    }

    /**
     * @see #setPerpendicularLength(int)
     *
     * @return current perpendicular length of one cell in pixels
     */
    public int getPerpendicularLength() {
        if (!cascadeChanges) {
            CutoutViewLayoutParams params = getLayoutParamsForCurrentItem();
            if (params != null) {
                return params.perpendicularLength;
            }
        }
        return defaultChildParams.perpendicularLength;
    }

    /**
     * {@link ViewPager}-specific alias for {@link #setStateProxy(StateProxy)}. Note
     * that {@link CutoutViewIndicator} uses {@link StateProxy}s internally to get state
     * information.
     * <p>
     * Call this after setting the other custom parameters ({@link #setIndicatorDrawableId(int)},
     * {@link #setCellLength(int)}, {@link #setInternalSpacing(int)}, {@link #setPerpendicularLength(int)},
     * {@link #setCellBackgroundId(int)})
     * to avoid redrawing or extra layout stuff.
     * </p>
     *
     * @param newPager    the new ViewPager that this'll sync with. Pass null to disable.
     * @see ViewPagerStateProxy
     */
    public void setViewPager(@Nullable ViewPager newPager) {
        StateProxy proxy;
        if (newPager == null) {
            proxy = null;
        } else {
            proxy = new ViewPagerStateProxy(newPager, this);
        }
        setStateProxy(proxy);
    }

    /**
     * Call this after setting the other custom parameters ({@link #setIndicatorDrawableId(int)},
     * {@link #setCellLength(int)}, {@link #setInternalSpacing(int)}, {@link #setPerpendicularLength(int)},
     * {@link #setCellBackgroundId(int)})
     * to avoid redrawing or extra layout stuff.
     *
     * @param newStateProxy the new StateProxy that this'll sync with. Pass null to disable.
     */
    public void setStateProxy(@Nullable StateProxy newStateProxy) {
        stateProxy.disassociateFrom(dataSetObserver);

        if (newStateProxy == null) {
            newStateProxy = new UnavailableProxy();
        }

        stateProxy = newStateProxy;
        if (newStateProxy.canObserve(dataSetObserver)) {
            newStateProxy.associateWith(dataSetObserver);
            dataSetObserver.onChanged();
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    ensureOnlyCurrentItemsSelected();
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isInEditMode()) {
            // This is the only part of setStateProxy we care about in EditMode
            dataSetObserver.onChanged();
        }
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            // Note that the superclass calls measure on the child for us
            bindChild(i, child);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ensureOnlyCurrentItemsSelected();
    }

    /**
     * Call this to hide the indicator on all views except for the one corresponding to the currently displaying item.
     * <p>
     *     This method is relatively expensive and should only really be called
     *     when no other animation is in progress on this view. To animate changes
     *     smoothly it is recommended to use the slightly less comprehensive
     *     {@link #showOffsetIndicator(int, float)} instead.
     * </p>
     */
    public void ensureOnlyCurrentItemsSelected() {
        final float current = getCurrentIndicatorPosition();
        for (int i = 0; i < getChildCount(); i++) {
            // offset outside the range -1..1 puts it just off-view (i.e. hiding it)
            showOffsetIndicator(i, current - i);
        }
    }

    @Nullable
    protected CutoutViewLayoutParams getLayoutParamsForCurrentItem() {
        CutoutViewLayoutParams params = null;
        float position = getCurrentIndicatorPosition();
        if (position >= 0) {
            CutoutCell cell = getCutoutCellAt((int) position);
            if (cell != null) {
                params = (CutoutViewLayoutParams) cell.getItemView().getLayoutParams();
            }
        }
        return params;
    }

    @Nullable
    private CutoutCell getCutoutCellAt(int position) {
        return cells.get(position);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CutoutViewLayoutParams;
    }

    @Override
    public CutoutViewLayoutParams generateDefaultLayoutParams() {
        return new CutoutViewLayoutParams(defaultChildParams);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        CutoutViewLayoutParams params = new CutoutViewLayoutParams(defaultChildParams);
        params.init(getContext(), attrs);
        return params;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new CutoutViewLayoutParams(p);
    }

    /**
     * Dedicated implementation of {@link DataSetObserver} for use with
     * {@link android.support.v4.view.PagerAdapter#registerDataSetObserver(DataSetObserver)}
     * and {@code RecyclerView.Adapter}.
     *
     * @see #onChanged()
     */
    protected class IntegratedDataSetObserver extends DataSetObserver {
        /**
         * This method is called when the entire data set has changed,
         * most likely through a call to {@link Cursor#requery()} on a {@link Cursor}.
         *
         * In our case, this is most commonly triggered by {@link android.support.v4.view.PagerAdapter#notifyDataSetChanged()}
         */
        @Override
        public void onChanged() {
            super.onChanged();
            int childCount = getChildCount();
            int pageCount = getPageCount();

            // Unlike in RecyclerView, the individual CutoutCells are considered interchangeable.
            // Therefore the only thing that matters here is how many views there are supposed to be on screen

            int newViews = pageCount - childCount;

            if (newViews > 0) {
                // We're adding new views
                addNewViews(childCount, pageCount);
            } else if (newViews < 0) {
                // We're removing views
                removeViews(pageCount, -newViews);
                // Make sure to null out those references to CutoutCells
                for (int i = pageCount; i < childCount; i++) {
                    unbindChild(cells.get(i));
                }
            } else {
                // Quantity isn't changing.
            }

            IndicatorOffsetEvent event = stateProxy.resendPositionInfo(CutoutViewIndicator.this, getCurrentIndicatorPosition());
            showOffsetIndicator(event.getPosition(), event);
        }

        /**
         * Calls {@link CutoutViewIndicator#addView} {@code pageCount - childCount} times. All new
         * views added are placed at the end of the CutoutViewIndicator.
         * <p>
         *     If there is a non-null entry in {@link CutoutViewIndicator#cells} at the
         *     position where this new view will be placed, then that value is queried
         *     for its {@link CutoutCell#getItemView() itemView}. If that itemView is then
         *     not null and not currently attached to a parent it will be added here.
         * </p>
         * <p>
         *     However, if the CutoutCell's itemView <em>is</em> null or attached to a
         *     {@link android.view.ViewParent parent}, it will not be used and a new
         *     CutoutCell will be created (with a backing view) by
         *     {@link CutoutViewIndicator#createCellFor(int)}.
         * </p>
         * At the end of this method, the entry in {@link CutoutViewIndicator#cells} for
         * each position of a newly-added View will contain a reference to that View's
         * {@link CutoutCell}.
         * @param childCount    the number of views currently added as children of this
         * {@link CutoutViewIndicator}
         * @param pageCount     the number of children this CutoutViewIndicator should
         *                      have to match the {@link CutoutViewIndicator#stateProxy}'s
         *                      {@link StateProxy#getCellCount() cell count}.
         */
        protected void addNewViews(int childCount, int pageCount) {
            for (int i = childCount; i < pageCount; i++) {
                // This is a cached view
                CutoutCell cell = cells.get(i);

                if (cell == null || cell.getItemView().getParent() != null) {
                    // Current CutoutCell is nonexistent or already in use elsewhere...create and add a new one!
                    if (cell != null && cell.getItemView().getParent() == CutoutViewIndicator.this) {
                        Log.w(TAG, "It would appear that the view at " + i + " was not removed properly.");
                    }

                    cell = createCellFor(i);
                    cells.put(i, cell);
                }

                // This will invalidate the added view, triggering a call to this class's onMeasure()
                addView(cell.getItemView(), i);
            }
        }

        /**
         * This method is called when the entire data becomes invalid,
         * most likely through a call to {@link Cursor#deactivate()} or {@link Cursor#close()} on a
         * {@link Cursor}.
         * <p>
         *     {@link ViewPager} doesn't really call this method.
         * </p>
         */
        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }
}

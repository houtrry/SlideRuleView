package com.houtrry.slideruleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: houtrry
 * @time: 2017/10/17 19:02
 * @versio: $Rev$
 * @desc: ${TODO}
 */

public class SlideRuleView extends ViewGroup {

    private static final String TAG = SlideRuleView.class.getSimpleName();
    private RulerScaleView mRulerScaleView;
    private TickMarkView mTickMarkView;
    private ViewDragHelper mViewDragHelper;
    private int mLastDragState;
    private int mCurrentDragLeft;
    private float mLastCalculateValue;

    public SlideRuleView(Context context) {
        this(context, null);
    }

    public SlideRuleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideRuleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: changed: "+changed+", l: "+l+", t: "+t+", r: "+r+", b: "+b);
        int measuredWidth = getMeasuredWidth();
        Log.d(TAG, "onLayout: measuredWidth: "+measuredWidth);
        mRulerScaleView.layout(0, 0, mRulerScaleView.getMeasuredWidth(), mRulerScaleView.getMeasuredHeight());
        mTickMarkView.layout((int)(getMeasuredWidth()*0.5f - mTickMarkView.getMeasuredWidth() * 0.5f+0.5f), 0, (int) (getMeasuredWidth()*0.5f + mTickMarkView.getMeasuredWidth() * 0.5f), mTickMarkView.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int measuredWidthRulerScale = mRulerScaleView.getMeasuredWidth();
        int measuredWidth = getMeasuredWidth();

        Log.d(TAG, "onFinishInflate: measuredWidthRulerScale: "+measuredWidthRulerScale+", measuredWidth: "+measuredWidth);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        mRulerScaleView = (RulerScaleView) getChildAt(0);
//        mTickMarkView = getChildAt(1);
    }

    private void init(Context context, AttributeSet attrs) {
        initViewDragHelper(context);
        initChildView(context);
        initAttrs(context, attrs);
    }

    private void initViewDragHelper(Context context) {
        mViewDragHelper = ViewDragHelper.create(this, mViewDragHelperCallback);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideRuleView);

        int tick_color = typedArray.getColor(R.styleable.SlideRuleView_tick_color, Color.RED);
        float tick_width = typedArray.getDimension(R.styleable.SlideRuleView_tick_width, 10);
        float tick_height = typedArray.getDimension(R.styleable.SlideRuleView_tick_height, 50);

        TypedValue minValueTypedValue = typedArray.peekValue(R.styleable.SlideRuleView_min_value);
        float minValue;
        if (minValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            minValue = minValueTypedValue.getFloat();
        } else {
            minValue = minValueTypedValue.data;
        }
        float maxValue;
        TypedValue maxValueTypedValue = typedArray.peekValue(R.styleable.SlideRuleView_max_value);
        if (maxValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            maxValue = maxValueTypedValue.getFloat();
        } else {
            maxValue = maxValueTypedValue.data;
        }


        int valueDecimal = typedArray.getInteger(R.styleable.SlideRuleView_value_decimal, 1);
        int gridGapNumber = typedArray.getInteger(R.styleable.SlideRuleView_grid_gap_number, 5);

        int gridOffset = typedArray.getInteger(R.styleable.SlideRuleView_grid_offset, 0);

        int longScaleLineColor = typedArray.getColor(R.styleable.SlideRuleView_long_scale_line_color, Color.BLACK);

        int longScaleLineWidth = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_long_scale_line_width, 10);

        int longScaleLineHeight = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_long_scale_line_height, 50);

        int shortScaleLineColor = typedArray.getColor(R.styleable.SlideRuleView_short_scale_line_color, Color.BLACK);

        int shortScaleLineWidth = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_short_scale_line_width, 6);

        int shortScaleLineHeight = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_short_scale_line_height, 30);

        int zeroLineColor = typedArray.getColor(R.styleable.SlideRuleView_zero_line_color, Color.BLACK);

        int zeroLineWidth = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_zero_line_width, 6);

        int rulerScaleBackgroundColor = typedArray.getColor(R.styleable.SlideRuleView_ruler_scale_background_color, Color.WHITE);

        int gapDistance = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_gap_distance, 6);

        int scaleTextSize = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_scale_text_size, 30);

        int scaleTextColor = typedArray.getColor(R.styleable.SlideRuleView_scale_text_color, Color.BLACK);

        int scaleTextMargin = typedArray.getDimensionPixelSize(R.styleable.SlideRuleView_scale_text_margin, 45);


        mTickMarkView.setTickMarkColor(tick_color)
                .setTickMarkWidth(tick_width)
                .setTickMarkHeight(tick_height);

        Log.d(TAG, "initAttrs: TickMarkView, tick_color: "+tick_color+", tick_width: "+tick_width+", tick_height: "+tick_height);

        mRulerScaleView.setMinValue(minValue)
                .setMaxValue(maxValue)
                .setValueDecimal(valueDecimal)
                .setGridGapNumber(gridGapNumber)
                .setGridOffset(gridOffset)
                .setLongScaleLineColor(longScaleLineColor)
                .setLongScaleLineWidth(longScaleLineWidth)
                .setLongScaleLineHeight(longScaleLineHeight)
                .setShortScaleLineColor(shortScaleLineColor)
                .setShortScaleLineWidth(shortScaleLineWidth)
                .setShortScaleLineHeight(shortScaleLineHeight)
                .setZeroLineColor(zeroLineColor)
                .setZeroLienWidth(zeroLineWidth)
                .setRulerScaleBackgroundColor(rulerScaleBackgroundColor)
                .setGapDistance(gapDistance)
                .setScaleTextSize(scaleTextSize)
                .setScaleTextColor(scaleTextColor)
                .setScaleTextMargin(scaleTextMargin);

        Log.d(TAG, "initAttrs: RulerScaleView, minValue: "+minValue+", maxValue: "+maxValue+", valueDecimal: "+valueDecimal+", gridGapNumber: "+gridGapNumber+", gridOffset: " +gridOffset);
        Log.d(TAG, "initAttrs: RulerScaleView, longScaleLineColor: "+longScaleLineColor+", longScaleLineWidth: "+longScaleLineWidth+", longScaleLineHeight: "+longScaleLineHeight);
        Log.d(TAG, "initAttrs: RulerScaleView, shortScaleLineColor: "+shortScaleLineColor+", shortScaleLineWidth: "+shortScaleLineWidth+", shortScaleLineHeight: "+shortScaleLineHeight);
        Log.d(TAG, "initAttrs: RulerScaleView, zeroLineColor: "+zeroLineColor+", zeroLineWidth: "+zeroLineWidth+", rulerScaleBackgroundColor: "+rulerScaleBackgroundColor);
        Log.d(TAG, "initAttrs: RulerScaleView, gapDistance: "+gapDistance+", scaleTextSize: "+scaleTextSize+", scaleTextColor: "+scaleTextColor);

        typedArray.recycle();

    }

    private void initChildView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_rulescale, this, true);
        mRulerScaleView = rootView.findViewById(R.id.rulerScaleView);
        mTickMarkView = rootView.findViewById(R.id.tickMarkView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback mViewDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mRulerScaleView == child;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d(TAG, "clampViewPositionHorizontal: left: "+left+", mRulerScaleView.getMeasuredWidth(): "+mRulerScaleView.getMeasuredWidth()+", "+getMeasuredWidth());
            //控制滑动边界
            if (left > 0) {
                left = 0;
            } else if (left < getMeasuredWidth() - mRulerScaleView.getMeasuredWidth()) {
                left = getMeasuredWidth() - mRulerScaleView.getMeasuredWidth();
            }

            return left;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.d(TAG, "onViewReleased: xvel: "+xvel+", yvel: "+yvel);
            mViewDragHelper.flingCapturedView(getMeasuredWidth() - mRulerScaleView.getMeasuredWidth(), 0, 0, 0);
            ViewCompat.postInvalidateOnAnimation(SlideRuleView.this);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float calculateValue = mRulerScaleView.calculateCurrentValue(left);
            if (mSlideRuleListener != null) {
                if (mLastCalculateValue != calculateValue) {
                   mSlideRuleListener.slideRule(calculateValue);
                    Log.d(TAG, "onViewPositionChanged: "+Thread.currentThread().getName());
                    mLastCalculateValue = calculateValue;
                }
            }


            Log.d(TAG, "onViewPositionChanged: left: "+left+", top: "+top+", dx: "+dx+", dy: "+dy+", calculateValue: " +calculateValue);
            mCurrentDragLeft = left;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (mLastDragState != ViewDragHelper.STATE_IDLE && state == ViewDragHelper.STATE_IDLE) {
                Log.d(TAG, "onViewDragStateChanged: 赶紧去处理一下");
                int finalLeft = mRulerScaleView.calculateLatestTick(mCurrentDragLeft);
                mViewDragHelper.smoothSlideViewTo(mRulerScaleView, finalLeft, 0);
                ViewCompat.postInvalidateOnAnimation(SlideRuleView.this);
            }
            mLastDragState = state;
        }
    };

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private SlideRuleListener mSlideRuleListener;

    public void setSlideRuleListener(SlideRuleListener slideRuleListener) {
        mSlideRuleListener = slideRuleListener;
        if (mSlideRuleListener != null && mRulerScaleView != null) {
            mSlideRuleListener.slideRule(mRulerScaleView.getCurrentValue());
        }
    }

    public void setSlideValue(float value) {
        float slideValue = mRulerScaleView.setSlideValue(value);
        if (mSlideRuleListener != null) {
            mSlideRuleListener.slideRule(slideValue);
        }
    }
}

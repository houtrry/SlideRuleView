package com.houtrry.slideruleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import static android.content.ContentValues.TAG;

/**
 * @author: houtrry
 * @time: 2017/10/23 17:01
 * @version: $Rev$
 * @desc: ${TODO}
 */

public class SlidingScaleView extends View {

    private int mTickLineColor;
    private float mTickLineWidth;
    private float mTickLineHeight;
    private float mMinValue;
    private float mMaxValue;
    private int mValueDecimal;
    private int mGridGapNumber;
    private int mGridOffset;
    private int mLongScaleLineColor;
    private int mLongScaleLineWidth;
    private int mLongScaleLineHeight;
    private int mShortScaleLineColor;
    private int mShortScaleLineWidth;
    private int mShortScaleLineHeight;
    private int mZeroLineColor;
    private int mZeroLineWidth;
    private int mRulerScaleBackgroundColor;
    private int mGapDistance;
    private int mScaleTextSize;
    private int mScaleTextColor;
    private int mScaleTextMargin;
    private Paint mZeroLinePaint;

    private Paint mShortScaleLinePaint;
    private Paint mLongScaleLinePaint;
    private Paint mTickLinePaint;
    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;
    private float mLastX;
    private float mLastY;
    private int mWidth;
    private int mHeight;
    private float mGapValue;
    private Paint mTextPaint;

    public SlidingScaleView(Context context) {
        this(context, null);
    }

    public SlidingScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint();
        initScroller(context);
        initVelocityTracker(context);
        initGap();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingScaleView);
        mTickLineColor = typedArray.getColor(R.styleable.SlidingScaleView_sliding_tick_line_color, Color.RED);
        mTickLineWidth = typedArray.getDimension(R.styleable.SlidingScaleView_sliding_tick_line_width, 10);
        mTickLineHeight = typedArray.getDimension(R.styleable.SlidingScaleView_sliding_tick_line_height, 50);
        TypedValue minValueTypedValue = typedArray.peekValue(R.styleable.SlidingScaleView_sliding_min_value);
        if (minValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            mMinValue = minValueTypedValue.getFloat();
        } else {
            mMinValue = minValueTypedValue.data;
        }
        TypedValue maxValueTypedValue = typedArray.peekValue(R.styleable.SlidingScaleView_sliding_max_value);
        if (maxValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            mMaxValue = maxValueTypedValue.getFloat();
        } else {
            mMaxValue = maxValueTypedValue.data;
        }
        mValueDecimal = typedArray.getInteger(R.styleable.SlidingScaleView_sliding_value_decimal, 1);
        mGridGapNumber = typedArray.getInteger(R.styleable.SlidingScaleView_sliding_grid_gap_number, 5);

        TypedValue gapValueTypedValue = typedArray.peekValue(R.styleable.SlidingScaleView_sliding_grid_gap_value);
        if (gapValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            this.mGapValue = gapValueTypedValue.getFloat();
        } else {
            this.mGapValue = gapValueTypedValue.data;
        }

        mGridOffset = typedArray.getInteger(R.styleable.SlidingScaleView_sliding_grid_offset, 0);

        mLongScaleLineColor = typedArray.getColor(R.styleable.SlidingScaleView_sliding_long_scale_line_color, Color.BLACK);

        mLongScaleLineWidth = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_long_scale_line_width, 10);

        mLongScaleLineHeight = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_long_scale_line_height, 50);

        mShortScaleLineColor = typedArray.getColor(R.styleable.SlidingScaleView_sliding_short_scale_line_color, Color.BLACK);

        mShortScaleLineWidth = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_short_scale_line_width, 6);

        mShortScaleLineHeight = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_short_scale_line_height, 30);

        mZeroLineColor = typedArray.getColor(R.styleable.SlidingScaleView_sliding_zero_line_color, Color.BLACK);

        mZeroLineWidth = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_zero_line_width, 6);

        mRulerScaleBackgroundColor = typedArray.getColor(R.styleable.SlidingScaleView_sliding_ruler_scale_background_color, Color.WHITE);

        mGapDistance = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_gap_distance, 6);

        mScaleTextSize = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_scale_text_size, 30);

        mScaleTextColor = typedArray.getColor(R.styleable.SlidingScaleView_sliding_scale_text_color, Color.BLACK);

        mScaleTextMargin = typedArray.getDimensionPixelSize(R.styleable.SlidingScaleView_sliding_scale_text_margin, 45);

        Log.d(TAG, "initAttrs: TickMarkView, mTickLineColor: " + mTickLineColor + ", mTickLineWidth: " + mTickLineWidth + ", mTickLineHeight: " + mTickLineHeight);
        Log.d(TAG, "initAttrs: RulerScaleView, minValue: " + mMinValue + ", maxValue: " + mMaxValue + ", valueDecimal: " + mValueDecimal + ", gridGapNumber: " + mGridGapNumber + ", gridOffset: " + mGridOffset);
        Log.d(TAG, "initAttrs: RulerScaleView, longScaleLineColor: " + mLongScaleLineColor + ", longScaleLineWidth: " + mLongScaleLineWidth + ", longScaleLineHeight: " + mLongScaleLineHeight);
        Log.d(TAG, "initAttrs: RulerScaleView, shortScaleLineColor: " + mShortScaleLineColor + ", shortScaleLineWidth: " + mShortScaleLineWidth + ", shortScaleLineHeight: " + mShortScaleLineHeight);
        Log.d(TAG, "initAttrs: RulerScaleView, zeroLineColor: " + mZeroLineColor + ", zeroLineWidth: " + mZeroLineWidth + ", rulerScaleBackgroundColor: " + mRulerScaleBackgroundColor);
        Log.d(TAG, "initAttrs: RulerScaleView, gapDistance: " + mGapDistance + ", scaleTextSize: " + mScaleTextSize + ", scaleTextColor: " + mScaleTextColor);
        typedArray.recycle();
    }

    private void initPaint() {
        mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mZeroLinePaint.setStrokeWidth(mZeroLineWidth);
        mZeroLinePaint.setColor(mZeroLineColor);

        mShortScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShortScaleLinePaint.setColor(mShortScaleLineColor);
        mShortScaleLinePaint.setStrokeWidth(mShortScaleLineWidth);

        mLongScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLongScaleLinePaint.setColor(mLongScaleLineColor);
        mLongScaleLinePaint.setStrokeWidth(mLongScaleLineWidth);

        mTickLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickLinePaint.setColor(mTickLineColor);
        mTickLinePaint.setStrokeWidth(mLongScaleLineWidth);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mScaleTextColor);
        mTextPaint.setTextSize(mScaleTextSize);
    }

    private void initScroller(Context context) {
        mScroller = new Scroller(context);
    }

    private void initVelocityTracker(Context context) {
        mVelocityTracker = VelocityTracker.obtain();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    /**
     * 总共有多少个小格
     */
    private int mTotalGap = 0;

    /**
     * 每个小格对应的值是多少
     */
    private void initGap() {
        mTotalGap = (int) Math.ceil((mMaxValue - mMinValue) / mGapValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawZeroLine(canvas);
        drawScaleLineAndText(canvas);
        drawTickMark(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float eventX = event.getX();
                float eventY = event.getY();
                ((View) getParent()).scrollBy((int) (mLastX - eventX), 0);
                mLastX = eventX;
                mLastY = eventY;
                break;
            }
            case MotionEvent.ACTION_UP: {

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, 0.01f);


                float xVelocity = velocityTracker.getXVelocity();
                float yVelocity = velocityTracker.getYVelocity();

                mScroller.fling(getLeft(), getTop(), (int) xVelocity, (int) yVelocity,
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                break;
            }

        }
        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 释放VelocityTracker
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private int mOriginWidth = 0;
    private int measureWidth(int widthMeasureSpec) {
        int width;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        mOriginWidth = size;
        if (mode == MeasureSpec.EXACTLY) {
            width = size + mTotalGap * mGapDistance;
        } else {
            width = size + mTotalGap * mGapDistance;
        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int height;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = size;
        }
        return height;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(mRulerScaleBackgroundColor);
    }

    private void drawZeroLine(Canvas canvas) {
        final float zeroLineY = mZeroLineWidth * 0.5f;
        canvas.drawLine(0, zeroLineY, mWidth, zeroLineY, mZeroLinePaint);
    }

    private float mCurrentX = 0;
    private String mDrawText = "";
    private float mDrawTextLeft = 0;
    private float mDrawTextBottom = 0;
    private Rect mTextRect = new Rect();
    private void drawScaleLineAndText(Canvas canvas) {
        mCurrentX = mOriginWidth * 0.5f;
        for (int i = 0; i < mTotalGap; i++) {
            if ((i - mGridOffset) % mGridGapNumber == 0) {
                //画长线条
                canvas.drawLine(mCurrentX, mZeroLineWidth, mCurrentX, mLongScaleLineHeight - mLongScaleLineWidth*0.5f, mLongScaleLinePaint);

                //画长线条下面的文字
                mDrawText = String.valueOf(mMinValue + mGapValue * i);
                mDrawTextLeft = mCurrentX - mTextPaint.measureText(mDrawText) * 0.5f;
                mTextPaint.getTextBounds(mDrawText, 0, mDrawText.length(), mTextRect);
                mDrawTextBottom = mZeroLineWidth + mLongScaleLineHeight + mScaleTextMargin + mTextRect.height();
                canvas.drawText(mDrawText, mDrawTextLeft, mDrawTextBottom, mTextPaint);
            } else {
                //画短线条
                canvas.drawLine(mCurrentX, mZeroLineWidth, mCurrentX, mShortScaleLineHeight - mShortScaleLineWidth*0.5f, mShortScaleLinePaint);
            }
            mCurrentX += mGapDistance;
        }
    }

    private void drawTickMark(Canvas canvas) {
        canvas.drawLine(mWidth * 0.5f, mZeroLineWidth, mWidth * 0.5f, mTickLineHeight - mTickLineWidth*0.5f, mTickLinePaint);
    }
}

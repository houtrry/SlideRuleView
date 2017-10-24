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
import android.view.View;

/**
 * @author: houtrry
 * @time: 2017/10/17 19:03
 * @version: $Rev$
 * @desc: 滑动尺的可滑动部分--下面的背景和刻度线
 */

public class RulerScaleView extends View {

    private static final String TAG = RulerScaleView.class.getSimpleName();
    /**
     * 卷尺显示的最小值
     */
    private float mMinValue = 30f;
    /**
     * 卷尺显示的最大值
     */
    private float mMaxValue = 70f;
    /**
     * 10小格1大格
     */
    private int mGridGapNumber = 10;
    /**
     * 每一小格的单位是0.1
     */
    private float mGridGapValue = 0.1f;

    /**
     * 第一个大格的位置
     * 第一个大格出现在第几个小格的位置(有可能会有最小值不是大格的情况)
     */
    private int mGridOffset = 0;

    /**
     * 长刻度线的宽
     */
    private int mLongScaleLineWidth = 8;
    /**
     * 长刻度线的高
     */
    private int mLongScaleLineHeight = 70;
    /**
     * 长刻度线的颜色
     */
    private int mLongScaleLineColor = Color.parseColor("#808080");


    /**
     * 短刻度线的宽
     */
    private int mShortScaleLineWidth = 6;
    /**
     * 短刻度线的高
     */
    private int mShortScaleLineHeight = 40;
    /**
     * 短刻度线的颜色
     */
    private int mShortScaleLineColor = Color.parseColor("#808080");

    /**
     * 基线的宽度(线条的宽度)
     */
    private int mZeroLienWidth = 6;
    /**
     * 基线的颜色
     */
    private int mZeroLineColor = Color.parseColor("#808080");


    /**
     * 尺子的背景色
     */
    private int mRulerScaleBackgroundColor = Color.parseColor("#ff99cc00");

    private int mWidth;
    private int mHeight;
    private Paint mLongScaleLinePaint;
    private Paint mShortScaleLinePaint;

    /**
     * 上面刻度, 下面文字
     */
    private static final int TYPE_TOP = 0x0001;
    /**
     * 上面文字, 下面刻度
     */
    private static final int TYPE_BOTTOM = 0x0002;

    /**
     * 滑动尺的类型(默认是上面刻度, 下面文字)
     */
    private int mScaleLineType = TYPE_TOP;

    /**
     * 小格的总数
     */
    private int mTotalGrid;
    /**
     * 每一小格的像素距离
     */
    private float mGapDistance = 20;
    private Paint mZeroLinePaint;

    /**
     * 文字大小
     */
    private float mScaleTextSize = 30;
    /**
     * 文字颜色
     */
    private int mScaleTextColor = Color.parseColor("#ff222222");
    private Paint mScaleTextPaint;
    /**
     * 文字距另一边的距离
     */
    private int mScaleTextMargin = 50;

    /**
     * 结果的返回值取一位小数
     */
    private int mValueDecimal = 2;

    public RulerScaleView(Context context) {
        this(context, null);
    }

    public RulerScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideRuleView);

        TypedValue minValueTypedValue = typedArray.peekValue(R.styleable.SlideRuleView_min_value);
        float minValue;
        if (minValueTypedValue == null) {
            minValue = 30;
        } else if (minValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            minValue = minValueTypedValue.getFloat();
        } else {
            minValue = minValueTypedValue.data;
        }
        float maxValue;
        TypedValue maxValueTypedValue = typedArray.peekValue(R.styleable.SlideRuleView_max_value);
        if (maxValueTypedValue == null) {
            maxValue = 70;
        } else if (maxValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            maxValue = maxValueTypedValue.getFloat();
        } else {
            maxValue = maxValueTypedValue.data;
        }


        int valueDecimal = typedArray.getInteger(R.styleable.SlideRuleView_value_decimal, 1);
        int gridGapNumber = typedArray.getInteger(R.styleable.SlideRuleView_grid_gap_number, 5);

        float gapValue;
        TypedValue gapValueTypedValue = typedArray.peekValue(R.styleable.SlideRuleView_grid_gap_value);
        if (gapValueTypedValue == null) {
            gapValue = 0.1f;
        } else if (gapValueTypedValue.type == TypedValue.TYPE_FLOAT) {
            gapValue = gapValueTypedValue.getFloat();
        } else {
            gapValue = gapValueTypedValue.data;
        }

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


        Log.d(TAG, "initAttrs: ---------------------------------------------RulerScaleView----start------------------------------------------------");
        Log.d(TAG, "initAttrs: RulerScaleView, minValue: "+minValue+", maxValue: "+maxValue+", valueDecimal: "+valueDecimal+", gridGapNumber: "+gridGapNumber+", gridOffset: " +gridOffset);
        Log.d(TAG, "initAttrs: RulerScaleView, longScaleLineColor: "+longScaleLineColor+", longScaleLineWidth: "+longScaleLineWidth+", longScaleLineHeight: "+longScaleLineHeight);
        Log.d(TAG, "initAttrs: RulerScaleView, shortScaleLineColor: "+shortScaleLineColor+", shortScaleLineWidth: "+shortScaleLineWidth+", shortScaleLineHeight: "+shortScaleLineHeight);
        Log.d(TAG, "initAttrs: RulerScaleView, zeroLineColor: "+zeroLineColor+", zeroLineWidth: "+zeroLineWidth+", rulerScaleBackgroundColor: "+rulerScaleBackgroundColor);
        Log.d(TAG, "initAttrs: RulerScaleView, gapDistance: "+gapDistance+", gapValue: "+gapValue+", scaleTextSize: "+scaleTextSize+", scaleTextColor: "+scaleTextColor+", scaleTextMargin: "+scaleTextMargin);
        Log.d(TAG, "initAttrs: ---------------------------------------------RulerScaleView----end------------------------------------------------");

        typedArray.recycle();

        mMinValue = minValue;
        mMaxValue = maxValue;
        mValueDecimal = valueDecimal;
        mGridGapNumber = gridGapNumber;
        mGridGapValue = gapValue;
        mGridOffset = gridOffset;

        mLongScaleLineColor = longScaleLineColor;
        mLongScaleLineWidth = longScaleLineWidth;
        mLongScaleLineHeight = longScaleLineHeight;

        mShortScaleLineColor = shortScaleLineColor;
        mShortScaleLineWidth = shortScaleLineWidth;
        mShortScaleLineHeight = shortScaleLineHeight;

        mZeroLineColor = zeroLineColor;
        mZeroLienWidth = zeroLineWidth;

        mRulerScaleBackgroundColor = rulerScaleBackgroundColor;

        mGapDistance = gapDistance;
        mScaleTextColor = scaleTextColor;
        mScaleTextSize = scaleTextSize;
        mScaleTextMargin = scaleTextMargin;

    }

    private void initPaint() {
        mLongScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLongScaleLinePaint.setColor(mLongScaleLineColor);
        mLongScaleLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLongScaleLinePaint.setStyle(Paint.Style.STROKE);
        mLongScaleLinePaint.setStrokeWidth(mLongScaleLineWidth);

        mShortScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShortScaleLinePaint.setColor(mShortScaleLineColor);
        mShortScaleLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mShortScaleLinePaint.setStyle(Paint.Style.STROKE);
        mShortScaleLinePaint.setStrokeWidth(mShortScaleLineWidth);

        mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mZeroLinePaint.setColor(mZeroLineColor);
        mZeroLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mZeroLinePaint.setStyle(Paint.Style.STROKE);
        mZeroLinePaint.setStrokeWidth(mZeroLienWidth);

        mScaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleTextPaint.setColor(mScaleTextColor);
        mScaleTextPaint.setTextSize(mScaleTextSize);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        Log.d(TAG, "measureWidth: originWidth: mWidth: " + mWidth + ", mHeight: " + mHeight);
        calculateGap();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawZeroLine(canvas);
        drawScaleLineAndText(canvas);
    }

    /**
     * 计算当前的值
     *
     * @param left
     * @return
     */
    public float calculateCurrentValue(int left) {
        Log.d(TAG, "calculateCurrentValue: left: " + left + ", (Math.abs(left): " + (Math.abs(left)));
        int position = (int) ((Math.abs(left)) / mGapDistance);
        float currentValue = mMinValue + position * mGridGapValue;
        float result = AndroidTools.roundKeepDecimal(currentValue, mValueDecimal);
        mCurrentValue = result;
        return result;
    }

    /**
     * 计算最近一个刻度线的位置(left)
     *
     * @param currentDragLeft
     * @return
     */
    public int calculateLatestTick(int currentDragLeft) {
        int position = Math.round((Math.abs(currentDragLeft)) / mGapDistance);
        int finalLeft = (int) (position * mGapDistance + 0.5);
        return finalLeft * (currentDragLeft < 0 ? -1 : 1);
    }

    /**
     * 画背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawColor(mRulerScaleBackgroundColor);
    }

    /**
     * 画零线
     *
     * @param canvas
     */
    private void drawZeroLine(Canvas canvas) {
        final float zeroLineY = mScaleLineType == TYPE_TOP ? mZeroLienWidth * 0.5f : mHeight - mZeroLienWidth * 0.5f;
        canvas.drawLine(0, zeroLineY, mWidth, zeroLineY, mZeroLinePaint);
    }

    private boolean isLongScale;
    private boolean isTypeTop;
    private float mCurrentX = 0;
    private float mStartY;
    private float mStopY;
    private Paint mLinePaint;
    private String mTextStr;
    private float mTextLeft;
    private float mTextBottom;
    private Rect mRectText = new Rect();
    /**
     * 画大小刻度线 以及大刻度线对应的文字
     *
     * @param canvas
     */
    private void drawScaleLineAndText(Canvas canvas) {
        mCurrentX = mOriginalWidth * 0.5f;
        isTypeTop = mScaleLineType == TYPE_TOP;
        for (int i = 0; i <= mTotalGrid; i++) {
            isLongScale = (i - mGridOffset) % mGridGapNumber == 0;
            if (isTypeTop) {
                mStartY = 0;
                mStopY = isLongScale ? mLongScaleLineHeight : mShortScaleLineHeight;
                mStartY += mZeroLienWidth;
                mStopY += mZeroLienWidth;
            } else {
                mStartY = isLongScale ? mHeight - mLongScaleLineHeight : mHeight - mShortScaleLineHeight;
                mStopY = mHeight;
                mStartY -= mZeroLienWidth;
                mStopY -= mZeroLienWidth;
            }
            mLinePaint = isLongScale ? mLongScaleLinePaint : mShortScaleLinePaint;
            canvas.drawLine(mCurrentX, mStartY, mCurrentX, mStopY, mLinePaint);

            mTextStr = String.valueOf(mMinValue + mGridGapValue * i);
            float measureTextWidth = mScaleTextPaint.measureText(mTextStr, 0, mTextStr.length());
            mTextLeft = mCurrentX - measureTextWidth * 0.5f;

            mScaleTextPaint.getTextBounds(mTextStr, 0, mTextStr.length(), mRectText);
            if (isTypeTop) {
                mTextBottom = mHeight - mScaleTextMargin;
            } else {
                mTextBottom = mScaleTextMargin + mRectText.height();
            }
            if (isLongScale) {
                canvas.drawText(mTextStr, mTextLeft, mTextBottom, mScaleTextPaint);
            }

            Log.d(TAG, "drawScaleLineAndText: mCurrentX: " + mCurrentX);
            mCurrentX += mGapDistance;
        }
    }


    private void calculateGap() {
        mTotalGrid = (int) Math.ceil((mMaxValue - mMinValue) / mGridGapValue);
        Log.d(TAG, "calculateGap: mTotalGrid: " + mTotalGrid);
    }

    private int mOriginalWidth = 0;

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        Log.d(TAG, "measureWidth: originWidth: size: " + size+", mTotalGrid: "+mTotalGrid+", mGapDistance: "+mGapDistance);
        if (mode == MeasureSpec.EXACTLY) {
            result = (int) (size + mTotalGrid * mGapDistance);
        } else {
            result = (int) (size + mTotalGrid * mGapDistance);
        }
        Log.d(TAG, "measureWidth: originWidth: size: " + size + ", result: " + result);
        mOriginalWidth = size;
        return result;
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = size;
        }
        return result;
    }

    /**
     * 卷尺显示的最小值
     *
     * @param minValue
     */
    public RulerScaleView setMinValue(float minValue) {
        Log.d(TAG, "setMinValue: minValue: "+minValue);
        mMinValue = minValue;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 卷尺显示的最大值
     *
     * @param maxValue
     */
    public RulerScaleView setMaxValue(float maxValue) {
        Log.d(TAG, "setMaxValue: maxValue: "+maxValue);
        mMaxValue = maxValue;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 1大格包含多少小格
     *
     * @param gridGapNumber
     */
    public RulerScaleView setGridGapNumber(int gridGapNumber) {
        Log.d(TAG, "setGridGapNumber: gridGapNumber: "+gridGapNumber);
        mGridGapNumber = gridGapNumber;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 每一小格对应的值是多少
     * @param value
     * @return
     */
    public RulerScaleView setGridGapValue(float value) {
        mGridGapValue = value;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 第一个大格的位置
     * 第一个大格出现在第几个小格的位置(有可能会有最小值不是大格的情况)
     *
     * @param gridOffset
     */
    public RulerScaleView setGridOffset(int gridOffset) {
        Log.d(TAG, "setGridOffset: gridOffset: "+gridOffset);
        mGridOffset = gridOffset;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 长刻度线的宽度
     *
     * @param longScaleLineWidth
     */
    public RulerScaleView setLongScaleLineWidth(int longScaleLineWidth) {
        Log.d(TAG, "setLongScaleLineWidth: longScaleLineWidth: "+longScaleLineWidth);
        mLongScaleLineWidth = longScaleLineWidth;
        mLongScaleLinePaint.setStrokeWidth(mLongScaleLineWidth);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 长刻度线的高度
     *
     * @param longScaleLineHeight
     */
    public RulerScaleView setLongScaleLineHeight(int longScaleLineHeight) {
        Log.d(TAG, "setLongScaleLineHeight: longScaleLineHeight: "+longScaleLineHeight);
        mLongScaleLineHeight = longScaleLineHeight;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 长刻度线的颜色
     *
     * @param longScaleLineColor
     */
    public RulerScaleView setLongScaleLineColor(int longScaleLineColor) {
        Log.d(TAG, "setLongScaleLineColor: longScaleLineColor: "+longScaleLineColor);
        mLongScaleLineColor = longScaleLineColor;
        mLongScaleLinePaint.setColor(mLongScaleLineColor);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 短刻度线的宽度
     *
     * @param shortScaleLineWidth
     */
    public RulerScaleView setShortScaleLineWidth(int shortScaleLineWidth) {
        Log.d(TAG, "setShortScaleLineWidth: shortScaleLineWidth: "+shortScaleLineWidth);
        mShortScaleLineWidth = shortScaleLineWidth;
        mShortScaleLinePaint.setStrokeWidth(mShortScaleLineWidth);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 短刻度线的高度
     *
     * @param shortScaleLineHeight
     */
    public RulerScaleView setShortScaleLineHeight(int shortScaleLineHeight) {
        Log.d(TAG, "setShortScaleLineHeight: shortScaleLineHeight: "+shortScaleLineHeight);
        mShortScaleLineHeight = shortScaleLineHeight;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 短刻度线的颜色
     *
     * @param shortScaleLineColor
     */
    public RulerScaleView setShortScaleLineColor(int shortScaleLineColor) {
        Log.d(TAG, "setShortScaleLineColor: shortScaleLineColor: "+shortScaleLineColor);
        mShortScaleLineColor = shortScaleLineColor;
        mShortScaleLinePaint.setColor(mShortScaleLineColor);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 基线的宽度
     *
     * @param zeroLienWidth
     */
    public RulerScaleView setZeroLienWidth(int zeroLienWidth) {
        mZeroLienWidth = zeroLienWidth;
        mZeroLinePaint.setStrokeWidth(mZeroLienWidth);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 基线的颜色
     *
     * @param zeroLineColor
     */
    public RulerScaleView setZeroLineColor(int zeroLineColor) {
        mZeroLineColor = zeroLineColor;
        mZeroLinePaint.setColor(mZeroLineColor);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 尺子的背景色
     *
     * @param rulerScaleBackgroundColor
     */
    public RulerScaleView setRulerScaleBackgroundColor(int rulerScaleBackgroundColor) {
        mRulerScaleBackgroundColor = rulerScaleBackgroundColor;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 每一小格的像素距离
     *
     * @param gapDistance
     */
    public RulerScaleView setGapDistance(float gapDistance) {
        mGapDistance = gapDistance;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 文字大小
     *
     * @param scaleTextSize
     */
    public RulerScaleView setScaleTextSize(float scaleTextSize) {
        mScaleTextSize = scaleTextSize;
        mScaleTextPaint.setTextSize(mScaleTextSize);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    public RulerScaleView setScaleTextColor(int scaleTextColor) {
        mScaleTextColor = scaleTextColor;
        mScaleTextPaint.setColor(mScaleTextColor);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 文字颜色
     *
     * @param textMargin
     */
    public RulerScaleView setScaleTextMargin(int textMargin) {
        mScaleTextMargin = textMargin;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 返回值取指定位数的小数
     *
     * @param valueDecimal 结果值的小数位数
     */
    public RulerScaleView setValueDecimal(int valueDecimal) {
        mValueDecimal = valueDecimal;
        return this;
    }

    private float mCurrentValue = mMinValue;
    public float setSlideValue(float slideValue) {
        if (slideValue < mMinValue || slideValue > mMaxValue) {
            throw new IllegalArgumentException("the value must between "+mMinValue +" and "+mMaxValue);
        }
        final float result = AndroidTools.roundKeepDecimal(slideValue, mValueDecimal);
        mCurrentValue = result;
        final int offset =  - (int) ((result - mMinValue) / mGridGapValue * mGapDistance + 0.5f) - getLeft();
        offsetLeftAndRight(offset);
        return result;
    }

    public float getCurrentValue() {
        return mCurrentValue;
    }
}

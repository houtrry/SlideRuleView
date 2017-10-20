package com.houtrry.slideruleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
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
    private float mTextMargin = 50;


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
        initPaint(context);
    }

    private void initPaint(Context context) {
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
        return AndroidTools.roundKeepADecimal(currentValue);
    }

    /**
     * 计算最近一个刻度线的位置(left)
     *
     * @param currentDragLeft
     * @return
     */
    public int calculateLatestTick(int currentDragLeft) {
        int finalLeft = 0;
        int position = Math.round((Math.abs(currentDragLeft)) / mGapDistance);
        finalLeft = (int) (position * mGapDistance + 0.5);
        Log.d(TAG, "calculateLatestTick: currentDragLeft: " + currentDragLeft + ", position: " + position + ", finalLeft: " + finalLeft);
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

    /**
     * 画大小刻度线 以及大刻度线对应的文字
     *
     * @param canvas
     */
    private void drawScaleLineAndText(Canvas canvas) {
        boolean isLongScale;
        float currentX = mOriginalWidth * 0.5f;
        float startY;
        float stopY;
        Paint paint;
        String textStr;
        float textLeft;
        float textBottom;
        Rect rectText = new Rect();
        boolean isTypeTop = mScaleLineType == TYPE_TOP;
        for (int i = 0; i <= mTotalGrid; i++) {
            isLongScale = (i - mGridOffset) % mGridGapNumber == 0;
            if (isTypeTop) {
                startY = 0;
                stopY = isLongScale ? mLongScaleLineHeight : mShortScaleLineHeight;
                startY += mZeroLienWidth;
                stopY += mZeroLienWidth;
            } else {
                startY = isLongScale ? mHeight - mLongScaleLineHeight : mHeight - mShortScaleLineHeight;
                stopY = mHeight;
                startY -= mZeroLienWidth;
                stopY -= mZeroLienWidth;
            }
            paint = isLongScale ? mLongScaleLinePaint : mShortScaleLinePaint;
            canvas.drawLine(currentX, startY, currentX, stopY, paint);

            textStr = String.valueOf(mMinValue + mGridGapValue * i);
            float measureTextWidth = mScaleTextPaint.measureText(textStr, 0, textStr.length());
            textLeft = currentX - measureTextWidth * 0.5f;

            mScaleTextPaint.getTextBounds(textStr, 0, textStr.length(), rectText);
            if (isTypeTop) {
                textBottom = mHeight - mTextMargin;
            } else {
                textBottom = mTextMargin + rectText.height();
            }
            if (isLongScale) {
                canvas.drawText(textStr, textLeft, textBottom, mScaleTextPaint);
            }

            Log.d(TAG, "drawScaleLineAndText: currentX: " + currentX);
            currentX += mGapDistance;
        }
    }


    private void calculateGap() {
        mTotalGrid = (int) Math.ceil((mMaxValue - mMinValue) / mGridGapValue);
        Log.d(TAG, "calculateGap: mTotalGrid: " + mTotalGrid + ", mGapDistance: " + mGapDistance);
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
        Log.d(TAG, "measureWidth: originWidth: size: " + size);
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
    public void setMinValue(float minValue) {
        mMinValue = minValue;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 卷尺显示的最大值
     *
     * @param maxValue
     */
    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 1大格包含多少小格
     *
     * @param gridGapNumber
     */
    public void setGridGapNumber(int gridGapNumber) {
        mGridGapNumber = gridGapNumber;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 每一小格的单位是多少(每一小格的值是多少)
     *
     * @param gridGapValue
     */
    public void setGridGapValue(float gridGapValue) {
        mGridGapValue = gridGapValue;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 第一个大格的位置
     * 第一个大格出现在第几个小格的位置(有可能会有最小值不是大格的情况)
     *
     * @param gridOffset
     */
    public void setGridOffset(int gridOffset) {
        mGridOffset = gridOffset;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 长刻度线的宽度
     *
     * @param longScaleLineWidth
     */
    public void setLongScaleLineWidth(int longScaleLineWidth) {
        mLongScaleLineWidth = longScaleLineWidth;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 长刻度线的高度
     *
     * @param longScaleLineHeight
     */
    public void setLongScaleLineHeight(int longScaleLineHeight) {
        mLongScaleLineHeight = longScaleLineHeight;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 长刻度线的颜色
     *
     * @param longScaleLineColor
     */
    public void setLongScaleLineColor(int longScaleLineColor) {
        mLongScaleLineColor = longScaleLineColor;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 短刻度线的宽度
     *
     * @param shortScaleLineWidth
     */
    public void setShortScaleLineWidth(int shortScaleLineWidth) {
        mShortScaleLineWidth = shortScaleLineWidth;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 短刻度线的高度
     *
     * @param shortScaleLineHeight
     */
    public void setShortScaleLineHeight(int shortScaleLineHeight) {
        mShortScaleLineHeight = shortScaleLineHeight;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 短刻度线的颜色
     *
     * @param shortScaleLineColor
     */
    public void setShortScaleLineColor(int shortScaleLineColor) {
        mShortScaleLineColor = shortScaleLineColor;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 基线的宽度
     *
     * @param zeroLienWidth
     */
    public void setZeroLienWidth(int zeroLienWidth) {
        mZeroLienWidth = zeroLienWidth;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 基线的颜色
     *
     * @param zeroLineColor
     */
    public void setZeroLineColor(int zeroLineColor) {
        mZeroLineColor = zeroLineColor;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 尺子的背景色
     *
     * @param rulerScaleBackgroundColor
     */
    public void setRulerScaleBackgroundColor(int rulerScaleBackgroundColor) {
        mRulerScaleBackgroundColor = rulerScaleBackgroundColor;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 每一小格的像素距离
     *
     * @param gapDistance
     */
    public void setGapDistance(float gapDistance) {
        mGapDistance = gapDistance;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 文字大小
     *
     * @param scaleTextSize
     */
    public void setScaleTextSize(float scaleTextSize) {
        mScaleTextSize = scaleTextSize;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setScaleTextColor(int scaleTextColor) {
        mScaleTextColor = scaleTextColor;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 文字颜色
     *
     * @param textMargin
     */
    public void setTextMagin(float textMargin) {
        mTextMargin = textMargin;
        ViewCompat.postInvalidateOnAnimation(this);
    }
}

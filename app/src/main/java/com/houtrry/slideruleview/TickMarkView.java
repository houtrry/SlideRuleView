package com.houtrry.slideruleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author: houtrry
 * @time: 2017/10/17 19:06
 * @version: $Rev$
 * @desc: 滑动尺的标准线--中间的那条标记线
 */

public class TickMarkView extends View {

    private static final String TAG = TickMarkView.class.getSimpleName();

    /**
     * 标记线的颜色
     */
    private int mTickMarkColor = Color.parseColor("#ff00ddff");
    /**
     * 标记线的宽度
     */
    private float mTickMarkWidth = 10;
    /**
     * 标记线的高度
     */
    private float mTickMarkHeight = 50;
    private Paint mTickMarkPaint;
    private int mWidth;
    private int mHeight;

    public TickMarkView(Context context) {
        this(context, null);
    }

    public TickMarkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickMarkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideRuleView);

        int tickColor = typedArray.getColor(R.styleable.SlideRuleView_tick_line_color, Color.RED);
        float tickWidth = typedArray.getDimension(R.styleable.SlideRuleView_tick_line_width, 10);
        float tickHeight = typedArray.getDimension(R.styleable.SlideRuleView_tick_line_height, 50);

        mTickMarkColor = tickColor;
        mTickMarkWidth = tickWidth;
        mTickMarkHeight = tickHeight;
        Log.d(TAG, "initAttrs: ---------------------------------------------TickMarkView----start------------------------------------------------");
        Log.d(TAG, "initAttrs: TickMarkView, tickColor: "+tickColor+", tickWidth: "+tickWidth+", tickHeight: "+tickHeight);
        Log.d(TAG, "initAttrs: ---------------------------------------------TickMarkView----end------------------------------------------------");

        typedArray.recycle();
    }

    private void initPaint(Context context) {
        mTickMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickMarkPaint.setColor(mTickMarkColor);
        mTickMarkPaint.setStrokeWidth(mTickMarkWidth);
        mTickMarkPaint.setStrokeCap(Paint.Cap.ROUND);
        mTickMarkPaint.setStyle(Paint.Style.STROKE);

        Log.d(TAG, "initPaint: mTickMarkColor: "+mTickMarkColor+", mTickMarkWidth: "+mTickMarkWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) mTickMarkWidth, (int) (mTickMarkHeight));
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
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawLine(mWidth * 0.5f, 0, mWidth * 0.5f, mTickMarkHeight - mTickMarkWidth*0.5f, mTickMarkPaint);
        Log.d(TAG, "onDraw: mTickMarkWidth: "+mTickMarkWidth+", color: "+mTickMarkPaint.getColor());
    }

    /**
     * 标记线的颜色
     * @param tickMarkColor
     */
    public TickMarkView setTickMarkColor(int tickMarkColor) {
        Log.d(TAG, "setTickMarkColor: tickMarkColor: "+tickMarkColor+", mTickMarkColor: "+mTickMarkColor);
        mTickMarkColor = tickMarkColor;
        mTickMarkPaint.setColor(mTickMarkColor);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 标记线的宽度
     * @param tickMarkWidth
     */
    public TickMarkView setTickMarkWidth(float tickMarkWidth) {
        Log.d(TAG, "setTickMarkWidth: tickMarkWidth: "+tickMarkWidth);
        mTickMarkWidth = tickMarkWidth;
        mTickMarkPaint.setStrokeWidth(mTickMarkWidth);
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }

    /**
     * 标记线的高度
     * @param tickMarkHeight
     */
    public TickMarkView setTickMarkHeight(float tickMarkHeight) {
        Log.d(TAG, "setTickMarkHeight: tickMarkHeight: "+tickMarkHeight);
        mTickMarkHeight = tickMarkHeight;
        ViewCompat.postInvalidateOnAnimation(this);
        return this;
    }
}

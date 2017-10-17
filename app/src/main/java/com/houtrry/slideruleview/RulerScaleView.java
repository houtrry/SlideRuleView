package com.houtrry.slideruleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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
     *
     */
    private int mRulerScaleColor = Color.parseColor("#33999999");
    /**
     *
     */
    private int mRulerScaleBackgroundColor = Color.WHITE;
    /**
     *
     */
    private int mRulerScaleSideColor = Color.GRAY;


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
        initPaint();
    }

    private void initPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mRulerScaleColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mRulerScaleBackgroundColor);


    }

}

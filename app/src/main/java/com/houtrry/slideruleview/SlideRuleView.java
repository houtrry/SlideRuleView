package com.houtrry.slideruleview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 80011433 on 2017/10/17.
 */

public class SlideRuleView extends View {

    private static final String TAG = SlideRuleView.class.getSimpleName();

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

    private void init(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

package com.houtrry.slideruleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.houtrry.slideruleview.R.id.slideRuleView;

/**
 * @author houtrry
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.tickMarkView)
    TickMarkView mTickMarkView;
    @BindView(R.id.rulerScaleView)
    RulerScaleView mRulerScaleView;
    @BindView(slideRuleView)
    SlideRuleView mSlideRuleView;
    @BindView(R.id.textView)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSlideRuleView.setSlideRuleListener(new SlideRuleListener() {
            @Override
            public void slideRule(float value) {
                mTextView.setText(String.valueOf(value));
            }
        });
    }

    @OnClick({R.id.btn})
    public void click(View view) {
        float value = (float) (30.0 + Math.random() * 50f);
        mSlideRuleView.setSlideValue(value);

        Log.d(TAG, "click: value: "+value);
    }
}

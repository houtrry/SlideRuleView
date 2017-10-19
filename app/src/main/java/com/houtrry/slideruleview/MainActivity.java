package com.houtrry.slideruleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * @author houtrry
 */
public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlideRuleView slideRuleView = (SlideRuleView) findViewById(R.id.slideRuleView);
        mTextView = (TextView) findViewById(R.id.textView);
        slideRuleView.setSlideRuleListener(new SlideRuleListener() {
            @Override
            public void slideRule(float value) {
                mTextView.setText(String.valueOf(value));
            }
        });
    }
}

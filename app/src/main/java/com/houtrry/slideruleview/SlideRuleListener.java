package com.houtrry.slideruleview;

/**
 * @author: houtrry
 * @time: 2017/10/19.
 * @desc:
 */

public interface SlideRuleListener {
    /**
     * 监听当前滑动的位置指向的具体的值
     *
     * @param value 当前的值
     */
    void slideRule(float value);
}

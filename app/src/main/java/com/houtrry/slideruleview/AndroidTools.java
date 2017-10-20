package com.houtrry.slideruleview;

import java.math.BigDecimal;

/**
 * @author: houtrry
 * @time: 2017/10/20 15:48
 * @version: $Rev$
 * @desc: 工具类
 */

public class AndroidTools {

    /**
     * 四舍五入, 保留一位小数
     *
     * @param value
     * @return
     */
    public static float roundKeepADecimal(float value) {
        final BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}

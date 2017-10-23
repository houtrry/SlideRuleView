package com.houtrry.slideruleview;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
        return roundKeepDecimal(value, 1);
    }

    /**
     * 四舍五入, 保留几位小数
     *
     * @param value   四舍五入处理前的值
     * @param decimal 小数的位数
     * @return
     */
    public static float roundKeepDecimal(float value, int decimal) {
        final BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(decimal, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 四舍五入, 保留一位小数
     *
     * @param value
     * @return
     */
    public static String roundKeepADecimalString(float value) {
        return roundKeepDecimalString(value, 1);
    }

    private static final String[] DECIMAL_FORMAT_STRINGS = {"#", "#.0", "#.00", "#.000", "#.0000", "#.00000", "#.000000"};

    /**
     * 四舍五入, 保留几位小数
     *
     * @param value
     * @param decimal
     * @return
     */
    public static String roundKeepDecimalString(float value, int decimal) {
        if (decimal >= DECIMAL_FORMAT_STRINGS.length) {
            throw new IllegalArgumentException("now, decimal is only support 0-"+(DECIMAL_FORMAT_STRINGS.length - 1));
        }
        return roundKeepDecimalString(value, DECIMAL_FORMAT_STRINGS[decimal]);
    }

    /**
     * 四舍五入, 以指定格式
     *
     * @param value
     * @param format 格式, 比如"#.0"表示保留一位小数
     * @return
     */
    public static String roundKeepDecimalString(float value, String format) {
        final DecimalFormat df = new DecimalFormat(format);
        return df.format(value);
    }


}

package org.apdoer.trade.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author apdoer
 */
public class NumberUtil {
    private static final BigDecimal POWER18 = new BigDecimal("1000000000000000000");
    private static final int SCALE = 18;

    private NumberUtil() {
    }

    public static String toBig(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        } else {
            BigDecimal ds = new BigDecimal(s);
            ds = ds.multiply(POWER18);
            return ds.stripTrailingZeros().toPlainString();
        }
    }

    public static String toSmall(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        } else {
            BigDecimal ds = new BigDecimal(s);
            ds = ds.divide(POWER18, 18, 4);
            return ds.stripTrailingZeros().toPlainString();
        }
    }
}
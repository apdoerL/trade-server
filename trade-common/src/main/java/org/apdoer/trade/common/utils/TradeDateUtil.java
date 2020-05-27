package org.apdoer.trade.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author apdoer
 */
public class TradeDateUtil {

    private TradeDateUtil() {
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");


    public static String formatMonth(Long timestamp) {
        return dateFormat.format(timestamp);
    }


    public static String formatMonth(Date date) {
        return dateFormat.format(date);
    }
}
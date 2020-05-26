package org.apdoer.trade.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apdoer
 */
@AllArgsConstructor
@Getter
public enum PosiSideEnum {
    /**
     * 持仓方向
     */
    BUY(1, "多头"),
    SELL(-1, "空头"),
    ;



    private int code;
    private String msg;

    public int getCode() {
        return code;
    }



    public static boolean isValid(Integer posiSide) {
        for (PosiSideEnum sideEnum : PosiSideEnum.values()) {
            if (sideEnum.getCode() == posiSide) {
                return true;
            }
        }
        return false;
    }
}
package org.apdoer.trade.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apdoer
 */

@AllArgsConstructor
@Getter
public enum CloseTypeEnum {
    /**
     * 平仓类型
     */
    USER_CLOSE(2, "用户正常平仓"),
    STOP_PROFIT_CLOSE(3, "触发止盈平仓"),
    FORCE_CLOSE(4, "强制平仓"),
    STOP_LOSS_CLOSE(5, "触发止损平仓")
    ;



    private int code;
    private String msg;


    public static boolean isValid(Integer closeType) {
        for (CloseTypeEnum typeEnum : CloseTypeEnum.values()) {
            if (typeEnum.getCode() == closeType) {
                return true;
            }
        }
        return false;
    }
}
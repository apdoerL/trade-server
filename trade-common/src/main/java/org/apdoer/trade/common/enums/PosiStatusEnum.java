package org.apdoer.trade.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PosiStatusEnum {
    UN_CLOSE(1, "未平仓"),
    USER_CLOSE(2, "用户正常平仓"),
    STOP_PROFIT_CLOSE(3, "触发止盈平仓"),
    FORCE_CLOSE(4, "强制平仓"),
    STOP_LOSS_CLOSE(5, "触发止损平仓")
    ;

    private int code;
    private String msg;

}
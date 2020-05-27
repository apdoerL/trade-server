package org.apdoer.trade.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatusEnum {

    /**
     * 从数据区数据排序规则
     */
    WAIT(1, "待触发"),
    TRIGGER(2, "已触发"),
    CANCEL(3, "撤销")
    ;



    private int code;
    private String msg;


    public static boolean isValid(Integer closeType) {
        for (OrderStatusEnum typeEnum : OrderStatusEnum.values()) {
            if (typeEnum.getCode() == closeType) {
                return true;
            }
        }
        return false;
    }
}
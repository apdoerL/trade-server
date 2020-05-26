package org.apdoer.trade.common.enums;


/**
 * 从数据区排序规则
 *
 * @author apdoer
 */

public enum SlaveDataSortTypeEnum {

    /**
     * 排序
     */
    ASC(1, "正序"),
    DESC(2, "反序");

    SlaveDataSortTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean isValid(Integer closeType) {
        for (SlaveDataSortTypeEnum typeEnum : SlaveDataSortTypeEnum.values()) {
            if (typeEnum.getCode() == closeType) {
                return true;
            }
        }
        return false;
    }
}

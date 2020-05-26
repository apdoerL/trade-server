package org.apdoer.trade.quot.source;

import org.apache.commons.lang3.StringUtils;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 10:56
 */
public enum SourceType {
    /**
     * 数据源类型
     */
    KAFKA,
    ZMQ,
    HTTP,
    DB,
    REDIS,
    CACHE;

    public static Boolean vaild(String o) {
        if (StringUtils.isBlank(o)) {
            return false;
        }
        for (SourceType type : SourceType.values()) {
            if (type.name().equalsIgnoreCase(o)) {
                return true;
            }
        }
        return false;
    }
}

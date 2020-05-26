package org.apdoer.trade.quot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apdoer
 */
@Getter
@AllArgsConstructor
public enum QuotMessageTypeEnum {

    /**
     * 最新指数价
     */
    INDEX_PRICE(1001, "最新指数价格");

    private int code;
    private String msg;


}

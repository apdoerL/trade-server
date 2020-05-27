package org.apdoer.trade.common.service;

/**
 * 下单币种初始化
 * @author apdoer
 */
public interface OrderCurrencyInitService {
    void init();

    void flush();
}
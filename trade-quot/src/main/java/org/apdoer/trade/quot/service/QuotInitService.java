package org.apdoer.trade.quot.service;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:17
 */
public interface QuotInitService {

    /**
     * 整体初始化
     */
    void init();

    /**
     * 刷新渠道
     */
    void flush();
}

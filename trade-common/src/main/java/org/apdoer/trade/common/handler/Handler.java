package org.apdoer.trade.common.handler;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 15:28
 */
public interface Handler<DATA> {

    /**
     * 处理
     * @param data
     */
    void handle(DATA data);
}

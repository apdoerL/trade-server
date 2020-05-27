package org.apdoer.trade.core.service;

/**
 * 核心模块初始化
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 11:22
 */
public interface CoreInitService {


    /**
     * 初始化数据通道及数据监听器
     */
    void init();

    /**
     * 刷新数据通道及数据监听器
     */
    void flush();

}

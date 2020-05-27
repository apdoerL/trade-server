package org.apdoer.trade.monitor.service;


/**
 * 监控模块初始化
 *
 * @author apdoer
 */
public interface MonitorInitService {

    /**
     * 初始化
     */
    void init();

    /**
     * 刷新通道及监听器
     */
    void flush();
}

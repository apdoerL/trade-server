package org.apdoer.trade.job.service;

/**
 * 任务初始化
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 11:15
 */
public interface InitService {

    /**
     * job 启动时，初始化
     */
    void init();

    /**
     * 添加合约时，内部组件刷新（通道、数据监听器刷新）
     */
    void flush();
}

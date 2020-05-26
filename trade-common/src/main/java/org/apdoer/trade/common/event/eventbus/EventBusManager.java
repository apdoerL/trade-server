package org.apdoer.trade.common.event.eventbus;

import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:49
 */
public interface EventBusManager {
    /**
     * 注册监听器-所有通道
     *
     * @param listener
     */
    void subscribe(SourceEventListener listener);

    /**
     * 给指定通道注册监听器
     *
     * @param channelName
     * @param listener
     */
    void subscribe(String channelName, SourceEventListener listener);

    /**
     * 移除所有通道的监听器
     *
     * @param listener
     */
    void unSubscribe(SourceEventListener listener);

    /**
     * 移除指定通道的监听器
     *
     * @param channelName
     * @param listener
     */
    void unSubscribe(String channelName, SourceEventListener listener);

    /**
     * 给指定通道发布事件
     *
     * @param channelName
     * @param event
     */
    void publish(String channelName, SourceEvent event);


    /**
     * 构建EventBus
     *
     * @param channel 数据通道名称
     */
    EventBus buildGuavaEventBus(String channelName);

    /**
     * 构建eventbus
     *
     * @param channelName
     * @param backPressureSize 数据通道反压阈值
     * @return
     */
    EventBus buildGuavaEventBus(String channelName, int backPressureSize);

    /**
     * 构建event bus
     *
     * @param channelName      通道名称
     * @param backpressureSize 数据通道内部反压阈值
     * @param corePoolSize     通道内部线程池 core
     * @param maxPoolSize      通道内部线程池 max
     * @return
     */
    EventBus buildGuavaEventBus(String channelName, int backpressureSize, int corePoolSize, int maxPoolSize);


    EventBus buildGuavaEventBus(String channelName, int corePoolSize, int maxPoolSize, int backPressureSize,int initCapacity,long keepAlive);

    /**
     * 指定通道是否已经存在
     *
     * @param channelName
     * @return
     */
    boolean isExist(String channelName);

    /**
     * 获取指定通道
     *
     * @param channelName
     * @return
     */
    EventBus getEventBus(String channelName);

    /**
     * 关闭所有通道
     */
    void shutdown();
}

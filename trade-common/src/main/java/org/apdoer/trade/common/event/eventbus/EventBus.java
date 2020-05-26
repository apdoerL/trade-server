package org.apdoer.trade.common.event.eventbus;

import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:49
 */
public interface EventBus {


    /**
     * 注册消息监听器
     *
     * @param sourceEventListener
     */
    void subscribe(SourceEventListener sourceEventListener);

    /**
     * 移除消息监听器
     *
     * @param sourceEventListener
     */
    void unSubscribe(SourceEventListener sourceEventListener);

    /**
     * 发送数据
     *
     * @param sourceEvent
     */
    void publish(SourceEvent sourceEvent);

    void shutdown();

    String getName();


}

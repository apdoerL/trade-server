package org.apdoer.trade.common.event.listener;

import org.apdoer.trade.common.event.SourceEvent;

import java.util.Set;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:50
 */
public interface SourceEventListener {
    /**
     * 监听数据
     *
     * @param event
     */
    void listen(SourceEvent event);

    /**
     * 获取当前已绑定渠道
     *
     * @return
     */
    Set<String> getSubscribeSystemChannels();

    /**
     * 判断当前channel是否已经绑定
     *
     * @param channel
     * @return
     */
    boolean isSubscribeSystemChannel(String channel);

    /**
     * 添加已监听的channel
     *
     * @param channel
     */
    void subScribeSystemChannel(String channel);
}

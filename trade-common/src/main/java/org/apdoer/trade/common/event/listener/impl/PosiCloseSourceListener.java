package org.apdoer.trade.common.event.listener.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.handler.PosiCloseSyncHandler;
import org.apdoer.trade.common.model.dto.PosiCloseSyncDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 平仓数据监听器
 *
 * @author apdoer
 */
@Service("posiCloseSourceListener")
@Scope("prototype")
public class PosiCloseSourceListener implements SourceEventListener {

    private PosiCloseSyncHandler handler;


    // 已监听的内部数据通道名称
    private Set<String> subscribeChannels = new HashSet<>();

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void listen(SourceEvent event) {
        if (null != event && null != event.getData()) {
            Object data = event.getData();
            if (data instanceof PosiCloseSyncDto) {
                this.handler.handle((PosiCloseSyncDto) data);
            }
        }
    }

    @Override
    public Set<String> getSubscribeSystemChannels() {
        return this.subscribeChannels;
    }

    @Override
    public boolean isSubscribeSystemChannel(String channel) {
        return this.subscribeChannels.contains(channel);
    }

    @Override
    public void subScribeSystemChannel(String channel) {
        if (!this.isSubscribeSystemChannel(channel)) {
            this.subscribeChannels.add(channel);
        }
    }

    public void setHandler(PosiCloseSyncHandler handler) {
        this.handler = handler;
    }
}

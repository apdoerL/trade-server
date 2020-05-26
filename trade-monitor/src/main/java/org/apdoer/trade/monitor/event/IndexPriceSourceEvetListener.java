package org.apdoer.trade.monitor.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.apdoer.trade.monitor.handler.IndexHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 15:33
 */
@Service("indexPriceSourceEvetListener")
@Scope("prototype")
public class IndexPriceSourceEvetListener implements SourceEventListener {

    private IndexHandler indexHandler;

    // 已监听的内部数据通道名称
    private Set<String> subscribeChannels = new HashSet<>();


    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void listen(SourceEvent event) {
        if (null != event && null != event.getData()) {
            Object data = event.getData();
            if (data instanceof IndexPriceDto) {
                this.indexHandler.handle((IndexPriceDto) data);
            }
        }
    }

    @Override
    public Set<String> getSubscribeSystemChannels() {
        return subscribeChannels;
    }

    @Override
    public boolean isSubscribeSystemChannel(String channel) {
        return subscribeChannels.contains(channel);
    }

    @Override
    public void subScribeSystemChannel(String channel) {
        if (!this.isSubscribeSystemChannel(channel)) {
            this.subscribeChannels.add(channel);
        }
    }
    public void setIndexHandler(IndexHandler indexHandler) {
        this.indexHandler = indexHandler;
    }
}

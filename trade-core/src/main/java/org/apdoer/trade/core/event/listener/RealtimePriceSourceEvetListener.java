package org.apdoer.trade.core.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.QuotData;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 最新成交价数据监听器-更新最新成交价
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 16:50
 */
@Slf4j
@Service("realtimePriceSourceEvetListener")
@Scope("prototype")
public class RealtimePriceSourceEvetListener implements SourceEventListener {

    // 已监听的内部数据通道名称
    private Set<String> subscribeChannels = new HashSet<>();

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void listen(SourceEvent event) {
        if (event != null && event.getData() != null) {
            Object data = event.getData();
            if (data instanceof IndexPriceDto) {
                IndexPriceDto priceDto = (IndexPriceDto) data;
                //实时行情写入内部缓存
                QuotData.add(priceDto.getContractId(), priceDto.getIndexPrice());
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
}

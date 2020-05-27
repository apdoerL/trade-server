package org.apdoer.trade.core.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;
import org.apdoer.trade.core.thread.factory.impl.PosiRunnableFactory;
import org.apdoer.trade.core.thread.pool.ClosePosiHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 平仓数据监听器-用于发送到平仓线程池
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 16:55
 */
@Slf4j
@Service("posiCloseSourceEvetListener")
@Scope("prototype")
public class PosiCloseSourceEvetListener implements SourceEventListener {


    private Set<String> subscribeChannels = new HashSet<>();

    @Autowired
    private PosiRunnableFactory posiRunnableFactory;


    @Autowired
    private ClosePosiHandleThreadPool closePosiHandleThreadPool;

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void listen(SourceEvent event) {
        if (null != event && event.getData() != null) {
            Object data = event.getData();
            //如果是平仓请求
            if (data instanceof OrderCloseReqVo) {
                sendClosePosiHandleQueue(((OrderCloseReqVo) data));
            }
        }
    }

    private void sendClosePosiHandleQueue(OrderCloseReqVo data) {
        try {
            while (closePosiHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
            }
            closePosiHandleThreadPool.execute(posiRunnableFactory.newOrderCloseInstance(data));
        } catch (Exception e) {
            log.error(" posi close handle task submit error", e);
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

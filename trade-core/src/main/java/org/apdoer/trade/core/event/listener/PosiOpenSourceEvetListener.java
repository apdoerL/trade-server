package org.apdoer.trade.core.event.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.model.dto.ConditionOrderOpenDto;
import org.apdoer.trade.core.thread.factory.impl.PosiRunnableFactory;
import org.apdoer.trade.core.thread.pool.OpenPosiHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 18:37
 */
@Slf4j
@Service("posiOpenSourceEvetListener")
@Scope("prototype")
public class PosiOpenSourceEvetListener implements SourceEventListener {

    @Autowired
    private OpenPosiHandleThreadPool openPosiHandleThreadPool;

    @Autowired
    private PosiRunnableFactory posiRunnableFactory;

    private Set<String> subscribeChannels = new HashSet<>();


    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void listen(SourceEvent event) {
        if (null != event && event.getData() != null) {
            Object data = event.getData();
            if (data instanceof ConditionOrderOpenDto) {
                sendOpenPosiHandleQueue(((ConditionOrderOpenDto) data));
            }
        }
    }

    private void sendOpenPosiHandleQueue(ConditionOrderOpenDto data) {
        try {
            while (openPosiHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
            }
            openPosiHandleThreadPool.execute(posiRunnableFactory.newOrderOpenInstance(data));
        } catch (Exception e) {
            log.error("posi open handle task submit error", e);
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
        if (!isSubscribeSystemChannel(channel)) {
            subscribeChannels.add(channel);
        }
    }
}

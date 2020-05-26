package org.apdoer.trade.monitor.event;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.SpringBeanUtils;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.utils.HandlerNameUitls;
import org.apdoer.trade.monitor.enums.IndexHandleTypeEnum;
import org.apdoer.trade.monitor.handler.IndexHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author apdoer
 */
@Slf4j
@Component
public class IndexPriceListenerManager {

    @Autowired
    private SpringBeanUtils springBeanUtils;

    private Map<String, SourceEventListener> listenerManagerMap = new ConcurrentHashMap<String, SourceEventListener>();


    public SourceEventListener buildIndexPriceListener(String listenerName, IndexHandleTypeEnum type) {
        if (!this.listenerManagerMap.containsKey(listenerName)) {
            IndexPriceSourceEvetListener listener = this.springBeanUtils.getBean("indexPriceSourceEvetListener", IndexPriceSourceEvetListener.class);
            String handlerName = HandlerNameUitls.buildIndexMonitorHandleName(type.name());
            IndexHandler handler = this.springBeanUtils.getBean(handlerName, IndexHandler.class);
            listener.setIndexHandler(handler);
            this.listenerManagerMap.put(listenerName, listener);
            log.info("build indexPriceListener={} succ", listenerName);
            return listener;
        } else {
            return this.listenerManagerMap.get(listenerName);
        }
    }

    public SourceEventListener getSourceEventListener(String channel) {
        return this.listenerManagerMap.get(channel);
    }

    public boolean contains(String channel) {
        return this.listenerManagerMap.containsKey(channel);
    }
}

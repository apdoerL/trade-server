package org.apdoer.trade.core.event.manager;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.SpringBeanUtils;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.core.event.listener.PosiCloseSourceEvetListener;
import org.apdoer.trade.core.event.listener.PosiOpenSourceEvetListener;
import org.apdoer.trade.core.event.listener.RealtimePriceSourceEvetListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * eventbus 数据监听器管理者
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 11:27
 */
@Slf4j
@Component
public class TradeDataListenerManager {

    @Autowired
    private SpringBeanUtils springBeanUtils;

    private Map<String, SourceEventListener> listenerMap = new ConcurrentHashMap<>();


    public SourceEventListener buildRealtimePriceListener(String name) {
        if (!listenerMap.containsKey(name)) {
            RealtimePriceSourceEvetListener listener = this.springBeanUtils.getBean("realtimePriceSourceEvetListener", RealtimePriceSourceEvetListener.class);
            this.listenerMap.put(name, listener);
            log.info("build RealtimePriceListener={} succ", name);
            return listener;
        } else {
            return listenerMap.get(name);
        }
    }


    public SourceEventListener buildPosiFlatListener(String name) {
        if (!listenerMap.containsKey(name)) {
            PosiCloseSourceEvetListener listener = this.springBeanUtils.getBean("posiCloseSourceEvetListener", PosiCloseSourceEvetListener.class);
            this.listenerMap.put(name, listener);
            log.info("build PosiFlatListener={} succ", name);
            return listener;
        } else {
            return listenerMap.get(name);
        }
    }

    public SourceEventListener buildPosiOpenListener(String name) {
        if (!listenerMap.containsKey(name)) {
            PosiOpenSourceEvetListener listener = this.springBeanUtils.getBean("posiOpenSourceEvetListener",
                    PosiOpenSourceEvetListener.class);
            this.listenerMap.put(name, listener);
            log.info("build PosiOpenListener={} succ", name);
            return listener;
        } else {
            return listenerMap.get(name);
        }
    }


}

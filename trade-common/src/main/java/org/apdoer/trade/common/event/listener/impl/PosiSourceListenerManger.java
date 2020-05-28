package org.apdoer.trade.common.event.listener.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.SpringBeanUtils;
import org.apdoer.trade.common.enums.PosiSyncTypeEnum;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.handler.PosiCloseSyncHandler;
import org.apdoer.trade.common.handler.PosiOpenSyncHandler;
import org.apdoer.trade.common.utils.HandlerNameUitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持仓数据更新监听器管理
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/28 10:29
 */
@Slf4j
@Component
public class PosiSourceListenerManger {

    @Autowired
    private SpringBeanUtils springBeanUtils;

    private Map<String, SourceEventListener> listenerManagerMap = new ConcurrentHashMap<>();


    public SourceEventListener buildPosiOpenSyncListener(String listenerName, PosiSyncTypeEnum type) {
        if (!listenerManagerMap.containsKey(listenerName)) {
            PosiOpenSourceListener posiOpenSourceListener = springBeanUtils.getBean("posiOpenSourceListener", PosiOpenSourceListener.class);
            String handlerName = HandlerNameUitls.buildPosiOpenSyncHandleName(type.name());
            PosiOpenSyncHandler openSyncHandler = springBeanUtils.getBean(handlerName, PosiOpenSyncHandler.class);
            posiOpenSourceListener.setHandler(openSyncHandler);
            listenerManagerMap.put(listenerName,posiOpenSourceListener);
            log.info("build posiOpenSyncListener={} succ", listenerName);
            return posiOpenSourceListener;
        } else {
            return listenerManagerMap.get(listenerName);
        }
    }

    public SourceEventListener buildPosiCloseSyncListener(String listenerName, PosiSyncTypeEnum type) {
        if (!this.listenerManagerMap.containsKey(listenerName)) {
            PosiCloseSourceListener listener = this.springBeanUtils.getBean("posiCloseSourceListener", PosiCloseSourceListener.class);
            String handlerName = HandlerNameUitls.buildPosiCloseSyncHandleName(type.name());
            PosiCloseSyncHandler handler = this.springBeanUtils.getBean(handlerName, PosiCloseSyncHandler.class);
            listener.setHandler(handler);
            this.listenerManagerMap.put(listenerName, listener);
            log.info("build posiCloseSyncListener={} succ", listenerName);
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

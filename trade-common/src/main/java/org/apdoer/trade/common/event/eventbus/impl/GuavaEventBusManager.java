package org.apdoer.trade.common.event.eventbus.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.eventbus.EventBus;
import org.apdoer.trade.common.event.eventbus.EventBusManager;
import org.apdoer.trade.common.event.listener.SourceEventListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * eventbus 内部通道管理
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 17:11
 */
@Slf4j
public class GuavaEventBusManager implements EventBusManager {
    private Map<String, EventBus> eventBusMap = new ConcurrentHashMap<>();


    private GuavaEventBusManager() {
    }

    private static class InnerGuavaEventBusManager {
        private static final GuavaEventBusManager INSTANCE = new GuavaEventBusManager();
    }

    public static GuavaEventBusManager getInstance() {
        return InnerGuavaEventBusManager.INSTANCE;
    }


    @Override
    public void subscribe(SourceEventListener listener) {
        for (Map.Entry<String, EventBus> entry : eventBusMap.entrySet()) {
            entry.getValue().subscribe(listener);
        }
    }

    @Override
    public void subscribe(String channelName, SourceEventListener listener) {
        if (eventBusMap.containsKey(channelName)) {
            eventBusMap.get(channelName).subscribe(listener);
        }
    }

    @Override
    public void unSubscribe(SourceEventListener listener) {
        for (Map.Entry<String, EventBus> entry : eventBusMap.entrySet()) {
            entry.getValue().unSubscribe(listener);
        }
    }

    @Override
    public void unSubscribe(String channelName, SourceEventListener listener) {
        if (eventBusMap.containsKey(channelName)) {
            eventBusMap.get(channelName).unSubscribe(listener);
        }
    }

    @Override
    public void publish(String channelName, SourceEvent event) {
        if (eventBusMap.containsKey(channelName)) {
            eventBusMap.get(channelName).publish(event);
        }
    }

    @Override
    public EventBus buildGuavaEventBus(String channelName) {
        if (eventBusMap.containsKey(channelName)) {
            return eventBusMap.get(channelName);
        }
        EventBus eventBus = new GuavaEventBus(channelName);
        eventBusMap.put(channelName, eventBus);
        return eventBus;
    }

    @Override
    public EventBus buildGuavaEventBus(String channelName, int backPressureSize) {
        if (eventBusMap.containsKey(channelName)) {
            return eventBusMap.get(channelName);
        }
        GuavaEventBus eventBus = new GuavaEventBus(channelName, backPressureSize);
        eventBusMap.put(channelName, eventBus);
        return eventBus;
    }

    @Override
    public EventBus buildGuavaEventBus(String channelName, int backpressureSize, int corePoolSize, int maxPoolSize) {
        if (eventBusMap.containsKey(channelName)) {
            return eventBusMap.get(channelName);
        }
        GuavaEventBus eventBus = new GuavaEventBus(channelName, backpressureSize, corePoolSize, maxPoolSize);
        eventBusMap.put(channelName, eventBus);
        return eventBus;
    }

    @Override
    public EventBus buildGuavaEventBus(String channelName, int corePoolSize, int maxPoolSize, int backPressureSize, int initCapacity, long keepAlive) {
        if (eventBusMap.containsKey(channelName)){
            return eventBusMap.get(channelName);
        }
        GuavaEventBus eventBus = new GuavaEventBus(channelName, corePoolSize, maxPoolSize, backPressureSize, keepAlive, initCapacity);
        log.info("======build guava event bus name:{};coreSize:{};maxSize:{},backPressure:{};initCapacity:{};keepAlive:{}",channelName,corePoolSize,maxPoolSize,backPressureSize,keepAlive,initCapacity);
        eventBusMap.put(channelName,eventBus);
        return eventBus;
    }

    @Override
    public boolean isExist(String channelName) {
        return eventBusMap.containsKey(channelName);
    }

    @Override
    public EventBus getEventBus(String channelName) {
        return eventBusMap.get(channelName);
    }

    @Override
    public void shutdown() {
        for (Map.Entry<String, EventBus> entry : eventBusMap.entrySet()) {
            entry.getValue().shutdown();
        }
    }
}

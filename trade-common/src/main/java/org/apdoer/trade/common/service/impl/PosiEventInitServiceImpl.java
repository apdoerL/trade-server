package org.apdoer.trade.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.config.EventBusThreadPoolProperties;
import org.apdoer.trade.common.constants.CommonConstant;
import org.apdoer.trade.common.enums.PosiSyncTypeEnum;
import org.apdoer.trade.common.event.eventbus.EventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.event.listener.impl.PosiSourceListenerManger;
import org.apdoer.trade.common.service.PosiEventInitService;
import org.apdoer.trade.common.thread.pool.PosiSyncHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PosiEventInitServiceImpl implements PosiEventInitService {

    @Autowired
    private PosiSyncHandleThreadPool posiSyncHandleThreadPool;

    @Autowired
    private PosiSourceListenerManger posiSourceListenerManger;
    @Autowired
    private EventBusThreadPoolProperties properties;

    @Override
    public void init() {
        log.info("==== Posi event init start");
        //主从数据同步线程池初始化
        this.posiSyncHandleThreadPool.init();
        //开仓通道/监听器初始化
        this.PosiOpenEventInit();
        //平仓通道/监听器初始化
        this.PosiCloseEventInit();
        log.info("==== Posi event init end");
    }

    private void PosiOpenEventInit() {
        EventBusThreadPoolProperties.ThreadPoolConfig config = properties.getPosiOpenEventBusConfig();
        GuavaEventBusManager.getInstance().buildGuavaEventBus(CommonConstant.POSI_OPEN_CHANNEL,
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getBackPressureSize(),
                config.getInitCapacity(),
                config.getKeepAlive());
        this.PosiOpenlistenerInit(CommonConstant.POSI_OPEN_CHANNEL, CommonConstant.SLAVE_POSI_OPEN_SYNC_LISTENER, PosiSyncTypeEnum.slave);
        this.PosiOpenlistenerInit(CommonConstant.POSI_OPEN_CHANNEL, CommonConstant.PROFIT_POSI_OPEN_SYNC_LISTENER, PosiSyncTypeEnum.stop_profit);
        this.PosiOpenlistenerInit(CommonConstant.POSI_OPEN_CHANNEL, CommonConstant.LOSS_POSI_OPEN_SYNC_LISTENER, PosiSyncTypeEnum.stop_Loss);
    }

    private void PosiCloseEventInit() {
        EventBusThreadPoolProperties.ThreadPoolConfig config = properties.getPosiCloseEventBusConfig();
        GuavaEventBusManager.getInstance().buildGuavaEventBus(CommonConstant.POSI_OPEN_CHANNEL,
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getBackPressureSize(),
                config.getInitCapacity(),
                config.getKeepAlive());
        this.PosiCloselistenerInit(CommonConstant.POSI_CLOSE_CHANNEL, CommonConstant.SLAVE_POSI_CLOSE_SYNC_LISTENER, PosiSyncTypeEnum.slave);
        this.PosiCloselistenerInit(CommonConstant.POSI_CLOSE_CHANNEL, CommonConstant.PROFIT_POSI_CLOSE_SYNC_LISTENER, PosiSyncTypeEnum.stop_profit);
        this.PosiCloselistenerInit(CommonConstant.POSI_CLOSE_CHANNEL, CommonConstant.LOSS_POSI_CLOSE_SYNC_LISTENER, PosiSyncTypeEnum.stop_Loss);
    }

    private void PosiOpenlistenerInit(String channelName, String listenerName, PosiSyncTypeEnum type) {
        SourceEventListener listener = this.posiSourceListenerManger.buildPosiOpenSyncListener(listenerName, type);
        // 沒有綁定改通道
        if (!listener.isSubscribeSystemChannel(channelName)) {
            EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(channelName);
            if (null != eventBus) {
                eventBus.subscribe(listener);
                listener.subScribeSystemChannel(channelName);
                log.info("PosiSyncListener={} sub evenbus={} success", listenerName, channelName);
            } else {
                log.error("posi sync listener init, eventBus={} not find", channelName);
            }
        }
    }

    private void PosiCloselistenerInit(String channelName, String listenerName, PosiSyncTypeEnum type) {
        SourceEventListener listener = this.posiSourceListenerManger.buildPosiCloseSyncListener(listenerName, type);
        // 沒有綁定改通道
        if (!listener.isSubscribeSystemChannel(channelName)) {
            EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(channelName);
            if (null != eventBus) {
                eventBus.subscribe(listener);
                listener.subScribeSystemChannel(channelName);
                log.info("PosiSyncListener={} sub evenbus={} success", listenerName, channelName);
            } else {
                log.error("posi sync listener init, eventBus={} not find", channelName);
            }
        }
    }
}

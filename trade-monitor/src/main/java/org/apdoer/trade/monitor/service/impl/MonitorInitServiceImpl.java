package org.apdoer.trade.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.config.EventBusThreadPoolProperties;
import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.monitor.enums.IndexHandleTypeEnum;
import org.apdoer.trade.monitor.event.IndexPriceListenerManager;
import org.apdoer.trade.monitor.service.MonitorInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 10:12
 */
@Slf4j
@Component
public class MonitorInitServiceImpl implements MonitorInitService {
    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    @Autowired
    private IndexPriceListenerManager indexPriceListenerManager;

    @Autowired
    private EventBusThreadPoolProperties eventBusThreadPoolProperties;

    @Override
    public void init() {
        flush();
    }

    @Override
    public void flush() {
        List<ContractChannelMappingPo> mappingList = this.quotConfigCenterService.queryAllMapping();
        if (null == mappingList || mappingList.size() == 0) {
            throw new RuntimeException("channel not config");
        }
        //强平监听器初始化-监听的是行情通道
        strongFlatIndexListenerInit(mappingList);

        //强平通道初始化- 下发通道
        strongFlatChannelInit(mappingList);

        //止盈监听器初始化-监听的是行情通道
        stopProfitIndexListenerInit(mappingList);

        //止盈通道初始化- 下发通道
        stopProfitChannelInit(mappingList);

        //止损监听器初始化-监听的是行情通道
        stopLossIndexListenerInit(mappingList);

        //止损通道初始化- 下发通道
        stopLossChannelInit(mappingList);

        //条件单监听器初始化-监听的是行情通道
        conditionOrderIndexListenerInit(mappingList);

        //条件单通道初始化- 下发通道
        conditionOrderChannelInit(mappingList);
    }

    private void conditionOrderChannelInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====condition order channel init start ");
        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getConditionOrderEventBusConfig();
        for (ContractChannelMappingPo mappingPo : mappingList) {
            GuavaEventBusManager.getInstance().buildGuavaEventBus(
                    mappingPo.getConditionOrderChannel(),
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getBackPressureSize(),
                    config.getInitCapacity(),
                    config.getKeepAlive());
        }
        log.info("====condition order channel init end ");
    }

    private void conditionOrderIndexListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====condition order index Listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = this.indexPriceListenerManager
                    .buildIndexPriceListener(mappingPo.getConditionOrderIndexListener(), IndexHandleTypeEnum.CONDITION_ORDER);
            if (!listener.isSubscribeSystemChannel(mappingPo.getQuotChannel())) {
                GuavaEventBus eventBus = (GuavaEventBus)GuavaEventBusManager.getInstance().getEventBus(mappingPo.getQuotChannel());
                if (null != eventBus) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getQuotChannel());
                    log.info("indexPriceListener={} sub evenbus={} success", mappingPo.getConditionOrderIndexListener(), mappingPo.getQuotChannel());
                } else {
                    log.error("index price listener init ,eventBus={} not find", mappingPo.getQuotChannel());
                }
            }
        }
        log.info("====condition order index Listener init end ");
    }

    private void stopLossChannelInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====stop loss channel init start ");
        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getStopLossEventBusConfig();
        for (ContractChannelMappingPo mappingPo : mappingList) {
            GuavaEventBusManager.getInstance().buildGuavaEventBus(
                    mappingPo.getStopLossChannel(),
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getBackPressureSize(),
                    config.getInitCapacity(),
                    config.getKeepAlive());
        }
        log.info("====stop loss channel init end ");
    }

    private void stopLossIndexListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====stop loss index Listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = this.indexPriceListenerManager
                    .buildIndexPriceListener(mappingPo.getStopProfitIndexListener(), IndexHandleTypeEnum.STOP_LOSS);
            // 沒有綁定改通道
            if (!listener.isSubscribeSystemChannel(mappingPo.getQuotChannel())) {
                GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getQuotChannel());
                if (null != eventBus) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getQuotChannel());
                    log.info("indexPriceListener={} sub evenbus={} success", mappingPo.getStopLossIndexListener(), mappingPo.getQuotChannel());
                } else {
                    log.error("index price listener init ,eventBus={} not find", mappingPo.getQuotChannel());
                }
            }
        }
        log.info("====stop loss index Listener init end ");
    }

    private void stopProfitChannelInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====stop profit channel init start ");
        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getStopProfitEventBusConfig();
        for (ContractChannelMappingPo mappingPo : mappingList) {
            GuavaEventBusManager.getInstance().buildGuavaEventBus(
                    mappingPo.getStopProfitChannel(),
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getBackPressureSize(),
                    config.getInitCapacity(),
                    config.getKeepAlive());
        }
        log.info("====stop profit channel init end ");
    }

    private void stopProfitIndexListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====stop profit index Listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = this.indexPriceListenerManager
                    .buildIndexPriceListener(mappingPo.getStopProfitIndexListener(), IndexHandleTypeEnum.STOP_PROFIT);
            // 沒有綁定改通道
            if (!listener.isSubscribeSystemChannel(mappingPo.getQuotChannel())) {
                GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getQuotChannel());
                if (null != eventBus) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getQuotChannel());
                    log.info("indexPriceListener={} sub evenbus={} success", mappingPo.getStopProfitIndexListener(), mappingPo.getQuotChannel());
                } else {
                    log.error("index price listener init ,eventBus={} not find", mappingPo.getQuotChannel());
                }
            }
        }
        log.info("====stop profit index Listener init end ");
    }

    private void strongFlatChannelInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====strong flat channel init start ");
        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getStopFlatEventBusConfig();
        for (ContractChannelMappingPo mappingPo : mappingList) {
            GuavaEventBusManager.getInstance().buildGuavaEventBus(
                    mappingPo.getFlChannel(),
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getBackPressureSize(),
                    config.getInitCapacity(),
                    config.getKeepAlive());
        }
        log.info("====strong flat channel init end ");
    }

    private void strongFlatIndexListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====strong flat index Listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener sourceEventListener = indexPriceListenerManager.buildIndexPriceListener(mappingPo.getFlListener(), IndexHandleTypeEnum.STRONG_FLAT);
            if (!sourceEventListener.isSubscribeSystemChannel(mappingPo.getFlChannel())) {
                GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getQuotChannel());
                if (eventBus != null) {
                    eventBus.subscribe(sourceEventListener);
                    sourceEventListener.subScribeSystemChannel(mappingPo.getQuotChannel());
                    log.info("indexPriceListener={} sub evenbus={} success", mappingPo.getIndexPriceListener(), mappingPo.getQuotChannel());
                } else {
                    log.error("index price listener init ,eventBus={} not find", mappingPo.getQuotChannel());
                }
            }
        }
        log.info("====strong flat index Listener init end ");
    }
}

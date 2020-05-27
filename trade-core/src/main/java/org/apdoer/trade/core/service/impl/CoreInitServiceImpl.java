package org.apdoer.trade.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.trade.common.event.eventbus.EventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.core.event.manager.TradeDataListenerManager;
import org.apdoer.trade.core.service.CoreInitService;
import org.apdoer.trade.core.thread.pool.ClosePosiHandleThreadPool;
import org.apdoer.trade.core.thread.pool.OpenPosiHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 17:35
 */
@Component
@Slf4j
public class CoreInitServiceImpl implements CoreInitService {

    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    @Autowired
    private TradeDataListenerManager tradeDataListenerManager;

    @Autowired
    private OpenPosiHandleThreadPool openPosiHandleThreadPool;

    @Autowired
    private ClosePosiHandleThreadPool closePosiHandleThreadPool;

    @Override
    public void init() {
        //开仓线程池/平仓线程池初始化
        openPosiHandleThreadPool.init();
        closePosiHandleThreadPool.init();
        flush();
    }

    @Override
    public void flush() {
        List<ContractChannelMappingPo> mappingList = this.quotConfigCenterService.queryAllMapping();
        if (null == mappingList || mappingList.size() == 0) {
            throw new RuntimeException("channel not config");
        }
        // 最新价格监听器初始化-用于将最新价同步到数据区
        realTimePriceListenerInit(mappingList);

        //强平监听器初始化-用于将平仓任务提交到平仓线程池
        strongFlatListenerInit(mappingList);

        //止盈监听器初始化-用于将平仓任务提交到平仓线程池
        stopProfitListenerInit(mappingList);

        //止损监听器初始化-用于将平仓任务提交到平仓线程池
        stopLossListenerInit(mappingList);

        //条件单监听器初始化-用于将开仓任务提交到开仓线程池
        conditionOrderListenerInit(mappingList);
    }

    private void conditionOrderListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====condition order listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = this.tradeDataListenerManager.buildPosiOpenListener(mappingPo.getConditionOrderListener());
            // 沒有綁定改通道
            if (!listener.isSubscribeSystemChannel(mappingPo.getConditionOrderChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance()
                        .getEventBus(mappingPo.getConditionOrderChannel());
                if (null != eventBus) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getConditionOrderChannel());
                    log.info("posiopenListener={} sub evenbus={} success", mappingPo.getConditionOrderListener(), mappingPo.getConditionOrderChannel());
                } else {
                    log.error("condition order listener init ,eventBus={} not find", mappingPo.getConditionOrderChannel());
                }
            }
        }
        log.info("====condition order listener init end ");
    }

    private void stopLossListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====stop Loss listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = this.tradeDataListenerManager.buildPosiFlatListener(mappingPo.getStopLossListener());
            // 沒有綁定改通道
            if (!listener.isSubscribeSystemChannel(mappingPo.getStopLossChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(mappingPo.getStopLossChannel());
                if (null != eventBus) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getStopLossChannel());
                    log.info("posiCloseListener={} sub evenbus={} success", mappingPo.getStopLossListener(), mappingPo.getStopLossChannel());
                } else {
                    log.error("stop Loss listener init ,eventBus={} not find", mappingPo.getStopLossChannel());
                }
            }
        }
        log.info("====stop Loss listener init end ");
    }

    private void stopProfitListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====stop profit listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = tradeDataListenerManager.buildPosiFlatListener(mappingPo.getStopProfitListener());
            if (!listener.isSubscribeSystemChannel(mappingPo.getStopProfitChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(mappingPo.getStopProfitChannel());
                if (eventBus != null) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getStopProfitChannel());
                    log.info("posiCloseListener={} sub evenbus={} success", mappingPo.getStopProfitListener(), mappingPo.getStopProfitChannel());
                } else {
                    log.error("stop profit listener init ,eventBus={} not find", mappingPo.getStopProfitChannel());
                }
            }
        }
        log.info("====stop profit listener init end ");
    }

    private void strongFlatListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====strong flat listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = tradeDataListenerManager.buildPosiFlatListener(mappingPo.getFlListener());
            if (!listener.isSubscribeSystemChannel(mappingPo.getFlChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(mappingPo.getFlChannel());
                if (eventBus != null) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getFlChannel());
                    log.info("posiCloseListener={} sub evenbus={} success", mappingPo.getFlListener(), mappingPo.getFlChannel());
                } else {
                    log.error("strong flat listener init ,eventBus={} not find", mappingPo.getFlChannel());
                }
            }
        }
        log.info("====strong flat listener init end ");
    }

    private void realTimePriceListenerInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====realtime price listener init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            SourceEventListener listener = tradeDataListenerManager.buildRealtimePriceListener(mappingPo.getPriceCacheListener());
            if (!listener.isSubscribeSystemChannel(mappingPo.getQuotChannel())) {
                EventBus eventBus = GuavaEventBusManager.getInstance().getEventBus(mappingPo.getQuotChannel());
                if (eventBus != null) {
                    eventBus.subscribe(listener);
                    listener.subScribeSystemChannel(mappingPo.getQuotChannel());
                    log.info("realtimePriceListener={} sub evenbus={} success", mappingPo.getPriceCacheListener(), mappingPo.getQuotChannel());
                } else {
                    log.error("realtime price listener init ,eventBus={} not find", mappingPo.getQuotChannel());
                }
            }
        }
        log.info("====realtime price listener init end ");
    }
}

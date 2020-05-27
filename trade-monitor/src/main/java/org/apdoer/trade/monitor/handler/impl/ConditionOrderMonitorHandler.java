package org.apdoer.trade.monitor.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.ConditionOrderData;
import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.model.dto.ConditionOrderOpenDto;
import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.apdoer.trade.common.model.vo.OrderOpenReqVo;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.monitor.handler.IndexHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 条件单数据处理
 * @author  apdoer
 * @version 1.0
 * @date 2020/5/26 15:46
 */
@Slf4j
@Component("conditionOrderMonitorHandler")
public class ConditionOrderMonitorHandler implements IndexHandler {

    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    @Override
    public void handle(IndexPriceDto indexPriceDto) {
        //待触发委托
        List<CoreContractOrderPo> triggerOrders = ConditionOrderData.getTriggerList(indexPriceDto.getContractId(), indexPriceDto.getIndexPrice());
        if (!CollectionUtils.isEmpty(triggerOrders)) {
            log.info("condition order monitor contract={} price={} size={}", indexPriceDto.getContractId(), indexPriceDto.getIndexPrice(), triggerOrders.size());
            try {
                ContractChannelMappingPo mappingPo = quotConfigCenterService.queryContractChannelMaping(indexPriceDto.getContractId());
                if (null != mappingPo) {
                    for (CoreContractOrderPo order : triggerOrders) {
                        GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getConditionOrderChannel());
                        if (null != eventBus) {
                            while(eventBus.tryDo(1, 1L, TimeUnit.SECONDS)) {
                                //反压
                            }
                            eventBus.publish(new SourceEvent(this.buildConditionOrderOpenDto(order, indexPriceDto.getIndexPrice())));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("condition order monitor thread error", e);
            }
        } else {
            log.info("condition order monitor contract={} price={} size=0", indexPriceDto.getContractId(), indexPriceDto.getIndexPrice());
        }
    }


    private ConditionOrderOpenDto buildConditionOrderOpenDto(CoreContractOrderPo order, BigDecimal indexPrice) {
        //设置当前真实指数价
        order.setRealTriggerPrice(indexPrice);
        ConditionOrderOpenDto openDto = new ConditionOrderOpenDto();
        openDto.setCoreContractOrderPo(order);
        openDto.setOrderOpenReqVo(this.buildOrderOpenReqVo(order, indexPrice));
        return openDto;
    }

    private OrderOpenReqVo buildOrderOpenReqVo(CoreContractOrderPo order, BigDecimal indexPrice) {
        OrderOpenReqVo openReqVo = new OrderOpenReqVo();
        openReqVo.setContractId(order.getContractId());
        openReqVo.setPosiSide(order.getPosiSide());
        openReqVo.setCurrencyId(order.getCurrencyId());
        openReqVo.setOpenAmt(order.getOpenAmt());
        openReqVo.setLeverage(order.getLeverage());
        openReqVo.setStopLossPrice(order.getStopLossPrice());
        openReqVo.setStopProfitPrice(order.getStopProfitPrice());
        openReqVo.setUserPrice(indexPrice);
        openReqVo.setUserId(order.getUserId());
        return openReqVo;
    }
}

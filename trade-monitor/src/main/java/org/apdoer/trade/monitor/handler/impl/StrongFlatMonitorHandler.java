package org.apdoer.trade.monitor.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.SlavePosiData;
import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.monitor.handler.IndexHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 强平数据监听处理
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 15:48
 */
@Slf4j
@Component("strongFlatMonitorHandler")
public class StrongFlatMonitorHandler implements IndexHandler {
    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    @Override
    public void handle(IndexPriceDto indexPriceDto) {
        // 获取待平仓(这里是强平)数据
        List<SlavePosiDataDto> closeList = SlavePosiData.getCloseList(indexPriceDto.getContractId(), indexPriceDto.getIndexPrice());
        if (!CollectionUtils.isEmpty(closeList)){
            log.info("strong flat monitor contract={} price={} size={}", indexPriceDto.getContractId(), indexPriceDto.getIndexPrice(), closeList.size());
            try {
                ContractChannelMappingPo mappingPo = quotConfigCenterService.queryContractChannelMaping(indexPriceDto.getContractId());
                if (null!=mappingPo){
                    for (SlavePosiDataDto slavePosiDataDto : closeList) {
                        GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(mappingPo.getFlChannel());
                        if (eventBus!=null){
                            while (eventBus.tryDo(1,1L, TimeUnit.SECONDS)){
                                //反压
                            }
                            eventBus.publish(new SourceEvent(buildOrderCloseReqVo(slavePosiDataDto, indexPriceDto.getIndexPrice())));
                        }
                    }
                }
            }catch (Exception e){
                log.error("posi monitor thread error", e);
            }
        }else {
            log.info("strong flat monitor contract={} price={} size=0", indexPriceDto.getContractId(), indexPriceDto.getIndexPrice());
        }
    }


    private OrderCloseReqVo buildOrderCloseReqVo(SlavePosiDataDto posiDto, BigDecimal closePrice) {
        OrderCloseReqVo requestVo = new OrderCloseReqVo();
        requestVo.setContractId(posiDto.getContractId());
        requestVo.setCloseType(CloseTypeEnum.FORCE_CLOSE.getCode());
        requestVo.setUserPrice(closePrice);
        requestVo.setUuid(posiDto.getUuid());
        requestVo.setUserId(posiDto.getUserId());
        return requestVo;
    }
}

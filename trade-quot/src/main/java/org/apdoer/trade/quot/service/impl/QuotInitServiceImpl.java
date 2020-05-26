package org.apdoer.trade.quot.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.quot.service.QuotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 行情模块初始化
 *
 * @author apdoer
 */
@Component
@Slf4j
public class QuotInitServiceImpl implements QuotInitService {

    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    @Override
    public void init() {
        this.flush();
    }

    @Override
    public void flush() {
        this.quotChannelInit();
    }

    private void quotChannelInit() {
        List<ContractChannelMappingPo> mappingList = this.quotConfigCenterService.queryAllMapping();
        if (null == mappingList || mappingList.size() == 0) {
            throw new RuntimeException("channel not config");
        }
        this.indexPriceChannelInit(mappingList);
    }


    public void indexPriceChannelInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====index price channel init start ");
        for (ContractChannelMappingPo mappingPo : mappingList) {
            GuavaEventBusManager.getInstance().buildGuavaEventBus(mappingPo.getQuotChannel());
        }
        log.info("====index price channel init end ");
    }

}

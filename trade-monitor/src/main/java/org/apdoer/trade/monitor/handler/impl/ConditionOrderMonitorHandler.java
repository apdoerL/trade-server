package org.apdoer.trade.monitor.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.monitor.handler.IndexHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
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
        //todo
    }
}

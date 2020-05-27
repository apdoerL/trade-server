package org.apdoer.trade.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.external.OrderCurrencyData;
import org.apdoer.trade.common.db.model.po.CoreContractOrderCurrencyPo;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.service.OrderCurrencyInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderCurrencyInitServiceImpl implements OrderCurrencyInitService {

    @Autowired
    private CoreContractDbService coreContractDbService;

    @Override
    public void init() {
        log.info(">>> init orderCurrency start.");
    	this.flush();
        log.info(">>> init orderCurrency start.");
    }

    @Override
    public void flush() {
        log.info(">>> flush orderCurrency start.");
        List<CoreContractOrderCurrencyPo> allOrderCurrency = coreContractDbService.getAllOrderCurrency();
        OrderCurrencyData.init(allOrderCurrency);
        log.info(">>> flush orderCurrency start.");
    }
}

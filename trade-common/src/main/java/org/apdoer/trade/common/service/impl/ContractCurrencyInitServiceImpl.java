package org.apdoer.trade.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.external.ContractCurrencyData;
import org.apdoer.trade.common.service.ContractCurrencyInitService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContractCurrencyInitServiceImpl implements ContractCurrencyInitService {

    @Override
    public void init() {
        log.info(">>> init contract_currency start.");
        ContractCurrencyData.init();
        log.info("<<< init contract_currency end.");
    }

    @Override
    public void flush() {
    }
}
package org.apdoer.trade.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.external.ContractLeverageData;
import org.apdoer.trade.common.db.model.po.CoreContractLeveragePo;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.service.ContractLeverageInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ContractLeverageInitServiceImpl implements ContractLeverageInitService {

    @Autowired
    private CoreContractDbService coreContractDbService;

    @Override
    public void init() {
        log.info(">>> init contract leverage start.");
        this.flush();
        log.info(">>> init contract leverage end.");
    }

    @Override
    public void flush() {
        log.info(">>> flush contract leverage start.");
        List<CoreContractLeveragePo> allContractLeverage = coreContractDbService.getAllContractLeverage();
        ContractLeverageData.init(allContractLeverage);
        log.info(">>> flush contract leverage end.");
    }
}
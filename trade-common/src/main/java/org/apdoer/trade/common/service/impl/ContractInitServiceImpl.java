package org.apdoer.trade.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.external.ContractData;
import org.apdoer.trade.common.db.model.po.CoreContractPo;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.service.ContractInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ContractInitServiceImpl implements ContractInitService {

    @Autowired
    private CoreContractDbService cfdContractDbService;

    @Override
    public void init() {
        log.info(">>> init contract start.");
        List<CoreContractPo> allContract = cfdContractDbService.getAllContract();
        ContractData.init(allContract);
        log.info(">>> init contract end.");
    }

    @Override
    public void flush() {
        log.info(">>> flush contract start.");
        List<CoreContractPo> allContract = cfdContractDbService.getAllContract();
        ContractData.init(allContract);
        log.info(">>> flush contract end.");
    }
}

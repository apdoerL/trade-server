package org.apdoer.trade.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.trade.common.data.external.ContractCurrencyData;
import org.apdoer.trade.core.service.TradeCommonService;
import org.springframework.stereotype.Service;

/**
 * @author apdoer
 */
@Slf4j
@Service
public class TradeCommonServiceImpl implements TradeCommonService {

    @Override
    public ResultVo getContractList() {
        return ResultVoBuildUtils.buildSuccessResultVo(ContractCurrencyData.get());
    }
}
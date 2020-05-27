package org.apdoer.trade.core.service;

import org.apdoer.common.service.model.vo.ResultVo;

public interface TradeCommonService {
    /**
     * 查询所有合约及下单币种-从内存获取
     * @return
     */
    ResultVo getContractList();
}

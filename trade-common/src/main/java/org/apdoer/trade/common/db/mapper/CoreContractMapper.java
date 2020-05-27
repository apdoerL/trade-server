package org.apdoer.trade.common.db.mapper;

import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.trade.common.db.model.po.CoreContractPo;

public interface CoreContractMapper extends BaseMapper<CoreContractPo> {
    /**
     * 获取最大合约id
     *
     * @return
     */
    Integer getMaxContractId();
}
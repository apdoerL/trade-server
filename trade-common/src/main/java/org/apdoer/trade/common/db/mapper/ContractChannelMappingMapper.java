package org.apdoer.trade.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;

import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:12
 */
public interface ContractChannelMappingMapper extends BaseMapper<ContractChannelMappingPo> {

    List<ContractChannelMappingPo> queryAllMapping();

    ContractChannelMappingPo queryMappingByContractId(@Param("contractId") Integer var1);
}

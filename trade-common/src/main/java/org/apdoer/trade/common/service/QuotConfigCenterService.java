package org.apdoer.trade.common.service;

import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;

import java.util.List;

public interface QuotConfigCenterService {
	
	/**
	 * 获取合约内部通道映射配置
	 * @param contractId
	 * @return
	 */
	ContractChannelMappingPo queryContractChannelMaping(Integer contractId);
	
	/**
	 * 获取所有配置
	 * @return
	 */
	List<ContractChannelMappingPo> queryAllMapping();

}

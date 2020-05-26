package org.apdoer.trade.common.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apdoer.trade.common.db.mapper.ContractChannelMappingMapper;
import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 配置中心
 * @author apdoer
 */
@Component
public class QuotConfigCenterServiceImpl implements QuotConfigCenterService {

	private Map<Integer, ContractChannelMappingPo> config = new ConcurrentHashMap<>();

	@Autowired
	private ContractChannelMappingMapper contractChannelMappingMapper;

	@Override
	public ContractChannelMappingPo queryContractChannelMaping(Integer contractId) {
		if (this.config.containsKey(contractId)) {
			return this.config.get(contractId);
		} else {
			ContractChannelMappingPo mappingPo = this.contractChannelMappingMapper
					.queryMappingByContractId(contractId);
			if (null != mappingPo) {
				this.config.put(contractId, mappingPo);
				return mappingPo;
			} else {
				return null;
			}
		}
	}

	@Override
	public List<ContractChannelMappingPo> queryAllMapping() {
		return this.contractChannelMappingMapper.queryAllMapping();
	}

}

package org.apdoer.trade.quot.process.assemble.impl;


import org.apdoer.trade.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.trade.common.event.payload.IndexPriceMessageProcessPayload;
import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.process.assemble.Assembler;

/**
 * 行情数据组装
 * @author apdoer
 */
public class QuotMessageAssembler implements Assembler<IndexPriceMessageSourcePayload, IndexPriceMessageProcessPayload> {
	
	private QuotConfigCenterService quotConfigCenterService;
	
	public QuotMessageAssembler(QuotConfigCenterService quotConfigCenterService) {
		this.quotConfigCenterService = quotConfigCenterService;
	}

	@Override
	public IndexPriceMessageProcessPayload assemble(IndexPriceMessageSourcePayload data) {
		return this.buildProcessPayload(data);
	}
	
	private synchronized IndexPriceMessageProcessPayload buildProcessPayload(IndexPriceMessageSourcePayload data) {
		if (null != data) {
			ContractChannelMappingPo po = this.quotConfigCenterService.queryContractChannelMaping(((IndexPriceDto)data.getData()).getContractId());
			IndexPriceMessageProcessPayload payload = new IndexPriceMessageProcessPayload();
			payload.setSystemChannel(po.getQuotChannel());
			payload.setData(data.getData());
			return payload;
		}
		return null;
	}
}

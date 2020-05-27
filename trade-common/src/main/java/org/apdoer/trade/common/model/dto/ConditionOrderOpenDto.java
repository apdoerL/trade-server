package org.apdoer.trade.common.model.dto;

import lombok.Data;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.model.vo.OrderOpenReqVo;

/**
 * 条件单开仓dto
 * @author apdoer
 */
@Data
public class ConditionOrderOpenDto {
	
	private OrderOpenReqVo orderOpenReqVo;
	
	private CoreContractOrderPo coreContractOrderPo;

}
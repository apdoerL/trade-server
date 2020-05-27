package org.apdoer.trade.core.service;

import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;


public interface TransactionService {
	
	/**
	 * 条件单-触发，冻结资产释放
	 * @param orderPo
	 * @return
	 */
	ResultVo conditionOrderUserAccLockedRelease(CoreContractOrderPo orderPo);
	
	/**
	 * 更新条件单
	 * @param orderPo
	 */
	void updateConditionOrder(CoreContractOrderPo orderPo);
}

package org.apdoer.trade.common.model.dto;

import lombok.Data;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;

/**
 * 异步清除从数据区持仓数据
 * @author apdoer
 */
@Data
public class PosiCloseSyncDto {
	
	//平仓类型
	private Integer closeType;
	
	//从数据区
	private SlavePosiDataDto posiDataDto;
	
	//待移除的持仓
	private CoreContractPosiPo removePosi;

}
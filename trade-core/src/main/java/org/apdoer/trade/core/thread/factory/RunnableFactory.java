package org.apdoer.trade.core.thread.factory;

import org.apdoer.trade.common.model.dto.ConditionOrderOpenDto;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;

public interface RunnableFactory {


	Runnable newOrderCloseInstance(OrderCloseReqVo requestVo);
	
	Runnable newOrderOpenInstance(ConditionOrderOpenDto openDto);

}

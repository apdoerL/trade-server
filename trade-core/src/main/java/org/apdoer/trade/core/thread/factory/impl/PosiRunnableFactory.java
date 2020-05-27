package org.apdoer.trade.core.thread.factory.impl;

import org.apdoer.trade.common.model.dto.ConditionOrderOpenDto;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;
import org.apdoer.trade.core.service.TradeService;
import org.apdoer.trade.core.service.TransactionService;
import org.apdoer.trade.core.thread.factory.RunnableFactory;
import org.apdoer.trade.core.thread.runnable.PosiFlatHandleRunnable;
import org.apdoer.trade.core.thread.runnable.PosiOpenHandleRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: PosiRunnableFactory
 * @date: 2020年3月31日
 * @author shen
 * 
 * @desc 开、平仓-runnable 工场
 */
@Component
public class PosiRunnableFactory implements RunnableFactory {
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private TransactionService transactionService;

	@Override
	public Runnable newOrderCloseInstance(OrderCloseReqVo requestVo) {
		return new PosiFlatHandleRunnable(this.tradeService, requestVo);
	}

	@Override
	public Runnable newOrderOpenInstance(ConditionOrderOpenDto openDto) {
		return new PosiOpenHandleRunnable(this.tradeService, openDto, this.transactionService);
	}

}
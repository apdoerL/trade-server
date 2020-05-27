package org.apdoer.trade.core.thread.runnable;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;
import org.apdoer.trade.core.service.TradeService;

/**
 * 平仓任务
 * @author apdoer
 */
@Slf4j
public class PosiFlatHandleRunnable implements Runnable {
	
	private TradeService tradeService;
	private OrderCloseReqVo closeRequestVo;
	
	public PosiFlatHandleRunnable(TradeService tradeService, OrderCloseReqVo closeRequestVo) {
		this.tradeService 	= tradeService;
		this.closeRequestVo = closeRequestVo;
	}

	@Override
	public void run() {
		log.info("PosiFlatHandleRunnable closeRequestVo={}", closeRequestVo);
		this.tradeService.closeOrder(this.closeRequestVo);
	}
}

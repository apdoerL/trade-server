package org.apdoer.trade.common.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.StopProfitSlavePosiData;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.handler.PosiCloseSyncHandler;
import org.apdoer.trade.common.model.dto.PosiCloseSyncDto;
import org.apdoer.trade.common.thread.pool.PosiSyncHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;


/**
 * 止盈平仓-从数据区异步更新
 * @author apdoer
 */
@Slf4j
@Component("stopProfitPosiCloseSyncHandler")
public class StopProfitPosiCloseSyncHandler implements PosiCloseSyncHandler {
	
	@Autowired
	private PosiSyncHandleThreadPool posiSyncHandleThreadPool;

	@Override
	public void handle(PosiCloseSyncDto syncDto) {
		
		if (CloseTypeEnum.STOP_PROFIT_CLOSE.getCode() != syncDto.getCloseType()
				&& syncDto.getRemovePosi().getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0) {
			try {
				while (this.posiSyncHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
					//反压
				}
				this.posiSyncHandleThreadPool.execute(new StopProfitPosiCloseSyncRunnable(syncDto));
			} catch (Exception e) {
				log.error("Stop profit Posi close sync error", e);
			}
		} else {
			//不需要，同步
		}
		
	}

	public class StopProfitPosiCloseSyncRunnable implements Runnable {

		private PosiCloseSyncDto syncDto;

		StopProfitPosiCloseSyncRunnable(PosiCloseSyncDto syncDto) {
			this.syncDto = syncDto;
		}

		@Override
		public void run() {

			BigDecimal triggerPrice = this.syncDto.getRemovePosi().getStopProfitPrice();
			Integer posiSide = this.syncDto.getRemovePosi().getPosiSide();

			this.syncDto.getPosiDataDto().setPosiSide(posiSide);
			this.syncDto.getPosiDataDto().setTriggerPrice(triggerPrice);
			StopProfitSlavePosiData.remove(this.syncDto.getPosiDataDto());
		}

	}

}

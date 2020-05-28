package org.apdoer.trade.common.handler.impl;


import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.handler.PosiCloseSyncHandler;
import org.apdoer.trade.common.model.dto.PosiCloseSyncDto;
import org.apdoer.trade.common.thread.pool.PosiSyncHandleThreadPool;
import org.apdoer.trade.common.thread.runnable.SlavePosiCloseSyncRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * 异步平仓-从数据区数据同步
 * @author apdoer
 */
@Slf4j
@Component("slavePosiCloseSyncHandler")
public class SlavePosiCloseSyncHandler implements PosiCloseSyncHandler {
	
	@Autowired
	private PosiSyncHandleThreadPool posiSyncHandleThreadPool;

	@Override
	public void handle(PosiCloseSyncDto syncDto) {
		if (CloseTypeEnum.FORCE_CLOSE.getCode() != syncDto.getCloseType()) {
			try {
				while (this.posiSyncHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
					//反压
				}
				this.posiSyncHandleThreadPool.execute(new SlavePosiCloseSyncRunnable(syncDto));
			} catch (Exception e) {
				log.error("Slave Posi close sync error", e);
			}	
		} else {
			//强平-不需要
		}
	}
}

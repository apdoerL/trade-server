package org.apdoer.trade.common.handler.impl;


import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.SlavePosiData;
import org.apdoer.trade.common.data.StopLossSlavePosiData;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.handler.PosiCloseSyncHandler;
import org.apdoer.trade.common.model.dto.PosiCloseSyncDto;
import org.apdoer.trade.common.thread.pool.PosiSyncHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;


/**
 * 止损平仓-异步同步从数据区数据
 *
 * @author apdoer
 */
@Slf4j
@Component("stopLossPosiCloseSyncHandler")
public class StopLossPosiCloseSyncHandler implements PosiCloseSyncHandler {

    @Autowired
    private PosiSyncHandleThreadPool posiSyncHandleThreadPool;

    @Override
    public void handle(PosiCloseSyncDto syncDto) {
        if (CloseTypeEnum.STOP_LOSS_CLOSE.getCode() != syncDto.getCloseType()
                && syncDto.getRemovePosi().getStopLossPrice().compareTo(BigDecimal.ZERO) > 0) {
            try {
                while (this.posiSyncHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
                    //反压
                }
                this.posiSyncHandleThreadPool.execute(new StopLossPosiCloseSyncRunnable(syncDto));
            } catch (Exception e) {
                log.error("Stop loss Posi close sync error", e);
            }
        } else {
            //不需要
        }
    }

    public class StopLossPosiCloseSyncRunnable implements Runnable {

        private PosiCloseSyncDto syncDto;

        StopLossPosiCloseSyncRunnable(PosiCloseSyncDto syncDto) {
            this.syncDto = syncDto;
        }

        @Override
        public void run() {

            BigDecimal triggerPrice = this.syncDto.getRemovePosi().getStopLossPrice();
            Integer posiSide = this.syncDto.getRemovePosi().getPosiSide();

            this.syncDto.getPosiDataDto().setPosiSide(posiSide);
            this.syncDto.getPosiDataDto().setTriggerPrice(triggerPrice);
            StopLossSlavePosiData.remove(this.syncDto.getPosiDataDto());
        }

    }
}

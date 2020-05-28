package org.apdoer.trade.common.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.StopLossSlavePosiData;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.handler.PosiOpenSyncHandler;
import org.apdoer.trade.common.thread.pool.PosiSyncHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;


/**
 * 止损开仓-从数据区数据同步
 * @author apdoer
 */
@Slf4j
@Component("stopLossPosiOpenSyncHandler")
public class StopLossPosiOpenSyncHandler implements PosiOpenSyncHandler {

    @Autowired
    private PosiSyncHandleThreadPool posiSyncHandleThreadPool;

    @Override
    public void handle(CoreContractPosiPo posiPo) {
        //该持仓有止损设置
        if (posiPo.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0) {
            try {
                while (this.posiSyncHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
                    //反压
                }
                this.posiSyncHandleThreadPool.execute(new StopLossPosiOpenSyncRunnable(posiPo));
            } catch (Exception e) {
                log.error("Stop loss Posi open sync error", e);
            }
        } else {
            //沒有設置
        }
    }

    class StopLossPosiOpenSyncRunnable implements Runnable {

        private CoreContractPosiPo posiPo;

        StopLossPosiOpenSyncRunnable(CoreContractPosiPo posiPo) {
            this.posiPo = posiPo;
        }

        @Override
        public void run() {
            StopLossSlavePosiData.add(this.posiPo);
        }

    }
}

package org.apdoer.trade.common.handler.impl;


import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.StopProfitSlavePosiData;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.handler.PosiOpenSyncHandler;
import org.apdoer.trade.common.thread.pool.PosiSyncHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;


/**
 * 止盈开仓-从数据区异步更新
 *
 * @author apdoer
 */
@Slf4j
@Component("stopProfitPosiOpenSyncHandler")
public class StopProfitPosiOpenSyncHandler implements PosiOpenSyncHandler {

    @Autowired
    private PosiSyncHandleThreadPool posiSyncHandleThreadPool;

    @Override
    public void handle(CoreContractPosiPo posiPo) {
        //该持仓有止赢设置
        if (posiPo.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0) {
            try {
                while (this.posiSyncHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
                    //反压
                }
                this.posiSyncHandleThreadPool.execute(new StopProfitPosiOpenSyncRunnable(posiPo));
            } catch (Exception e) {
                log.error("Stop profit Posi open sync error", e);
            }
        } else {
            //沒有設置
        }
    }

    class StopProfitPosiOpenSyncRunnable implements Runnable {

        private CoreContractPosiPo posiPo;

        StopProfitPosiOpenSyncRunnable(CoreContractPosiPo posiPo) {
            this.posiPo = posiPo;
        }

        @Override
        public void run() {
            StopProfitSlavePosiData.add(this.posiPo);
        }

    }
}

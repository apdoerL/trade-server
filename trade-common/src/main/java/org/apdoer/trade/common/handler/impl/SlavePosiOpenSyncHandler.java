package org.apdoer.trade.common.handler.impl;


import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.SlavePosiData;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.handler.PosiOpenSyncHandler;
import org.apdoer.trade.common.thread.pool.PosiSyncHandleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 异步开仓-从数据区同步
 *
 * @author apdoer
 */
@Slf4j
@Component("slavePosiOpenSyncHandler")
public class SlavePosiOpenSyncHandler implements PosiOpenSyncHandler {

    @Autowired
    private PosiSyncHandleThreadPool posiSyncHandleThreadPool;

    @Override
    public void handle(CoreContractPosiPo posi) {
        try {
            while (this.posiSyncHandleThreadPool.tryDo(1, 1L, TimeUnit.SECONDS)) {
                //反压
            }
            this.posiSyncHandleThreadPool.execute(new SlavePosiOpenSyncRunnable(posi));
        } catch (Exception e) {
            log.error("Slave Posi open sync error", e);
        }
    }

    class SlavePosiOpenSyncRunnable implements Runnable {


        private CoreContractPosiPo posiPo;

        SlavePosiOpenSyncRunnable(CoreContractPosiPo posiPo) {
            this.posiPo = posiPo;
        }

        @Override
        public void run() {
            SlavePosiData.add(this.posiPo);
        }

    }

}

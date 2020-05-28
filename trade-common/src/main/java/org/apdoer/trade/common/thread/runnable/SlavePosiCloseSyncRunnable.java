package org.apdoer.trade.common.thread.runnable;

import org.apdoer.trade.common.data.SlavePosiData;
import org.apdoer.trade.common.model.dto.PosiCloseSyncDto;

import java.math.BigDecimal;

/**
 * 异步移除从数据区的持仓
 *
 * @author apdoer
 */
public class SlavePosiCloseSyncRunnable implements Runnable {

    private PosiCloseSyncDto syncDto;

    public SlavePosiCloseSyncRunnable(PosiCloseSyncDto syncDto) {
        this.syncDto = syncDto;
    }

    @Override
    public void run() {
        // 移除：从持仓
        BigDecimal triggerPrice = this.syncDto.getRemovePosi().getForceClosePrice();
        Integer posiSide = this.syncDto.getRemovePosi().getPosiSide();
        this.syncDto.getPosiDataDto().setPosiSide(posiSide);
        this.syncDto.getPosiDataDto().setTriggerPrice(triggerPrice);
        SlavePosiData.remove(this.syncDto.getPosiDataDto());
    }

}
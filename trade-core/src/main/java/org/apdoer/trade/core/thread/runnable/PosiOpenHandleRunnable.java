package org.apdoer.trade.core.thread.runnable;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.model.dto.ConditionOrderOpenDto;
import org.apdoer.trade.core.service.TradeService;
import org.apdoer.trade.core.service.TransactionService;


/**
 * 开仓
 *
 * @author apdoer
 */
@Slf4j
public class PosiOpenHandleRunnable implements Runnable {

    private TransactionService transactionService;

    private TradeService tradeService;
    private ConditionOrderOpenDto openDto;

    public PosiOpenHandleRunnable(TradeService tradeService, ConditionOrderOpenDto openDto, TransactionService transactionService) {
        this.tradeService = tradeService;
        this.openDto = openDto;
        this.transactionService = transactionService;
    }

    @Override
    public void run() {
        //處理條件單，資產
        try {
            ResultVo resultVo = this.transactionService.conditionOrderUserAccLockedRelease(this.openDto.getCoreContractOrderPo());
            if (ExceptionCode.SUCCESS.getCode() != resultVo.getCode()) {
                return;
            }
            ResultVo openResult = this.tradeService.openOrder(this.openDto.getOrderOpenReqVo());
            if (ExceptionCode.SUCCESS.getCode() != openResult.getCode()) {
                log.error("condition order open error, code={},userId={}, orderId={}", openResult.getCode(), this.openDto.getOrderOpenReqVo().getUserId(), this.openDto.getCoreContractOrderPo().getOrderId());
                return;
            }
            log.info("condition order open succ, userId={}, orderId={}, posiId={}", this.openDto.getOrderOpenReqVo().getUserId(), this.openDto.getCoreContractOrderPo().getOrderId(), openResult.getData());

            Long uuid = (Long) openResult.getData();
            this.openDto.getCoreContractOrderPo().setPosiId(uuid);

            this.transactionService.updateConditionOrder(this.openDto.getCoreContractOrderPo());

        } catch (Exception e) {
            log.error("Posi open handle error", e);
            return;
        }
    }
}

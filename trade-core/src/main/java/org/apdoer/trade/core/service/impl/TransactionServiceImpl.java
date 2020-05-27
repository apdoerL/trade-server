package org.apdoer.trade.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.db.model.po.WebAssetAccountPo;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.db.service.TradeUserDbService;
import org.apdoer.trade.common.enums.OrderStatusEnum;
import org.apdoer.trade.core.handler.RedisCacheHandler;
import org.apdoer.trade.core.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;


@Slf4j
@Component
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TradeUserDbService tradeUserDbService;

    @Autowired
    private CoreContractDbService cfdContractDbService;

    @Autowired
    private RedisCacheHandler redisCacheHandler;

    @Override
    public ResultVo conditionOrderUserAccLockedRelease(CoreContractOrderPo orderPo) {
        WebAssetAccountPo userAccount = this.tradeUserDbService.getAccountForUpdate(orderPo.getUserId(), orderPo.getCurrencyId());
        // 资金是否足额：余额 >= 下单本金 + 开仓手续费
        if (null == userAccount) {
            log.error("condition order userAcc locked release error=[account is blank], userId={}, orderId={}", orderPo.getUserId(), orderPo.getOrderId());
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getCode(), ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getName());
        }
        BigDecimal beforeAvi = userAccount.getAvailable();
        BigDecimal beforeLock = userAccount.getLocked();
        BigDecimal newAvi = userAccount.getAvailable().add(orderPo.getOpenAmt());
        BigDecimal newLocked = userAccount.getLocked().subtract(orderPo.getOpenAmt());
        userAccount.setAvailable(newAvi);
        userAccount.setLocked(newLocked);
        userAccount.setUpdateTime(new Date());
        // 修改资产
        int result = this.tradeUserDbService.updateAccount(userAccount);
        if (1 != result) {
            log.error("condition order userAcc locked release error=[update acc error], userId={}, orderId={}", orderPo.getUserId(), orderPo.getOrderId());
            throw new RuntimeException("update account error");
        }

        orderPo.setStatus(OrderStatusEnum.TRIGGER.getCode());
        result = this.cfdContractDbService.updateOrder(orderPo);
        if (1 != result) {
            log.error("condition order userAcc locked release error=[update acc error], userId={}, orderId={}", orderPo.getUserId(), orderPo.getOrderId());
            throw new RuntimeException("update account error");
        }
        //redis 去除
        this.redisCacheHandler.hDeleteConditionOrder(orderPo.getUserId(), orderPo.getOrderId());
        log.info("condition order userAcc locked release succ, userId={}, orderId={}, beforeAvi={}, newAvi={}, beforeLock={}, newLocked={}", orderPo.getUserId(),
                orderPo.getOrderId(), beforeAvi, newAvi, beforeLock, newLocked);
        return ResultVoBuildUtils.buildSuccessResultVo();
    }

    @Override
    public void updateConditionOrder(CoreContractOrderPo orderPo) {
        int result = this.cfdContractDbService.updateOrder(orderPo);
        if (1 != result) {
            log.error("condition order update error, userId={} orderId={} posiId={}", orderPo.getUserId(), orderPo.getOrderId(), orderPo.getPosiId());
        }
    }

}

package org.apdoer.trade.core.handler;

import com.aliyun.openservices.ons.api.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.config.CurrencyProperties;
import org.apdoer.trade.common.data.ConditionOrderData;
import org.apdoer.trade.common.data.MasterPosiData;
import org.apdoer.trade.common.data.StopLossSlavePosiData;
import org.apdoer.trade.common.data.StopProfitSlavePosiData;
import org.apdoer.trade.common.db.mapper.UserLendingRecordMapper;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.db.model.po.UserLendingRecordPo;
import org.apdoer.trade.common.db.model.po.WebAssetAccountPo;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.db.service.TradeUserDbService;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.enums.OrderStatusEnum;
import org.apdoer.trade.common.enums.PosiStatusEnum;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;
import org.apdoer.trade.common.model.vo.OrderConditionReqVo;
import org.apdoer.trade.common.model.vo.OrderOpenReqVo;
import org.apdoer.trade.common.model.vo.OrderStopProfitLossReqVo;
import org.apdoer.trade.common.utils.OrderNumberUtil;
import org.apdoer.trade.common.utils.PosiNumberUtil;
import org.apdoer.trade.core.check.TradeChecker;
import org.apdoer.trade.core.mq.producer.ClosePosiProducerClient;
import org.apdoer.trade.core.mq.producer.LendingOpenPosiProducerClient;
import org.apdoer.trade.core.mq.producer.OpenPosiProducerClient;
import org.apdoer.trade.core.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author apdoer
 */
@Slf4j
@Service
public class TradeHandler {

    @Autowired
    private CoreContractDbService coreContractDbService;

    @Autowired
    private TradeUserDbService tradeUserDbService;

    @Autowired
    private RedisCacheHandler redisCacheHandler;

    @Autowired
    private ClosePosiProducerClient closePosiProducerClient;

    @Autowired
    private OpenPosiProducerClient openPosiProducerClient;

    @Autowired
    private LendingOpenPosiProducerClient lendingOpenPosiProducerClient;

    @Autowired
    private PosiNumberUtil posiNumberUtil;
    
    @Autowired
    private OrderNumberUtil orderNumberUtil;

    @Autowired
    private TradeService tradeService;

    @Resource
    private UserLendingRecordMapper userLendingRecordMapper;

    @Transactional
    public ResultVo handleOpen(OrderOpenReqVo reqVo, CoreContractPosiPo calOpenPosi, Integer agentUserId) {
        WebAssetAccountPo userAccount = tradeUserDbService.getAccountForUpdate(reqVo.getUserId(), reqVo.getCurrencyId());
        // 资金是否足额：余额 >= 下单本金 + 开仓手续费
        if (null == userAccount) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getCode(),ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getName());
        }
        BigDecimal available = userAccount.getAvailable()
                .subtract(reqVo.getOpenAmt().add(calOpenPosi.getOpenFee()));
        if (available.compareTo(BigDecimal.ZERO) < 0) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getCode(),ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getName());
        }
        // 修改资产
        tradeUserDbService.updateUserAccount(reqVo.getUserId(), reqVo.getCurrencyId(), available);
        // 新增持仓数据
        calOpenPosi.setUuid(posiNumberUtil.getPosiUuid());
        calOpenPosi.setUserId(reqVo.getUserId());
        calOpenPosi.setContractId(reqVo.getContractId());
        calOpenPosi.setCurrencyId(reqVo.getCurrencyId());
        calOpenPosi.setPosiSide(reqVo.getPosiSide());
        calOpenPosi.setStatus(PosiStatusEnum.UN_CLOSE.getCode());
        calOpenPosi.setStopProfitPrice(reqVo.getStopProfitPrice());
        calOpenPosi.setStopLossPrice(reqVo.getStopLossPrice());
        calOpenPosi.setAgentUserId(agentUserId);
        Date date = new Date();
        calOpenPosi.setCreateTime(date);
        calOpenPosi.setUpdateTime(date);
        coreContractDbService.insertPosi(calOpenPosi);

        // 赠金币种发送mq延迟消息（用于24小时后自动平仓）
        if (CurrencyProperties.LENDING_CURRENCY_ID == reqVo.getCurrencyId()) {
            SendResult sendResult = lendingOpenPosiProducerClient.sendMsg(calOpenPosi.getUuid().toString(), calOpenPosi);
            if (null != sendResult) {
                log.info("开仓延迟消息发送成功 posi[{}] sendResult[{}]", calOpenPosi, sendResult);
            }
        } else {
            // 其他币种发送mq普通消息
            SendResult sendResult = openPosiProducerClient.sendMsg(calOpenPosi.getUuid().toString(), calOpenPosi);
            if (null != sendResult) {
                log.info("开仓消息发送成功 posi[{}] sendResult[{}]", calOpenPosi, sendResult);
            }
        }

        // 新增redis
        redisCacheHandler.hPutPosi(reqVo.getUserId(), calOpenPosi.getUuid(), calOpenPosi);

        // todo 流水

        // 新增内存持仓
        ResultVo resultVo = MasterPosiData.add(calOpenPosi);
        if (resultVo.getCode() != ExceptionCode.SUCCESS.getCode()) {
            throw new RuntimeException("[handleOpen] add posi_data error.");
        }
        return ResultVoBuildUtils.buildSuccessResultVo(calOpenPosi.getUuid());
    }

    @Transactional
    public void handleClose(OrderCloseReqVo reqVo, CoreContractPosiPo posi) {
        BigDecimal closeFee = posi.getCloseFee();
        BigDecimal closeProfitLoss = posi.getCloseProfitLoss();
        if (reqVo.getCloseType() == CloseTypeEnum.FORCE_CLOSE.getCode()) {
            // 风险基金 = 开仓本金 + 平仓盈亏 - 平仓手续费
            BigDecimal riskFund = posi.getOpenAmt()
                    .add(closeProfitLoss)
                    .subtract(closeFee);
            posi.setRiskFunding(riskFund);
            // 入库风险基金账户表
            tradeUserDbService.insertRiskFund(posi.getCurrencyId(), riskFund);
        } else {
            // 修改用户我的钱包资产
            BigDecimal closeBalance = posi.getOpenAmt()
                    .add(closeProfitLoss)
                    .subtract(closeFee);
            tradeUserDbService.addUserAccount(posi.getUserId(), posi.getCurrencyId(), closeBalance);
        }
        // 修改持仓db状态
        posi.setStatus(reqVo.getCloseType());
        coreContractDbService.updatePosi(posi);

        // 新增持仓历史
        coreContractDbService.insertPosiHistory(posi);

        // 移除持仓redis
        redisCacheHandler.hDelete(posi.getUserId(), posi.getUuid());

        // 发送平仓mq
        SendResult sendResult = closePosiProducerClient.sendMsg(posi.getUuid().toString(), posi);
        if (null != sendResult) {
            log.info("平仓消息发送成功 posi[{}] sendResult[{}]", posi, sendResult);
        }
    }

    @Transactional
    public ResultVo handleOpenLending(UserLendingRecordPo lendingRecordPo, OrderOpenReqVo reqVo) {
        // 修改赠金回收数量及使用状态
        lendingRecordPo.setRecycleNum(lendingRecordPo.getNum());
        lendingRecordPo.setNotRecycleNum(BigDecimal.ZERO);
        lendingRecordPo.setIsUsed(TradeChecker.LendingUsedEnum.USED.getCode());
        userLendingRecordMapper.updateByPrimaryKeySelective(lendingRecordPo);

        // 调用正常开仓流程
        ResultVo openResult = tradeService.openOrder(reqVo);
        if (openResult.getCode() != ExceptionCode.SUCCESS.getCode()) {
            throw new RuntimeException("handleOpenLending error.");
        }
        return openResult;
    }
    
    public void handleStopProfitLoss(OrderStopProfitLossReqVo requestVo, CoreContractPosiPo posi) {
    	boolean hasChange = false;
		//设置止损
		if (null != requestVo.getStopLossPrice()) {
			//设置止损
			if (requestVo.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0) {
				//持仓已有止损
				if (posi.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0) {
					//先去除原止损映射数据
					SlavePosiDataDto removeData = this.buildSlavePosiDataDto(posi.getUserId(), posi.getContractId(), posi.getUuid(), posi.getPosiSide(), posi.getStopLossPrice());
					StopLossSlavePosiData.remove(removeData);
					
					//重设止损
					posi.setStopLossPrice(requestVo.getStopLossPrice());
					StopLossSlavePosiData.add(posi);
					hasChange = true;
					//持仓无止损
				} else {
					//添加止损
					posi.setStopLossPrice(requestVo.getStopLossPrice());
					StopLossSlavePosiData.add(posi);
					hasChange = true;
				}
				//取消止损
			} else {
				//持仓已有止损
				if (posi.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0) {
					//先去除原止损映射数据
					SlavePosiDataDto removeData = this.buildSlavePosiDataDto(posi.getUserId(), posi.getContractId(), posi.getUuid(), posi.getPosiSide(), posi.getStopLossPrice());
					StopLossSlavePosiData.remove(removeData);
					
					posi.setStopLossPrice(requestVo.getStopLossPrice());
					hasChange = true;
				}
			}
		}
		//设置止盈
		if (null != requestVo.getStopProfitPrice()) {
			//设置止盈
			if (requestVo.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0) {
				//持仓已有止盈
				if (posi.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0) {
					//先去除原止盈
					SlavePosiDataDto removeData = this.buildSlavePosiDataDto(posi.getUserId(), posi.getContractId(), posi.getUuid(), posi.getPosiSide(), posi.getStopProfitPrice());
					StopProfitSlavePosiData.remove(removeData);
					
					//重设止盈
					posi.setStopProfitPrice(requestVo.getStopProfitPrice());
					StopProfitSlavePosiData.add(posi);
					hasChange = true;
					//持仓无止盈
				} else {
					//添加止盈
					posi.setStopProfitPrice(requestVo.getStopProfitPrice());
					StopProfitSlavePosiData.add(posi);
					hasChange = true;
				}
			} else {
				//持仓已有止盈
				if (posi.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0) {
					//先去除原止损映射数据
					SlavePosiDataDto removeData = this.buildSlavePosiDataDto(posi.getUserId(), posi.getContractId(), posi.getUuid(), posi.getPosiSide(), posi.getStopProfitPrice());
					StopProfitSlavePosiData.remove(removeData);
					
					posi.setStopProfitPrice(requestVo.getStopProfitPrice());
					hasChange = true;
				}
			}
		}
		if (hasChange) {
	        // 新增redis
	        this.redisCacheHandler.hPutPosi(posi.getUserId(), posi.getUuid(), posi);
	        this.coreContractDbService.updatePosi(posi);
		}
    }
    
    @Transactional
    public ResultVo handleOrderCondition(OrderConditionReqVo requestVo) {
        WebAssetAccountPo userAccount = this.tradeUserDbService.getAccountForUpdate(requestVo.getUserId(), requestVo.getCurrencyId());
        // 资金是否足额：余额 >= 下单本金 + 开仓手续费
        if (null == userAccount) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getCode(),ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getName());
        }
        BigDecimal beforeAvi = userAccount.getAvailable();
        BigDecimal beforeLock = userAccount.getLocked();
        BigDecimal newAvi = userAccount.getAvailable().subtract(requestVo.getOpenAmt());
        if (newAvi.compareTo(BigDecimal.ZERO) < 0) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getCode(),ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getName());
        } else {
        	userAccount.setAvailable(newAvi);
        }
        BigDecimal newlock = userAccount.getLocked().add(requestVo.getOpenAmt());
        userAccount.setLocked(newlock);
        userAccount.setUpdateTime(new Date());
        // 修改资产
        int result = this.tradeUserDbService.updateAccount(userAccount);
        if (1 != result) {
        	throw new RuntimeException("update account error");
        }
        //插入记录
        CoreContractOrderPo orderPo = this.buildCfdContractOrderPo(requestVo);
        result = this.coreContractDbService.insertOrder(orderPo);
        if (1 != result) {
        	throw new RuntimeException("insert order error");
        }
        //加入内存数据
        ConditionOrderData.add(orderPo);
        //写入redis
        this.redisCacheHandler.hPutConditionOrder(orderPo.getUserId(), orderPo.getOrderId(), orderPo);
        log.info("condition order, userId={}, orderId={}, currencyId={} beforeAvi={} afterAvi={} beforeLock={} afterLock={}", 
        		orderPo.getUserId(), orderPo.getOrderId(), orderPo.getCurrencyId(), beforeAvi, newAvi, beforeLock, newlock);
        return ResultVoBuildUtils.buildSuccessResultVo(orderPo.getOrderId());
    }
    
    @Transactional
    public ResultVo handleOrderConditionCancel(CoreContractOrderPo orderPo) {
        WebAssetAccountPo userAccount = this.tradeUserDbService.getAccountForUpdate(orderPo.getUserId(), orderPo.getCurrencyId());
        if (null == userAccount) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getCode(),ExceptionCode.INSUFFICIENT_ACCOUNT_BALANCE.getName());
        }
        BigDecimal beforeAvi = userAccount.getAvailable();
        BigDecimal beforeLock = userAccount.getLocked();
        BigDecimal newAvi = userAccount.getAvailable().add(orderPo.getOpenAmt());
        userAccount.setAvailable(newAvi);
        BigDecimal newlock = userAccount.getLocked().subtract(orderPo.getOpenAmt());
        userAccount.setLocked(newlock);
        // 修改资产
        int result = this.tradeUserDbService.updateAccount(userAccount);
        if (1 != result) {
        	throw new RuntimeException("update account error");
        }
        orderPo.setStatus(OrderStatusEnum.CANCEL.getCode());
        orderPo.setUpdateTime(new Date());
        //更新状态
        result = this.coreContractDbService.updateOrder(orderPo);
        if (1 != result) {
        	throw new RuntimeException("insert order error");
        }
        //移除数据
        ConditionOrderData.remove(orderPo);
        //移除redis数据
        this.redisCacheHandler.hDeleteConditionOrder(orderPo.getUserId(), orderPo.getOrderId());
        log.info("condition order cancel, userId={}, orderId={}, currencyId={} beforeAvi={} afterAvi={} beforeLock={} afterLock={}", 
        		orderPo.getUserId(), orderPo.getOrderId(), orderPo.getCurrencyId(), beforeAvi, newAvi, beforeLock, newlock);
        return ResultVoBuildUtils.buildSuccessResultVo();
    }
    
    private SlavePosiDataDto buildSlavePosiDataDto(Integer userId, Integer contractId, Long uuid, Integer posiSide, BigDecimal triggerPrice) {
    	SlavePosiDataDto slaveDto = new SlavePosiDataDto();
    	slaveDto.setUserId(userId);
    	slaveDto.setContractId(contractId);
    	slaveDto.setUuid(uuid);
    	slaveDto.setPosiSide(posiSide);
    	slaveDto.setTriggerPrice(triggerPrice);
    	return slaveDto;
    }
    
    private CoreContractOrderPo buildCfdContractOrderPo(OrderConditionReqVo requestVo) {
        CoreContractOrderPo orderPo = new CoreContractOrderPo();
    	orderPo.setOrderId(this.orderNumberUtil.getOrderUuid());
    	orderPo.setUserId(requestVo.getUserId());
    	orderPo.setContractId(requestVo.getContractId());
    	orderPo.setCurrencyId(requestVo.getCurrencyId());
    	orderPo.setPosiSide(requestVo.getPosiSide());
    	orderPo.setOpenAmt(requestVo.getOpenAmt());
    	orderPo.setLeverage(requestVo.getLeverage());
    	orderPo.setTriggerPrice(requestVo.getTriggerPrice());
    	orderPo.setStopLossPrice(requestVo.getStopLossPrice());
    	orderPo.setStopProfitPrice(requestVo.getStopProfitPrice());
    	orderPo.setStatus(OrderStatusEnum.WAIT.getCode());
    	orderPo.setCreateTime(new Date());
    	orderPo.setUpdateTime(new Date());
    	return orderPo;
    }
}

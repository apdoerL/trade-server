package org.apdoer.trade.core.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.data.MasterPosiData;
import org.apdoer.trade.common.data.QuotData;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.db.model.po.CoreContractPo;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.db.model.po.UserLendingRecordPo;
import org.apdoer.trade.common.db.service.AgentUserDbService;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.enums.PosiSideEnum;
import org.apdoer.trade.common.model.dto.AgentUserCurrencyDto;
import org.apdoer.trade.common.model.dto.AgentUserCurrencyWrapDto;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;
import org.apdoer.trade.common.model.vo.*;
import org.apdoer.trade.common.model.vo.query.OrderHistoryQueryVo;
import org.apdoer.trade.common.model.vo.query.OrderHoldQueryVo;
import org.apdoer.trade.common.utils.ModelBuilder;
import org.apdoer.trade.core.check.TradeChecker;
import org.apdoer.trade.core.handler.RedisCacheHandler;
import org.apdoer.trade.core.handler.TradeHandler;
import org.apdoer.trade.core.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeChecker tradeChecker;

    @Autowired
    private TradeHandler tradeHandler;

    @Autowired
    private AgentUserDbService agentUserDbService;

    @Autowired
    private RedisCacheHandler redisCacheHandler;

    @Autowired
    private CoreContractDbService coreContractDbService;

    @Override
    public ResultVo openOrder(OrderOpenReqVo reqVo) {
        // 获取当前行情价
        BigDecimal indexPrice = QuotData.get(reqVo.getContractId());
        if (null == indexPrice) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRADE_SERVER_NOT_READY.getCode(),ExceptionCode.TRADE_SERVER_NOT_READY.getName());
        }

        // 校验请求参数
        ResultVo checkOpen = tradeChecker.checkOpen(reqVo, indexPrice);
        if (checkOpen.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return checkOpen;
        }
        CoreContractPo contract = (CoreContractPo) checkOpen.getData();

        // 计算开仓数据
        CoreContractPosiPo calOpenPosi = this.calOpen(reqVo, contract, indexPrice);
        try {
            // 查询是否代理旗下用户
            Integer agentUserId = this.getAgentUserId(reqVo.getUserId(), reqVo.getCurrencyId(), reqVo);
        	return tradeHandler.handleOpen(reqVo, calOpenPosi, agentUserId);
        } catch (Exception e) {
            log.error("openOrder error.", e);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_POSI_FAILED.getCode(),ExceptionCode.OPEN_POSI_FAILED.getName());
        }
    }

    @Override
    public ResultVo closeOrder(OrderCloseReqVo reqVo) {
        // 校验请求参数
        ResultVo checkClose = tradeChecker.checkClose(reqVo);
        if (checkClose.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return checkClose;
        }
        CoreContractPo contract = (CoreContractPo) checkClose.getData();

        // 获取当前行情价
        BigDecimal indexPrice;
        if (reqVo.getCloseType() == CloseTypeEnum.USER_CLOSE.getCode()) {
            indexPrice = QuotData.get(reqVo.getContractId());
            if (null == indexPrice) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRADE_SERVER_NOT_READY.getCode(),ExceptionCode.TRADE_SERVER_NOT_READY.getName());
            }
        } else {
            indexPrice = reqVo.getUserPrice();
        }

        // 移除内存持仓数据
        SlavePosiDataDto slaveData = SlavePosiDataDto.builder()
                .uuid(reqVo.getUuid())
                .userId(reqVo.getUserId())
                .contractId(reqVo.getContractId())
                .build();
        ResultVo removeResult = MasterPosiData.remove(slaveData, reqVo.getCloseType());
        if (removeResult.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return removeResult;
        }
        CoreContractPosiPo posi = (CoreContractPosiPo) removeResult.getData();

        // 计算平仓数据
        this.calClose(reqVo, posi, contract, indexPrice);

        try {
            // 事务处理
            tradeHandler.handleClose(reqVo, posi);
            return ResultVoBuildUtils.buildSuccessResultVo();
        } catch (Exception e) {
            log.error("handleClose error.", e);
            MasterPosiData.add(posi);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CLOSE_POSI_FAILED.getCode(),ExceptionCode.CLOSE_POSI_FAILED.getName());
        }
    }
    
	@Override
	public ResultVo stopProfitLossOrder(OrderStopProfitLossReqVo requestVo) {
        //参数校验
		ResultVo checkResult = this.tradeChecker.checkStopProfitLossPriceIsLegal(requestVo);
		if (ExceptionCode.SUCCESS.getCode() != checkResult.getCode()) {
			return checkResult;
		}
		try {
            this.tradeHandler.handleStopProfitLoss(requestVo, (CoreContractPosiPo)checkResult.getData());
            return ResultVoBuildUtils.buildSuccessResultVo();
		} catch (Exception e) {
			log.error("stop profit loss error", e);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_PROFIT_LOSS_FAILD.getCode(),ExceptionCode.STOP_PROFIT_LOSS_FAILD.getName());
		}
	}
	
	@Override
	public ResultVo orderCondition(OrderConditionReqVo requestVo) {
        // 获取当前行情价
        BigDecimal indexPrice = QuotData.get(requestVo.getContractId());
        if (null == indexPrice) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRADE_SERVER_NOT_READY.getCode(),ExceptionCode.TRADE_SERVER_NOT_READY.getName());
        }
        //参数校验
		ResultVo checkResult = this.tradeChecker.checkOrderConditionIsLegal(requestVo);
		if (ExceptionCode.SUCCESS.getCode() != checkResult.getCode()) {
			return checkResult;
		}
		try {
			return this.tradeHandler.handleOrderCondition(requestVo);
		} catch (Exception e) {
			log.error("order condition error", e);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.ORDER_CONDITION_ERROR.getCode(),ExceptionCode.ORDER_CONDITION_ERROR.getName());
		}
	}

	@Override
	public ResultVo orderConditionCancel(OrderConditionCancelReqVo requestVo) {
        //参数校验
		ResultVo checkResult = this.tradeChecker.checkOrderConditionCancelIsLegal(requestVo);
		if (ExceptionCode.SUCCESS.getCode() != checkResult.getCode()) {
			return checkResult;
		}
		CoreContractOrderPo orderPo = (CoreContractOrderPo) checkResult.getData();
        // 获取当前行情价
        BigDecimal indexPrice = QuotData.get(orderPo.getContractId());
        if (null == indexPrice) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRADE_SERVER_NOT_READY.getCode(),ExceptionCode.TRADE_SERVER_NOT_READY.getName());
        }
		try {
			return this.tradeHandler.handleOrderConditionCancel(orderPo);
		} catch (Exception e) {
			log.error("order condition cancel error", e);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.ORDER_CONDITION_ERROR.getCode(),ExceptionCode.ORDER_CONDITION_ERROR.getName());
		}
	}
	
	@Override
	public ResultVo orderConditionQuery(Integer userId, Integer contractId) {
		return ResultVoBuildUtils.buildSuccessResultVo(this.redisCacheHandler.hEntriesConditionOrder(userId, contractId));
	}
	
	@Override
	public ResultVo orderConditionHistoryQuery(Integer userId, Integer contractId, Integer side, Integer status,
			int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CoreContractOrderPo> list = this.coreContractDbService.queryHistoryOrder(userId, contractId, side, status);
		PageInfo<CoreContractOrderPo> poPageInfo = new PageInfo<>(list);
		
		List<CoreContractOrderRespVo> orderRespList = new ArrayList<>(list.size());
		for (CoreContractOrderPo orderPo : list) {
			orderRespList.add(ModelBuilder.buildCfdContractOrderRespVo(orderPo));
		}
		
        PageInfo<CoreContractOrderRespVo> voPageInfo = new PageInfo<>(orderRespList);
        voPageInfo.setPageNum(poPageInfo.getPageNum());
        voPageInfo.setPageSize(poPageInfo.getPageSize());
        voPageInfo.setTotal(poPageInfo.getTotal());
        voPageInfo.setPages(poPageInfo.getPages());
        voPageInfo.setSize(poPageInfo.getSize());
        return ResultVoBuildUtils.buildSuccessResultVo(voPageInfo);
	}
	

    @Override
    public ResultVo holdOrderList(OrderHoldQueryVo queryVo) {
        ResultVo checkHold = tradeChecker.checkHold(queryVo);
        if (checkHold.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return checkHold;
        }
        try {
            List<PosiListRespVo> posiListRespVos = redisCacheHandler.hEntriesPosi(queryVo.getUserId());
            return ResultVoBuildUtils.buildSuccessResultVo(posiListRespVos);
        } catch (Exception e) {
            log.error("holdOrderList error.", e);
            return ResultVoBuildUtils.buildFaildResultVo();
        }
    }

    @Override
    public ResultVo historyOrderList(OrderHistoryQueryVo queryVo) {
        ResultVo checkHistory = tradeChecker.checkHistory(queryVo);
        if (checkHistory.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return checkHistory;
        }
        try {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
            List<CoreContractPosiPo> posiHistoryList = coreContractDbService.historyOrderList(queryVo.getUserId(), queryVo.getQueryDate());
            PageInfo<CoreContractPosiPo> poPageInfo = new PageInfo<>(posiHistoryList);

            List<PosiListRespVo> respVoList = new ArrayList<>(posiHistoryList.size());
            for (CoreContractPosiPo posiPo : posiHistoryList) {
                respVoList.add(PosiListRespVo.convertFrom(posiPo));
            }

            PageInfo<PosiListRespVo> voPageInfo = new PageInfo<>(respVoList);
            voPageInfo.setPageNum(poPageInfo.getPageNum());
            voPageInfo.setPageSize(poPageInfo.getPageSize());
            voPageInfo.setTotal(poPageInfo.getTotal());
            voPageInfo.setPages(poPageInfo.getPages());
            voPageInfo.setSize(poPageInfo.getSize());
            return ResultVoBuildUtils.buildSuccessResultVo(voPageInfo);
        } catch (Exception e) {
            log.error("historyOrderList error.", e);
            return ResultVoBuildUtils.buildFaildResultVo();
        }
    }

    @Override
    public ResultVo openLendingOrder(OrderOpenReqVo reqVo) {
        ResultVo checkOpenLending = tradeChecker.checkOpenLending(reqVo);
        if (checkOpenLending.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return checkOpenLending;
        }
        UserLendingRecordPo lendingRecordPo = (UserLendingRecordPo) checkOpenLending.getData();

        try {
            return tradeHandler.handleOpenLending(lendingRecordPo, reqVo);
        } catch (Exception e) {
            log.error("openLendingOrder error.", e);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_POSI_FAILED.getCode(),ExceptionCode.OPEN_POSI_FAILED.getName());
        }
    }

    private CoreContractPosiPo calOpen(OrderOpenReqVo reqVo, CoreContractPo contract, BigDecimal indexPrice) {
        BigDecimal leverage = new BigDecimal(reqVo.getLeverage());
        BigDecimal realIndexPrice;
        // 开仓手续费
        BigDecimal openFee = contract.getOpenFeeRate()
                .multiply(reqVo.getOpenAmt())
                .multiply(leverage);
        // 预估强平价
        BigDecimal forceClosePrice;
        if (PosiSideEnum.BUY.getCode() == reqVo.getPosiSide()) {
            // 开多，实际成交价=indexPrice+bid_spread
            realIndexPrice = indexPrice.add(contract.getBidSpread());
            forceClosePrice = realIndexPrice.add(
                    new BigDecimal(reqVo.getPosiSide())
                            .multiply(realIndexPrice)
                            .multiply(
                                    leverage.multiply(contract.getCloseFeeRate())
                                            .subtract(contract.getMaxLossRatio())
                            )
                            .divide(leverage, 18, RoundingMode.UP)
            );
        } else {
            // 开空，实际成交价=indexPrice+ask_spread
            realIndexPrice = indexPrice.add(contract.getAskSpread());
            forceClosePrice = realIndexPrice.add(
                    new BigDecimal(reqVo.getPosiSide())
                            .multiply(realIndexPrice)
                            .multiply(
                                    leverage.multiply(contract.getCloseFeeRate())
                                            .subtract(contract.getMaxLossRatio())
                            )
                            .divide(leverage, 18, RoundingMode.DOWN)
            );
        }
        return CoreContractPosiPo.builder()
                .leverage(reqVo.getLeverage())
                .openAmt(reqVo.getOpenAmt())
                .openPrice(realIndexPrice)
                .openFee(openFee)
                .openFeeRate(contract.getOpenFeeRate())
                .forceClosePrice(forceClosePrice)
                .maxLossRatio(contract.getMaxLossRatio())
                .build();
    }

    private void calClose(OrderCloseReqVo reqVo, CoreContractPosiPo posi, CoreContractPo contract, BigDecimal indexPrice) {
        BigDecimal leverage = new BigDecimal(posi.getLeverage());
        BigDecimal realIndexPrice;
        if (PosiSideEnum.BUY.getCode() == posi.getPosiSide()) {
            // 平多，实际成交价=indexPrice+ask_spread
            realIndexPrice = indexPrice.add(contract.getAskSpread());
            // 做多，平仓时，当前指数价小于等于强平价，触发强平
            if (indexPrice.compareTo(posi.getForceClosePrice()) <= 0) {
                reqVo.setCloseType(CloseTypeEnum.FORCE_CLOSE.getCode());
            }
        } else {
            // 平空，实际成交价=indexPrice+bid_spread
            realIndexPrice = indexPrice.add(contract.getBidSpread());
            // 做空，平仓时，当前指数价大于等于强平价，触发强平
            if (indexPrice.compareTo(posi.getForceClosePrice()) >= 0) {
                reqVo.setCloseType(CloseTypeEnum.FORCE_CLOSE.getCode());
            }
        }
        // 平仓手续费 = 平仓手续费率 * 平仓价 * 杠杆倍数
        BigDecimal closeFee = contract.getCloseFeeRate()
                .multiply(posi.getOpenAmt())
                .multiply(leverage);
        // 平仓盈亏 = 持仓方向 * 本金 * （平仓价 - 开仓价）/ 开仓价 * 杠杆倍数
        BigDecimal tempCloseProfitLoss = new BigDecimal(posi.getPosiSide())
                .multiply(posi.getOpenAmt())
                .multiply(realIndexPrice.subtract(posi.getOpenPrice()))
                .multiply(leverage);
        BigDecimal closeProfitLoss;
        if (tempCloseProfitLoss.compareTo(BigDecimal.ZERO) > 0) {
            closeProfitLoss = tempCloseProfitLoss
                    .divide(posi.getOpenPrice(), 18, BigDecimal.ROUND_DOWN);
        } else {
            closeProfitLoss = tempCloseProfitLoss
                    .divide(posi.getOpenPrice(), 18, BigDecimal.ROUND_UP);
        }
        // 设置平仓数据
        posi.setClosePrice(realIndexPrice);
        posi.setCloseFeeRate(contract.getCloseFeeRate());
        posi.setCloseFee(closeFee);
        posi.setCloseProfitLoss(closeProfitLoss);
        Date date = new Date();
        posi.setCloseTime(date);
        posi.setUpdateTime(date);
    }

    private Integer getAgentUserId(Integer userId, Integer currencyId, OrderOpenReqVo reqVo) {
        // 根据son获取agent_user_id，并校验agent是否允许分成
        AgentUserCurrencyDto agentUserCurrencyDto = agentUserDbService.getAgentBySon(userId);
        if (null != agentUserCurrencyDto && CollectionUtils.isNotEmpty(agentUserCurrencyDto.getCurrencyList())) {
            for (AgentUserCurrencyWrapDto agentUserCurrencyWrapDto : agentUserCurrencyDto.getCurrencyList()) {
                if (agentUserCurrencyWrapDto.getCurrencyId() == currencyId.intValue()) {
                    // 都分成
                    if (agentUserCurrencyWrapDto.getIsRebate() == AgentIsRebateEnum.ALL_REBATE.getCode()) {
                        // 判断名义开仓金额是否超过限制
                        if (!this.isLimitOpenAmt(reqVo, agentUserCurrencyWrapDto)) {
                            // 代理人该币种允许分成，才返回代理人id
                            return agentUserCurrencyDto.getAgentUserId();
                        }
                    } else if (agentUserCurrencyWrapDto.getIsRebate() == AgentIsRebateEnum.BUY_REBATE.getCode()
                            && reqVo.getPosiSide() == PosiSideEnum.BUY.getCode()) { // 仅多头分成
                        if (!this.isLimitOpenAmt(reqVo, agentUserCurrencyWrapDto)) {
                            return agentUserCurrencyDto.getAgentUserId();
                        }
                    } else if (agentUserCurrencyWrapDto.getIsRebate() == AgentIsRebateEnum.SELL_REBATE.getCode()
                            && reqVo.getPosiSide() == PosiSideEnum.SELL.getCode()) { // 仅空头分成
                        if (!this.isLimitOpenAmt(reqVo, agentUserCurrencyWrapDto)) {
                            return agentUserCurrencyDto.getAgentUserId();
                        }
                    } else { // 都不分成
                        return null;
                    }
                }
            }
        }
        return null;
    }
    @AllArgsConstructor
    @Getter
    private enum AgentIsRebateEnum {
        /**
         * 代理分成模式
         */
        ALL_REBATE(0,"都分成"),
        UN_REBATE(1, "都不分成"),
        BUY_REBATE(2, "仅多头分成"),
        SELL_REBATE(3, "仅空投分成"),
        ;

        private int code;
        private String msg;

    }

    /**
     * 判断名义开仓金额是否超过限制
     *
     * @param reqVo
     * @param agentUserCurrencyWrapDto
     * @return
     */
    private boolean isLimitOpenAmt(OrderOpenReqVo reqVo, AgentUserCurrencyWrapDto agentUserCurrencyWrapDto) {
        // 名义开仓金额
        BigDecimal nominalOpenAmt = reqVo.getOpenAmt().multiply(new BigDecimal(reqVo.getLeverage()));
        if (nominalOpenAmt.compareTo(agentUserCurrencyWrapDto.getNominalMaxTradeAmt()) > 0) {
            // 开仓名义金额 > 配置的每单名义交易金额最大值，则不跟单
            return true;
        }
        return false;
    }
}

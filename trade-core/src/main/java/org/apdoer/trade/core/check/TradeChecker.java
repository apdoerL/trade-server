package org.apdoer.trade.core.check;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.common.service.util.SecurityContextUtil;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.config.CurrencyProperties;
import org.apdoer.trade.common.data.MasterPosiData;
import org.apdoer.trade.common.data.QuotData;
import org.apdoer.trade.common.data.external.ContractData;
import org.apdoer.trade.common.data.external.ContractLeverageData;
import org.apdoer.trade.common.data.external.OrderCurrencyData;
import org.apdoer.trade.common.db.mapper.UserLendingRecordMapper;
import org.apdoer.trade.common.db.model.po.*;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.enums.OrderStatusEnum;
import org.apdoer.trade.common.enums.PosiSideEnum;
import org.apdoer.trade.common.model.vo.*;
import org.apdoer.trade.common.model.vo.query.OrderHistoryQueryVo;
import org.apdoer.trade.common.model.vo.query.OrderHoldQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


/**
 * 交易相关校验
 * @author apdoer
 */
@Service
public class TradeChecker {

    @Value("${trade-server.debug}")
    public boolean debug;

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Resource
    private UserLendingRecordMapper userLendingRecordMapper;
    
    @Resource
    private CoreContractDbService cfdContractDbService;

    public ResultVo checkOpen(OrderOpenReqVo reqVo, BigDecimal indexPrice) {
        if (null == reqVo) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_EMPTY.getCode(),ExceptionCode.REQUEST_PARAM_EMPTY.getName());
        }
        if (null == reqVo.getContractId()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTRACT_ID_EMPTY.getCode(),ExceptionCode.CONTRACT_ID_EMPTY.getName());
        }
        if (null == reqVo.getStopProfitPrice()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getCode(),ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getName());
        }
        if (null == reqVo.getStopLossPrice()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_LOSS_PRICE_EMPTY.getCode(),ExceptionCode.STOP_LOSS_PRICE_EMPTY.getName());
        }
        // 获取合约参数
        CoreContractPo contract = ContractData.get(reqVo.getContractId());
        if (null == contract) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTRACT_NOT_EXIST.getCode(),ExceptionCode.CONTRACT_NOT_EXIST.getName());
        }
        if (null == reqVo.getPosiSide() || !PosiSideEnum.isValid(reqVo.getPosiSide())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.POSI_SIDE_ILLEGAL.getCode(),ExceptionCode.POSI_SIDE_ILLEGAL.getName());
        }
        if (null == reqVo.getCurrencyId()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.ORDER_CURRENCY_EMPTY.getCode(),ExceptionCode.ORDER_CURRENCY_EMPTY.getName());
        }
        //校验杠杠倍数是否合法
        if (null == reqVo.getLeverage() || !this.checkLeverageIsLegal(reqVo.getLeverage(), reqVo.getContractId())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.LEVERAGE_INVALID.getCode(),ExceptionCode.LEVERAGE_INVALID.getName());
        }
        //校验止损价是否合法
        if (reqVo.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0
        		&& !this.checkStopLossPriceIsLegal(reqVo.getPosiSide(), reqVo.getStopLossPrice(), indexPrice, contract.getPriceTick())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_LOSS_PRICE_EMPTY.getCode(),ExceptionCode.STOP_LOSS_PRICE_EMPTY.getName());
        }
        //校验止盈价是否合法
        if (reqVo.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0
        		&& !this.checkStopProfitPriceIsLegal(reqVo.getPosiSide(), reqVo.getStopProfitPrice(), indexPrice, contract.getPriceTick())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getCode(),ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getName());
        }
        // 查询可下单币种
        CoreContractOrderCurrencyPo orderCurrency = OrderCurrencyData.get(reqVo.getContractId(), reqVo.getCurrencyId());
        if (null == orderCurrency) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CURRENCY_NOT_ALLOW_ORDER.getCode(),ExceptionCode.CURRENCY_NOT_ALLOW_ORDER.getName());
        }
        if (null == reqVo.getOpenAmt()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_AMT_EMPTY.getCode(),ExceptionCode.OPEN_AMT_EMPTY.getName());
        }
        // 开仓金额最小交易量单位校验
        BigDecimal[] remainder = reqVo.getOpenAmt().divideAndRemainder(orderCurrency.getLotSize());
        if (remainder[1].compareTo(BigDecimal.ZERO) != 0) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_AMT_NOT_DIVISIBLE_LOT_SIZE.getCode(),ExceptionCode.OPEN_AMT_NOT_DIVISIBLE_LOT_SIZE.getName());
        }
        // 开仓金额范围校验
        if (reqVo.getOpenAmt().compareTo(orderCurrency.getMaxOrderAmt()) > 0 ||
                reqVo.getOpenAmt().compareTo(orderCurrency.getMinOrderAmt()) < 0) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_AMT_LIMIT.getCode(),ExceptionCode.OPEN_AMT_LIMIT.getName());
        }
        if (null == reqVo.getUserPrice()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_PRICE_EMPTY.getCode(),ExceptionCode.USER_PRICE_EMPTY.getName());
        }
        return ResultVoBuildUtils.buildSuccessResultVo(contract);
    }

    public ResultVo checkClose(OrderCloseReqVo reqVo) {
        if (null == reqVo) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_EMPTY.getCode(),ExceptionCode.REQUEST_PARAM_EMPTY.getName());
        }
        if (null == reqVo.getCloseType() || !CloseTypeEnum.isValid(reqVo.getCloseType())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CLOSE_TYPE_ILLEGAL.getCode(),ExceptionCode.CLOSE_TYPE_ILLEGAL.getName());
        }
        if (null == reqVo.getContractId()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTRACT_ID_EMPTY.getCode(),ExceptionCode.CONTRACT_ID_EMPTY.getName());
        }
        // 获取合约参数
        CoreContractPo contract = ContractData.get(reqVo.getContractId());
        if (null == contract) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTRACT_NOT_EXIST.getCode(),ExceptionCode.CONTRACT_NOT_EXIST.getName());
        }
        if (null == reqVo.getUuid()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.POSI_UUID_EMPTY.getCode(),ExceptionCode.POSI_UUID_EMPTY.getName());
        }
        if (null == reqVo.getUserPrice()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_PRICE_EMPTY.getCode(),ExceptionCode.USER_PRICE_EMPTY.getName());
        }
        return ResultVoBuildUtils.buildSuccessResultVo(contract);
    }

    public ResultVo checkStopProfitLossPriceIsLegal(OrderStopProfitLossReqVo requestVo) {
        if (null == requestVo.getContractId()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTRACT_ID_EMPTY.getCode(),ExceptionCode.CONTRACT_ID_EMPTY.getName());
        }
        if (null == requestVo.getUuid()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.POSI_UUID_EMPTY.getCode(),ExceptionCode.POSI_UUID_EMPTY.getName());
        }
        // 获取当前行情价
        BigDecimal indexPrice = QuotData.get(requestVo.getContractId());
        if (null == indexPrice) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRADE_SERVER_NOT_READY.getCode(),ExceptionCode.TRADE_SERVER_NOT_READY.getName());
        }
        // 获取合约参数
        CoreContractPo contract = ContractData.get(requestVo.getContractId());
        if (null == contract) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRADE_SERVER_NOT_READY.getCode(),ExceptionCode.TRADE_SERVER_NOT_READY.getName());
        }
        if (debug) {
            if (null == requestVo.getUserId()) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_EMPTY.getCode(),ExceptionCode.USER_ID_EMPTY.getName());
            }
        } else {
            Integer userId = securityContextUtil.getCurrentUserId();
            if (null != userId) {
            	requestVo.setUserId(userId);
            } else {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGIN.getCode(),ExceptionCode.USER_NOT_LOGIN.getName());
            }
        }
        CoreContractPosiPo posi = MasterPosiData.get(requestVo.getContractId(), requestVo.getUserId(), requestVo.getUuid());
        if (null == posi) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.POSI_NOT_EXIST.getCode(),ExceptionCode.POSI_NOT_EXIST.getName());
        }
        //校验止损价是否合法
        if (null != requestVo.getStopLossPrice() && requestVo.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0
        		&& !this.checkStopLossPriceIsLegal(posi.getPosiSide(), requestVo.getStopLossPrice(), indexPrice, contract.getPriceTick())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_LOSS_PRICE_EMPTY.getCode(),ExceptionCode.STOP_LOSS_PRICE_EMPTY.getName());
        }
        //校验止盈价是否合法
        if (null != requestVo.getStopProfitPrice() && requestVo.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0
        		&& !this.checkStopProfitPriceIsLegal(posi.getPosiSide(), requestVo.getStopProfitPrice(), indexPrice, contract.getPriceTick())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getCode(),ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getName());
        }
        return ResultVoBuildUtils.buildSuccessResultVo(posi);
    }
    
    public ResultVo checkOrderConditionIsLegal(OrderConditionReqVo requestVo) {
        if (null == requestVo) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_EMPTY.getCode(),ExceptionCode.REQUEST_PARAM_EMPTY.getName());
        }
        if (null == requestVo.getContractId()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTRACT_ID_EMPTY.getCode(),ExceptionCode.CONTRACT_ID_EMPTY.getName());
        }
        if (null == requestVo.getStopProfitPrice()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getCode(),ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getName());
        }
        if (null == requestVo.getStopLossPrice()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_LOSS_PRICE_EMPTY.getCode(),ExceptionCode.STOP_LOSS_PRICE_EMPTY.getName());
        }
        if (null == requestVo.getTriggerPrice()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRIGGER_PRICE_INVALID.getCode(),ExceptionCode.TRIGGER_PRICE_INVALID.getName());
        }
        // 获取合约参数
        CoreContractPo contract = ContractData.get(requestVo.getContractId());
        if (null == contract) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CONTRACT_NOT_EXIST.getCode(),ExceptionCode.CONTRACT_NOT_EXIST.getName());
        }
        if (null == requestVo.getPosiSide() || !PosiSideEnum.isValid(requestVo.getPosiSide())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.POSI_SIDE_ILLEGAL.getCode(),ExceptionCode.POSI_SIDE_ILLEGAL.getName());
        }
        if (null == requestVo.getCurrencyId()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.ORDER_CURRENCY_EMPTY.getCode(),ExceptionCode.ORDER_CURRENCY_EMPTY.getName());
        }
        //校验杠杠倍数是否合法
        if (null == requestVo.getLeverage() || !this.checkLeverageIsLegal(requestVo.getLeverage(), requestVo.getContractId())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.LEVERAGE_INVALID.getCode(),ExceptionCode.LEVERAGE_INVALID.getName());
        }
        //校验触发价是否合法
        if (null == requestVo.getTriggerPrice() || !this.checkPriceIsLegal(requestVo.getTriggerPrice(), contract.getPriceTick())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.TRIGGER_PRICE_INVALID.getCode(),ExceptionCode.TRIGGER_PRICE_INVALID.getName());
        }
        //校验止损价是否合法
        if (requestVo.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0
        		&& !this.checkStopLossPriceIsLegal(requestVo.getPosiSide(), requestVo.getStopLossPrice(), requestVo.getTriggerPrice(), contract.getPriceTick())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_LOSS_PRICE_EMPTY.getCode(),ExceptionCode.STOP_LOSS_PRICE_EMPTY.getName());
        }
        //校验止盈价是否合法
        if (requestVo.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0
        		&& !this.checkStopProfitPriceIsLegal(requestVo.getPosiSide(), requestVo.getStopProfitPrice(), requestVo.getTriggerPrice(), contract.getPriceTick())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getCode(),ExceptionCode.STOP_PROFIT_PRICE_EMPTY.getName());
        }
        // 查询可下单币种
        CoreContractOrderCurrencyPo orderCurrency = OrderCurrencyData.get(requestVo.getContractId(), requestVo.getCurrencyId());
        if (null == orderCurrency) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CURRENCY_NOT_ALLOW_ORDER.getCode(),ExceptionCode.CURRENCY_NOT_ALLOW_ORDER.getName());
        }
        if (null == requestVo.getOpenAmt()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_AMT_EMPTY.getCode(),ExceptionCode.OPEN_AMT_EMPTY.getName());
        }
        // 开仓金额最小交易量单位校验
        BigDecimal[] remainder = requestVo.getOpenAmt().divideAndRemainder(orderCurrency.getLotSize());
        if (remainder[1].compareTo(BigDecimal.ZERO) != 0) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_AMT_NOT_DIVISIBLE_LOT_SIZE.getCode(),ExceptionCode.OPEN_AMT_NOT_DIVISIBLE_LOT_SIZE.getName());
        }
        // 开仓金额范围校验
        if (requestVo.getOpenAmt().compareTo(orderCurrency.getMaxOrderAmt()) > 0 || requestVo.getOpenAmt().compareTo(orderCurrency.getMinOrderAmt()) < 0) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.OPEN_AMT_LIMIT.getCode(),ExceptionCode.OPEN_AMT_LIMIT.getName());
        }
        return ResultVoBuildUtils.buildSuccessResultVo(contract);
    }
    
    public ResultVo checkOrderConditionCancelIsLegal(OrderConditionCancelReqVo requestVo) {
    	if (null == requestVo.getOrderId()) {
    		return ResultVoBuildUtils.buildFaildResultVo();
    	}
    	CoreContractOrderPo order = this.cfdContractDbService.queryOrder(requestVo.getOrderId(), requestVo.getUserId(), OrderStatusEnum.WAIT.getCode());
    	if (null == order) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.ORDERID_INVALID.getCode(),ExceptionCode.ORDERID_INVALID.getName());
    	}
    	  return ResultVoBuildUtils.buildSuccessResultVo(order);
    }
    

    public ResultVo checkHold(OrderHoldQueryVo queryVo) {
        if (null == queryVo) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_EMPTY.getCode(),ExceptionCode.REQUEST_PARAM_EMPTY.getName());
        }
        if (debug) {
            if (null == queryVo.getUserId()) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_EMPTY.getCode(),ExceptionCode.USER_ID_EMPTY.getName());
            }
        } else {
            Integer userId = securityContextUtil.getCurrentUserId();
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGIN.getCode(),ExceptionCode.USER_NOT_LOGIN.getName());
            }
            queryVo.setUserId(userId);
        }
        return ResultVoBuildUtils.buildSuccessResultVo();
    }

    public ResultVo checkHistory(OrderHistoryQueryVo queryVo) {
        if (null == queryVo) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_EMPTY.getCode(),ExceptionCode.REQUEST_PARAM_EMPTY.getName());
        }
        if (null == queryVo.getQueryDate()) {
            queryVo.setQueryDate(System.currentTimeMillis());
        }
        if (debug) {
            if (null == queryVo.getUserId()) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_EMPTY.getCode(),ExceptionCode.USER_ID_EMPTY.getName());
            }
        } else {
            Integer userId = securityContextUtil.getCurrentUserId();
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_NOT_LOGIN.getCode(),ExceptionCode.USER_NOT_LOGIN.getName());
            }
            queryVo.setUserId(userId);
        }
        return ResultVoBuildUtils.buildSuccessResultVo();
    }

    public ResultVo checkOpenLending(OrderOpenReqVo reqVo) {
        if (null == reqVo) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.REQUEST_PARAM_EMPTY.getCode(),ExceptionCode.REQUEST_PARAM_EMPTY.getName());
        }
        if (CurrencyProperties.LENDING_CURRENCY_ID != reqVo.getCurrencyId()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.ORDER_CURRENCY_NOT_LENDING.getCode(),ExceptionCode.ORDER_CURRENCY_NOT_LENDING.getName());
        }
        if (StringUtils.isBlank(reqVo.getLendNo())) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.ORDER_CURRENCY_NOT_LENDING.getCode(),ExceptionCode.ORDER_CURRENCY_NOT_LENDING.getName());
        }
        UserLendingRecordPo lendingRecordPo = userLendingRecordMapper.selectByPrimaryKey(reqVo.getLendNo());
        if (null == lendingRecordPo ) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.LENDING_NOT_EXIST.getCode(),ExceptionCode.LENDING_NOT_EXIST.getName());
        }
        if (LendingUsedEnum.UN_USED.getCode() != lendingRecordPo.getIsUsed()) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.LENDING_INVALID.getCode(),ExceptionCode.LENDING_INVALID.getName());
        }
        if (lendingRecordPo.getUserId() != reqVo.getUserId().intValue()) {
            // 该笔赠金不属于该用户
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.LENDING_NOT_BELONG_USER.getCode(),ExceptionCode.LENDING_NOT_BELONG_USER.getName());
        }
        // 设置开仓金额，赠金金额 all in
        reqVo.setOpenAmt(lendingRecordPo.getNotRecycleNum());
        return ResultVoBuildUtils.buildSuccessResultVo(lendingRecordPo);
    }

    @AllArgsConstructor
    @Getter
    public enum LendingUsedEnum {
        /**
         * 赠金使用类型
         */
        USED(0, "已使用"),
        UN_USED(1, "未使用"),
        EXPIRE(2, "已过期"),
        ;


        private int code;
        private String msg;

    }


    /**
     * 校验
     * @param leverage
     * @param contractId
     * @return
     */
    private boolean checkLeverageIsLegal(Integer leverage, Integer contractId) {
    	List<CoreContractLeveragePo> list = ContractLeverageData.get(contractId);
    	if (null != list) {
    		for (CoreContractLeveragePo leverge : list) {
    			if (leverge.getLeverage().intValue() == leverage.intValue()) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    /**
     * 校验止损价格，是否合法
     * @param stopLossPrice
     * @param indexPrice
     * @return
     */
    private boolean checkStopLossPriceIsLegal(Integer posiSide, BigDecimal stopLossPrice, BigDecimal indexPrice, BigDecimal priceTick) {
    	if (!this.checkPriceIsLegal(stopLossPrice, priceTick)) {
    		return false;
    	}
    	if (PosiSideEnum.BUY.getCode() == posiSide) {
    		//买开仓，止损价格需低于当前指数价格
            return stopLossPrice.compareTo(indexPrice) < 0;
    	} else {
    		//卖开仓，止损价格需高于当前指数价格
            return stopLossPrice.compareTo(indexPrice) > 0;
    	}
    }

    /**
     * 校验止盈价格，是否合法
     * @param priceTick
     * @param stopProfitPrice
     * @param indexPrice
     * @return
     */
    private boolean checkStopProfitPriceIsLegal(Integer posiSide, BigDecimal stopProfitPrice, BigDecimal indexPrice, BigDecimal priceTick) {
    	if (!this.checkPriceIsLegal(stopProfitPrice, priceTick)) {
    		return false;
    	}
    	if (PosiSideEnum.BUY.getCode() == posiSide.intValue()) {
    		//买开仓，止赢价格需低于当前指数价格
    		if (stopProfitPrice.compareTo(indexPrice) > 0) {
    			return true;
    		} else {
    			return false;
    		}
    	} else {
    		//卖开仓，止盈价格需高于当前指数价格
    		if (stopProfitPrice.compareTo(indexPrice) < 0) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    }


	/**
	 * 报价是否合法
	 *
	 * @param price
	 * @param priceTick
	 * @return
	 */
	private boolean checkPriceIsLegal(BigDecimal price, BigDecimal priceTick) {
		if (null == priceTick || priceTick.compareTo(new BigDecimal(0)) == 0) {
			return true;
		}
		// price是否是priceTick的整数倍
		BigDecimal priceRemainder = price.divideAndRemainder(priceTick)[1];
		if (priceRemainder.compareTo(BigDecimal.ZERO) == 0) {
			return true;
		} else {
			return false;
		}
	}
}

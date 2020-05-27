package org.apdoer.trade.job.controller;

import org.apdoer.common.service.annotation.RequiresIdempotency;
import org.apdoer.common.service.annotation.RequiresPermissions;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.model.vo.*;
import org.apdoer.trade.common.model.vo.query.OrderHistoryQueryVo;
import org.apdoer.trade.common.model.vo.query.OrderHoldQueryVo;
import org.apdoer.trade.core.service.TradeService;
import org.apdoer.trade.job.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 交易
 * @author apdoer
 */
@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserHandler userHandler;

    /**
     * 开仓
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/order/open")
    public ResultVo openOrder(@RequestBody OrderOpenReqVo reqVo) {
        ResultVo openUser = userHandler.getOpenUser(reqVo);
        if (openUser.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return openUser;
        }
        return tradeService.openOrder(reqVo);
    }

    /**
     * 平仓
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/order/close")
    public ResultVo closeOrder(@RequestBody OrderCloseReqVo reqVo) {
        ResultVo closeUser = userHandler.getCloseUser(reqVo);
        if (closeUser.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return closeUser;
        }
        return tradeService.closeOrder(reqVo);
    }

    /**
     * 止盈止损
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/order/profitLoss")
    public ResultVo stopProfitLossOrder(@RequestBody OrderStopProfitLossReqVo requestVo) {
    	return this.tradeService.stopProfitLossOrder(requestVo);
    }
    
    /**
     * 条件单-开单
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/order/condition")
    public ResultVo orderCondition(@RequestBody OrderConditionReqVo requestVo) {
        ResultVo openUser = this.userHandler.getUser(requestVo);
        if (openUser.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return openUser;
        }
    	return this.tradeService.orderCondition(requestVo);
    }
    
    /**
     * 条件单-撤单
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/order/conditionCancel")
    public ResultVo orderConditionCancel(@RequestBody OrderConditionCancelReqVo requestVo) {
        ResultVo openUser = this.userHandler.getUser(requestVo);
        if (openUser.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return openUser;
        }
    	return this.tradeService.orderConditionCancel(requestVo);
    }
    
    /**
     * 条件单-当前查询
     */
    @GetMapping("/order/conditionQuery")
    public ResultVo orderConditionQuery(@RequestParam(name="userId", required=false) Integer userId,
    		@RequestParam(name="contractId", required=false) Integer contractId) {
        ResultVo openUser = this.userHandler.getUser(userId);
        if (openUser.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return openUser;
        } else {
        	userId = (Integer) openUser.getData();
        }
		return this.tradeService.orderConditionQuery(userId, contractId);
    }
    
    /**
     * 条件单-历史查询
     */
    @GetMapping("/order/conditionHisQuery")
    public ResultVo orderConditionHistoryQuery(@RequestParam(name="userId", required=false) Integer userId,
    		@RequestParam(name="contractId", required=false) Integer contractId,
    		@RequestParam(name = "side", required=false) Integer side,
    		@RequestParam(name = "status", required=false) Integer status,
    		@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "100") int pageSize) {
        ResultVo openUser = this.userHandler.getUser(userId);
        if (openUser.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return openUser;
        } else {
        	userId = (Integer) openUser.getData();
        }
        return this.tradeService.orderConditionHistoryQuery(userId, contractId, side, status, pageNum, pageSize);
    }

    /**
     * 赠金开仓
     */
    @RequiresIdempotency
    @RequiresPermissions(value = {"order:place"})
    @PostMapping("/lending/order/open")
    public ResultVo openLendingOrder(@RequestBody OrderOpenReqVo reqVo) {
        ResultVo openUser = userHandler.getOpenUser(reqVo);
        if (openUser.getCode() != ExceptionCode.SUCCESS.getCode()) {
            return openUser;
        }
        return tradeService.openLendingOrder(reqVo);
    }
    
    /**
     * 查询当前持仓
     */
    @GetMapping("/order/hold")
    public ResultVo holdOrderList(OrderHoldQueryVo queryVo) {
        return tradeService.holdOrderList(queryVo);
    }

    /**
     * 查询历史持仓
     */
    @GetMapping("/order/history")
    public ResultVo historyOrderList(OrderHistoryQueryVo queryVo) {
        return tradeService.historyOrderList(queryVo);
    }
}

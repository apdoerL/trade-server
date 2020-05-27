package org.apdoer.trade.core.service;

import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.trade.common.model.vo.*;
import org.apdoer.trade.common.model.vo.query.OrderHistoryQueryVo;
import org.apdoer.trade.common.model.vo.query.OrderHoldQueryVo;

public interface TradeService {

    /**
     * 开仓
     *
     * @param reqVo
     * @return
     */
    ResultVo openOrder(OrderOpenReqVo reqVo);

    /**
     * 平仓
     *
     * @param reqVo
     * @return
     */
    ResultVo closeOrder(OrderCloseReqVo reqVo);

    /**
     * 止盈止损委托
     *
     * @param requestVo
     * @return
     */
    ResultVo stopProfitLossOrder(OrderStopProfitLossReqVo requestVo);

    /**
     * 条件单-开单
     *
     * @param requestVo
     * @return
     */
    ResultVo orderCondition(OrderConditionReqVo requestVo);

    /**
     * 條件單-撤單
     *
     * @param requestVo
     * @return
     */
    ResultVo orderConditionCancel(OrderConditionCancelReqVo requestVo);

    /**
     * 条件单-查询
     *
     * @param contractId
     * @return
     */
    ResultVo orderConditionQuery(Integer userId, Integer contractId);

    /**
     * 条件单-历史查询
     *
     * @param userId
     * @param contractId
     * @param side
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultVo orderConditionHistoryQuery(Integer userId, Integer contractId, Integer side, Integer status, int pageNum, int pageSize);

    /**
     * 查询当前持仓
     *
     * @param queryVo
     * @return
     */
    ResultVo holdOrderList(OrderHoldQueryVo queryVo);

    /**
     * 查询历史持仓
     *
     * @param queryVo
     * @return
     */
    ResultVo historyOrderList(OrderHistoryQueryVo queryVo);

    /**
     * 赠金开仓
     *
     * @param reqVo
     * @return
     */
    ResultVo openLendingOrder(OrderOpenReqVo reqVo);
}

package org.apdoer.trade.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderStopProfitLossReqVo {

    // 用户id
    private Integer userId;

    // 持仓uuid
    private Long uuid;

    // 合约id
    private Integer contractId;

    private BigDecimal stopLossPrice;

    private BigDecimal stopProfitPrice;

}
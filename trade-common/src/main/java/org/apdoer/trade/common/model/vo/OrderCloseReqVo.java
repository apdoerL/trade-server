package org.apdoer.trade.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 平仓请求
 * @author apdoer
 */
@Data
public class OrderCloseReqVo {
    // 平仓类型
    private Integer closeType;
    // 合约id
    private Integer contractId;
    // 用户id
    private Integer userId;
    // 持仓uuid
    private Long uuid;
    // 用户价格
    private BigDecimal userPrice;
}
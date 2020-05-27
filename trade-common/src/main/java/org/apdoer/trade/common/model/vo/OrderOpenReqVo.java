package org.apdoer.trade.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 开仓请求
 *
 * @author apdoer
 */
@Data
public class OrderOpenReqVo {
    // 合约id
    private Integer contractId;
    // 持仓方向
    private Integer posiSide;
    // 下单币种id
    private Integer currencyId;
    // 开仓金额
    private BigDecimal openAmt;
    // 杠杆倍数
    private Integer leverage;
    // 止盈价，-1未设置
    private BigDecimal stopProfitPrice;
    // 止损价，-1未设置
    private BigDecimal stopLossPrice;
    // 用户价格
    private BigDecimal userPrice;
    // 用户id
    private Integer userId;
    // 赠金编号
    private String lendNo;
}

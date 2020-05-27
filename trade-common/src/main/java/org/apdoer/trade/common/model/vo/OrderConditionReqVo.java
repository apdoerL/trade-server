package org.apdoer.trade.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderConditionReqVo {
	
    // 用户id，debug模式
    private Integer userId;
    
    // 合约id
    private Integer contractId;
    
    // 下单币种id
    private Integer currencyId;
    
    // 持仓方向
    private Integer posiSide;

    // 开仓金额
    private BigDecimal openAmt;
    
    // 杠杆倍数
    private Integer leverage;
    
    // 触发价格
    private BigDecimal triggerPrice;
    
    // 止盈价，-1未设置
    private BigDecimal stopProfitPrice;
    
    // 止损价，-1未设置
    private BigDecimal stopLossPrice;

}
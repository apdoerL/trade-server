package org.apdoer.trade.common.model.vo;

import lombok.Data;

@Data
public class OrderConditionCancelReqVo {
	
    // 用户id，debug模式
    private Integer userId;
    
    //委託ID
    private Long orderId;

}
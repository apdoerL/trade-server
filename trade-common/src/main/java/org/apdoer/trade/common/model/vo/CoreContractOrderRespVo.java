package org.apdoer.trade.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author apdoer
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CoreContractOrderRespVo {
	
    private String orderId;

    private String posiId;

    private Integer userId;

    private Integer contractId;

    private Integer currencyId;

    private Integer posiSide;

    private BigDecimal openAmt;

    private Integer leverage;

    private BigDecimal triggerPrice;

    private BigDecimal realTriggerPrice;

    private BigDecimal stopProfitPrice;

    private BigDecimal stopLossPrice;

    private Integer status;

    private Date createTime;

    private Date updateTime;
    
}
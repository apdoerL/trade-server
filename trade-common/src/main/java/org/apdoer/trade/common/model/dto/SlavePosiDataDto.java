package org.apdoer.trade.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 从持仓数据区 数据,用于判定能否触发
 * @author apdoer
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SlavePosiDataDto {
	
	//持仓ID
    private Long uuid;
    //持仓用户
    private Integer userId;
    //持仓合约
    private Integer contractId;
    //持仓方向
    private Integer posiSide;
    //触发价
    private BigDecimal triggerPrice;
}

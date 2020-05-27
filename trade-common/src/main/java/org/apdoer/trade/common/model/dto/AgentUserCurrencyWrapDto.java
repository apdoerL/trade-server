package org.apdoer.trade.common.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 代理人
 * @author apdoer
 */
@Data
public class AgentUserCurrencyWrapDto {

    private Integer currencyId;
    // 分成模式，0都分成，1都不分成，2仅多头分成，3仅空头分成
    private Integer isRebate;
    // 每单名义交易金额最大值（即本金*杠杆），超过该值则该单不参与代理跟单
    private BigDecimal nominalMaxTradeAmt;
}

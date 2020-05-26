package org.apdoer.trade.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractCurrencyWrapRespVo {
    private Integer currencyId;

    private BigDecimal minOrderAmt;

    private BigDecimal maxOrderAmt;

    private BigDecimal lotSize;
}

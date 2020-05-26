package org.apdoer.trade.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ContractCurrencyRespVo {
    private Integer contractId;

    private String symbol;

    private BigDecimal openFeeRate;

    private BigDecimal closeFeeRate;

    private BigDecimal fundingFeeRate;

    private BigDecimal priceTick;

    private BigDecimal bidSpread;

    private BigDecimal askSpread;

    private BigDecimal maxLossRatio;

    private Integer clearInterval;

    private List<ContractCurrencyWrapRespVo> orderCurrencyList;

    // 杠杆列表
    private List<Integer> leverageList;
}

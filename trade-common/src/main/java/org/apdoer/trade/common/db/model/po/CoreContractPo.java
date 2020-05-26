package org.apdoer.trade.common.db.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(
        name = "core_contract"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoreContractPo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(
            name = "contract_id"
    )
    private Integer contractId;
    private String symbol;
    @Column(
            name = "open_fee_rate"
    )
    private BigDecimal openFeeRate;
    @Column(
            name = "close_fee_rate"
    )
    private BigDecimal closeFeeRate;
    @Column(
            name = "funding_fee_rate"
    )
    private BigDecimal fundingFeeRate;
    @Column(
            name = "price_tick"
    )
    private BigDecimal priceTick;
    @Column(
            name = "bid_spread"
    )
    private BigDecimal bidSpread;
    @Column(
            name = "ask_spread"
    )
    private BigDecimal askSpread;
    @Column(
            name = "max_loss_ratio"
    )
    private BigDecimal maxLossRatio;
    @Column(
            name = "clear_interval"
    )
    private Integer clearInterval;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "update_time"
    )
    private Date updateTime;
}
package org.apdoer.trade.common.db.model.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.entity.IDynamicTableName;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 持仓数据
 * @author apdoer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoreContractPosiPo implements Serializable, IDynamicTableName {
    private static final long serialVersionUID = 1L;
    @Id
    private Long uuid;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "currency_id")
    private Integer currencyId;

    @Column(name = "posi_side")
    private Integer posiSide;

    @Column(name = "open_amt")
    private BigDecimal openAmt;

    private Integer leverage;

    private Integer status;

    @Column(name = "open_price")
    private BigDecimal openPrice;

    @Column(name = "open_fee_rate")
    private BigDecimal openFeeRate;

    @Column(name = "open_fee")
    private BigDecimal openFee;

    @Column(name = "close_price")
    private BigDecimal closePrice;

    @Column(name = "close_fee_rate")
    private BigDecimal closeFeeRate;

    @Column(name = "close_fee")
    private BigDecimal closeFee;

    @Column(name = "close_profit_loss")
    private BigDecimal closeProfitLoss;

    @Column(name = "close_time")
    private Date closeTime;

    @Column(name = "funding_fee")
    private BigDecimal fundingFee;

    @Column(name = "stop_profit_price")
    private BigDecimal stopProfitPrice;

    @Column(name = "stop_loss_price")
    private BigDecimal stopLossPrice;

    @Column(name = "max_loss_ratio")
    private BigDecimal maxLossRatio;

    @Column(name = "force_close_price")
    private BigDecimal forceClosePrice;

    @Column(name = "risk_funding")
    private BigDecimal riskFunding;

    @Column(name = "follow_user_id")
    private Integer followUserId;

    @Column(name = "follow_posi_id")
    private Long followPosiId;

    @Column(name = "agent_user_id")
    private Integer agentUserId;

    @Column(name = "is_throw_out")
    private Integer isThrowOut;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    @JsonIgnore
    private String tableName;

    @Override
    @JsonIgnore
    public String getDynamicTableName() {
        return this.tableName;
    }
}
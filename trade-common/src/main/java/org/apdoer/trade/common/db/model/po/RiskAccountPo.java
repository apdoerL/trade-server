package org.apdoer.trade.common.db.model.po;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(
    name = "web_risk_account"
)
@Data
@Builder
public class RiskAccountPo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(
            name = "currency_id"
    )
    private Integer currencyId;
    @Column(
            name = "risk_fund_balance"
    )
    private BigDecimal riskFundBalance;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "update_time"
    )
    private Date updateTime;
}
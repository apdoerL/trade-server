package org.apdoer.trade.common.db.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.DeclareAnnotation;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(
        name = "core_contract_order_currency"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoreContractOrderCurrencyPo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(
            name = "contract_id"
    )
    private Integer contractId;
    @Id
    @Column(
            name = "currency_id"
    )
    private Integer currencyId;
    @Column(
            name = "min_order_amt"
    )
    private BigDecimal minOrderAmt;
    @Column(
            name = "max_order_amt"
    )
    private BigDecimal maxOrderAmt;
    @Column(
            name = "lot_size"
    )
    private BigDecimal lotSize;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "update_time"
    )
    private Date updateTime;
}
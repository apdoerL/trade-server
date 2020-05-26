package org.apdoer.trade.common.db.model.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tk.mybatis.mapper.entity.IDynamicTableName;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author apdeor 合约下单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CoreContractOrderPo implements Serializable, IDynamicTableName {
    private static final long serialVersionUID = 1759578827400446415L;
    @Id
    private Long orderId;
    @Column(
            name = "posi_id"
    )
    private Long posiId;
    @Column(
            name = "user_id"
    )
    private Integer userId;
    @Column(
            name = "contract_id"
    )
    private Integer contractId;
    @Column(
            name = "currency_id"
    )
    private Integer currencyId;
    @Column(
            name = "posi_side"
    )
    private Integer posiSide;
    @Column(
            name = "open_amt"
    )
    private BigDecimal openAmt;
    @Column(
            name = "leverage"
    )
    private Integer leverage;
    @Column(
            name = "trigger_price"
    )
    private BigDecimal triggerPrice;
    @Column(
            name = "real_trigger_price"
    )
    private BigDecimal realTriggerPrice;
    @Column(
            name = "stop_profit_price"
    )
    private BigDecimal stopProfitPrice;
    @Column(
            name = "stop_loss_price"
    )
    private BigDecimal stopLossPrice;
    @Column(
            name = "status"
    )
    private Integer status;
    @Column(
            name = "create_time"
    )
    private Date createTime;
    @Column(
            name = "update_time"
    )
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
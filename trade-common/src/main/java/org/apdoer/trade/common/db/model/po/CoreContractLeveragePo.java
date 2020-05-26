package org.apdoer.trade.common.db.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(
    name = "core_contract_leverage"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoreContractLeveragePo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(
        name = "contract_id"
    )
    private Integer contractId;
    @Id
    private Integer leverage;
    @Column(
        name = "create_time"
    )
    private Date createTime;
    @Column(
        name = "update_time"
    )
    private Date updateTime;
}
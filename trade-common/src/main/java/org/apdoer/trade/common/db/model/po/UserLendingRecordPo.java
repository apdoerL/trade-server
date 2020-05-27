package org.apdoer.trade.common.db.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 赠金记录
 * @author apdoer
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "tu_user_lending_record")
public class UserLendingRecordPo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String lendNo;

    private Integer userId;

    private Integer currencyId;

    private BigDecimal num;

    private BigDecimal recycleNum;

    private BigDecimal notRecycleNum;

    private String reason;

    private Integer lendingStatus;

    private Integer flowType;

    private Integer isUsed;

    private Integer operatorId;

    private Date updateTime;

    private Date createTime;

}
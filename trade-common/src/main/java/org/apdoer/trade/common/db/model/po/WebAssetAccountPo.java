package org.apdoer.trade.common.db.model.po;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author apdoer
 *
 */
@Table(
        name = "web_asset_account"
)
@Data
@Builder
public class WebAssetAccountPo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;
    @Column(
            name = "user_id"
    )
    private Integer userId;
    @Column(
            name = "currency_id"
    )
    private Integer currencyId;
    @Column(
            name = "available"
    )
    private BigDecimal available;
    @Column(
            name = "locked"
    )
    private BigDecimal locked;
    @Column(
            name = "update_time"
    )
    private Date updateTime;
}
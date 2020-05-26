package org.apdoer.trade.common.db.model.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author apdoer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "core_contract_channel_mapping")
public class ContractChannelMappingPo implements Serializable {
    private static final long serialVersionUID = 4352978903826326753L;
    @Id
    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "quot_channel")
    private String quotChannel;

    @Column(name = "index_price_listener")
    private String indexPriceListener;

    @Column(name = "fl_channel")
    private String flChannel;

    @Column(name = "fl_listener")
    private String flListener;

    @Column(name = "price_cache_listener")
    private String priceCacheListener;

    @Column(name = "stop_profit_index_listener")
    private String stopProfitIndexListener;

    @Column(name = "stop_profit_channel")
    private String stopProfitChannel;

    @Column(name = "stop_profit_listener")
    private String stopProfitListener;

    @Column(name = "stop_loss_index_listener")
    private String stopLossIndexListener;

    @Column(name = "stop_loss_channel")
    private String stopLossChannel;

    @Column(name = "stop_loss_listener")
    private String stopLossListener;

    @Column(name = "condition_order_index_listener")
    private String conditionOrderIndexListener;

    @Column(name = "condition_order_channel")
    private String conditionOrderChannel;

    @Column(name = "condition_order_listener")
    private String conditionOrderListener;

    @Column(name = "description")
    private String description;

    @Column(name = "create_time")
    private Date createTime;
}
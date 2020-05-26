package org.apdoer.trade.common.model.dto;

import org.apdoer.trade.common.utils.NumberUtil;

import java.math.BigDecimal;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:03
 */
public class IndexPriceDto {

    /**
     * 消息类型：1001
     */
    private Integer messageType;
    /**
     * 合约ID
     */
    private Integer contractId;
    /**
     * 最新指数价格
     */
    private BigDecimal indexPrice;
    /**
     * 时间：微秒
     */
    private Long time;

    public Integer getMessageType() {
        return messageType;
    }
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    public Integer getContractId() {
        return contractId;
    }
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }
    public BigDecimal getIndexPrice() {
        return indexPrice;
    }
    public void setIndexPrice(String _indexPrice) {
        this.indexPrice = new BigDecimal(NumberUtil.toSmall(_indexPrice));
    }
    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
}

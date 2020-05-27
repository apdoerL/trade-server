package org.apdoer.trade.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PosiListRespVo {
    private String uuid;
    private Integer userId;
    private Integer contractId;
    private Integer currencyId;
    private Integer posiSide;
    private BigDecimal openAmt;
    private Integer leverage;
    private Integer status;
    private BigDecimal openPrice;
    private BigDecimal openFeeRate;
    private BigDecimal openFee;
    private BigDecimal closePrice;
    private BigDecimal closeFeeRate;
    private BigDecimal closeFee;
    private BigDecimal closeProfitLoss;
    private Date closeTime;
    private BigDecimal fundingFee;
    private BigDecimal stopProfitPrice;
    private BigDecimal stopLossPrice;
    private BigDecimal maxLossRatio;
    private BigDecimal forceClosePrice;
    private BigDecimal riskFunding;
    private Integer followUserId;
    private Long followPosiId;
    private Integer agentUserId;
    private Integer isThrowOut;
    private Date createTime;
    private Date updateTime;

    public void setUuid(Long uuid) {
        this.uuid = uuid.toString();
    }

    public static PosiListRespVo convertFrom(CoreContractPosiPo posiPo) {
        PosiListRespVo convert = PosiListRespVo.builder()
                .userId(posiPo.getUserId())
                .contractId(posiPo.getContractId())
                .currencyId(posiPo.getCurrencyId())
                .posiSide(posiPo.getPosiSide())
                .openAmt(posiPo.getOpenAmt())
                .leverage(posiPo.getLeverage())
                .status(posiPo.getStatus())
                .openPrice(posiPo.getOpenPrice())
                .openFeeRate(posiPo.getOpenFeeRate())
                .openFee(posiPo.getOpenFee())
                .closePrice(posiPo.getClosePrice())
                .closeFeeRate(posiPo.getCloseFeeRate())
                .closeFee(posiPo.getCloseFee())
                .closeProfitLoss(posiPo.getCloseProfitLoss())
                .closeTime(posiPo.getCloseTime())
                .fundingFee(posiPo.getFundingFee())
                .stopProfitPrice(posiPo.getStopProfitPrice())
                .stopLossPrice(posiPo.getStopLossPrice())
                .maxLossRatio(posiPo.getMaxLossRatio())
                .forceClosePrice(posiPo.getForceClosePrice())
                .riskFunding(posiPo.getRiskFunding())
                .followUserId(posiPo.getFollowUserId())
                .followPosiId(posiPo.getFollowPosiId())
                .agentUserId(posiPo.getAgentUserId())
                .isThrowOut(posiPo.getIsThrowOut())
                .createTime(posiPo.getCreateTime())
                .updateTime(posiPo.getUpdateTime())
                .build();
        convert.setUuid(posiPo.getUuid());
        return convert;
    }
}
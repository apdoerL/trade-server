package org.apdoer.trade.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.trade.common.db.model.po.RiskAccountPo;

import java.math.BigDecimal;

public interface RiskAccountMapper extends BaseMapper<RiskAccountPo> {

    int insertOrUpdate(@Param("currencyId") Integer currencyId, @Param("riskFund") BigDecimal riskFund);
}
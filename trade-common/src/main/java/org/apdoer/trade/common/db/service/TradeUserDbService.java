package org.apdoer.trade.common.db.service;

import org.apdoer.trade.common.db.model.po.WebAssetAccountPo;

import java.math.BigDecimal;

public interface TradeUserDbService {

    WebAssetAccountPo getAccountForUpdate(Integer userId, Integer currencyId);

    void addUserAccount(Integer userId, Integer currencyId, BigDecimal amt);

    int updateUserAccount(Integer userId, Integer currencyId, BigDecimal amt);

    int insertRiskFund(Integer currencyId, BigDecimal riskFund);
    
    int updateAccount(WebAssetAccountPo account);
}

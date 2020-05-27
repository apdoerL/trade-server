package org.apdoer.trade.common.db.service.impl;

import org.apdoer.trade.common.db.mapper.RiskAccountMapper;
import org.apdoer.trade.common.db.mapper.WebAssetAccountMapper;
import org.apdoer.trade.common.db.model.po.WebAssetAccountPo;
import org.apdoer.trade.common.db.service.TradeUserDbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class TradeUserDbServiceImpl implements TradeUserDbService {

    @Resource
    private WebAssetAccountMapper webAssetAccountMapper;

    @Resource
    private RiskAccountMapper riskAccountMapper;

    @Override
    public WebAssetAccountPo getAccountForUpdate(Integer userId, Integer currencyId) {
        return webAssetAccountMapper.getUserAccountForUpdate(userId, currencyId);
    }

    @Transactional
    @Override
    public void addUserAccount(Integer userId, Integer currencyId, BigDecimal amt) {
        WebAssetAccountPo userAccount = webAssetAccountMapper.getUserAccountForUpdate(userId, currencyId);
        if (null == userAccount) {
            webAssetAccountMapper.insertSelective(
                    WebAssetAccountPo.builder()
                            .userId(userId)
                            .currencyId(currencyId)
                            .available(amt)
                            .build()
            );
        } else {
            userAccount.setAvailable(userAccount.getAvailable().add(amt));
            userAccount.setUpdateTime(new Date());
            webAssetAccountMapper.updateByPrimaryKeySelective(userAccount);
        }
    }

    @Override
    public int updateUserAccount(Integer userId, Integer currencyId, BigDecimal amt) {
        return webAssetAccountMapper.updateUserAccount(
                WebAssetAccountPo.builder()
                        .userId(userId)
                        .currencyId(currencyId)
                        .available(amt)
                        .build()
        );
    }

    @Override
    public int insertRiskFund(Integer currencyId, BigDecimal riskFund) {
        return riskAccountMapper.insertOrUpdate(currencyId, riskFund);
    }

	@Override
	public int updateAccount(WebAssetAccountPo account) {
		return webAssetAccountMapper.updateByPrimaryKeySelective(account);
	}
}
package org.apdoer.trade.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.trade.common.db.model.po.WebAssetAccountPo;

public interface WebAssetAccountMapper extends BaseMapper<WebAssetAccountPo> {

    WebAssetAccountPo getUserAccountForUpdate(@Param("userId") Integer userId,
                                              @Param("currencyId") Integer currencyId);

    int updateUserAccount(WebAssetAccountPo account);
}
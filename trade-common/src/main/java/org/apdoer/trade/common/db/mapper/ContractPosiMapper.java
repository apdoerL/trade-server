package org.apdoer.trade.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;

import java.util.List;

public interface ContractPosiMapper extends BaseMapper<CoreContractPosiPo> {

    /**
     * 根据表名获取未平仓数据
     * @param tableName
     * @return
     */
    List<CoreContractPosiPo> getUnClosePosi(@Param("tableName") String tableName);


    /**
     * 新增持仓
     * @param posi
     * @param tableName
     * @return
     */
    int insertPosi(CoreContractPosiPo posi, @Param("tableName") String tableName);
}
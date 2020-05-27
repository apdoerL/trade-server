package org.apdoer.trade.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;

import java.util.List;

public interface CoreContractOrderMapper extends BaseMapper<CoreContractOrderPo> {
	
	List<CoreContractOrderPo> queryHistory(@Param("tableName") String tableName, @Param("userId") Integer userId, @Param("contractId") Integer contractId, @Param("side") Integer side, @Param("status") Integer status);

	CoreContractOrderPo queryOrder(@Param("userId") Integer userId, @Param("orderId") Long orderId, @Param("status") Integer status, @Param("tableName") String tableName);

	/**
	 * 查询未触发挂单
	 * @param tableName
	 * @return
	 */
	List<CoreContractOrderPo> getUnTriggerOrder(@Param("tableName") String tableName);

	/**
	 * 查询用户未触发挂单
	 * @param userId
	 * @param contractId
	 * @param tableName
	 * @return
	 */
	List<CoreContractOrderPo> queryUserUnTriggerOrder(@Param("userId") Integer userId, @Param("contractId") Integer contractId, @Param("tableName") String tableName);
}
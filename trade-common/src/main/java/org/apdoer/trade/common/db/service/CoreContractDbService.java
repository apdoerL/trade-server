package org.apdoer.trade.common.db.service;

import org.apdoer.trade.common.db.model.po.*;

import java.util.List;

public interface CoreContractDbService {
    /**
     * 获取最大合约id
     * @return
     */
    Integer getMaxContractId();

    /**
     * 获取所有合约
     * @return
     */
    List<CoreContractPo> getAllContract();

    /**
     * 获取所有可下单币种
     * @return
     */
    List<CoreContractOrderCurrencyPo> getAllOrderCurrency();

    /**
     * 获取所有合约杠杆
     * @return
     */
    List<CoreContractLeveragePo> getAllContractLeverage();


    /**
     * 根据表名获取未平仓数据
     * @param userMod
     * @return
     */
    List<CoreContractPosiPo> getUnClosePosi(int userMod);

    /**
     * 更新持仓数据
     * @param posiPo
     * @return
     */
    int updatePosi(CoreContractPosiPo posiPo);

    /**
     * 新增持仓数据
     * @param posiPo
     * @return
     */
    int insertPosi(CoreContractPosiPo posiPo);

    /**
     * 新增持仓历史记录
     * @param posiPo
     * @return
     */
    int insertPosiHistory(CoreContractPosiPo posiPo);

    /**
     * 查询历史持仓
     * @param userId
     * @param queryDate
     * @return
     */
    List<CoreContractPosiPo> historyOrderList(Integer userId, Long queryDate);

    /**
     * 插入条件委托
     * @param orderPo
     * @return
     */
    int insertOrder(CoreContractOrderPo orderPo);
    
    /**
     * 更新条件委托
     * @param orderPo
     * @return
     */
    int updateOrder(CoreContractOrderPo orderPo);
    
    CoreContractOrderPo queryOrder(Long orderId, Integer userId, Integer status);

    /**
     * 查询历史条件单
     * @param userId
     * @param contractId
     * @param side
     * @param status
     * @return
     */
    List<CoreContractOrderPo> queryHistoryOrder(Integer userId, Integer contractId, Integer side, Integer status);
	
    /**
     * 获取尚未触发的条件单
     * @param userMod
     * @return
     */
    List<CoreContractOrderPo> getUnTriggerOrder(int userMod);
    
    /**
     * 查询用户尚未触发的条件委托
     * @param userId
     * @param contractId
     * @param status
     * @return
     */
    List<CoreContractOrderPo> queryUserUnTriggerOrder(Integer userId, Integer contractId, Integer status);
    
}

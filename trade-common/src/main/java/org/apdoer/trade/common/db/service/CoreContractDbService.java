package org.apdoer.trade.common.db.service;

import org.apdoer.trade.common.db.model.po.*;

import java.util.List;

public interface CoreContractDbService {
    /**
     * @Author y
     * @Description 获取最大合约id
     * @Date 2020/3/29 1:09 下午
     * @Return
     **/
    Integer getMaxContractId();

    /**
     * @Author y
     * @Description 获取所有合约
     * @Date 2020/3/31 4:14 下午
     * @Return
     **/
    List<CoreContractPo> getAllContract();

    /**
     * @Author y
     * @Description 获取所有可下单币种
     * @Date 2020/4/1 12:07 上午
     * @Return
     **/
    List<CoreContractOrderCurrencyPo> getAllOrderCurrency();

    /**
     * @Author y
     * @Description 获取所有合约杠杆
     * @Date 2020/4/1 12:07 上午
     * @Return
     **/
    List<CoreContractLeveragePo> getAllContractLeverage();


    /**
     * @Author y
     * @Description 根据表名获取未平仓数据
     * @Date 2020/3/29 11:09 下午
     * @Return
     **/
    List<CoreContractPosiPo> getUnClosePosi(int userMod);

    /**
     * @Author y
     * @Description 更新持仓数据
     * @Date 2020/4/1 12:06 上午
     * @Return
     **/
    int updatePosi(CoreContractPosiPo posiPo);

    /**
     * @Author y
     * @Description 新增持仓数据
     * @Date 2020/4/1 1:37 上午
     * @Return
     **/
    int insertPosi(CoreContractPosiPo posiPo);

    /**
     * @Author y
     * @Description 新增持仓历史记录
     * @Date 2020/4/2 11:11 上午
     * @Return
     **/
    int insertPosiHistory(CoreContractPosiPo posiPo);

    /**
     * @Author y
     * @Description 查询历史持仓
     * @Date 2020/4/2 4:32 下午
     * @Return
     **/
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
     * @param contractId
     * @param userId
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

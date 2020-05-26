package org.apdoer.trade.common.data.base;

import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.enums.SlaveDataSortTypeEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件单数据区
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 18:36
 */
public class BaseConditionOrderData {

    //ConcurrentSkipListMap 的排序方式
    private SlaveDataSortTypeEnum sortType;

    private Lock reentrantLock = new ReentrantLock(true);

    private List<ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>>> orderDataList = new ArrayList<>();

    public BaseConditionOrderData(SlaveDataSortTypeEnum _sortType) {

        this.sortType = _sortType;
    }

    public void init(ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> enptySkipListMap) {
        this.orderDataList.add(enptySkipListMap);
    }


    public void add(CoreContractOrderPo orderPo) {
        // 获取该合约下所有条件单
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> priceMap = this.orderDataList.get(orderPo.getContractId() - 1);
        // 获取该强平价下的所有持仓
        ConcurrentHashMap<Long, CoreContractOrderPo> priceOrderMap = priceMap.get(orderPo.getTriggerPrice().stripTrailingZeros());
        if (null != priceOrderMap) {
            priceOrderMap.put(orderPo.getOrderId(), orderPo);
        } else {
            try {
                this.reentrantLock.lock();
                if (null == (priceOrderMap = priceMap.get(orderPo.getTriggerPrice().stripTrailingZeros()))) {
                    ConcurrentHashMap<Long, CoreContractOrderPo> newPriceOrderMap = new ConcurrentHashMap<>();
                    newPriceOrderMap.put(orderPo.getOrderId(), orderPo);
                    priceMap.put(orderPo.getTriggerPrice().stripTrailingZeros(), newPriceOrderMap);
                } else {
                    priceOrderMap.put(orderPo.getOrderId(), orderPo);
                }
            } finally {
                this.reentrantLock.unlock();
            }
        }
    }

    public void remove(CoreContractOrderPo orderPo) {
        // 获取该合约下所有force_close_price的持仓
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> priceMap = this.orderDataList.get(orderPo.getContractId() - 1);
        // 获取该强平价下的所有持仓
        ConcurrentHashMap<Long, CoreContractOrderPo> pricePosiMap = priceMap.get(orderPo.getTriggerPrice().stripTrailingZeros());
        if (null == pricePosiMap) {
            return;
        }
        pricePosiMap.remove(orderPo.getOrderId());
    }

    /**
     * 获取本次移除的数据
     * @param contractId
     * @param price
     * @return
     */
    public List<Map<Long, CoreContractOrderPo>> getRemoveList(Integer contractId, BigDecimal price) {
        //正序排列
        if (SlaveDataSortTypeEnum.ASC.getCode() == this.sortType.getCode()) {
            return this.getASCSortCloseList(contractId, price);
            //反序排列
        } else {
            return this.getDESCSortCloseList(contractId, price);
        }
    }

    private List<Map<Long, CoreContractOrderPo>> getDESCSortCloseList(Integer contractId, BigDecimal price) {
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> priceMap = this.orderDataList.get(contractId - 1);

        List<Map<Long, CoreContractOrderPo>> result = new ArrayList<>();
        Iterator<Map.Entry<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>>> it = priceMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> entry = it.next();
            // 看涨，倒序，强平价 大于等于 当前价（即触发强平），如果小于则不继续遍历
            if (entry.getKey().compareTo(price) < 0) {
                break;
            }
            result.add(entry.getValue());
            it.remove();
        }
        return result;
    }

    private List<Map<Long, CoreContractOrderPo>> getASCSortCloseList(Integer contractId, BigDecimal price) {
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> priceMap = this.orderDataList.get(contractId - 1);

        List<Map<Long, CoreContractOrderPo>> result = new ArrayList<>();
        Iterator<Map.Entry<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>>> it = priceMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> entry = it.next();
            // 看空，正序，强平价 小于等于 当前价（即触发强平），如果大于则不继续遍历
            if (entry.getKey().compareTo(price) > 0) {
                break;
            }

            result.add(entry.getValue());
            it.remove();
        }
        return result;
    }

}

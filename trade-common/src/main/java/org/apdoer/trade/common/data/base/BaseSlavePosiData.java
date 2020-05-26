package org.apdoer.trade.common.data.base;

import org.apdoer.trade.common.enums.SlaveDataSortTypeEnum;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;
import org.springframework.util.CollectionUtils;

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
 * 从持仓数据区
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 16:31
 */
public class BaseSlavePosiData {

    private Lock reentrantLock = new ReentrantLock(true);

    /**
     * 跳跃表的key为触发价格,value为map<uuid,从持仓数据>
     */
    private List<ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>>> posiData = new ArrayList<>();

    private SlaveDataSortTypeEnum sort;


    public BaseSlavePosiData(SlaveDataSortTypeEnum sortType) {
        this.sort = sortType;
    }

    public void init(ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> emptySlaveBuy) {
        posiData.add(emptySlaveBuy);
    }

    public void add(SlavePosiDataDto slavePosiDataDto) {
        //获取该合约下所有触发价(强平)持仓
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> contractMap = posiData.get(slavePosiDataDto.getContractId() - 1);
        //获取该强平价下的持仓
        ConcurrentHashMap<Long, SlavePosiDataDto> pricePosiMap = contractMap.get(slavePosiDataDto.getTriggerPrice());
        if (null != pricePosiMap) {
            pricePosiMap.put(slavePosiDataDto.getUuid(), slavePosiDataDto);
        } else {
            try {
                reentrantLock.lock();
                if (null == (pricePosiMap = contractMap.get(slavePosiDataDto.getTriggerPrice().stripTrailingZeros()))) {
                    ConcurrentHashMap<Long, SlavePosiDataDto> newPricePosiMap = new ConcurrentHashMap<>(16);
                    newPricePosiMap.put(slavePosiDataDto.getUuid(), slavePosiDataDto);
                    contractMap.put(slavePosiDataDto.getTriggerPrice().stripTrailingZeros(), newPricePosiMap);
                } else {
                    pricePosiMap.put(slavePosiDataDto.getUuid(), slavePosiDataDto);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public void remove(SlavePosiDataDto slavePosiDataDto) {
        //获取该合约下所有强平持仓
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> contractMap = posiData.get(slavePosiDataDto.getContractId() - 1);
        //获取强平价下所有持仓
        ConcurrentHashMap<Long, SlavePosiDataDto> priceMap = contractMap.get(slavePosiDataDto.getTriggerPrice().stripTrailingZeros());
        if (priceMap == null) {
            return;
        }
        priceMap.remove(slavePosiDataDto.getUuid());
    }

    /**
     * 获取本次移除的数据
     * @param contractId
     * @param price
     * @return
     */
    public List<Map<Long, SlavePosiDataDto>> getRemoveList(Integer contractId, BigDecimal price) {
        //正序排列
        if (SlaveDataSortTypeEnum.ASC.getCode() == sort.getCode()) {
            return getSellCloseList(contractId, price);
        } else {
            return getBuyCloseList(contractId, price);
        }
    }

    private List<Map<Long, SlavePosiDataDto>> getBuyCloseList(Integer contractId, BigDecimal price) {
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> contractMap = posiData.get(contractId - 1);
        List<Map<Long, SlavePosiDataDto>> result = new ArrayList<>();
        Iterator<Map.Entry<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>>> iterator = contractMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> entry = iterator.next();
            //看涨,倒序,强平价大于等于当前价(触发强平),如果小于则不继续遍历
            if (entry.getKey().compareTo(price) < 0) {
                break;
            }
            result.add(entry.getValue());
            iterator.remove();
        }
        return result;
    }

    private List<Map<Long, SlavePosiDataDto>> getSellCloseList(Integer contractId, BigDecimal price) {
        ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> contractMap = posiData.get(contractId - 1);
        List<Map<Long, SlavePosiDataDto>> result = new ArrayList<>();
        Iterator<Map.Entry<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>>> iterator = contractMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> entry = iterator.next();
            //看空,正序,强平价 小于等于当前价(即触发强平),如果大于则不继续遍历
            if (entry.getKey().compareTo(price) > 0) {
                break;
            }
            result.add(entry.getValue());
            iterator.remove();
        }
        return result;
    }


}

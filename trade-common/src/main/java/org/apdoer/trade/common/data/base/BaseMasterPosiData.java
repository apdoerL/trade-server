package org.apdoer.trade.common.data.base;

import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主数据区,基础数据结构
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 16:03
 */
public class BaseMasterPosiData {

    private Lock reentrantLock = new ReentrantLock(true);

    /**
     * 数据区按合约分片 下标索引为合约id-1
     */
    private List<ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CoreContractPosiPo>>> posiData = new ArrayList<>();

    public void add(CoreContractPosiPo contractPosiPo) {
        //获取该合约下所有用户的持仓
        ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CoreContractPosiPo>> contractMap = posiData.get(contractPosiPo.getContractId() - 1);
        //获取指定用户的持仓
        ConcurrentHashMap<Long, CoreContractPosiPo> userPosiMap = contractMap.get(contractPosiPo.getUserId());
        if (!CollectionUtils.isEmpty(userPosiMap)) {
            //以持仓唯一标识为下标
            userPosiMap.put(contractPosiPo.getUuid(), contractPosiPo);
        } else {
            try {
                reentrantLock.lock();
                if (null == (userPosiMap = contractMap.get(contractPosiPo.getUserId()))) {
                    ConcurrentHashMap<Long, CoreContractPosiPo> newUserPosiMap = new ConcurrentHashMap<>(16);
                    newUserPosiMap.put(contractPosiPo.getUuid(), contractPosiPo);
                    contractMap.put(contractPosiPo.getUserId(), newUserPosiMap);
                } else {
                    userPosiMap.put(contractPosiPo.getUuid(), contractPosiPo);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }


    public CoreContractPosiPo remove(SlavePosiDataDto posiDataDto) {
        //获取该合约下所有用户的持仓
        ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CoreContractPosiPo>> uidMap = posiData.get(posiDataDto.getContractId() - 1);
        // 获取指定用户的该合约所有持仓
        ConcurrentHashMap<Long, CoreContractPosiPo> userPosiMap = uidMap.get(posiDataDto.getUserId());
        if (null == userPosiMap) {
            return null;
        }
        return userPosiMap.remove(posiDataDto.getUuid());
    }

    public CoreContractPosiPo get(Integer contractId, Integer userId, Long uuid) {
        //获取合约下所有用户的持仓
        ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CoreContractPosiPo>> contractMap = posiData.get(contractId - 1);
        //获取指定用户持仓(该合约)
        ConcurrentHashMap<Long, CoreContractPosiPo> userPosiMap = contractMap.get(userId);
        if (CollectionUtils.isEmpty(userPosiMap)) {
            return null;
        } else {
            return userPosiMap.get(uuid);
        }
    }

    public void init() {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CoreContractPosiPo>> emptyMaster = new ConcurrentHashMap<>(16);
        posiData.add(emptyMaster);
    }
}

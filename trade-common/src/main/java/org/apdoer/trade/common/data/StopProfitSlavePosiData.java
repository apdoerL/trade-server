package org.apdoer.trade.common.data;


import org.apdoer.trade.common.data.base.BaseSlavePosiData;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.enums.PosiSideEnum;
import org.apdoer.trade.common.enums.SlaveDataSortTypeEnum;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 * 止盈从数据区
 * @author apdoer
 */
public class StopProfitSlavePosiData {

    // 从持仓数据存储结构，以{cid}-1作为list下标， List<Map<{stop_profit_price},Map<{uuid},data>>>
    static BaseSlavePosiData BUY_POSI = new BaseSlavePosiData(SlaveDataSortTypeEnum.ASC);
    static BaseSlavePosiData SELL_POSI = new BaseSlavePosiData(SlaveDataSortTypeEnum.DESC);

    /**
     * 初始化数据
     *
     * @param fixedCount
     */
    public static void init(int fixedCount) {
        for (int i = 0; i < fixedCount; i++) {
            // 开辟从持仓空间
            // 看涨：正序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> emptySlaveBuy = new ConcurrentSkipListMap<>();
            BUY_POSI.init(emptySlaveBuy);
            // 看跌：倒序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> emptySlaveSell = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
            SELL_POSI.init(emptySlaveSell);
        }
    }

    /**
     * 仅主持仓调用
     *
     * @param posiReq
     */
    public static void add(CoreContractPosiPo posiReq) {
        BaseSlavePosiData baseSlavePosiData = posiReq.getPosiSide() == PosiSideEnum.BUY.getCode() ? BUY_POSI : SELL_POSI;
        baseSlavePosiData.add(buildSlavePosiDataDto(posiReq));
    }

    /**
     * 仅主持仓调用
     *
     * @param posiDataDto
     */
    public static void remove(SlavePosiDataDto posiDataDto) {
        BaseSlavePosiData baseSlavePosiData = posiDataDto.getPosiSide() == PosiSideEnum.BUY.getCode() ? BUY_POSI : SELL_POSI;
        baseSlavePosiData.remove(posiDataDto);
    }

    /**
     * 获取待强平持仓列表
     *
     * @param contractId
     * @param price
     * @return
     */
    public static List<SlavePosiDataDto> getCloseList(Integer contractId, BigDecimal price) {
        List<SlavePosiDataDto> allClosePosiList = new ArrayList<>();

        List<Map<Long, SlavePosiDataDto>> closeList = new ArrayList<>(2);
        closeList.addAll(BUY_POSI.getRemoveList(contractId, price));
        closeList.addAll(SELL_POSI.getRemoveList(contractId, price));

        for (Map<Long, SlavePosiDataDto> map : closeList) {
            Iterator<Map.Entry<Long, SlavePosiDataDto>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, SlavePosiDataDto> entry = it.next();
                allClosePosiList.add(entry.getValue());
            }
        }
        return allClosePosiList;
    }

    private static SlavePosiDataDto buildSlavePosiDataDto(CoreContractPosiPo posiReq) {
        SlavePosiDataDto data = SlavePosiDataDto.builder()
                .uuid(posiReq.getUuid())
                .userId(posiReq.getUserId())
                .contractId(posiReq.getContractId())
                .posiSide(posiReq.getPosiSide())
                .triggerPrice(posiReq.getStopProfitPrice())
                .build();
        return data;
    }
}

package org.apdoer.trade.common.data;

import lombok.extern.slf4j.Slf4j;
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
 * 从持仓数据 - 用于强平
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 17:14
 */
@Slf4j
public class SlavePosiData {

    /**
     * 从持仓数据存储结构，以{cid}-1作为list下标， List<Map<{force_close_price},Map<{uuid},data>>>
     */
    static BaseSlavePosiData BUY_POSI = new BaseSlavePosiData(SlaveDataSortTypeEnum.DESC);
    static BaseSlavePosiData SELL_POSI = new BaseSlavePosiData(SlaveDataSortTypeEnum.ASC);

    public static void init(int fixedCount) {
        for (int i = 0; i < fixedCount; i++) {
            //申请持仓内存,看涨,倒序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> emptySlaveBuy = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
            BUY_POSI.init(emptySlaveBuy);

            //看跌-正序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> emptySlaveSell = new ConcurrentSkipListMap<>();
            SELL_POSI.init(emptySlaveSell);
        }
    }

    /**
     * 仅主持仓调用
     *
     * @param contractPosiPo
     */
    public static void add(CoreContractPosiPo contractPosiPo) {
        BaseSlavePosiData baseSlavePosiData = contractPosiPo.getPosiSide() == PosiSideEnum.BUY.getCode() ? BUY_POSI : SELL_POSI;
        baseSlavePosiData.add(buildSlavePosiDataDto(contractPosiPo));
    }

    /**
     * 仅住持仓区域调用
     *
     * @param slavePosiDataDto
     */
    public static void remove(SlavePosiDataDto slavePosiDataDto) {
        BaseSlavePosiData baseSlavePosiData = slavePosiDataDto.getPosiSide() == PosiSideEnum.BUY.getCode() ? BUY_POSI : SELL_POSI;
        baseSlavePosiData.remove(slavePosiDataDto);
    }

    /**
     * 获取待强平持仓列表
     * @param contractId
     * @param price
     * @return
     */
    public static List<SlavePosiDataDto> getCloseList(Integer contractId, BigDecimal price) {
        List<SlavePosiDataDto> allClosePosiList = new ArrayList<>();
        List<Map<Long, SlavePosiDataDto>> closeList = new ArrayList<>(2);
        closeList.addAll(BUY_POSI.getRemoveList(contractId, price));
        closeList.addAll(SELL_POSI.getRemoveList(contractId, price));

        for (Map<Long, SlavePosiDataDto> slavePosiDataDtoMap : closeList) {
            for (Map.Entry<Long, SlavePosiDataDto> entry : slavePosiDataDtoMap.entrySet()) {
                allClosePosiList.add(entry.getValue());
            }
        }
        log.info("getCloseList contract={} price={} listSize={}", contractId, price, allClosePosiList.size());
        return allClosePosiList;
    }

    private static SlavePosiDataDto buildSlavePosiDataDto(CoreContractPosiPo posiReq) {
        SlavePosiDataDto data = SlavePosiDataDto.builder()
                .uuid(posiReq.getUuid())
                .userId(posiReq.getUserId())
                .contractId(posiReq.getContractId())
                .posiSide(posiReq.getPosiSide())
                .triggerPrice(posiReq.getForceClosePrice())
                .build();
        return data;
    }
}

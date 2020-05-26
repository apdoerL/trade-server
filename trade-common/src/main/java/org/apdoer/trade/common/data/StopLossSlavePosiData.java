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
 * 止损数据区-从
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 18:15
 */
@Slf4j
public class StopLossSlavePosiData {

    // 从持仓数据存储结构，以{cid}-1作为list下标， List<Map<{stop_loss_price},Map<{uuid},data>>>
    static BaseSlavePosiData BUY_POSI = new BaseSlavePosiData(SlaveDataSortTypeEnum.DESC);
    static BaseSlavePosiData SELL_POSI = new BaseSlavePosiData(SlaveDataSortTypeEnum.ASC);

    public static void init(int fixedCount) {
        for (int i = 0; i < fixedCount; i++) {
            //申请从持仓内存 ,看涨:倒序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> emptySlaveBuy = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
            BUY_POSI.init(emptySlaveBuy);
            //看跌,正序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, SlavePosiDataDto>> emptySlaveSell = new ConcurrentSkipListMap<>();
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
    private static SlavePosiDataDto buildSlavePosiDataDto(CoreContractPosiPo posiReq) {
        SlavePosiDataDto data = SlavePosiDataDto.builder()
                .uuid(posiReq.getUuid())
                .userId(posiReq.getUserId())
                .contractId(posiReq.getContractId())
                .posiSide(posiReq.getPosiSide())
                .triggerPrice(posiReq.getStopLossPrice())
                .build();
        return data;
    }

    /**
     * 获取待平仓列表
     * @param contractId
     * @param price
     * @return
     */
    public static List<SlavePosiDataDto> getCloseList(Integer contractId,BigDecimal price){
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
}

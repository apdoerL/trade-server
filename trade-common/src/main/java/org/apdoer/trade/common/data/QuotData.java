package org.apdoer.trade.common.data;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 最新行情数据缓存区
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 16:00
 */
@Slf4j
public class QuotData {

    private static ConcurrentHashMap<Integer, BigDecimal> QUOT_DATA = new ConcurrentHashMap<>();

    //todo
    public static void add(Integer contractId, BigDecimal price) {
        log.info("QUOT: contractId[{}],price[{}]", contractId, price);
        QUOT_DATA.put(contractId, price);
    }

    public static BigDecimal get(Integer contractId) {
        return QUOT_DATA.get(contractId);
    }
}

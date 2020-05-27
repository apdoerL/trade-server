package org.apdoer.trade.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.LongAccumulator;

/**
 * 委托编号工具类 todo
 * @author apdoer
 */
@Slf4j
@Component
public class OrderNumberUtil {

    /**
     * This class is usually preferable to AtomicLong when
     * multiple threads update a common value that is used for purposes such
     * as collecting statistics, not for fine-grained synchronization
     * control.  Under low update contention, the two classes have similar
     * characteristics. But under high contention, expected throughput of
     * this class is significantly higher, at the expense of higher space
     * consumption.
     */
    private static LongAccumulator accumulator = null;

    @Value("${trade-server.machine-partition}")
    public String mp;

    //@PostConstruct
    public void init() {
        accumulator = new LongAccumulator(Long::sum, System.currentTimeMillis() * 1000);
        try {
            Long.parseLong(mp);
        } catch (NumberFormatException e) {
            log.error("machine-partition format number error.", e);
            throw e;
        }
    }

    /**
     * 委托编号生成规则：两位机器分区号+微秒时间戳递增
     * @return
     */
    public long getOrderUuid() {
        accumulator.accumulate(1);
        String uuid = mp + accumulator.get();
        return Long.parseLong(uuid);
    }
}

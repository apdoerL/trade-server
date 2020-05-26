package org.apdoer.trade.common.backpressure.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.backpressure.BackPressure;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 10:58
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FixedThreadPoolBackPressure implements BackPressure {
    /**
     * 反压器名称
     */
    private String name;

    private int backPressureSize;

    private ThreadPoolExecutor threadPoolExecutor;


    @Override
    public boolean tryDo(int batch, long timeout, TimeUnit timeUnit) throws InterruptedException {
        if (threadPoolExecutor.getQueue().size() + batch > this.backPressureSize) {
            Thread.sleep(timeUnit.toMillis(timeout));
            log.warn("name={} backpressure,thread sleep:{}", name, timeUnit.toMillis(timeout));
            return true;
        }
        return false;
    }
}

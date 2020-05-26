package org.apdoer.trade.common.backpressure;

import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 10:57
 */
public interface BackPressure {

    /**
     * 反压
     * @param batch 当前数据
     * @param timeout 反压时间
     * @param timeUnit 时间单位
     * @return
     * @throws InterruptedException
     */
    boolean tryDo(int batch, long timeout, TimeUnit timeUnit) throws InterruptedException;
}

package org.apdoer.trade.core.thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.thread.NameableThreadFactory;
import org.apdoer.trade.common.backpressure.BackPressure;
import org.apdoer.trade.common.backpressure.impl.FixedThreadPoolBackPressure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 平仓数据处理线程池
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 17:02
 */
@Component
@Slf4j
public class ClosePosiHandleThreadPool implements BackPressure {

    @Value("${thread.posi-flat.core-pool-size:20}")
    private Integer corePoolSize;

    @Value("${thread.posi-flat.max-pool-size:20}")
    private Integer maxPoolSize;

    @Value("${thread.posi-flat.keep-alive_time:30}")
    private Long keepAliveTime;

    @Value("${thread.posi-flat.queue-capacity:300000}")
    private Integer queueCapacity;

    @Value("${thread.posi-flat.queue-backpressure:280000}")
    private Integer queueBackPressure;

    private ThreadPoolExecutor threadPoolExecutor;

    private BackPressure backPressure;

    public void init() {
        this.threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>(this.queueCapacity), new NameableThreadFactory("closePosiHandleThreadPool"));
        this.backPressure = new FixedThreadPoolBackPressure("closePosiHandle", this.queueBackPressure, this.threadPoolExecutor);
        log.info(" close posi  thread pool init success core:{},max:{},keepAlive:{},initQueue:{},backPressure:{}",corePoolSize,maxPoolSize,keepAliveTime,queueCapacity,queueBackPressure);
    }


    @Override
    public boolean tryDo(int batch, long timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.backPressure.tryDo(batch, timeout, timeUnit);
    }

    public int size() {
        return this.threadPoolExecutor.getQueue().size();
    }

    public void execute(Runnable runnable) {
        this.threadPoolExecutor.execute(runnable);
    }

    public void shutdown() {
        this.threadPoolExecutor.shutdown();
    }
}

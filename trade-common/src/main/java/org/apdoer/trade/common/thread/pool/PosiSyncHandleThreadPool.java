package org.apdoer.trade.common.thread.pool;

import org.apdoer.common.service.thread.NameableThreadFactory;
import org.apdoer.trade.common.backpressure.BackPressure;
import org.apdoer.trade.common.backpressure.impl.FixedThreadPoolBackPressure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 持仓数据同步线程池
 *
 * @author apdoer
 */
@Component
public class PosiSyncHandleThreadPool implements BackPressure {

    private static final String BACKPRESSURE_NAME = "posiSyncBackpressure";

    @Value("${thread.posi-sync.core-pool-size:20}")
    private Integer corePoolSize;

    @Value("${thread.posi-sync.max-pool-size:20}")
    private Integer maxPoolSize;

    @Value("${thread.posi-sync.keep-alive_time:60}")
    private Long keepAliveTime;

    @Value("${thread.posi-sync.queue-capacity:300000}")
    private Integer queueCapacity;

    @Value("${thread.posi-sync.queue-backpressure:280000}")
    private Integer queueBackPressure;

    private ThreadPoolExecutor threadPoolExecutor;

    private BackPressure backPressure;

    public void init() {
        this.threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>(this.queueCapacity), new NameableThreadFactory("posi-sync-threadpool"));
        this.backPressure = new FixedThreadPoolBackPressure(BACKPRESSURE_NAME, this.queueBackPressure, this.threadPoolExecutor);
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

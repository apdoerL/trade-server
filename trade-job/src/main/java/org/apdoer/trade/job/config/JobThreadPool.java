package org.apdoer.trade.job.config;


import org.apdoer.common.service.thread.NameableThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/13 12:16
 */
public class JobThreadPool {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 5;
    private static final int INIT_CAPACITY = 1000;
    private static final long KEEP_ALIVE = 3L;


    private volatile ThreadPoolExecutor threadPool;

    private JobThreadPool() {
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(INIT_CAPACITY),
                new NameableThreadFactory("condition-job-threadpool"));
    }

    private static class InnerJobThreadPool {
        private static final JobThreadPool INSTANCE = new JobThreadPool();
    }

    public static JobThreadPool getInstance() {
        return InnerJobThreadPool.INSTANCE;
    }


    public void excute(Runnable runnable){
        threadPool.execute(runnable);
    }

    public int size(){
        return this.threadPool.getQueue().size();
    }

    public void shutdown(){
        threadPool.shutdown();
    }

}


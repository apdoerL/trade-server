package org.apdoer.trade.common.config;


import org.apdoer.common.service.thread.NameableThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/13 12:16
 */
public class MasterDataInitThreadPool {
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 10;
    private static final long KEEP_ALIVE = 0L;


    private volatile ThreadPoolExecutor threadPool;

    private MasterDataInitThreadPool() {
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new NameableThreadFactory("master-data-init-threadpool"));
    }

    private static class InnerMasterDataInitThreadPool {
        private static final MasterDataInitThreadPool INSTANCE = new MasterDataInitThreadPool();
    }

    public static MasterDataInitThreadPool getInstance() {
        return InnerMasterDataInitThreadPool.INSTANCE;
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


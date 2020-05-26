package org.apdoer.trade.common.event.eventbus.impl;

import com.google.common.eventbus.AsyncEventBus;
import org.apdoer.common.service.thread.NameableThreadFactory;
import org.apdoer.trade.common.backpressure.BackPressure;
import org.apdoer.trade.common.backpressure.impl.FixedThreadPoolBackPressure;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.eventbus.EventBus;
import org.apdoer.trade.common.event.listener.SourceEventListener;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:53
 */
public class GuavaEventBus implements EventBus, BackPressure {
    // 初始线程池队列大小
    private final static int INITIAL_CAPACITY = 100000;
    // 默认反压初始大小
    private final static int DEFAULT_BACKPRESSURE_SIZE = 80000;
    // 默认线程池初始线程大小
    private final static int DEFAULT_CORE_POOL_SIZE = 10;
    // 默认线程池最大线程大小
    private final static int DEFAULT_MAX_POOL_SIZE = 10;
    // 默认线程池线程空闲时间
    private final static long DEFAULT_KEEP_ALIVE_TIME = 60L;

    private String name;

    /**
     * guava 异步eventbus
     */
    private final AsyncEventBus eventBus;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final BackPressure backPressure;

    public GuavaEventBus(String name) {
        this(name, DEFAULT_BACKPRESSURE_SIZE);
    }

    public GuavaEventBus(String name, int backpressureSize) {
        this(name, backpressureSize, DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE);
    }

    public GuavaEventBus(String name,  int corePoolSize, int maxPoolSize,int backpressureSize) {
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(INITIAL_CAPACITY), new NameableThreadFactory("guavaEventBus-" + name));
        this.eventBus = new AsyncEventBus(threadPoolExecutor);
        this.backPressure = new FixedThreadPoolBackPressure(name, backpressureSize, threadPoolExecutor);
        this.name = name;
    }

    public GuavaEventBus(String name, int corePoolSize, int maxPoolSize, int backpressureSize,long keepAlive,int initCapacity) {
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(initCapacity), new NameableThreadFactory("guavaEventBus-" + name));
        this.eventBus = new AsyncEventBus(threadPoolExecutor);
        this.backPressure = new FixedThreadPoolBackPressure(name, backpressureSize, threadPoolExecutor);
        this.name = name;
    }
    @Override
    public void subscribe(SourceEventListener listener) {
        eventBus.register(listener);
    }

    @Override
    public void unSubscribe(SourceEventListener listener) {
        eventBus.unregister(listener);
    }


    @Override
    public void publish(SourceEvent event) {
        eventBus.post(event);
    }

    @Override
    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean tryDo(int batch, long timeout, TimeUnit timeUnit) throws InterruptedException {
        return backPressure.tryDo(batch, timeout, timeUnit);
    }
}

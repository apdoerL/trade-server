package org.apdoer.trade.job.job;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:22
 */
public interface Job {

    /**
     * 任务初始化
     */
    void init();

    /**
     * 开始任务
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 具体干活
     */
    void doWork() throws Exception;

    /**
     * 停止任务
     * @throws Exception
     */
    void shutdown() throws Exception;

    /**
     * 清楚任务
     */
    void cleanup();

    /**
     * 异常处理
     * @param e
     */
    void exceptionHandle(Exception e);

    /**
     * 获取JOB描述
     * @return
     */
    JobDesc getJobDesc();

}

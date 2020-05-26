package org.apdoer.trade.job.job.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.event.payload.IndexPriceMessageProcessPayload;
import org.apdoer.trade.job.config.JobThreadPool;
import org.apdoer.trade.job.job.Job;
import org.apdoer.trade.job.job.JobDesc;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.process.Processor;
import org.apdoer.trade.quot.source.Source;
import org.apdoer.trade.quot.source.SourceType;
import org.apdoer.trade.quot.source.impl.ZmqSourceImpl;

import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:41
 */
@Slf4j
public class QuotSourceJob implements Job {

    // 数据源
    private Source<IndexPriceMessageSourcePayload> source;
    // 数据处理
    private Processor<IndexPriceMessageSourcePayload, IndexPriceMessageProcessPayload> processor;
    // job属性
    private JobDesc jobDescribution;

    public QuotSourceJob(Source<IndexPriceMessageSourcePayload> source,
                         Processor<IndexPriceMessageSourcePayload, IndexPriceMessageProcessPayload> processor, JobDesc jobDescribution) {
        this.source = source;
        this.processor = processor;
        this.jobDescribution = jobDescribution;
    }

    @Override
    public void init() {
        log.info("jobName={}, excute func init start", this.jobDescribution.getJobName());
        this.source.init();
        log.info("jobName={}, excute func init() end", this.jobDescribution.getJobName());
    }

    @Override
    public void start() throws Exception {
        log.info("jobName={}, excute func start start", this.jobDescribution.getJobName());
        this.source.open();
        if (this.source.getSourceType().equals(SourceType.ZMQ)) {
            ZmqSourceImpl source = (ZmqSourceImpl) this.source;
            JobThreadPool.getInstance().excute(source);
        }
        log.info("jobName={}, excute func start end", this.jobDescribution.getJobName());
    }

    @Override
    public void doWork() throws Exception {
        // 源端读取数据
        IndexPriceMessageSourcePayload sourcePayload = this.source.read();
        if (null != sourcePayload) {
            // 数据处理
            IndexPriceMessageProcessPayload processPayload = this.processor.process(sourcePayload);
            if (null != processPayload && StringUtils.isNotBlank(processPayload.getSystemChannel())
                    && null != processPayload.getData()) {
                // 获取数据的内部通道
                GuavaEventBus eventbus = (GuavaEventBus) GuavaEventBusManager.getInstance()
                        .getEventBus(processPayload.getSystemChannel());
                if (null != eventbus) {
                    // 是否反压
                    while (eventbus.tryDo(1, 1L, TimeUnit.SECONDS)) {
                    }
                    // 数据发送到内部通道
                    eventbus.publish(new SourceEvent(processPayload.getData()));
                }
            }
        } else {
            Thread.sleep(this.jobDescribution.getSourceBlankSleepTime());
        }
    }

    @Override
    public void shutdown() throws Exception {
        this.source.close();
    }

    @Override
    public void cleanup() {
        this.source.cleanup();
    }

    @Override
    public void exceptionHandle(Exception e) {
        log.error("job execute error!,jobName={}", this.jobDescribution.getJobName(), e);
    }

    @Override
    public JobDesc getJobDesc() {
        return jobDescribution;
    }
}

package org.apdoer.trade.job.job.manager;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apdoer.trade.common.event.payload.IndexPriceMessageProcessPayload;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.job.job.Job;
import org.apdoer.trade.job.job.JobDesc;
import org.apdoer.trade.job.job.factory.impl.SourceJobFactory;
import org.apdoer.trade.quot.config.Configuration;
import org.apdoer.trade.quot.config.impl.PropertiesConfiguration;
import org.apdoer.trade.quot.constants.QuotConstants;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.process.Processor;
import org.apdoer.trade.quot.process.impl.QuotMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:58
 */
@Slf4j
@Component
public class SourceJobManager extends AbstractJobManager {

    @Autowired
    private QuotConfigCenterService quotConfigCenterService;

    //用于监控
    private Map<String, Job> jobMap = new ConcurrentHashMap<>();
    private Map<String, Runnable> runableMap = new ConcurrentHashMap<>();


    @Override
    public void init() {
        for (Job job : this.getJobList()) {
            this.jobMap.put(job.getJobDesc().getJobName(), job);
            Runnable runable = new JobRunnable(job);
            this.runableMap.put(job.getJobDesc().getJobName(), runable);
            new Thread(runable).start();
        }
    }

    @Override
    protected List<Job> getJobList() {
        log.info("Build job start");
        List<Job> jobList = new ArrayList<>();
        String sourcePath = System.getProperty(QuotConstants.SOURCE_PATH_KEY);
        List<Configuration<String, Object>> configList = this.getSourceConfiguration(sourcePath);
        if (null != configList && configList.size() > 0) {
            for (Configuration<String, Object> config : configList) {
                Processor<IndexPriceMessageSourcePayload, IndexPriceMessageProcessPayload> processor = new QuotMessageProcessor(this.quotConfigCenterService);
                Job job = SourceJobFactory.getFactory().newInstance(config, processor);
                if (null != job) {
                    jobList.add(job);
                    log.info("Build job={}", job.getJobDesc().getJobName());
                }
            }
        }
        log.info("Build job end, jobSize={}", jobList.size());
        return jobList;
    }

    private List<Configuration<String, Object>> getSourceConfiguration(String sourcePath) {
        if (StringUtils.isBlank(sourcePath)) {
            log.info("{} is blank", sourcePath);
            return null;
        }
        List<Configuration<String, Object>> configList = new ArrayList<>();

        File sourceFileRoot = new File(sourcePath);
        if (sourceFileRoot.exists() && sourceFileRoot.isDirectory()) {
            File[] fileList = sourceFileRoot.listFiles();
            for (File file : fileList) {
                if (file.isFile()) {
                    Configuration<String, Object> config = new PropertiesConfiguration(file.getAbsolutePath());
                    config.set(QuotConstants.JOB_NAME_PRE_KEY, file.getName());
                    configList.add(config);
                }
            }
        }
        return configList;
    }

    private class JobRunnable implements Runnable {
        private Job job;
        private CountDownLatch countDownLatch;

        public JobRunnable(Job job) {
            this.job = job;
        }

        public JobRunnable(Job job, CountDownLatch countDownLatch) {
            this.job = job;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                while (!job.getJobDesc().isRunnable()) {
                    Thread.sleep(job.getJobDesc().getDelayTime());
                }
                job.init();
                job.start();

                while (job.getJobDesc().getJobStatus() != JobDesc.JobStatus.STOPED) {
                    try {
                        job.doWork();
                        Thread.sleep(job.getJobDesc().getWorkSleepTime());
                    } catch (Exception e) {
                        log.error("Job work exception", e);
                        job.getJobDesc().exceptionContineTimesAdd();
                        if (job.getJobDesc().isAllowException()) {
                            Thread.sleep(job.getJobDesc().getExceptionSleepTime());
                        } else {
                            this.job.exceptionHandle(e);
                            this.job.getJobDesc().setJobStatus(JobDesc.JobStatus.STOPED);
                        }
                    }
                }
            } catch (Exception e) {
                this.job.exceptionHandle(e);
            } finally {
                try {
                    job.shutdown();
                    job.cleanup();
                } catch (Exception e) {
                    log.error("job work exception", e);
                }
                if (null != countDownLatch) {
                    countDownLatch.countDown();
                }
            }
        }
    }
}

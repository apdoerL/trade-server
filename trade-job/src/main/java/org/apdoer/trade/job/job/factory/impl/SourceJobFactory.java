package org.apdoer.trade.job.job.factory.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.job.job.Job;
import org.apdoer.trade.job.job.JobDesc;
import org.apdoer.trade.job.job.factory.JobFactory;
import org.apdoer.trade.job.job.impl.QuotSourceJob;
import org.apdoer.trade.quot.config.Configuration;
import org.apdoer.trade.quot.constants.QuotConstants;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.process.Processor;
import org.apdoer.trade.quot.source.Source;
import org.apdoer.trade.quot.source.SourceType;
import org.apdoer.trade.quot.source.serialization.Deserializer;

import java.lang.reflect.Constructor;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:34
 */
@Slf4j
public class SourceJobFactory implements JobFactory<String, Object> {
    private SourceJobFactory() {
    }


    private static class InnerSourceJobFactory {
        private static final SourceJobFactory INSTANCE = new SourceJobFactory();
    }

    public static SourceJobFactory getFactory() {
        return InnerSourceJobFactory.INSTANCE;
    }

    @Override
    public Job newInstance(Configuration<String, Object> configuration, Processor processor) {
        try {
            String sourceFileName = configuration.get(QuotConstants.JOB_NAME_PRE_KEY).toString();
            Object sourceType = configuration.get(QuotConstants.SOURCE_TYPE_KEY);
            if (null == sourceType || !SourceType.vaild(sourceType.toString())) {
                log.error("{} {} is bank or type invalid", sourceFileName, QuotConstants.SOURCE_TYPE_KEY);
                return null;
            }
            Object deserializerStr = configuration.get(QuotConstants.SOURCE_DESERIALIZER_CLASS_KEY);
            if (null == deserializerStr) {
                log.error("{} {} is blank;", sourceFileName, QuotConstants.SOURCE_DESERIALIZER_CLASS_KEY);
                return null;
            }
            return this.buildJob(processor, configuration, deserializerStr.toString(), sourceType.toString());
        } catch (Exception e) {
            log.error("Create Job error", e);
            return null;
        }
    }

    private Job buildJob(Processor processor, Configuration<String, Object> configuration, String deserializerClassStr,
                         String sourceType) {
        try {
            Deserializer zmqDeserializer = this.buildDeserializer(deserializerClassStr);
            Object sourceClassObject = configuration.get(QuotConstants.SOURCE_CLASS_KEY);
            if (null == sourceClassObject) {
                log.error("{} is blank", QuotConstants.SOURCE_CLASS_KEY);
                return null;
            }
            Source<IndexPriceMessageSourcePayload> source = this.buildSource(sourceClassObject.toString(), configuration,
                    zmqDeserializer);
            String jobName = sourceType + "_" + configuration.get(QuotConstants.JOB_NAME_PRE_KEY).toString();
            JobDesc jobDescribution = this.buildJobDescribution(jobName, sourceType, configuration);
            return new QuotSourceJob(source, processor, jobDescribution);
        } catch (Exception e) {
            log.error("Create zmq Job error", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Source<IndexPriceMessageSourcePayload> buildSource(String sourceClassStr, Configuration<String, Object> configuration,
                                                               Deserializer deserializer) throws Exception {
        Class<?> clazz = Class.forName(sourceClassStr);
        Class<?>[] parameterTypes = {Configuration.class, Deserializer.class};
        Object[] initArgs = {configuration, deserializer};
        Constructor constructor = clazz.getConstructor(parameterTypes);
        return (Source<IndexPriceMessageSourcePayload>) constructor.newInstance(initArgs);
    }


    private Deserializer buildDeserializer(String deserializerClassStr) throws Exception {
        return (Deserializer) (Class.forName(deserializerClassStr).newInstance());
    }

    private JobDesc buildJobDescribution(String jobName, String jobGroup,
                                         Configuration<String, Object> configuration) {
        JobDesc jobDescribution = this.buildJobDescribution(jobName, jobGroup);
        Object workSleepTime = configuration.get(QuotConstants.SOURCE_WORK_SLEEP_TIME);
        if (null != workSleepTime) {
            jobDescribution.setWorkSleepTime(new Long(workSleepTime.toString()));
        }
        Object blankSleepTime = configuration.get(QuotConstants.SOURCE_BLANK_SLEEP_TIME);
        if (null != blankSleepTime) {
            jobDescribution.setSourceBlankSleepTime(new Long(blankSleepTime.toString()));
        }
        return jobDescribution;
    }


    private JobDesc buildJobDescribution(String jobName, String jobGroup) {
        JobDesc jobDescribution = new JobDesc();
        jobDescribution.setJobType(JobDesc.JobType.QUOT_JOB);
        jobDescribution.setJobGroup(jobGroup);
        jobDescribution.setJobName(jobName);
        return jobDescribution;
    }
}

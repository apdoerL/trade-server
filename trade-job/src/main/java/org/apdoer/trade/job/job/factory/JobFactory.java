package org.apdoer.trade.job.job.factory;

import org.apdoer.trade.job.job.Job;
import org.apdoer.trade.quot.config.Configuration;
import org.apdoer.trade.quot.process.Processor;


/**
 * JOb创建工厂
 * @author apdoer
 */
public interface JobFactory<K, V> {

	Job newInstance(Configuration<K, V> sourceConfiguration, Processor processor);

}
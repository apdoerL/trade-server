package org.apdoer.trade.job.job.manager;


import org.apdoer.trade.job.job.Job;
import org.apdoer.trade.quot.config.Configuration;

import java.util.List;

public abstract class AbstractJobManager {
	
	protected Configuration<String, Object> configuration;
	
	/**
	 * 初始化
	 */
	public abstract void init();
	
	/**
	 * 构造Job
	 * @return
	 */
	protected abstract List<Job> getJobList();
	
}

package com.yaochen.boss.job;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.boss.model.CfgData;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;


/**
 * 业务基础数据加载
 * 
 * @author Tom
 */
@Service
public class BusiCfgDataJob implements Job2, InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private JobComponent jobComponent;

	public static CfgData CFG = null;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		this.execute();
	}
	
	private void execute(){
		logger.info(" ----- 加载基础数据 ------ ");

		try {
			CFG = jobComponent.loadCfgData();
			jobComponent.loadBaseConfig();
		} catch (Exception e) {
			logger.error("加载基础数据失败", e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 在Spring初始化的时候自动加载基础数据
		if(CFG == null){
			this.execute();
		}
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
	
}

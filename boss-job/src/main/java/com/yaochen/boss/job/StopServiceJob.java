package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.commons.constants.SystemConstants;

/**
 * 报停任务
 * @author Tom
 */
@Service
@Scope("prototype")
public class StopServiceJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try{
			//处理报停预的用户
			List<JUserStop> stopJobList = jobComponent.queryUserStopJob();
			for (JUserStop stopJob:stopJobList){
				try{
					busiComponent.userRequireStop(stopJob);
					jobComponent.saveJobExecute(stopJob.getJob_id(), stopJob.getArea_id(),stopJob.getCounty_id(), SystemConstants.BOOLEAN_TRUE, null);
			 		jobComponent.deleteUserStopJob(stopJob.getJob_id());
				}catch(Exception e){
					e.printStackTrace();
					logger.error("报停任务", e.getMessage());
				}
			}
		}catch(Exception e){
			logger.error("报停任务出错", e);
		}
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
	
}

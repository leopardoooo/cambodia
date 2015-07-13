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
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.commons.constants.SystemConstants;

/**
 * 资费变更的任务处理
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class ChangeTariffJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {

		try{
			//查找资费变更任务
			List<JProdNextTariff> tariffJobList = jobComponent.queryTariffJob();
			for (JProdNextTariff tariffJob:tariffJobList){
				try{
					busiComponent.changeTariff(tariffJob);
					jobComponent.saveJobExecute(tariffJob.getJob_id(), tariffJob.getArea_id(),tariffJob.getCounty_id(), SystemConstants.BOOLEAN_TRUE, null);
					jobComponent.deleteTariffJobWithHis(tariffJob);
				}catch(Exception e){
					logger.error("资费变更任务", e.getMessage());
					jobComponent.saveJobExecute(tariffJob.getJob_id(), tariffJob.getArea_id(),tariffJob.getCounty_id(), SystemConstants.BOOLEAN_FALSE, e.getMessage());
				}
			}
		}catch(Exception e){
			logger.error("资费变更任务出错", e);
		}
		
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

}

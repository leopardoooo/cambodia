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
import com.ycsoft.business.dto.core.prod.CProdDto;


/**
 * 自动加授权
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class AutoPlusAccreditJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		
		try{
			//自动加授权
			List<CProdDto> stopJobList = jobComponent.queryAutoBusiCmd();
			if(stopJobList.size()>0){
				busiComponent.saveAutoBusiCmd(stopJobList);
			}
			
		}catch(Exception e){
			logger.error("自动加授权", e);
		}

	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
}

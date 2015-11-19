
package com.yaochen.boss.job;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.AuthComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
/**
 * OSD定时发送执行任务
 */
@Service
public class OsdSendJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AuthComponent authComponent;

	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		try{
			int cnt=authComponent.saveSendPlanOsd();
			if(cnt>0){
				logger.info("执行计划OSD条数："+cnt);
			}
		} catch(Exception e){
			logger.error("执行计划OSD失败"+e.getMessage());
			return;
		}
	
	}

}


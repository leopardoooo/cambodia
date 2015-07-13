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
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;

/**
 * 资金解冻实时处理
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class DealAcctUnfreezeJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		
		try {
			//查找首次需要解冻的资金
			List<CAcctAcctitemInactive> acctUnfreezeJobList = jobComponent.queryAcctFirstUnfreezeJob();
			dealAcctUnfreeze(acctUnfreezeJobList);
			
		} catch (Exception e){
			logger.error("系统错误", e);
		}
		
	}

	public void dealAcctUnfreeze(List<CAcctAcctitemInactive> jobList)  {
		logger.info("资金解冻","启动执行:"+jobList.size());
		for (CAcctAcctitemInactive unfreezeJob:jobList){
			try{
				busiComponent.acctUnfreeze(unfreezeJob);
			}catch(Exception e){
				logger.error("资金解冻","账目【"+unfreezeJob.getAcct_id()+"|"+unfreezeJob.getAcctitem_id()+"】"+e.getMessage());
			}
		}
		logger.info("资金解冻","结束执行");
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
}

package com.yaochen.boss.job.unuse;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.yaochen.myquartz.Task;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;


/**
 * 资金解冻
 * 
 * @author Tom
 */
public class UnFreezeDealTask implements Task {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private List<CAcctAcctitemInactive> acctUnfreezeJobList;
	
	public UnFreezeDealTask(List<CAcctAcctitemInactive> acctUnfreezeJobList){
		this.acctUnfreezeJobList = acctUnfreezeJobList;
	}
	
	@Override
	public void execute(Job2 parentJob, Job2ExecutionContext context)
			throws JobExecutionException {
		AcctUnFreezeJob auf = (AcctUnFreezeJob)parentJob;
		
		for (CAcctAcctitemInactive unfreezeJob: acctUnfreezeJobList){
			try{
				auf.getBusiComponent().acctUnfreeze(unfreezeJob);
			}catch(Exception e){
				logger.error("月初资金解冻", e);
			}
		}
		
	}

}

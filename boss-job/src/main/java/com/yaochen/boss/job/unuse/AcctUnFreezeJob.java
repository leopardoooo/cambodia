package com.yaochen.boss.job.unuse;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.CustomThreadJob;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.daos.core.DataHandler;

/**
 * 月初资金解冻，线程池
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class AcctUnFreezeJob extends CustomThreadJob {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void distribute(Job2ExecutionContext context)
			throws JobExecutionException {
		try {
			final AcctUnFreezeJob that = this;
			logger.info("资金解冻阻塞并发处理开始..");
			jobComponent.queryAcctUnfreezeJob(new DataHandler<CAcctAcctitemInactive>(){
				@Override
				public void fetchRows(List<CAcctAcctitemInactive> results,
						int fetchCount) throws Exception {
					that.runInThread(new UnFreezeDealTask(results));
				}
				
			});
		} catch (Exception e) {
			logger.error("资金解冻失败", e);
		}
	}

	public BusiComponent getBusiComponent() {
		return busiComponent;
	}

	public JobComponent getJobComponent() {
		return jobComponent;
	}
	
	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
}

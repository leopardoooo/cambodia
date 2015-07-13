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
import com.ycsoft.beans.core.job.JCustWriteoffAcct;

/**
 * 删除账目
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class DealDelAcctItemJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try {
			List<JCustWriteoffAcct> delAcctItemList = jobComponent.queryWriteOffAcct();
			dealDelAcctItem(delAcctItemList);
		} catch (Exception e){
			logger.error("系统错误",e);
		}
	}
	
	public void dealDelAcctItem(List<JCustWriteoffAcct> delAcctItemList)throws Exception{
		logger.info("删除账户任务处理，启动执行条数:"+delAcctItemList.size());
		int del_i=0;
		for (JCustWriteoffAcct acctJob:delAcctItemList){
			if (busiComponent.canRemove(acctJob.getJob_id(),acctJob.getAcct_id(),acctJob.getAcctitem_id())){
				try{
					busiComponent.delAcctItem(acctJob.getJob_id(),acctJob.getAcct_id(),acctJob.getAcctitem_id(),acctJob.getDone_code());
					this.jobComponent.removeAcctJob(acctJob.getJob_id());
					del_i++;
				} catch (Exception e){
					logger.error("账目【"+acctJob.getAcct_id()+"|"+acctJob.getAcctitem_id()+"】"+e.getMessage());
				}
			}
		}
		logger.info("删除账户任务结束,成功删除条数:"+del_i);
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

}

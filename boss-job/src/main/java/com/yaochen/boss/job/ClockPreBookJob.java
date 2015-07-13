package com.yaochen.boss.job;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.job.JProdPreopen;
import com.ycsoft.commons.helper.DateHelper;

/**
 * 预开通
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class ClockPreBookJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try{
			int hour = DateHelper.getCurrHour();
			int min = DateHelper.getCurrMinute();
			logger.info("预开通 - " + min + " 分.");
			if (hour ==21){
				//发指令，并删除当天
				openProd(hour,true);
			}else{
				openProd(hour, false);
			}
		} catch (Exception e) {
			logger.error("系统错误",e.getMessage());
		}

	}
	
	/**
	 * 开通产品
	 * @param jobstep
	 */
	private void openProd(int jobstep,boolean isdelete)throws Exception{
		for(JProdPreopen preopen: jobComponent.queryProdPreopen(jobstep)){
			try{
				this.busiComponent.preOpen(jobComponent.queryProdBySn(preopen.getProd_sn(), preopen.getCounty_id()), preopen);
				jobComponent.updateProdPreopenStep(preopen.getJob_id(), jobstep);
				preopen.setIs_success("T");
			}catch(Exception e){
				preopen.setIs_success("F");
				String remark=e.getMessage();
				if(remark!=null&& !remark.trim().equals(""))
					preopen.setRemark(remark.substring(0,remark.length()>100? 100:remark.length()));
				logger.error("预开通错误","job_id("+preopen.getJob_id()+"):"+e.getMessage());
			}
			if(isdelete||"F".equals(preopen.getIs_success()))
				jobComponent.removeProdPreopen(preopen);
		}
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
}

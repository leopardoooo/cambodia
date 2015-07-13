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
import com.ycsoft.beans.core.user.CUser;

/**
 * 缴费的促销
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class FeeDealPromotionJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try {
			List<CUser> userList = jobComponent.queryPromotionUsers();
			dealPromotion(userList);
		} catch (Exception e){
			logger.error("系统错误", e);
		}

	}
	
	public void dealPromotion(List<CUser> userList) throws Exception{
		logger.info("自动促销", "开始执行"+userList.size()+"条");
		for (CUser user:userList){
			try{
				busiComponent.promotion(user.getUser_id());
			} catch (Exception e){
				logger.error("自动促销","用户【"+user.getUser_id()+"】"+e.getMessage());
			}
			//更新用户所有费用
			this.jobComponent.updateFeeAutoPromotion(user.getUser_id(),user.getCust_id(),user.getCounty_id());
		}
		logger.info("自动促销", "结束执行"+userList.size()+"条");
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
}

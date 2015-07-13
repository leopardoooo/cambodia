package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.system.SCounty;

/**
 * 修改长期欠费状态
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class ModifyUserStatusJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try{
			List<SCounty> countyList = busiComponent.queryAllCounty();
			for(SCounty county : countyList){
				List<CUser> userList = busiComponent.queryOwnFeeUser(county.getCounty_id());
				logger.info("长期欠费：", county.getCounty_id()+"共:"+userList.size());
				if(userList != null && userList.size()>0){
					busiComponent.modifyUserStatus(userList);
				}
			}
		}catch(Exception e){
			logger.error("修改长期欠费用户状态时出错", e);
		}

	}
	
	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}
	
}

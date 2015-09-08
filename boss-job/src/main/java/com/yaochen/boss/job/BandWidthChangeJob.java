package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;

/**
 * 宽带用户订单修改带宽
 * 每天晚上11:30执行
 * @author new
 *
 */
@Service
public class BandWidthChangeJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JobComponent jobComponent;
	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			List<String> userIds=jobComponent.queryUserNeedChangeBandWidth();
		} catch (Exception e) {
			logger.error("修改带宽失败",e);
		}
	}
	
	private void changeBandWidth(String userId){
		
	}

}

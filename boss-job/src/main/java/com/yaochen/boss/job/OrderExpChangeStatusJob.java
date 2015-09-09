package com.yaochen.boss.job;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
/**
 * 订单失效修改状态为到期停
 * 每天凌晨1点执行
 * @author new
 *
 */
@Service
public class OrderExpChangeStatusJob implements Job2  {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JobComponent jobComponent;

	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		logger.info("启动处理订单失效修改状态为到期停");
		try{
			jobComponent.changeHasExpOrderStatusToForStop();
			logger.info("结束处理订单失效修改状态为到期停");
		}catch(Exception e){
			logger.error("处理订单失效修改状态为到期停失败",e);
		}
		
	}

}

package com.yaochen.boss.job;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
/**
 * 订单失效修改状态为到期停
 * @author new
 *
 */
public class OrderExpChangeStatusJob implements Job2  {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		
		
	}

}

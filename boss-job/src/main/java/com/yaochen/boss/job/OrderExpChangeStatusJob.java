package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.AuthComponent;
import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.prod.CProdOrder;
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
	@Autowired
	private AuthComponent authComponent;

	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		logger.info("启动处理订单失效修改状态为到期停");
		try{
			Integer doneCode=-7;
			List<CProdOrder>  orders=jobComponent.changeHasExpOrderStatusToForStop(doneCode);
			//宽带所有订单到期日后，对宽带补发清除授权指令，因为宽带在汇聚系统发的长授权
			authComponent.clearBandAuth(orders,doneCode);
			logger.info("结束处理订单失效修改状态为到期停");
		}catch(Exception e){
			logger.error("处理订单失效修改状态为到期停失败",e);
		}
		
	}

}

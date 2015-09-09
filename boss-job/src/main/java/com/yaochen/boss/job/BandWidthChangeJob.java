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
	@Autowired
	private AuthComponent authComponent;
	
	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		try {
			logger.info("启动修改宽带带宽JOB");
			List<CProdOrder> ordres=jobComponent.queryUserNeedChangeBandWidth();
			logger.info("需要处理"+ordres.size()+"个订购");
			for(CProdOrder order:ordres){
				changeBandWidth(order);
			}
			logger.info("结束修改宽带带宽JOB");
		} catch (Exception e) {
			logger.error("修改带宽失败",e);
		}
	}
	
	private void changeBandWidth(CProdOrder order){
		try{
		authComponent.changeBandWidth(order);
		}catch(Exception e){
			logger.error("修改宽带的带宽失败",e);
		}
	}

}

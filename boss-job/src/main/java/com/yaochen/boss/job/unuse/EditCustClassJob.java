package com.yaochen.boss.job.unuse;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.cust.CCust;

/**
 * 修改客户优惠类型
 * 
 * @author Tom
 */
@Service
public class EditCustClassJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		logger.info("定时处理优惠类型失效的客户");
		try {
			List<CCust> custList = busiComponent.queryCustWithInvalidCustClass();
			for (CCust cust : custList) {
				this.doWork(cust);
			}
			logger.info("本次处理结束，共处理"+ custList.size() +"个客户!");
		} catch (Exception e) {
			logger.error("处理优惠类型失效的客户出错", e);
		}
	}
	
	private void doWork(CCust cust){
		Exception e = null;
		try{
			busiComponent.resumeCustClass(cust);
		}catch(Exception _e){
			e = _e;
		}
		if(e == null){
			logger.info("["+ cust.getCust_name() + cust.getCust_no() + "]处理成功", e);
		}else{
			logger.error("["+ cust.getCust_name() + cust.getCust_no() + "]处理失败", e);
		}
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

}

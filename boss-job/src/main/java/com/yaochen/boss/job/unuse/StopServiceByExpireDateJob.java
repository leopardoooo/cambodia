package com.yaochen.boss.job.unuse;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.commons.helper.DateHelper;


/**
 * 按到期日停机
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class StopServiceByExpireDateJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		
		try{
			//23点一个小时内，多次查询操作，一次处理不完
			//删除昨天操作记录
			busiComponent.deleteInvaliCal();
			List<CProdDto> prodList = busiComponent.queryInvalidProd(DateHelper.now());
			for(CProdDto prod : prodList){
				try {
					busiComponent.dealInvalidProd(prod);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("按到期日停机错误", "产品【"+prod.getProd_sn()+"】"+e.getMessage());
				}
			}
			
		}catch(Exception e){
			logger.error("按到期日停机错误", e);
		}

	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}
	
}

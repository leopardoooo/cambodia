package com.yaochen.boss.job.unuse;

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
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.commons.constants.SystemConstants;

/**
 * 同步用户产品报停状态， 缴费后报停，信控会将报停状态的产品变为正常
 * 
 * 当天订购的，整理产品包含关系
 * @author Tom
 */
@Service
@Scope("prototype")
public class ProdIncludeRelationJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try{
			
			//同步用户产品报亭状态， 缴费后报停，信控会将报停状态的产品变为正常
			jobComponent.dealProdStatusError();
			
			// 当天订购的，整理产品包含关系
			List<CDoneCodeDetail> detailList = jobComponent.queryDoneCodeDetailByOrder();
			for (CDoneCodeDetail detail:detailList){
				try{
					int size = busiComponent.setProdInclude(detail.getDone_code(),detail.getCust_id(),detail.getUser_id(), detail.getCounty_id(), detail.getArea_id());
					if(size>0)
						busiComponent.saveProdIncludeRecord(detail.getDone_code(),detail.getCust_id(), detail.getUser_id(), detail.getCounty_id(),SystemConstants.BOOLEAN_TRUE,null);
				}catch(Exception e){
					busiComponent.saveProdIncludeRecord(detail.getDone_code(),detail.getCust_id(), detail.getUser_id(), detail.getCounty_id(),SystemConstants.BOOLEAN_FALSE,e.getMessage().substring(0, 99));
					logger.error("设置产品包含关系","用户【"+detail.getUser_id()+"】"+e.getMessage());
				}
			}
			
		}catch(Exception e){
			logger.error("设置产品包含关系", e);
		}

	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
}

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
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.commons.helper.DateHelper;

/**
 * 产品自动退订
 * 
 * @author Tom
 */
@Service
@Scope("prototype")
public class ProdCancelJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try{
			String prodCancelDays = busiComponent.queryTemplateConfig(TemplateConfigDto.Config.PROD_CANCEL_DAYS.toString(), "5001");
			//不同周期资费变更，欠费停产品有新资费未生效时修改为产品暂停
			List<JProdNextTariff> nextTariffList = jobComponent.queryNextTariffByJobId();
//			for(JProdNextTariff nextTariff : nextTariffList){
//				busiComponent.execPause(nextTariff.getDone_code(), nextTariff.getJob_id(), nextTariff.getProd_sn(), 
//						nextTariff.getOld_tariff_id(), nextTariff.getTariff_id());
//			}
			logger.info("产品暂停结束：当前时间:"+ DateHelper.formatNowTime());
			
			//查找需要自动退订的产品
			while(true){
				List<CProdDto> stopJobList = jobComponent.queryProdStopJob(prodCancelDays);//每次取1000条
				if (stopJobList.size()==0){
					logger.debug("产品自动退订停止");
					break;
				}
				for (CProdDto prod:stopJobList){
					try{
						busiComponent.saveProdStop(prod);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			logger.error("产品自动退订", e);
		}
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
	
}

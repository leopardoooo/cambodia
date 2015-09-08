package com.yaochen.boss.job.unuse;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.DataSynaComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.business.dto.core.prod.CProdDto;

@Service
@Scope("prototype")
public class DataSynaCzlyJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private DataSynaComponent dataSynaComponent;

	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		try{
			dataSynaComponent.dataSynaCzly();
			
		}catch(Exception e){
			logger.error("", e);
		}
	}

	/**
	 * @param dataSynaComponent the dataSynaComponent to set
	 */
	public void setDataSynaComponent(DataSynaComponent dataSynaComponent) {
		this.dataSynaComponent = dataSynaComponent;
	}

}

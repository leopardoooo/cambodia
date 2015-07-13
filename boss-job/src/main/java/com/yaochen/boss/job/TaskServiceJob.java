package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.ly.intface.taskclient.Service1Client;
import com.ycsoft.ly.intface.taskclient.Service1Soap;

/**
 * 发送工单创建信息
 * @author liujq
 */
@Service
@Scope("prototype")
public class TaskServiceJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private JobComponent jobComponent;
	private String wsUrl;

	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		 Service1Soap service =  new Service1Client(wsUrl).getService1Soap();
		try{
			List<TaskQueryWorkDto> tasks = jobComponent.queryUnSyncWork();
			for (TaskQueryWorkDto task :tasks){
				try {
					String string = service.createTask(task.getWork_id(), task.getCust_id(), task.getUser_id(),task.getInstall_addr(), task.getCust_name(), task.getTel(), task.getAddr_pid(), task.getOld_addr(), task.getTask_type(), task.getTask_status(), task.getBooks_time(), "", task.getRemark());
				} catch (Exception e) {
					jobComponent.syncWork(task.getWork_id(),e.getMessage());
					break;
				}
				jobComponent.syncWork(task.getWork_id(), SystemConstants.BOOLEAN_TRUE);
			}
		}catch(Exception e){
			logger.error("出错", e);
		}
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

	/**
	 * @return the wsUrl
	 */
	public String getWsUrl() {
		return wsUrl;
	}

	/**
	 * @param wsUrl the wsUrl to set
	 */
	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}
	
}

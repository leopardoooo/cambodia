/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;
import java.util.Map;

import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.service.impl.TaskService;

/**
 * @author liujiaqi
 * 
 */
public class TaskServiceExternal extends ParentService implements
		ITaskServiceExternal {

	public  List<Map<String,Object>> queryPrintContent(BusiParameter p,String []  taskTypes,String[] cust_id, String [] task_id) throws Exception {
		TaskService taskService = (TaskService) getBean(TaskService.class, p);
		return taskService.queryPrintContent(taskTypes,cust_id,task_id);
	}

	

}

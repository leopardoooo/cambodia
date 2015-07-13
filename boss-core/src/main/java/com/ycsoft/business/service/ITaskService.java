/**
 * 
 */
package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.task.TTaskDetailType;
import com.ycsoft.business.dto.core.cust.QueryTaskConditionDto;
import com.ycsoft.business.dto.core.cust.QueryTaskResultDto;
import com.ycsoft.daos.core.Pager;


/**
 * 工单服务
 * 
 * @author allex
 */
public interface ITaskService {
	/**
	 * @param bugCause
	 */
	public void saveBugTask(String bugCause) throws Exception;
	
	/**
	 * 查询打印内容
	 */
	public List<Map<String,Object>> queryPrintContent(String[] task_types,
			String[] cust_ids, String[] task_ids) throws Exception;
	/**
	 * 查询施工单，根据条件
	 * 
	 * @return
	 * @throws Exception
	 */
	public Pager<QueryTaskResultDto> queryTask(QueryTaskConditionDto cond)
			throws Exception;

	/**
	 * @param task_ids
	 */
	public void cancelTask(String[] task_ids, String cancelRemark)throws Exception;

	/**
	 * @param task_ids
	 * @throws Exception
	 */
	public void assignTask(String[] task_ids) throws Exception;

	/**
	 * @param task_id
	 * @param booksTime
	 * @param taskCustName
	 * @param tel
	 * @param remark
	 */
	public void modifyTask(String task_id, String booksTime,
			String taskCustName, String tel, String remark, String bugCause)throws Exception;

	/**
	 * 
	 */
	public void saveNewTask()throws Exception;

	/**
	 * @return
	 */
	public List<TTaskDetailType> getTaskType() throws Exception;

}

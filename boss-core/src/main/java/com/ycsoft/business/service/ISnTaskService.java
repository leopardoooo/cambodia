package com.ycsoft.business.service;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.beans.task.WTaskBaseInfo;

public interface ISnTaskService {
	/**
	 * 修改施工队
	 * @param taskId
	 * @param deptId
	 * @param bugType 如果工单是故障单
	 * @throws Exception 
	 */
	public void editTaskTeam(String taskId,String deptId,String bugType) throws Exception;
	
	/**
	 * 取消工单
	 * @param taskId
	 */
	public void cancelTask(String taskId) throws Exception;
	
	/**
	 * 回填工单信息
	 * @param taskId
	 * @param otlNo 网络参数
	 * @param ponNo 网络参数
	 * @param deviceList 设备列表
	 */
	public void fillTask(String taskId,String otlNo,String ponNo,List<TaskFillDevice> deviceList)throws Exception;
	
	/**
	 * 完工
	 * @param taskId
	 * @param resultType 完工类型
	 */
	public void finishTask(String taskId,String resultType)throws Exception;
	
	/**
	 * 工单查询
	 * @param taskTypes
	 * @param areaIds
	 * @param beginDate
	 * @param endDate
	 * @param taskId
	 * @param teamId
	 * @param status
	 * @param custNo
	 * @param custName
	 * @param custAddr
	 * @throws Exception
	 */
	public List<WTaskBaseInfo> queryTask(String[] taskTypes,String[] areaIds,Date beginDate,Date endDate,
			String taskId,String teamId,String status,
			String custNo,String custName,String custAddr) throws Exception;
	
	/**
	 * 查询待处理工单
	 * @return
	 * @throws Exception
	 */
	public List<WTaskBaseInfo> queryUnProcessTask() throws Exception;
	
	

}

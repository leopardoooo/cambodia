package com.ycsoft.business.service;

import java.util.List;

import com.ycsoft.beans.task.TaskFillDevice;

public interface ISnTaskService {
	/**
	 * 修改施工队
	 * @param taskId
	 * @param deptId
	 * @param bugType 如果工单是故障单
	 */
	public void editTaskTeam(String taskId,String deptId,String bugType);
	
	/**
	 * 取消工单
	 * @param taskIds
	 */
	public void cancelTask(String[] taskIds);
	
	/**
	 * 回填工单信息
	 * @param taskId
	 * @param otlNo 网络参数
	 * @param ponNo 网络参数
	 * @param deviceList 设备列表
	 */
	public void fillTask(String taskId,String otlNo,String ponNo,List<TaskFillDevice> deviceList);
	
	/**
	 * 完工
	 * @param taskId
	 * @param resultType 完工类型
	 */
	public void finishTask(String taskId,String resultType);

}

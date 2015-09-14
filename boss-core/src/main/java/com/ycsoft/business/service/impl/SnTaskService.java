package com.ycsoft.business.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.SystemException;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.FillUserDeviceDto;
import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.business.component.core.DoneCodeComponent;
import com.ycsoft.business.component.task.SnTaskComponent;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.service.ISnTaskService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;

public class SnTaskService  extends BaseBusiService implements ISnTaskService{

	private SnTaskComponent snTaskComponent ; 
	private WTaskBaseInfoDao wTaskBaseInfoDao;
	private DoneCodeComponent doneCodeComponent;
	private CProdOrderDao cProdOrderDao;
	private CUserDao cUserDao;
	
	@Override
	public void editTaskTeam(String taskId, String deptId, String bugType) throws Exception{
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task == null)
			throw new SystemException("工单不存在!");
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT) && 
				StringHelper.isEmpty(task.getBug_type()))
			throw new SystemException("请指定故障类型!");	
		if (task.getTeam_id().equals(deptId))
			throw new SystemException("施工队不能相同!");	
		if (task.getTask_status().equals(StatusConstants.TASK_CANCEL))
			throw new SystemException("工单已取消，不能修改");	
		if (task.getTask_status().equals(StatusConstants.TASK_END))
			throw new SystemException("工单已完工，不能修改");	
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.changeTaskTeam(doneCode, taskId, deptId,bugType);
		
	}

	@Override
	public void cancelTask(String taskId)  throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.cancelTask(doneCode, taskId);
		
	}

	@Override
	public void fillTask(String taskId, String otlNo, String ponNo, List<TaskFillDevice> deviceList)  throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<WTaskUser> taskUsers = snTaskComponent.fillOpenTaskInfo(doneCode, taskId, otlNo, ponNo, deviceList);
		//修改用户设备信息
		this.fillUserDevice(doneCode, taskUsers);
	}

	
	@Override
	/**
	 * 完工
	 */
	public void finishTask(String taskId, String resultType)  throws Exception{
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task == null)
			throw new SystemException("工单不存在!");
		if (task.getTask_status().equals(StatusConstants.TASK_CANCEL))
			throw new SystemException("工单已取消");	
		if (task.getTask_status().equals(StatusConstants.TASK_END))
			throw new SystemException("工单已完工");	
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.finishTask(doneCode, taskId, resultType);
		List<CUser> users = cUserDao.queryTaskUser(taskId);
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL) && 
				resultType.equals(SystemConstants.TASK_FINISH_TYPE_SUCCESS)){
			//如果是安装工单且为正常完工，修改用户产品的开始计费日期和状态 
			installSuccess(doneCode, task.getTask_id(), users);
		} else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL) && 
				resultType.equals(SystemConstants.TASK_FINISH_TYPE_FAILURE)){
			//如果是安装工单且施工失败，修改用户状态为施工失败
			for (CUser user:users){
				updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.INSTALL_FAILURE);
				//对所有用户发解授权
				List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
				authComponent.sendAuth(user, prodList, BusiCmdConstants.PASSVATE_USER, doneCode);
			}
		}
		
		saveAllPublic(doneCode, getBusiParam());
		
	}
	
	public void installSuccess(Integer doneCode,String custId,List<CUser> users) throws Exception {
		//获取操作的客户、用户信息
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(custId);
		boolean isCustPkgOpen = false;
		for(CUser user:users){
			updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.ACTIVE);
			//修改订单状态为正常状态，并更新到期日
			Date startDate = null;
			String curProdId = null;
			for (CProdOrderDto order:orderList){
				if (StringHelper.isNotEmpty(order.getUser_id()) && order.getUser_id().equals(user.getUser_id())){
					if (curProdId == null || !order.getProd_id().equals(curProdId))
						startDate = null;
					startDate = openProd(doneCode, order,startDate);
					if (StringHelper.isNotEmpty(order.getPackage_sn())){
						isCustPkgOpen = true;
					}
					
					curProdId = order.getProd_id();
				}
			}
			
			//发授权
			authComponent.sendAuth(user, null, BusiCmdConstants.ACCTIVATE_USER, doneCode);
			//产品的到期日可能变化了，需要重发加授权
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			authComponent.sendAuth(user, prodList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
		}
		
		if (isCustPkgOpen){
			//修改套餐状态
			Date startDate = null;
			for (CProdOrderDto order:orderList){
				if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
					startDate = openProd(doneCode, order,startDate);
				}
			}
		}
		
	}

	@Override
	public List<WTaskBaseInfo> queryTask(String[] taskTypes, String[] areaIds, Date beginDate, Date endDate,
			String taskId, String teamId, String status, String custNo, String custName, String custAddr)
					throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WTaskBaseInfo> queryUnProcessTask() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}

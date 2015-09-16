package com.ycsoft.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.beans.task.WTeam;
import com.ycsoft.business.component.core.DoneCodeComponent;
import com.ycsoft.business.component.resource.DeviceComponent;
import com.ycsoft.business.component.task.SnTaskComponent;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.business.dao.task.WTeamDao;
import com.ycsoft.business.dto.config.TaskBaseInfoDto;
import com.ycsoft.business.dto.config.TaskUserDto;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.dto.device.DeviceSmallDto;
import com.ycsoft.business.service.ISnTaskService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;
@Service
public class SnTaskService  extends BaseBusiService implements ISnTaskService{
	@Autowired
	private SnTaskComponent snTaskComponent ; 
	@Autowired
	private WTaskBaseInfoDao wTaskBaseInfoDao;
	@Autowired
	private DoneCodeComponent doneCodeComponent;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private WTaskUserDao wTaskUserDao;
	@Autowired
	private WTaskLogDao wTaskLogDao;
	@Autowired
	private DeviceComponent deviceComponent;
	@Autowired
	private WTeamDao wTeamDao;
	@Autowired
	private CCustDao cCustDao;
	
	
	
	@Override
	public void createBugTask(String custId, String bugDetail) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = cCustDao.findByKey(custId);
		snTaskComponent.createBugTask(doneCode, cust, bugDetail);
		
	}

	@Override
	public void editTaskTeam(String taskId, String deptId, String bugType) throws Exception{
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task == null)
			throw new SystemException("工单不存在!");
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT) && 
				StringHelper.isEmpty(bugType))
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

	public Pager<TaskBaseInfoDto> queryTask(String taskTypes, String addrIds, String beginDate, String endDate,
			String taskId, String teamId, String status, String custNo, String custName, String custAddr,String mobile, Integer start, Integer limit)
					throws Exception {
		return wTaskBaseInfoDao.queryTask(taskTypes,addrIds,beginDate,endDate,taskId,teamId,status,custNo,custName,custAddr,mobile, start, limit);
	}

	@Override
	public List<WTaskBaseInfo> queryUnProcessTask() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Map<String , Object> queryTaskDetail(String task_id)throws Exception{
		Map<String , Object> map = new HashMap<String, Object>();
		List<TaskUserDto> userList = wTaskUserDao.queryUserDetailByTaskId(task_id);
		if(userList.size()>0){
			List<DeviceSmallDto> list = deviceComponent.getDeviceCodeByDeviceId(CollectionHelper.converValueToArray(userList, "device_id"));
			if(list!=null && list.size()>0){
				Map<String, DeviceSmallDto> deviceMap = CollectionHelper.converToMapSingle(list, "device_id");
				if(deviceMap != null)
				for(TaskUserDto dto: userList){
					DeviceSmallDto device = deviceMap.get(dto.getDevice_id());
					if(device != null){
						dto.setDevice_code(device.getDevice_code());
					}
				}
			}
		}
		List<WTaskLog> logList = wTaskLogDao.queryByTaskId(task_id);
		map.put("taskUserList", userList);
		map.put("taskLogList", logList);	
		return map;
	}

	public List<WTeam> queryTaskTeam() throws Exception {
		return wTeamDao.findAll();
	}

	public DeviceDto queryDeviceInfoByCodeAndModel(String deviceCode, String deviceModel) throws Exception{
		return deviceComponent.queryDeviceInfoByCodeAndModel(deviceCode, deviceModel);
	}
	

}

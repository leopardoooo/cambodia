package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TDeviceBuyMode;
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
import com.ycsoft.business.service.ISnTaskService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
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
			throw new ServicesException("工单不存在!");
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT) && 
				StringHelper.isEmpty(bugType))
			throw new ServicesException("请指定故障类型!");	
		if (task.getTask_status().equals(StatusConstants.TASK_CANCEL))
			throw new ServicesException("工单已取消，不能修改");	
		if (task.getTask_status().equals(StatusConstants.TASK_END))
			throw new ServicesException("工单已完工，不能修改");	
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.changeTaskTeam(doneCode, taskId, deptId,bugType);
		
	}

	@Override
	public void cancelTask(String taskId)  throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.cancelTask(doneCode, taskId);
		getBusiParam().setBusiCode(BusiCodeConstants.TASK_CANCEL);
		saveAllPublic(doneCode, getBusiParam());
		
	}
	
	public void withdrawTask(String taskId)  throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.withdrawTask(doneCode, taskId);
	}
	
	//回填销户回收设备
	public void fillWriteOffTerminalTask(String taskId,String[] userIds) throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.fillWriteOffTerminalTask(doneCode,taskId,userIds);
	}

	//回填开户、移机、故障单
	@Override
	public void fillTask(String taskId, List<TaskFillDevice> deviceList)  throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//验证设备信息
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task == null){
			throw new ServicesException("工单不存在!");
		}
		List<WTaskUser> userList = wTaskUserDao.queryByTaskId(taskId);
		//验证回填设备并设置设备信息
		checkDevice(deviceList,task,userList);
		snTaskComponent.fillTaskInfo(doneCode, task,userList, deviceList );
		//修改用户设备信息
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)){
			this.fillInstallUserDevice(doneCode, deviceList);
		}
		
	}
	
	//回填新装用户设备
	private void fillInstallUserDevice(int doneCode,List<TaskFillDevice> deviceList) throws Exception {
		CCust cust = null;
		for (TaskFillDevice fillDevice:deviceList){
			CUser user = userComponent.queryUserById(fillDevice.getUserId());
			setUserDeviceInfo(user, fillDevice.getDevice());
			userComponent.updateDevice(doneCode, user);
			//发送授权
			if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)||
					user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
				//开户指令
				this.createUserJob(user, user.getCust_id(), doneCode);
			}
			//发产品授权
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			authComponent.sendAuth(user, prodList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
			//处理设备
			TDeviceBuyMode buyModeCfg = busiConfigComponent.queryBuyMode(user.getStr10());
			String ownership = SystemConstants.OWNERSHIP_GD;
			if (buyModeCfg.getChange_ownship().equals(SystemConstants.BOOLEAN_TRUE))
				ownership = SystemConstants.OWNERSHIP_CUST;
			if (cust == null)
				cust = custComponent.queryCustById(user.getCust_id());
			this.buyDevice(fillDevice.getDevice(), user.getStr10(), ownership, null, BusiCodeConstants.TASK_FILL, cust, doneCode);
			if (StringHelper.isNotEmpty(fillDevice.getOldDeviceCode())){
				
				DeviceDto oldDevice = fillDevice.getOldDevice();
				//更新设备仓库状态
				deviceComponent.updateDeviceDepotStatus(doneCode, BusiCodeConstants.TASK_FILL, oldDevice.getDevice_id(),
						oldDevice.getDepot_status(), StatusConstants.IDLE,true);
				//更新设备产权
				if (!SystemConstants.OWNERSHIP_GD.equals(ownership)){
					deviceComponent.updateDeviceOwnership(doneCode, BusiCodeConstants.TASK_FILL,
							oldDevice.getDevice_id(),oldDevice.getOwnership(),SystemConstants.OWNERSHIP_GD,true);
				}
				
			}
		}
	}

	
	private void checkDevice(List<TaskFillDevice> deviceList,WTaskBaseInfo task,List<WTaskUser> userList) throws Exception{
		//验证工单状态是施工中状态
		if (task.getTask_status().equals(StatusConstants.TASK_INIT)){
			if (task.getTask_status().equals(StatusConstants.TASK_CANCEL))
				throw new ServicesException("工单已取消");	
			if (task.getTask_status().equals(StatusConstants.TASK_END))
				throw new ServicesException("工单已完工");	
		}
		
		//判断设备是否正确
		int index=0;
		for (TaskFillDevice fillDevice:deviceList){
			if (StringHelper.isEmpty(fillDevice.getDeviceCode()))
				throw new ServicesException("所有设备编号不能为空");
			if (!task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL) && 
					!fillDevice.isFcPort()){
				throw new ServicesException("非安装工单不能回填OTT设备");
			}
			
			if(fillDevice.isFcPort()){
				if (StringHelper.isEmpty(fillDevice.getOccNo()) || 
						StringHelper.isEmpty(fillDevice.getPosNo()))
					//判断是否是柬光施工
					if (snTaskComponent.getTeamId(SystemConstants.TEAM_TYPE_CFOCN).equals(task.getTeam_id()))
						throw new ServicesException("光口设备"+fillDevice.getDeviceCode()+"没有交接箱或分光器编号");
			}
			boolean isVirtual = false;//宽带虚拟设备为 virtual_**(user_id)
			if(task.getTask_type_id().equals(SystemConstants.TASK_TYPE_MOVE) || task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT)){
				String[] userId = fillDevice.getDeviceCode().split("_");
				if(userId.length == 2){
					isVirtual = true;
				}
			}
			
			//查找设备信息
			DeviceDto device = new DeviceDto();
			if(!isVirtual){//非虚拟宽带设备
				device = deviceComponent.queryDeviceByDeviceCode(fillDevice.getDeviceCode());
				if (device == null)
					throw new ServicesException(fillDevice.getDeviceCode()+"不存在");
				//设备类型是否正确
				if (!fillDevice.isFcPort() == device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM))
					throw new ServicesException(fillDevice.getDeviceCode()+"设备类型不正确");
			}
			//检查更换设备操作中，旧设备是否存在;设置设备对应的用户id
			if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)){
				//判断设备是否被其他用户使用
				CUser cu = cUserDao.queryUserByDeviceCode(fillDevice.getDeviceCode());
				if (cu != null)
					throw new ServicesException(fillDevice.getDeviceCode()+"已被其他用户使用");
				
				if (StringHelper.isNotEmpty(fillDevice.getOldDeviceCode())){
					//更换设备
					DeviceDto oldDevice = deviceComponent.queryDeviceByDeviceCode(fillDevice.getOldDeviceCode());
					if (oldDevice == null)
						throw new ServicesException(fillDevice.getOldDeviceCode()+"不存在");
					//检查新旧设备设备类型是否一致
					if (!oldDevice.getDevice_type().equals(device.getDevice_type()))
						throw new ServicesException(fillDevice.getOldDeviceCode()+"和"+fillDevice.getDeviceCode()+"设备类型不一致");
					
					//判断旧设备是否是本次施工用户使用的设备
					boolean  exists = false;
					for (WTaskUser user:userList){
						if (fillDevice.getOldDeviceCode().equals(user.getDevice_id())){
							exists = true;
							fillDevice.setUserId(user.getUser_id());
							break;
						}
					}
					
					if (!exists){
						throw new ServicesException("没有用户使用"+fillDevice.getOldDeviceCode()+",不能更换");
					}
					fillDevice.setOldDevice(oldDevice);
				} else {
					//新装设备，设置设备对应的用户id
					boolean  exists = false;
					for (;index<userList.size();index++){
						WTaskUser user = userList.get(index);
						if (StringHelper.isEmpty(user.getDevice_id())){
							if(fillDevice.isFcPort() ==  user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
								fillDevice.setUserId(user.getUser_id());
								index++;
								exists=true;
								break;
							}
						}
					}
					if (!exists){
						if (fillDevice.isFcPort())
							throw new ServicesException("回填的光猫过多");
						else 
							throw new ServicesException("回填的机顶盒过多");
					}
						
				}
				
			} else {
				//移机单、故障单是不能更换设备的，但是CFOCN通过变更设备来实现光路信息回传，因此新设备编号是当前用户使用的光猫
				boolean  exists = false;
				for (WTaskUser user:userList){
					if (fillDevice.getDeviceCode().equals(user.getDevice_id())){
						if(isVirtual){
							String[] userIds = fillDevice.getDeviceCode().split("_");
							if(userIds.length==2){
								fillDevice.setUserId(userIds[1]);
							}else{
								throw new ServicesException("虚拟设备格式错误");
							}
						}else{
							fillDevice.setUserId(user.getUser_id());
						}
						exists = true;
						break;
					}
				}
				
			}
			
			fillDevice.setDevice(device);
		}
	}

	@Override
	/**
	 * 完工
	 * isBusi=true表示完工是前台发起
	 */
	public void finishTask(String taskId, String resultType,String remark,boolean isBusi)  throws Exception{
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task == null)
			throw new ServicesException("工单不存在!");
		if (task.getTask_status().equals(StatusConstants.TASK_CANCEL))
			throw new ServicesException("工单已取消");	
		if (task.getTask_status().equals(StatusConstants.TASK_END))
			throw new ServicesException("工单已完工");	
		if(!task.getTask_status().equals(StatusConstants.TASK_INIT)
				&&!task.getTask_status().equals(StatusConstants.TASK_ENDWAIT)){
			throw new ServicesException("工单可完工状态");	
		}
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.finishTask(doneCode, task, resultType,remark);
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
		} else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL)){
			//回收设备
			List<WTaskUser> userList = wTaskUserDao.queryByTaskId(taskId);
			for (WTaskUser user:userList){
				if(StringHelper.isEmpty(user.getRecycle_result())){
					throw new ServicesException("需要先回填设备，确认是否回收");
				}
				if (user.getRecycle_device().equals(SystemConstants.BOOLEAN_TRUE)){
					if(user.getRecycle_result().equals(SystemConstants.BOOLEAN_TRUE) ){
						//更新设备状态和仓库
						DeviceDto device = deviceComponent.queryDeviceByDeviceCode(user.getDevice_id());
						deviceComponent.updateDeviceDepotId(doneCode, BusiCodeConstants.TASK_FINISH, device.getDevice_id(), 
								device.getDepot_id(), getOptr().getDept_id(), true);
						deviceComponent.updateDeviceDepotStatus(doneCode, BusiCodeConstants.TASK_FINISH, device.getDevice_id(),
								device.getDepot_status(), StatusConstants.IDLE, true);
						//删除客户设备
						custComponent.removeDevice(task.getCust_id(), device.getDevice_id(), doneCode, SystemConstants.BOOLEAN_FALSE);
						//更新用户设备信息为空
						CUser cuser = new CUser();
						cuser.setUser_id(user.getUser_id());
						cuser.setStb_id("");
						cuser.setCard_id("");
						cuser.setModem_mac("");
						cuser.setStatus(StatusConstants.UNTUCKEND);
						cUserDao.update(cuser);
					}else{
						//supernet的设备，但是没有回收，需要前台收取设备费用
						CUser cuser = new CUser();
						cuser.setUser_id(user.getUser_id());
						cuser.setStatus(StatusConstants.UNTUCKEND);
						cUserDao.update(cuser);
					}
				}
			}
		}else if(task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT)&&!isBusi){
			//故障单，如果是cfocn完工，状态改为完工等待，需要supernet这边确认完工或重派给自己部门处理
			snTaskComponent.updateTaskStatus(taskId, StatusConstants.TASK_ENDWAIT);
			
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
			String taskId, String teamId, String status, String custNo, String custName, String custAddr,
			String mobile,String zteStatus, String syncStatus, Integer start, Integer limit)
					throws Exception {
		return wTaskBaseInfoDao.queryTask(taskTypes,addrIds,beginDate,endDate,taskId,teamId,status,custNo,custName
				,custAddr,mobile,zteStatus,syncStatus, start, limit);
	}

	public Pager<TaskBaseInfoDto> queryUnProcessTask(Integer start, Integer limit) throws Exception {
		return wTaskBaseInfoDao.queryUnProcessTask(snTaskComponent.getTeamId(SystemConstants.TASK_ASSIGN_SUPPERNET),start, limit);
	}

	
	public Map<String , Object> queryTaskDetail(String task_id)throws Exception{
		Map<String , Object> map = new HashMap<String, Object>();
		List<TaskUserDto> userList = wTaskUserDao.queryUserDetailByTaskId(task_id);

		if(userList.size()>0){
			String[] userIds = CollectionHelper.converValueToArray(userList, "user_id"); 
					
			List<CUser> users = userComponent.queryAllUserAndHisByUserIds(userIds);
			Map<String,CUser> userMap = CollectionHelper.converToMapSingle(users, "user_id");
			for(TaskUserDto task: userList){
				CUser user = userMap.get(task.getUser_id());
				if(user == null){
					throw new ServicesException("用户不存在");
				}
				task.setUser_name( taskComponent.getFillUserName(user) );
				task.setOccNo(user.getStr7());
				task.setPosNo(user.getStr8());
				if(!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
					if(StringHelper.isEmpty(user.getDevice_model()) && StringHelper.isNotEmpty(user.getStr3())){
						user.setDevice_model(user.getStr3());
						if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
							task.setDevice_model_text( MemoryDict.getDictName(DictKey.MODEM_MODEL, user.getStr3()) );
						}else{
							task.setDevice_model_text( MemoryDict.getDictName(DictKey.STB_MODEL, user.getStr3()) );
						}
					}
				}
			}
		}
		
		List<WTaskLog> logList = wTaskLogDao.queryByTaskId(task_id);
		for(WTaskLog log : logList){
			String errorstr = StringHelper.isNotEmpty(log.getError_remark())?log.getError_remark():"";
			String logstr = log.getLog_detail()==null?"":log.getLog_detail();
			log.setLog_detail(logstr+errorstr);
		}
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

	public List<TaskBaseInfoDto> queryTaskByCustId(String custId) throws Exception {
		// TODO Auto-generated method stub
		return wTaskBaseInfoDao.queryTaskByCustId(custId);
	}

	public List<TaskUserDto> queryTaskDevice(String task_id) throws Exception {
		WTaskBaseInfo taskBase = wTaskBaseInfoDao.findByKey(task_id);
		List<TaskUserDto> userList = new ArrayList<TaskUserDto>();
		if(taskBase.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL)){
			userList = wTaskUserDao.queryTaskWriteoffTerminal(task_id);
		}else{
			userList = wTaskUserDao.queryUserDetailByTaskId(task_id);
		}
		for(TaskUserDto task: userList){
			CUser user = userComponent.queryUserById(task.getUser_id());
			task.setUser_name( taskComponent.getFillUserName(user) );
			if(taskBase.getTask_type_id().equals(SystemConstants.TASK_TYPE_MOVE) && user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				if(StringHelper.isNotEmpty(task.getDevice_id())){
					task.setDevice_code(task.getDevice_id());
					task.setDevice_id(null);
				}
			}
			task.setOccNo(user.getStr7());
			task.setPosNo(user.getStr8());
			if(!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
				if(StringHelper.isEmpty(user.getDevice_model()) && StringHelper.isNotEmpty(user.getStr3())){
					user.setDevice_model(user.getStr3());
					if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
						task.setDevice_model_text( MemoryDict.getDictName(DictKey.MODEM_MODEL, user.getStr3()) );
					}else{
						task.setDevice_model_text( MemoryDict.getDictName(DictKey.STB_MODEL, user.getStr3()) );
					}
				}
			}
		}
		return userList;
	}

	@Override
	public void saveZte(String task_id, String zte_status, String log_remark) throws Exception {
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(task_id);
		if (task == null)
			throw new ServicesException("工单不存在!");
		if (task.getTask_status().equals(StatusConstants.TASK_CANCEL))
			throw new ServicesException("工单已取消");	
		if (task.getTask_status().equals(StatusConstants.TASK_END))
			throw new ServicesException("工单已完工");	
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.saveZte(doneCode, task_id, zte_status,log_remark);
		
	}

	

}

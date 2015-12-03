package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.beans.task.WTeam;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.core.DoneCodeComponent;
import com.ycsoft.business.component.resource.DeviceComponent;
import com.ycsoft.business.component.task.SnTaskComponent;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.business.dao.task.WTeamDao;
import com.ycsoft.business.dto.config.TaskBaseInfoDto;
import com.ycsoft.business.dto.config.TaskUserDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.ISnTaskService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
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
	@Autowired
	private PProdDao pProdDao;
	
	@Override
	public void createBugTask(String custId, String bugDetail) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = cCustDao.findByKey(custId);
		String taskId = snTaskComponent.createBugTask(doneCode, cust, bugDetail, "");
		this.setDoneCodeInfo(taskId, getBusiParam(), BusiCodeConstants.TASK_INIT);
		saveAllPublic(doneCode, getBusiParam());
	}

	@Override
	public void editTaskTeam(String taskId, String deptId, String optrId, String bugType,String finishRemark) throws Exception{
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task == null)
			throw new ServicesException("工单不存在!");
		/**if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT) && 
				StringHelper.isEmpty(bugType))
			throw new ServicesException("请指定故障类型!");	**/
		if (task.getTask_status().equals(StatusConstants.TASK_CANCEL))
			throw new ServicesException("工单已取消，不能修改");	
		if (task.getTask_status().equals(StatusConstants.TASK_END))
			throw new ServicesException("工单已完工，不能修改");	
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		this.setDoneCodeInfo(taskId, getBusiParam(), BusiCodeConstants.TASK_ASSIGN);
		snTaskComponent.changeTaskTeam(doneCode, taskId, deptId, optrId,bugType,finishRemark);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId);
		saveAllPublic(doneCode, getBusiParam());
	}
	/**
	 * 记录BOSS接口被调用报错日志
	 * @param taskId
	 * @param errorInfo
	 * @throws Exception
	 */
	public void saveErrorLog(String taskId,String busiCode,String errorInfo) throws Exception{
		if(errorInfo!=null&&errorInfo.length()>150){
			errorInfo=errorInfo.substring(0, 150);
		}
		snTaskComponent.createTaskLog(taskId, busiCode,  doneCodeComponent.gDoneCode(), errorInfo, StatusConstants.FAILURE);
	}

	@Override
	public void cancelTask(String taskId)  throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		this.setDoneCodeInfo(taskId, getBusiParam(), BusiCodeConstants.TASK_CANCEL);
		snTaskComponent.cancelTask(doneCode, taskId);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId);
		saveAllPublic(doneCode, getBusiParam());
		
	}
	
	public void withdrawTask(String taskId)  throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		this.setDoneCodeInfo(taskId, getBusiParam(), BusiCodeConstants.TASK_Withdraw);
		snTaskComponent.withdrawTask(doneCode, taskId);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId);
		saveAllPublic(doneCode, getBusiParam());
	}
	
	//回填销户回收设备,不管设备是否属于客户都可以回收
	public void fillWriteOffTerminalTask(String taskId,List<TaskFillDevice> deviceList) throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		this.setDoneCodeInfo(taskId, getBusiParam(), BusiCodeConstants.TASK_FILL);
		
		Map<String, List<TaskFillDevice>> map = CollectionHelper.converToMap(deviceList, "recycle_result");
		for(String key : map.keySet()){
			List<TaskFillDevice> userList = map.get(key);
			if(key.equals(SystemConstants.BOOLEAN_TRUE)){
				for(TaskFillDevice dto : userList){
					CUser user = userComponent.queryUserById(dto.getUserId());
					if(user!=null){
						authComponent.sendAuth(user, null, BusiCmdConstants.DEL_USER, doneCode);
					}
				}
			}
			snTaskComponent.fillWriteOffTerminalTask(doneCode,taskId,CollectionHelper.converValueToArray(userList, "userId"),key);
		}
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId);
		saveAllPublic(doneCode, getBusiParam());
	}
	
	private void setDoneCodeInfo(String taskId, BusiParameter parameter, String busiCode) throws Exception {
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task == null){
			throw new ServicesException("工单不存在!");
		}
		CCust cust = cCustDao.findByKey(task.getCust_id());
		if(cust == null){
			throw new ServicesException("客户不存在!");
		}
		List<CUser> userList = cUserDao.queryTaskUser(taskId);
		if(userList.size() > 0){
			parameter.setSelectedUsers(userList);
		}
		CustFullInfoDto custFullDto = new CustFullInfoDto();
		custFullDto.setCust(cust);
		parameter.setCustFullInfo(custFullDto);
		parameter.setBusiCode(busiCode);
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
			this.fillInstallUserDevice(doneCode, deviceList,task);
		}
		this.setDoneCodeInfo(taskId, getBusiParam(), BusiCodeConstants.TASK_FILL);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId);
		saveAllPublic(doneCode, getBusiParam());
	}
	
	//回填新装用户设备
	private void fillInstallUserDevice(int doneCode,List<TaskFillDevice> deviceList,WTaskBaseInfo task) throws Exception {
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
			//回填更换设备
			if (StringHelper.isNotEmpty(fillDevice.getOldDeviceCode()) && fillDevice.getOldDevice() != null){
				DeviceDto oldDevice = fillDevice.getOldDevice();
				//删除custdevice的旧设备
				custComponent.removeDevice(task.getCust_id(), oldDevice.getDevice_id(), doneCode, SystemConstants.BOOLEAN_FALSE);
				//更新设备仓库状态
				deviceComponent.updateDeviceDepotStatus(doneCode, BusiCodeConstants.TASK_FILL, oldDevice.getDevice_id(),
						oldDevice.getDepot_status(), StatusConstants.IDLE,null,true);
				//更新设备产权
				if (!SystemConstants.OWNERSHIP_GD.equals(ownership)){
					deviceComponent.updateDeviceOwnership(doneCode, BusiCodeConstants.TASK_FILL,
							oldDevice.getDevice_id(),oldDevice.getOwnership(),SystemConstants.OWNERSHIP_GD,null,true);
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

	/**
	 * 完工
	 * isBusi=true表示完工是前台发起
	 */
	@Override
	public void finishTask(String taskId, String resultType, String bugType, String custSignNo, String remark, boolean isBusi)  throws Exception{
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
		if(!isBusi&&StatusConstants.NOT_EXEC.equals(task.getZte_status())){
			throw new ServicesException("请等待supernet完成ZTE授权.Please Wait supernet ZTE Auth.");
		}
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		snTaskComponent.finishTask(doneCode, task, resultType, bugType, custSignNo, remark);
		List<CUser> users = cUserDao.queryTaskUser(taskId);
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL) && 
				resultType.equals(SystemConstants.TASK_FINISH_TYPE_SUCCESS)){
			//如果是安装工单且为正常完工，修改用户产品的开始计费日期和状态 
			installSuccess(doneCode, task.getCust_id(), users);
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
				CUser cuser = cUserDao.findByKey(user.getUser_id());
				if(user.getRecycle_result().equals(SystemConstants.BOOLEAN_TRUE) ){
					//更新设备状态和仓库
					DeviceDto device = deviceComponent.queryDeviceByDeviceCode(user.getDevice_id());
					deviceComponent.updateDeviceDepotId(doneCode, BusiCodeConstants.TASK_FINISH, device.getDevice_id(), 
							device.getDepot_id(), getOptr().getDept_id(),cuser.getStr10(), true);
					deviceComponent.updateDeviceDepotStatus(doneCode, BusiCodeConstants.TASK_FINISH, device.getDevice_id(),
							device.getDepot_status(), StatusConstants.IDLE,cuser.getStr10(), true);
					deviceComponent.updateDeviceOwnership(doneCode, BusiCodeConstants.TASK_FINISH, device.getDevice_id(), 
							device.getOwnership(), SystemConstants.OWNERSHIP_GD, cuser.getStr10(), true);
					//删除客户设备
					custComponent.removeDevice(task.getCust_id(), device.getDevice_id(), doneCode, SystemConstants.BOOLEAN_FALSE);
					//更新用户设备信息为空
					
					List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
					if(StringHelper.isNotEmpty(cuser.getStb_id()))
						propChangeList.add(new CUserPropChange("stb_id", cuser.getStb_id(), ""));
					if(StringHelper.isNotEmpty(cuser.getCard_id()))
						propChangeList.add(new CUserPropChange("card_id", cuser.getCard_id(), ""));
					if(StringHelper.isNotEmpty(cuser.getModem_mac()))
						propChangeList.add(new CUserPropChange("modem_mac", cuser.getModem_mac(), ""));
					propChangeList.add(new CUserPropChange("status", cuser.getStatus(), StatusConstants.UNTUCKEND));
					propChangeList.add(new CUserPropChange("status_date", DateHelper.dateToStr(cuser.getStatus_date()),DateHelper.dateToStr(new Date())));
					userComponent.editUser(doneCode, user.getUser_id(), propChangeList);
				}else{
					//supernet的设备，但是没有回收，需要前台收取设备费用
					List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
					propChangeList.add(new CUserPropChange("status", cuser.getStatus(), StatusConstants.UNTUCKEND));
					propChangeList.add(new CUserPropChange("status_date", DateHelper.dateToStr(cuser.getStatus_date()),DateHelper.dateToStr(new Date())));
					userComponent.editUser(doneCode, user.getUser_id(), propChangeList);
				}
			}
		}else if(task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT)&&!isBusi){
			//故障单，如果是cfocn完工，状态改为完工等待，需要supernet这边确认完工或重派给自己部门处理
			snTaskComponent.updateTaskStatus(taskId, StatusConstants.TASK_ENDWAIT);
			
		}
		
		BusiParameter parameter = getBusiParam();
		if(parameter == null){
			parameter = new BusiParameter();
		}
		parameter.setSelectedUsers(users);
		this.setDoneCodeInfo(taskId, parameter, BusiCodeConstants.TASK_FINISH);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId);
		saveAllPublic(doneCode, parameter);
	}
	
	public void editCustSignNo(String taskId, String custSignNo) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if(task == null)
			throw new ServicesException("工单不存在!");
		if(!task.getTask_status().equals(StatusConstants.TASK_END))
			throw new ServicesException("工单不是完工中，不能修改回单客户签单号!");
		BusiParameter parameter = getBusiParam();
		if(parameter == null){
			parameter = new BusiParameter();
		}
		this.setDoneCodeInfo(taskId, parameter, BusiCodeConstants.TASK_MODIFY_CUSTSIGNNO);
		snTaskComponent.editCustSignNo(doneCode, task, custSignNo);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+taskId); 
		saveAllPublic(doneCode, parameter);	
	}
	
	public void installSuccess(Integer doneCode,String custId, List<CUser> users) throws Exception {
		//获取操作的客户、用户信息
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(custId);
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
					
					curProdId = order.getProd_id();
				}
			}
			
			//发授权
			authComponent.sendAuth(user, null, BusiCmdConstants.ACCTIVATE_USER, doneCode);
			//产品的到期日可能变化了，需要重发加授权
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			authComponent.sendAuth(user, prodList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
			
			/*for(CProdOrder order : prodList){
				List<CProdPropChange> propChangeList = new ArrayList<CProdPropChange>();
				propChangeList.add(new CProdPropChange("status", order.getStatus(), StatusConstants.ACTIVE));
				propChangeList.add(new CProdPropChange("status_date", DateHelper.dateToStr(order.getStatus_date()), DateHelper.formatNow()));
				userProdComponent.editProd(doneCode, order.getOrder_sn(), propChangeList);
			}*/
		}
		
		Date startDate = null;
		boolean isOpen = true;
		for (CProdOrderDto order:orderList){
			if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				List<CProdOrder> prodChildren = orderComponent.queryPakDetailOrder(order.getOrder_sn());
				for(CProdOrder child : prodChildren){
					if(!child.getStatus().equals(StatusConstants.ACTIVE)){
						isOpen = false;
						break;
					}
				}
				if(isOpen){
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
		return wTaskBaseInfoDao.queryUnProcessTask(snTaskComponent.getTeamId(SystemConstants.TASK_ASSIGN_SUPPERNET),getOptr()==null?null:getOptr().getOptr_id(),start, limit);
	}

	public Map<String , Object> queryAllTaskDetail(String task_id)throws Exception{
		Map<String , Object> map = new HashMap<String, Object>();
		map = queryTaskDetail(task_id);
		TaskBaseInfoDto task= wTaskBaseInfoDao.findTaskDetailByTaskId(task_id);
		List<WTaskUser> userList=(List<WTaskUser>)map.get("taskUserList");
		if(userList==null){
			userList=new ArrayList<>();
		}
		//提取工单对应的设备数量信息
		if(task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)){
			//安装、 取wTaskUser信息，判断四核数量
			task.setRemark(this.getWTaskIntallDeviceCnt(userList));
		}else if(task.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL)||
				task.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_LINE)){
			//拆除、回收终端工单 取wTaskUser信息
			task.setRemark(this.getWTaskUserDeviceCnt(userList));
		}else if(task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT)||
				task.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL)){
			//故障工单和迁移工单 取客户名下所有设备信息
			task.setRemark(this.getCustDeviceCnt(userComponent.queryUserByCustId(task.getCust_id())));
		}
		map.put("taskBaseInfo", task);	
		return map;
	}
	
	
	public Map<String , Object> queryTaskDetail(String task_id)throws Exception{
		Map<String , Object> map = new HashMap<String, Object>();
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(task_id);
		List<TaskBaseInfoDto> list = wTaskBaseInfoDao.queryTaskByTaskTypeAndCustId(task.getCust_id(), task.getTask_type_id());
		List<TaskBaseInfoDto> sameTaskList = new ArrayList<TaskBaseInfoDto>();
		for(TaskBaseInfoDto dto : list){
			if(!dto.getTask_id().equals(task.getTask_id())){
				sameTaskList.add(dto);
			}
		}
		
		List<WTaskUser> userList = snTaskComponent.queryTaskDetailUser(task_id);
		
		List<WTaskLog> logList = wTaskLogDao.queryByTaskId(task_id);
		for(WTaskLog log : logList){
			String errorstr = StringHelper.isNotEmpty(log.getError_remark())?log.getError_remark():"";
			String logstr = log.getLog_detail()==null?"":log.getLog_detail();
			log.setLog_detail(logstr+errorstr);
		}

		map.put("taskUserList", userList);
		map.put("taskLogList", logList);	
		map.put("sameTaskList", sameTaskList);
		return map;
	}
	
	private String getWTaskIntallDeviceCnt(List<WTaskUser> userList){
		int ont_cnt=0;
		int ott_cnt_4core=0;
		int ott_cnt_2core=0;
	    for(WTaskUser taskUser: userList){
	    	if(SystemConstants.USER_TYPE_BAND.equals(taskUser.getUser_type())){
	    		ont_cnt++;
	    	}
	    	if(SystemConstants.USER_TYPE_OTT.equals(taskUser.getUser_type())){
	    		if("PND_OTT_04".equals(taskUser.getDevice_model())){
	    			ott_cnt_4core++;
	    		}else{
	    			ott_cnt_2core++;
	    		}
	    	}
	    }
	    StringBuilder OrderContent=new StringBuilder();
	    if(ont_cnt+ott_cnt_4core+ott_cnt_2core>0){
	    	if(ont_cnt>0){
	    		OrderContent.append(" ONU:").append(ont_cnt);
	    	}
	    	if(ott_cnt_4core>0){
		    	OrderContent.append("  OTT(四核4core):").append(ott_cnt_4core);
	    	}
	       if(ott_cnt_2core>0){
	    	   OrderContent.append("  OTT(两核2core):").append(ott_cnt_2core);
	       }
	    }
	    return OrderContent.toString();
	}
	/**
	 * 工单信息中的设备信息
	 * @param userList
	 * @return
	 */
	private String getWTaskUserDeviceCnt(List<WTaskUser> userList){
		int ont_cnt=0;
		int ott_cnt=0;
	    for(WTaskUser taskUser: userList){
	    	if(SystemConstants.USER_TYPE_BAND.equals(taskUser.getUser_type())){
	    		ont_cnt++;
	    	}
	    	if(SystemConstants.USER_TYPE_OTT.equals(taskUser.getUser_type())){
	    		ott_cnt++;
	    	}
	    }
	    StringBuilder OrderContent=new StringBuilder();
	    if(ont_cnt+ott_cnt>0){
	    	if(ont_cnt>0){
	    		OrderContent.append(" ONU:").append(ont_cnt);
	    	}
	    	if(ott_cnt>0){
	    		OrderContent.append(" OTT:").append(ott_cnt);
	    	}
	    }
	    return OrderContent.toString();
	}
	private String getCustDeviceCnt(List<CUser> userList){
		if(userList==null||userList.size()==0)
			return "";
		int ont_cnt=0;
		int ott_cnt=0;
	    for(CUser taskUser: userList){
	    	if(SystemConstants.USER_TYPE_BAND.equals(taskUser.getUser_type())){
	    		ont_cnt++;
	    	}
	    	if(SystemConstants.USER_TYPE_OTT.equals(taskUser.getUser_type())){
	    		ott_cnt++;
	    	}
	    }
	    StringBuilder OrderContent=new StringBuilder();
	    if(ont_cnt+ott_cnt>0){
	    	if(ont_cnt>0){
	    		OrderContent.append(" ONU:").append(ont_cnt);
	    	}
	    	if(ott_cnt>0){
	    		OrderContent.append(" OTT:").append(ott_cnt);
	    	}
	    }
		return OrderContent.toString();
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
		List<TaskUserDto> userDtoList = new ArrayList<TaskUserDto>();
		List<WTaskUser> userList = snTaskComponent.queryTaskDetailUser(task_id);
		WTaskBaseInfo taskBase = wTaskBaseInfoDao.findByKey(task_id);
		for(WTaskUser task: userList){
			TaskUserDto _t = new TaskUserDto();
			BeanUtils.copyProperties(task, _t);
			//（除开新安装设备回填更换，拆机时候设备回收） Device_id为原设备，Device_code为新的设备
			if (!taskBase.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL)&&
					!taskBase.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)){
				_t.setDevice_code(_t.getDevice_id());
				_t.setDevice_id(null);
			}
			userDtoList.add(_t);
		}
		return userDtoList;
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
		snTaskComponent.saveZte(doneCode, task_id, zte_status,log_remark,getOptr().getOptr_id());
		this.setDoneCodeInfo(task_id, getBusiParam(), BusiCodeConstants.TASK_ZTE_OPEN);
		this.getBusiParam().setOperateObj("WorkOrdersSn:"+task_id);
		saveAllPublic(doneCode, getBusiParam());
	}

	@Override
	public Object queryCanEndTask(String task_id, String resultType) throws Exception {
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(task_id);
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)
				&&resultType.equals(SystemConstants.TASK_FINISH_TYPE_SUCCESS)
				&&wTaskUserDao.queryUnFillUserCountIsBand(task.getTask_id()) > 0){
			return false;
//			throw new ComponentException(ErrorCode.TaskDeviceIsNull);
		}
		return true;
	}

	

}

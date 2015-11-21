package com.ycsoft.business.component.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RModemModel;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.beans.task.WTeam;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dao.config.TConfigTemplateDao;
import com.ycsoft.business.dao.core.cust.CCustLinkmanDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.core.user.CUserPropChangeDao;
import com.ycsoft.business.dao.resource.device.RDeviceDao;
import com.ycsoft.business.dao.resource.device.RModemModelDao;
import com.ycsoft.business.dao.resource.device.RStbModelDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.business.dao.task.WTeamDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;

/**
 * supernet 工单
 * 
 * @author yuben
 *
 */
@Component
public class SnTaskComponent extends BaseBusiComponent {
	@Autowired
	private WTaskBaseInfoDao wTaskBaseInfoDao;
	@Autowired
	private CCustLinkmanDao CCustLinkmanDao;
	@Autowired
	private WTaskUserDao wTaskUserDao;
	@Autowired
	private WTeamDao wTeamDao;
	@Autowired
	private WTaskLogDao wTaskLogDao;
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private RDeviceDao rDeviceDao;
	@Autowired
	private TConfigTemplateDao tConfigTemplateDao;

	private CUserPropChangeDao cUserPropChangeDao;
	@Autowired
	private RModemModelDao rModemModelDao;
	@Autowired
	private RStbModelDao rStbModelDao;
	// 创建开户工单
	public void createOpenTask(Integer doneCode, CCust cust, List<CUser> userList, String assignType) throws Exception {
		this.createTaskWithUser(doneCode, cust, userList, SystemConstants.TASK_TYPE_INSTALL, assignType);
	}
	/**
	 * 保存创建工单的业务扩展信息
	 * @param custId
	 * @param doneCode
	 * @param BusiParameter
	 * @throws Exception
	 */
	public void saveTaskCreateBusiExt(String custId,Integer doneCode,BusiParameter BusiParameter) throws Exception{
		List<WTaskBaseInfo> list=wTaskBaseInfoDao.queryTaskByDoneCode(doneCode);
		if(list.size()>0){
			String taskIds="";
			for(WTaskBaseInfo w:list){
				if(w.getCust_id().equals(custId)){
					taskIds+=" "+w.getTask_id();
				}
			}
			if(StringHelper.isNotEmpty(taskIds)){
				BusiParameter.setOperateObj("WorkOrdersSn:"+taskIds);
			}
		}
	}

	/**
	 *  创建拆机工单（最多有两种工单）
	 * @param doneCode
	 * @param cust
	 * @param userList
	 * @param assignType
	 * @throws Exception
	 */
	public void createWriteOffTask(Integer doneCode, CCust cust, List<CUser> userList, String assignType)
			throws Exception {
		// 去除OTT_MOBILE
		filterUserList(userList, SystemConstants.USER_TYPE_OTT_MOBILE);
		if (userList.size() == 0) {
			return;// no user
		}
		// 创建终端回收单
		String terminalTaskId=createSingleTaskWithUser(doneCode, cust, userList, getTeamId(SystemConstants.TEAM_TYPE_SUPERNET),
				SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL);
		//记录终端回收工单创建日志
		createTaskLog(terminalTaskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NONE);
		// 去除DTT
		filterUserList(userList, SystemConstants.USER_TYPE_DTT);
		if (userList.size() == 0) {
			return;// no user
		}
		// 创建拆线路单
		List<CUser> bandList = getUserByTyoe(userList, SystemConstants.USER_TYPE_BAND);
		if (bandList.size() == 0) {
			return;// no user
		}
		String teamType = SystemConstants.TEAM_TYPE_SUPERNET;
		String synStatus=StatusConstants.NONE;
		if (SystemConstants.TASK_ASSIGN_CFOCN.equals(assignType)){
			teamType = SystemConstants.TEAM_TYPE_CFOCN;
			synStatus=StatusConstants.NOT_EXEC;
		}
		String teamId = getTeamId(teamType);
		String taskId = this.saveTaskBaseInfo(cust, doneCode, SystemConstants.TASK_TYPE_WRITEOFF_LINE, teamId, null,
				null);
		this.saveTaskUser(bandList, SystemConstants.TASK_TYPE_WRITEOFF_LINE, taskId,doneCode);
		createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, synStatus);
	}

	/**
	 *  创建故障单
	 * @param doneCode
	 * @param cust
	 * @param bugDetail
	 * @throws Exception
	 */
	public String createBugTask(Integer doneCode, CCust cust, String bugDetail, String bugPhone) throws Exception {
		String taskId = this.saveTaskBaseInfo(cust, doneCode, SystemConstants.TASK_TYPE_FAULT,
				getTeamId(SystemConstants.TEAM_TYPE_SUPERNET), null, bugDetail, bugPhone);
		List<CUser> userList = cUserDao.queryUserByCustId(cust.getCust_id());
		List<CUser> bandList = getUserByTyoe(userList, SystemConstants.USER_TYPE_BAND);
		saveTaskUser(bandList, SystemConstants.TASK_TYPE_FAULT, taskId,doneCode);
		//记录日志
		createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NONE);
		return taskId;
	}

	/**
	 *  生成移机工单
	 * @param doneCode
	 * @param cust
	 * @param newAddrId
	 * @param newAddr
	 * @param assignType
	 * @throws Exception
	 */
	public void createMoveTask(Integer doneCode, CCust cust, String newAddrId, String newAddr, String assignType)
			throws Exception {
		String teamType = SystemConstants.TEAM_TYPE_SUPERNET;
		String synStatus=StatusConstants.NONE;
		if (SystemConstants.TASK_ASSIGN_CFOCN.equals(assignType)){
			teamType = SystemConstants.TEAM_TYPE_CFOCN;
			synStatus=StatusConstants.NOT_EXEC;
		}
		String taskId = this.saveTaskBaseInfo(cust, doneCode, SystemConstants.TASK_TYPE_MOVE, getTeamId(teamType),
				newAddr, null);
		List<CUser> userList = cUserDao.queryUserByCustId(cust.getCust_id());
		//取宽带是为了光路信息变化回填用的
		List<CUser> bandList = getUserByTyoe(userList, SystemConstants.USER_TYPE_BAND);
		saveTaskUser(bandList, SystemConstants.TASK_TYPE_MOVE, taskId,doneCode);
		//记录日志
		createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null,synStatus);
	}

	/**
	 * 派单
	 * 1.待派单的工单可以操作派单。 指定给cfocn的待派单工单如果存在未执行的同步日志，则要作废同步日志。
	 * 2.施工中的cfocn工单，不能派单
	 * 3.施工中的工程部工单，可以重新派单给cfocn
	 * @param doneCode
	 * @param taskId
	 * @param deptId
	 * @param buyType
	 * @throws Exception
	 */
	public void changeTaskTeam(Integer doneCode, String taskId, String deptId, String optrId, String buyType,String finishRemark) throws Exception {
		//需要查询锁
		// 修改工单对应的施工队
		WTaskBaseInfo task = wTaskBaseInfoDao.queryForLock(taskId);
		if (task == null) {
			throw new ComponentException("工单不存在");
		}
		String cfonTeamId = getTeamId(SystemConstants.TEAM_TYPE_CFOCN);
		if(cfonTeamId.equals(task.getTeam_id())
				&&!task.getTask_status().equals(StatusConstants.TASK_CREATE)
				&&!task.getTask_status().equals(StatusConstants.TASK_ENDWAIT)){
			throw new ComponentException("只有待派单或完工等待的cfocn工单才能重新派单");
		}
		if(!cfonTeamId.equals(task.getTeam_id())
				&&!task.getTask_status().equals(StatusConstants.TASK_CREATE)
				&&!task.getTask_status().equals(StatusConstants.TASK_INIT)
				&&!task.getTask_status().equals(StatusConstants.TASK_ENDWAIT)){
			throw new ComponentException("只有待派单或施工中或完工等待的supernet工单才能重新派单");
		}
		
		String oldTeamId = task.getTeam_id();
		task.setBug_type(buyType);
		task.setTeam_id(deptId);
		task.setInstaller_id(optrId);
		task.setTask_finish_desc(finishRemark);
		wTaskBaseInfoDao.update(task);
		// 记录操作日志
		//String cfonTeamId = getTeamId(SystemConstants.TEAM_TYPE_CFOCN);
		//查询未执行的工单日志，更新为NONE
		if(oldTeamId.equals(cfonTeamId)&&wTaskLogDao.queryUnSynLogByTaskId(taskId).size()>0){
			wTaskLogDao.updateUnSynLogToNone(taskId, "重新派单取消执行");
		}

		if (StringHelper.isNotEmpty(cfonTeamId) && cfonTeamId.equals(deptId)) {
			JsonObject jo = new JsonObject();
			jo.addProperty("Team", SystemConstants.TEAM_TYPE_CFOCN);
			createTaskLog(taskId, BusiCodeConstants.TASK_ASSIGN, doneCode, jo.toString(), StatusConstants.NOT_EXEC);
			wTaskBaseInfoDao.updateTaskStatus(taskId, StatusConstants.TASK_CREATE);
			//派给cfocn，同步状态设置为未执行
			wTaskBaseInfoDao.updateTaskSyncStatus(taskId, StatusConstants.NOT_EXEC);
		} else {
			JsonObject jo = new JsonObject();
			jo.addProperty("Team", SystemConstants.TEAM_TYPE_SUPERNET);
			createTaskLog(taskId, BusiCodeConstants.TASK_ASSIGN, doneCode,jo.toString(), StatusConstants.NONE);
			wTaskBaseInfoDao.updateTaskStatus(taskId, StatusConstants.TASK_INIT);
			//派给supernet取消同步状态
			wTaskBaseInfoDao.updateTaskSyncStatus(taskId, null);
		}
	}
	
	/**
	 * 撤回
	 * 施工中的cfocn的工单可以操作撤回。
	 * @param doneCode
	 * @param taskId
	 * @throws Exception 
	 */
	public void  withdrawTask(Integer doneCode,String taskId) throws Exception{
		
		WTaskBaseInfo oldTask = wTaskBaseInfoDao.queryForLock(taskId);
		if (oldTask == null) {
			throw new ComponentException("工单不存在");
		}
		if (oldTask.getTask_status().equals(StatusConstants.TASK_END)) {
			throw new ComponentException("工单已经完工");
		}
		// 施工是cfocn,状态是施工中
		if(!getTeamId(SystemConstants.TEAM_TYPE_CFOCN).equals(oldTask.getTeam_id())||
				!oldTask.getTask_status().equals(StatusConstants.TASK_INIT)){
			throw new ComponentException("不是施工中cfocn工单");		
		}
		//插入工单撤回日志
		createTaskLog(taskId, BusiCodeConstants.TASK_Withdraw, doneCode, null, StatusConstants.NOT_EXEC);
	}

	/**
	 * a.工单管理的作废工单功能
	 *  1.未派单的工单，可以作废
	 *  2.施工中的派给cfocn的工单，不能作废
	 *  3.施工中的派给工程部的工单，可以作废
	 * b.单据面板的作废工单按钮（这个放service中验证）
	 *  未派单的工单可以作废
	 * @param doneCode
	 * @param taskId
	 * @throws Exception
	 */
	public void cancelTask(Integer doneCode, String taskId) throws Exception {
		//加查询锁  加验证
		WTaskBaseInfo oldTask =  wTaskBaseInfoDao.queryForLock(taskId);
		if (oldTask == null) {
			throw new ComponentException("工单不存在");
		}
		String cfonTeamId = getTeamId(SystemConstants.TEAM_TYPE_CFOCN);
		if(cfonTeamId.equals(oldTask.getTeam_id())&&!oldTask.getTask_status().equals(StatusConstants.TASK_CREATE)){
			throw new ComponentException("只有待派单的cfocn工单才能作废");
		}
		if(!cfonTeamId.equals(oldTask.getTeam_id())
				&&!oldTask.getTask_status().equals(StatusConstants.TASK_CREATE)
				&&!oldTask.getTask_status().equals(StatusConstants.TASK_INIT)){
			throw new ComponentException("只有待派单和施工中的supernet工单才能作废");
		}
		
		// 作废销终端工单，用户状态还原
		if (oldTask.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL)) {
			List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(oldTask.getCust_id());
			List<WTaskUser> userList = wTaskUserDao.queryByTaskId(taskId);
			for (WTaskUser user : userList) {
				CUser cuser = cUserDao.findByKey(user.getUser_id());

				List<CUserPropChange> userChangeList = new ArrayList<CUserPropChange>();
				userChangeList.add(new CUserPropChange("status", cuser.getStatus(), StatusConstants.ACTIVE));
				userChangeList.add(new CUserPropChange("status_date", DateHelper.dateToStr(cuser.getStatus_date()),
						DateHelper.dateToStr(new Date())));

				this.editUser(doneCode, cuser.getUser_id(), userChangeList);

				for (CProdOrderDto order : orderList) {
					if (StringHelper.isNotEmpty(order.getUser_id()) && order.getUser_id().equals(cuser.getUser_id())) {
						List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
						changeList.add(new CProdPropChange("status", order.getStatus(), StatusConstants.ACTIVE));
						changeList.add(new CProdPropChange("status_date", DateHelper.dateToStr(order.getStatus_date()),
								DateHelper.dateToStr(new Date())));

						this.editProd(doneCode, order.getOrder_sn(), changeList);
					}
				}
			}
		}

		WTaskBaseInfo task = new WTaskBaseInfo();
		task.setTask_id(taskId);
		task.setTask_status(StatusConstants.CANCEL);
		task.setTask_invalide_time(new Date());
		task.setTask_status_date(new Date());
		wTaskBaseInfoDao.update(task);
		//task = wTaskBaseInfoDao.findByKey(taskId);
		wTaskLogDao.updateUnSynLogToNone(taskId, "作废工单取消执行");
		
		createTaskLog(taskId, BusiCodeConstants.TASK_CANCEL, doneCode, null, StatusConstants.NONE);
	}

	// 保存开户、移机、故障单的回填信息
	public void fillTaskInfo(Integer doneCode, WTaskBaseInfo task, List<WTaskUser> userList,
			List<TaskFillDevice> deviceList) throws Exception {
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)) {
			// 更新工单用户对应的设备信息
			for (TaskFillDevice fillDevice : deviceList) {
				wTaskUserDao.updateTaskUserDevice(fillDevice.getDeviceCode(), fillDevice.getUserId(),task.getTask_id());
				if (fillDevice.isFcPort()) {
					updateBandFc(fillDevice, task);
				}
			}
		} else {
			for (TaskFillDevice fillDevice : deviceList) {
				updateBandFc(fillDevice, task);
			}
		}
		// 记录工单操作日志
		createTaskLog(task.getTask_id(), BusiCodeConstants.TASK_FILL, doneCode, null, StatusConstants.NONE);
	}

	// 更新宽带用的光路信息
	private void updateBandFc(TaskFillDevice fillDevice, WTaskBaseInfo task) throws Exception {
		if (StringHelper.isNotEmpty(fillDevice.getOccNo())) {
			CUser user = new CUser();
			user.setUser_id(fillDevice.getUserId());
			user.setStr7(fillDevice.getOccNo());
			user.setStr8(fillDevice.getPosNo());

			cUserDao.update(user);
			// 更新工单修改中兴配置的状态
			task.setZte_status(StatusConstants.NOT_EXEC);
			task.setZte_status_date(new Date());
			wTaskBaseInfoDao.update(task);
			
		}
	}

	// 回填工单
	public List<WTaskUser> fillOpenTaskInfo(Integer doneCode, String taskId, String otlNo, String ponNo,
			List<TaskFillDevice> deviceList) throws Exception {
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		if (task.getTask_status().equals(StatusConstants.CANCEL)) {
			throw new ComponentException("工单已经被取消，不能回填");
		}

		task.setTask_id(gTaskId());
		List<WTaskUser> userList = wTaskUserDao.queryByTaskId(taskId);
		if (StringHelper.isNotEmpty(otlNo)) {
			// 判断工单是否有对应的宽带用户
			CUser band = null;
			for (WTaskUser tu : userList) {
				if (tu.getUser_type().equals(SystemConstants.USER_TYPE_BAND)) {
					band = cUserDao.findByKey(tu.getUser_id());
				}
			}

			if (band != null) {
				// 更新用户信息
				band.setStr7(otlNo);
				band.setStr8(ponNo);

				cUserDao.update(band);
				// 更新工单修改中兴配置的状态
				task.setZte_status(StatusConstants.NOT_EXEC);
				task.setZte_status_date(new Date());
				wTaskBaseInfoDao.update(task);

				// 记录操作日志
				JsonObject jo = new JsonObject();
				jo.addProperty("Zte_status", StatusConstants.NOT_EXEC);
				jo.addProperty("str7", otlNo);
				jo.addProperty("str8", ponNo);
				createTaskLog(taskId, BusiCodeConstants.TASK_FILL, doneCode, jo.toString(), StatusConstants.NONE);

			}
		}
		for (TaskFillDevice device : deviceList) {
			updateUserDevice(device, userList, doneCode, taskId);
		}

		return userList;
	}

	public void fillWriteOffTerminalTask(int doneCode, String taskId, String[] userIds, String recycle_result) throws Exception {
		if(userIds.length>0){
			wTaskUserDao.updateRecycle(taskId, userIds,recycle_result);
			// 记录操作日志
			JsonObject jo = new JsonObject();
			jo.addProperty("user_ids", StringHelper.join(userIds, ","));
			jo.addProperty("recycle_result", recycle_result);
			createTaskLog(taskId, BusiCodeConstants.TASK_FILL, doneCode, jo.toString(), StatusConstants.NONE);
		}
	}

	// 完工
	public void finishTask(Integer doneCode, WTaskBaseInfo wtask, String resultType, String bugType, String custSignNo, String finishDesc) throws Exception {
		// 安装工单且完工成功要检查设备是否已经回填
		if (wtask.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)
				&&resultType.equals(SystemConstants.TASK_FINISH_TYPE_SUCCESS)
				&&wTaskUserDao.queryUnFillUserCount(wtask.getTask_id()) > 0){
			throw new ComponentException(ErrorCode.TaskDeviceIsNull);
		}
		// 记录操作日志
		JsonObject jo = new JsonObject();
				
		WTaskBaseInfo task = new WTaskBaseInfo();
		task.setTask_id(wtask.getTask_id());
		task.setTask_status(StatusConstants.TASK_END);
		task.setTask_finish_type(resultType);
		task.setTask_finish_desc(finishDesc);
		task.setTask_finish_time(new Date());
		task.setTask_status_date(new Date());
		task.setFinish_done_code(doneCode);
		task.setCust_sign_no(custSignNo);
		
		if(StringHelper.isNotEmpty(bugType)){
			task.setBug_type(bugType);
			jo.addProperty("bugType", bugType);
		}
		
		wTaskBaseInfoDao.update(task);
		
		jo.addProperty("resultType", resultType);
		jo.addProperty("finishDesc", finishDesc);
		createTaskLog(wtask.getTask_id(), BusiCodeConstants.TASK_FINISH, doneCode, jo.toString(), StatusConstants.NONE);

	}
	
	public void saveZte(Integer doneCode, String task_id, String zte_status, String log_remark,String zte_optr_id) throws Exception {
		WTaskBaseInfo task = new WTaskBaseInfo();
		task.setTask_id(task_id);
		task.setZte_status(zte_status);
		task.setZte_status_date(new Date());
		task.setZte_optr_id(zte_optr_id);
		wTaskBaseInfoDao.update(task);

		// 记录操作日志
		JsonObject jo = new JsonObject();
		jo.addProperty("zte_status", zte_status);
		jo.addProperty("zte_remark", log_remark);
		createTaskLog(task_id, BusiCodeConstants.TASK_ZTE_OPEN, doneCode, jo.toString(), StatusConstants.NONE);
	}

	// 删除ott_mobile和dtt用户 ,dtt用户还是需要生成销终端工单，否则销户的时候还得判断设备回收
	private void filterUserList(List<CUser> userList, String userType) {
		for (Iterator<CUser> it = userList.iterator(); it.hasNext();) {
			CUser user = it.next();
			if (user.getUser_type().equals(userType)) {
				it.remove();
			}
		}
	}

	private List<CUser> getUserByTyoe(List<CUser> userList, String userType) {
		List<CUser> ul = new ArrayList<CUser>();
		for (CUser user : userList) {
			if (user.getUser_type().equals(userType)) {
				ul.add(user);
			}
		}
		return ul;
	}

	// 创建需要记录用户信息的工单
	private void createTaskWithUser(Integer doneCode, CCust cust, List<CUser> userList, String taskType,
			String assignType) throws Exception {
		if (StringHelper.isEmpty(assignType))
			return;
		// 剔除ott_mobile用户
		List<CUser> bandList = new ArrayList<CUser>();
		List<CUser> tvUserList = new ArrayList<CUser>();
		for (Iterator<CUser> it = userList.iterator(); it.hasNext();) {
			CUser user = it.next();
			if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)) {
				it.remove();
			} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)) {
				bandList.add(user);
			} else {
				tvUserList.add(user);
			}
		}
		if (assignType.equals(SystemConstants.TASK_ASSIGN_BOTH)){
			String taskId = createSingleTaskWithUser(doneCode, cust, tvUserList, 
					getTeamId(SystemConstants.TEAM_TYPE_SUPERNET), taskType);			
				createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NONE);
			//一个宽带用户一个工单
			int i = 1;
			for (CUser user:bandList){
				List<CUser> l = new ArrayList<CUser>();
				l.add(user);
				// 把所有ott用户和第一个宽带用户归为第一个工单
				if (i == 1){
					l.addAll(tvUserList);
				}
				i++;
				taskId = createSingleTaskWithUser(doneCode, cust, l, 
						getTeamId(SystemConstants.TEAM_TYPE_CFOCN), taskType);
				JsonObject jo = new JsonObject();
				jo.addProperty("Team", SystemConstants.TEAM_TYPE_CFOCN);
				createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, jo.toString(), StatusConstants.NOT_EXEC);
			}
		} else {
			String teamType = SystemConstants.TEAM_TYPE_SUPERNET;
			if (assignType.equals(SystemConstants.TASK_ASSIGN_CFOCN))
				teamType = SystemConstants.TEAM_TYPE_CFOCN;
			if (bandList.size() <= 1) {
				String taskId = createSingleTaskWithUser(doneCode, cust, userList, getTeamId(teamType), taskType);
				if (teamType.equals(SystemConstants.TEAM_TYPE_CFOCN)){
					JsonObject jo = new JsonObject();
					jo.addProperty("Team", SystemConstants.TEAM_TYPE_CFOCN);
					this.createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, jo.toString(), StatusConstants.NOT_EXEC);
				}else
					this.createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NONE);
			} else {
				int i = 1;
				for (CUser user : bandList) {
					List<CUser> l = new ArrayList<CUser>();
					l.add(user);
					// 把所有ott用户和第一个宽带用户归为第一个工单
					if (i == 1){
						l.addAll(tvUserList);
					}
					i++;
					String taskId = createSingleTaskWithUser(doneCode, cust, l,getTeamId(teamType), taskType);
					if (teamType.equals(SystemConstants.TEAM_TYPE_CFOCN)){
						JsonObject jo = new JsonObject();
						jo.addProperty("Team", SystemConstants.TEAM_TYPE_CFOCN);
						createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, jo.toString(), StatusConstants.NOT_EXEC);
					}else{
						createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NONE);
					}
				}
			}
		}

	}

	private String createSingleTaskWithUser(Integer doneCode, CCust cust, List<CUser> userList, String deptId,
			String taskType) throws Exception {
		if (userList == null || userList.size() == 0)
			return null;
		String taskId = this.saveTaskBaseInfo(cust, doneCode, taskType, deptId, null, null);
		saveTaskUser(userList, taskType, taskId,doneCode);
		return taskId;
	}

	// 保存工单用户
	private void saveTaskUser(List<CUser> userList, String taskType, String taskId,Integer doneCode) throws Exception {
		for (CUser user : userList) {
			WTaskUser taskUser = new WTaskUser();
			taskUser.setTask_id(taskId);
			taskUser.setUser_id(user.getUser_id());
			taskUser.setDevice_model(getUserDeviceModel(user,doneCode));
			taskUser.setUser_type(user.getUser_type());
			taskUser.setDevice_id(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND) ? user.getModem_mac()
					: user.getStb_id());
			// 如果是销终端工单，则需要指定哪些设备需要回收
			if (taskType.equals(SystemConstants.TASK_TYPE_WRITEOFF_TERMINAL)) {
				// 如果产品是广电的设备，需要回收
				RDevice device = rDeviceDao.findByDeviceCode(taskUser.getDevice_id());
				if (device.getOwnership().equals(SystemConstants.OWNERSHIP_GD)) {
					taskUser.setRecycle_device(SystemConstants.BOOLEAN_TRUE);
				} else {
					taskUser.setRecycle_device(SystemConstants.BOOLEAN_FALSE);
//					taskUser.setRecycle_result(SystemConstants.BOOLEAN_FALSE);
				}
			}else if((taskType.equals(SystemConstants.TASK_TYPE_MOVE) || taskType.equals(SystemConstants.TASK_TYPE_FAULT))
					&& user.getUser_type().equals(SystemConstants.USER_TYPE_BAND) && StringHelper.isEmpty(taskUser.getDevice_id())){
				//移机故障单，宽带用户，没有设备的情况下，虚拟一个设备编号，virtual_userId
				taskUser.setDevice_id("virtual_"+user.getUser_id());
			}
			wTaskUserDao.save(taskUser);
		}
	}
	
	
	private String getUserDeviceModel(CUser user,Integer doneCode)throws Exception{
		if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
			if(StringHelper.isNotEmpty(user.getModem_mac())){
				RModemModel modemModel = rModemModelDao.queryByModemMac(user.getModem_mac());
				if(modemModel != null){
					user.setDevice_model(modemModel.getDevice_model());
				}
			}
		}else{
			if(StringHelper.isNotEmpty(user.getStb_id())){
				RStbModel stbModel = rStbModelDao.queryByStbId(user.getStb_id());
				if(stbModel != null){
					user.setDevice_model(stbModel.getDevice_model());
				}
			}
		}
		if(!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			//如果str3原先没有的，根据设备编号找到设备型号，修改cuser
			if(StringHelper.isNotEmpty(user.getDevice_model()) && StringHelper.isEmpty(user.getStr3())){
				CUser cuser = cUserDao.findByKey(user.getUser_id());

				List<CUserPropChange> userChangeList = new ArrayList<CUserPropChange>();
				userChangeList.add(new CUserPropChange("str3", cuser.getStr3(), user.getDevice_model()));
				this.editUser(doneCode, cuser.getUser_id(), userChangeList);
			}
			if(StringHelper.isEmpty(user.getDevice_model()) && StringHelper.isNotEmpty(user.getStr3())){
				user.setDevice_model(user.getStr3());
			}
		}
		return user.getDevice_model();
	}

	private void updateUserDevice(TaskFillDevice fillDevice, List<WTaskUser> userList, Integer doneCode, String taskId)
			throws Exception {
		WTaskUser user = null;
		if (StringHelper.isNotEmpty(fillDevice.getOldDeviceCode())) {
			for (WTaskUser tu : userList) {
				if (fillDevice.getOldDeviceCode().equals(tu.getDevice_id())) {
					user = tu;
					tu.setDevice_id(fillDevice.getDeviceCode());
					break;
				}
			}
		} else {
			for (WTaskUser tu : userList) {
				if (StringHelper.isEmpty(tu.getDevice_id())) {
					if (tu.getDevice_model().equals(fillDevice.getDevice().getDeviceModel())) {
						user = tu;
						tu.setDevice_id(fillDevice.getDeviceCode());
						break;
					}
				}
			}
		}

		if (user != null) {
			user.setDevice_id(fillDevice.getDeviceCode());
			// 更新设备号
			wTaskUserDao.updateTaskUserDevice(user.getDevice_id(), user.getUser_id(), user.getTask_id());
			// 记录操作日志
			JsonObject jo = new JsonObject();
			jo.addProperty("device_id", fillDevice.getDeviceCode() + "->" + user.getDevice_id());
			createTaskLog(taskId, BusiCodeConstants.TASK_FILL, doneCode, jo.toString(), StatusConstants.NONE);
		}
	}
	
	private WTaskBaseInfo setTaskBaseInfo(CCust cust, Integer doneCode, String taskType, String teamId, String newAddr,
			String bugDetail, String bugPhone) throws Exception {
		CCustLinkman linkman = CCustLinkmanDao.findByKey(cust.getCust_id());

		WTaskBaseInfo task = new WTaskBaseInfo();
		task.setTask_id(gTaskId());
		task.setTask_type_id(taskType);
		task.setCust_id(cust.getCust_id());
		task.setDone_code(doneCode);
		task.setCust_name(cust.getCust_name());
		task.setTeam_id(teamId);
		// 设置工单的施工地址
		if (StringHelper.isNotEmpty(newAddr)) {
			task.setNew_addr(newAddr);
			task.setOld_addr(cust.getAddress());
		} else {
			task.setNew_addr(cust.getAddress());
		}
		task.setMobile(linkman.getMobile());
		task.setTel(linkman.getTel());
		// 设置地区县市、操作员信息
		task.setCounty_id(cust.getCounty_id());
		task.setArea_id(cust.getArea_id());
		task.setOptr_id(getOptr().getOptr_id());
		task.setBug_detail(bugDetail);
		task.setBug_phone(bugPhone);
		return task;
	}

	// 保存工单基本信息
	private String saveTaskBaseInfo(CCust cust, Integer doneCode, String taskType, String teamId, String newAddr,
			String bugDetail) throws Exception {
		WTaskBaseInfo task = this.setTaskBaseInfo(cust, doneCode, taskType, teamId, newAddr, bugDetail, "");
		wTaskBaseInfoDao.save(task);
		return task.getTask_id();
	}
	
	private String saveTaskBaseInfo(CCust cust, Integer doneCode, String taskType, String teamId, String newAddr,
			String bugDetail, String bugPhone) throws Exception {
		WTaskBaseInfo task = this.setTaskBaseInfo(cust, doneCode, taskType, teamId, newAddr, bugDetail, bugPhone);
		wTaskBaseInfoDao.save(task);
		return task.getTask_id();
	}

	private String gTaskId() throws Exception {
		return wTaskBaseInfoDao.findSequence(SequenceConstants.SEQ_TASK).toString();
	}

	public String getTeamId(String teamType) throws Exception {
		// 获取施工队信息
		List<WTeam> teamList = wTeamDao.findAll();
		for (WTeam team : teamList) {
			if (team.getTeam_type().equals(teamType))
				return team.getDept_id();
		}
		return null;
	}

	public void createTaskLog(String taskId, String busiCode, Integer doneCode, String logDetail, String synStatus)
			throws Exception {
		if (StringHelper.isNotEmpty(taskId)) {
			WTaskLog log = new WTaskLog();
			log.setSyn_status(synStatus);
			if(busiCode.equals(BusiCodeConstants.TASK_INIT) && StatusConstants.NOT_EXEC.equals(synStatus)){
				log.setSyn_status(StatusConstants.NONE);
			}
			
			log.setLog_sn(Integer.parseInt(wTaskLogDao.findSequence().toString()));
			log.setTask_id(taskId);
			log.setBusi_code(busiCode);
			log.setDone_code(doneCode);
			log.setOptr_id(getOptr().getOptr_id());
			log.setLog_time(new Date());
			log.setLog_detail(logDetail);
			wTaskLogDao.save(log);
		}
		if (StringHelper.isNotEmpty(taskId)
			&&busiCode.equals(BusiCodeConstants.TASK_INIT) 
			&& StatusConstants.NOT_EXEC.equals(synStatus)){
			WTaskLog log = new WTaskLog();
			//获取延迟时间
			TConfigTemplate ct = tConfigTemplateDao.queryConfigByConfigName(
					TemplateConfigDto.Config.DELAY_TASK_TIME.toString(), getOptr().getCounty_id());	//当前值
			if(ct != null){
				String delayTime = ct.getConfig_value();
				if(StringHelper.isNotEmpty(delayTime)){
					log.setDelay_time(Integer.parseInt(delayTime));
				}
			}
			log.setLog_sn(Integer.parseInt(wTaskLogDao.findSequence().toString()));
			log.setTask_id(taskId);
			log.setBusi_code(BusiCodeConstants.TASK_ASSIGN);
			log.setDone_code(doneCode);
			log.setOptr_id(getOptr().getOptr_id());
			log.setSyn_status(synStatus);
			log.setLog_time(new Date());
			log.setLog_detail(logDetail);
			wTaskLogDao.save(log);
		}
	}
	
	public void editCustSignNo(Integer doneCode, WTaskBaseInfo task, String newCustSignNo) throws Exception {
		if(!newCustSignNo.equals(task.getCust_sign_no())){
			WTaskBaseInfo wTask = new WTaskBaseInfo();
			wTask.setTask_id(task.getTask_id());
			wTask.setCust_sign_no(newCustSignNo);
			wTaskBaseInfoDao.update(wTask);
			
			JsonObject jo = new JsonObject();
			jo.addProperty("custSignNo", newCustSignNo);
			createTaskLog(task.getTask_id(), BusiCodeConstants.TASK_MODIFY_CUSTSIGNNO, doneCode, jo.toString(), StatusConstants.NONE);
		}
	}

	/**
	 * 更改工单状态
	 * @param taskId
	 * @param status
	 * @throws Exception
	 */
	public void updateTaskStatus(String taskId,String status) throws Exception{
		wTaskBaseInfoDao.updateTaskStatus(taskId, status);
	}
	
	public List<WTaskUser> queryTaskDetailUser(String taskId)  throws Exception{
		List<WTaskUser> list= queryTaskUser(taskId);
		//提取BNAD用户状态和产品状态，产品截止日期
		for(WTaskUser wu:list){
			if(SystemConstants.USER_TYPE_BAND.equals(wu.getUser_type())){
				//提取产品
				List<CProdOrder> orders=cProdOrderDao.queryOrderProdByUserId(wu.getUser_id());
				if(orders.size()>0){
					CProdOrder lastorder=orders.get(orders.size()-1);
					wu.setExp_date(lastorder.getExp_date());
					if(StatusConstants.ACTIVE.equals(wu.getStatus())
							&&lastorder.getStatus().equals(StatusConstants.FORSTOP)){
						//当用户状态正常 产品到期停时，装入产品的状态和状态时间
						wu.setStatus(lastorder.getStatus());
						wu.setStatus_date(lastorder.getStatus_date());
					}
				}
			}
		}
		return list;
	}
	
	public void updateTask(WTaskBaseInfo task) throws Exception {
		wTaskBaseInfoDao.update(task);
	}
	public WTaskBaseInfoDao getwTaskBaseInfoDao() {
		return wTaskBaseInfoDao;
	}

	public void setwTaskBaseInfoDao(WTaskBaseInfoDao wTaskBaseInfoDao) {
		this.wTaskBaseInfoDao = wTaskBaseInfoDao;
	}

	public CCustLinkmanDao getCCustLinkmanDao() {
		return CCustLinkmanDao;
	}

	public void setCCustLinkmanDao(CCustLinkmanDao cCustLinkmanDao) {
		CCustLinkmanDao = cCustLinkmanDao;
	}

	public WTaskUserDao getwTaskUserDao() {
		return wTaskUserDao;
	}

	public void setwTaskUserDao(WTaskUserDao wTaskUserDao) {
		this.wTaskUserDao = wTaskUserDao;
	}

	public WTeamDao getwTeamDao() {
		return wTeamDao;
	}

	public void setwTeamDao(WTeamDao wTeamDao) {
		this.wTeamDao = wTeamDao;
	}

	public WTaskLogDao getwTaskLogDao() {
		return wTaskLogDao;
	}

	public void setwTaskLogDao(WTaskLogDao wTaskLogDao) {
		this.wTaskLogDao = wTaskLogDao;
	}


}
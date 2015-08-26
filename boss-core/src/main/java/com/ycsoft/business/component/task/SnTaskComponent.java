package com.ycsoft.business.component.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.beans.task.WTeam;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.cust.CCustLinkmanDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.business.dao.task.WTeamDao;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;

/**
 * supernet 工单
 * @author yuben
 *
 */
@Component
public class SnTaskComponent extends BaseBusiComponent{
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
	
	//创建开户工单
	public void createOpenTask(int doneCode,CCust cust,List<CUser> userList,String assignType) throws Exception{
		this.createTaskWithUser(doneCode, cust, userList, SystemConstants.TASK_TYPE_INSTALL, assignType);
	}
	
	//创建销户工单
	public void createWriteOffTask(int doneCode,CCust cust,List<CUser> userList,String assignType) throws Exception{
		this.createTaskWithUser(doneCode, cust, userList, SystemConstants.TASK_TYPE_WRITEOFF, assignType);
	}
	
	
	//创建需要记录用户信息的工单
	private void createTaskWithUser(int doneCode,CCust cust,List<CUser> userList,String taskType,String assignType) throws Exception{
		if (StringHelper.isEmpty(assignType))
			return;
		//剔除ott_mobile用户
		List<CUser> bandList = new ArrayList<CUser>();
		List<CUser> tvUserList = new ArrayList<CUser>();
		for (Iterator<CUser> it = userList.iterator();it.hasNext();){
			CUser user = it.next();
			if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
				it.remove();
			} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				bandList.add(user);
			} else {
				tvUserList.add(user);
			}
		}
		
		if (assignType.equals(SystemConstants.TASK_ASSIGN_BOTH)){
			String taskId = createSingleTaskWithUser(doneCode, cust, tvUserList, 
					getTeamId(SystemConstants.TEAM_TYPE_SUPERNET), taskType);
			createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NOT_EXEC);
			
			taskId = createSingleTaskWithUser(doneCode, cust, bandList, 
					getTeamId(SystemConstants.TEAM_TYPE_CFOCN), taskType);
			createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NONE);
		} else {
			String teamType = SystemConstants.TEAM_TYPE_SUPERNET;
			if(assignType.equals(SystemConstants.TASK_ASSIGN_CFOCN))
				teamType = SystemConstants.TEAM_TYPE_CFOCN;
			String taskId = createSingleTaskWithUser(doneCode, cust, userList, getTeamId(teamType),taskType);
			if (teamType.equals(SystemConstants.TEAM_TYPE_CFOCN))
				this.createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NOT_EXEC);
			else 
				this.createTaskLog(taskId, BusiCodeConstants.TASK_INIT, doneCode, null, StatusConstants.NONE);
		}
		
	}
	
	private String createSingleTaskWithUser(int doneCode,CCust cust,List<CUser> userList,String deptId,String taskType) throws Exception{
		String taskId = this.saveTaskBaseInfo(cust, doneCode,taskType, deptId, null);
		for (CUser user:userList){
			WTaskUser taskUser = new WTaskUser();
			taskUser.setTask_id(taskId);
			taskUser.setUser_id(user.getUser_id());
			taskUser.setDivice_model(user.getDevice_model());
			taskUser.setUser_type(user.getUser_type());
			wTaskUserDao.save(taskUser);
		}
		
		return taskId;
	}
	
	
	//生成移机工单
	public void createMoveTask(int doneCode,CCust cust,String newAddrId,String newAddr,String assignType) throws Exception{
		String teamType = SystemConstants.TEAM_TYPE_SUPERNET;
		if(assignType.equals(SystemConstants.TASK_ASSIGN_CFOCN))
			teamType = SystemConstants.TEAM_TYPE_CFOCN;
		this.saveTaskBaseInfo(cust, doneCode, SystemConstants.TASK_TYPE_MOVE, getTeamId(teamType), newAddr);
	}
	
	
	//修改施工队
	public void changeTaskTeam(int doneCode,String taskId,Integer deptId){
		
	}
	
	//删除开户工单的用户
	public void deleteTaskUser(int doneCode,String taskId,List<Integer> userList){
		
	}
	
	//作废工单
	public void cancelTask(int doneCode,String taskId) throws Exception{
		
	}
	
	//回填移机工单
	public void fillMoveTaskInfo(int doneCode,String taskId,String otlNo,String ponNo) throws Exception{
		
	}
	
	//回填开户工单
	public void fillOpenTaskInfo(int doneCode,String taskId,String otlNo,String ponNo,List<RDevice> deviceList) throws Exception{
		
	}
	
	//修改回填的设备
	public void updateFillDeviceInfo(int doneCode,String taskId,String oldDeviceId,String newDeviceId) throws Exception{
		
	}
	
	//完工
	public void finishTask(int doneCode,String taskId,String resultType) throws Exception{
		WTaskBaseInfo task = new WTaskBaseInfo();
		task.setTask_id(gTaskId());
		task.setTask_status(StatusConstants.TASK_END);
		task.setTask_finish_type(resultType);
		task.setTask_finish_time(new Date());
		
		wTaskBaseInfoDao.save(task);
		//记录操作日志
		JsonObject jo = new JsonObject();
		jo.addProperty("resultType", resultType);
		createTaskLog(taskId,BusiCodeConstants.TASK_FINISH, doneCode, jo.toString(), StatusConstants.NONE);
		
	}
	
	//保存工单基本信息
	private String saveTaskBaseInfo(CCust cust,int doneCode,String taskType,String teamId,String newAddr) throws Exception{
		CCustLinkman linkman = CCustLinkmanDao.findByKey(cust.getCust_id());
		
		WTaskBaseInfo task = new WTaskBaseInfo();
		task.setTask_id(gTaskId());
		task.setTask_type_id(taskType);
		task.setCust_id(cust.getCust_id());
		task.setDone_code(doneCode);
		task.setCust_name(cust.getCust_name());
		task.setTeam_id(teamId);
		//设置工单的施工地址
		if (StringHelper.isNotEmpty(newAddr)){
			task.setNew_addr(newAddr);
			task.setOld_addr(cust.getAddress());
		} else {
			task.setNew_addr(cust.getAddress());
		}
		task.setMobile(linkman.getMobile());
		task.setTel(linkman.getTel());
		//设置地区县市信息
		task.setCounty_id(cust.getCounty_id());
		task.setArea_id(cust.getArea_id());
		wTaskBaseInfoDao.save(task);
		return task.getTask_id();
	}
	
	
	private String gTaskId()throws Exception{
		return wTaskBaseInfoDao.findSequence(SequenceConstants.SEQ_TASK).toString();
	}
	
	private String getTeamId(String teamType) throws Exception{
		//获取施工队信息
		List<WTeam> teamList = wTeamDao.findAll();
		for (WTeam team:teamList){
			if(team.getTeam_type().equals(teamType))
				return team.getDept_id();
		}
		return null;
	}
	
	private void createTaskLog(String taskId,String busiCode,int doneCode,String logDetail,String synStatus) throws Exception{
		WTaskLog log = new WTaskLog();
		log.setTask_id(taskId);
		log.setBusi_code(busiCode);
		log.setDone_code(doneCode);
		log.setOptr_id(getOptr().getOptr_id());
		log.setSyn_status(synStatus);
		log.setLog_time(new Date());
		log.setLog_detail(logDetail);
		wTaskLogDao.save(log);
		
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

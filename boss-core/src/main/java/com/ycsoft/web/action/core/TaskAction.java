package com.ycsoft.web.action.core;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.business.dto.core.cust.QueryTaskConditionDto;
import com.ycsoft.business.service.ISnTaskService;
import com.ycsoft.business.service.ITaskService;
import com.ycsoft.business.service.impl.UserServiceSN;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/** 
 * 
 * 工单控制器
 * 
 */
@Controller
public class TaskAction extends BaseBusiAction{

	private ITaskService taskService;
	
	private String [] task_ids;
	private String [] cust_ids;
	
	private WTaskBaseInfo task;
	private String[] materials;
	private String task_id;
	private String booksTime;
	private String taskCustName;
	private String tel;
	private String remark;
	private String cancelRemark;
	private QueryTaskConditionDto taskCond;
	private String bugCause;
	private ISnTaskService snTaskService;
	private UserServiceSN userServiceSN;
	private String deptId;
	private String bugType;
	private String resultType;
	private String deviceCode;
	private String deviceModel;
	private String custId;
	private String taskType;

	public String saveBugTask()throws Exception{
		String bugCause = request.getParameter("bugCause");
		taskService.saveBugTask(bugCause);
		return JSON_SUCCESS;	
	}
	
	
	public String saveNewTask()throws Exception{
		taskService.saveNewTask();
		return JSON_SUCCESS;	
	}
	
	/**
	 * 查询工单打印信息
	 * @return
	 * @throws Exception
	 */
	public String queryPrintContent()throws Exception{
		String [] tasks = request.getParameterValues("tasks");
		if(tasks == null || tasks.length == 0){
			return JSON_RECORDS;
		}
		String[] task_types = new String[tasks.length];
		task_ids = new String[tasks.length];
		cust_ids = new String[tasks.length];
		for (int i = 0 ; i < tasks.length; i++) {
			String [] tmp = tasks[i].split("#");
			task_types[i] = tmp[0];
			cust_ids[i] = tmp[1];
			task_ids[i] = tmp[2];
		}
		List<Map<String,Object>> records = taskService.queryPrintContent(task_types, cust_ids, task_ids);
		getRoot().setRecords(records);
		return JSON_RECORDS;
	}

	/**
	 * 查询工单
	 * @return
	 * @throws Exception
	 */
	public String queryTasks()throws Exception{
		String isWaitTask = request.getParameter("isWaitTask");
		//查询待办工单
		if(SystemConstants.BOOLEAN_TRUE.equals(isWaitTask)){
			getRoot().setPage(snTaskService.queryUnProcessTask(start,limit));
		}else{
			if(taskCond == null){
				return JSON_PAGE;
			}
			taskCond.setStart(start);
			taskCond.setLimit(limit);
			getRoot().setPage(snTaskService.queryTask(taskCond.getTaskType(),taskCond.getAddrIds(),taskCond.getStartTime(),taskCond.getEndTime(),taskCond.getTaskId()
					,taskCond.getTaskTeam(),taskCond.getStatus(),taskCond.getCustNo(),taskCond.getCustName(),taskCond.getAddr(),taskCond.getMobile(),
					taskCond.getZteStatus(),taskCond.getSyncStatus(),taskCond.getStart(),taskCond.getLimit()));
		}
		return JSON_PAGE;
	}
	
	public String queryTaskDetail() throws Exception{
		getRoot().setOthers(snTaskService.queryTaskDetail(task_id));
		return JSON_OTHER;
	}
	public String queryAllTaskDetail() throws Exception{
		getRoot().setOthers(snTaskService.queryAllTaskDetail(task_id));
		return JSON_OTHER;
	}
	
	
	
	public String queryTaskDevice()  throws Exception{
		getRoot().setRecords(snTaskService.queryTaskDevice(task_id));
		return JSON_RECORDS;
	}
	
	public String queryTaskTeam() throws Exception{
		getRoot().setRecords(snTaskService.queryTaskTeam());
		return JSON_RECORDS;
	}
	
	public String getTaskType() throws Exception{
		getRoot().setRecords(taskService.getTaskType());
		return JSON_RECORDS;
	}
	
	public String cancelTask()throws Exception{
		taskService.cancelTask(this.task_ids,cancelRemark);
		getRoot().setSuccess(true);
		return JSON_SUCCESS;
	}
	
	public String assignTask()throws Exception{		
		taskService.assignTask(this.task_ids);
		getRoot().setSuccess(true);
		return JSON_SUCCESS;
	}
	
	public String modifyTask() throws Exception{
		taskService.modifyTask(task_id,booksTime,taskCustName,tel,remark,bugCause);
		return JSON_SUCCESS;
	}
	
	
	/**
	 * 分配施工队
	 * @return
	 * @throws Exception
	 */
	public String editTaskTeam() throws Exception{
		String optrId = request.getParameter("optrId");
		String finishRemark = request.getParameter("finishRemark");
		snTaskService.editTaskTeam(task_id, deptId, optrId, bugType,finishRemark);
		return JSON_SUCCESS;
	}
	
	/**
	 * 取消工单
	 * @return
	 * @throws Exception
	 */
	public String cancelTaskSn()throws Exception{
		if(SystemConstants.TASK_TYPE_INSTALL.equals(taskType)){
			userServiceSN.cancelInstallTask(task_id);
		}else{
			snTaskService.cancelTask(task_id);
		}
		getRoot().setSuccess(true);
		return JSON_SUCCESS;
	}
	
	
	public String withdrawTask()throws Exception{
		snTaskService.withdrawTask(task_id);
		getRoot().setSuccess(true);
		return JSON_SUCCESS;
	}
	
	/**
	 * 完工
	 * @return
	 * @throws Exception
	 */
	public String endTask() throws Exception{
		String finishRemark = request.getParameter("finishRemark");
		String custSignNo = request.getParameter("custSignNo");
		snTaskService.finishTask(task_id,resultType, bugType, custSignNo,finishRemark,true);
		return JSON_SUCCESS;
	}
	
	public String queryCanEndTask() throws Exception{
		getRoot().setSimpleObj(snTaskService.queryCanEndTask(task_id,resultType));
		return JSON_SIMPLEOBJ;
		
	}
	
	public String editCustSignNo() throws Exception {
		String custSignNo = request.getParameter("custSignNo");
		snTaskService.editCustSignNo(task_id, custSignNo);
		return JSON_SUCCESS;
	}
	
	/**
	 * 保存zte
	 * @return
	 * @throws Exception
	 */
	public String saveZte() throws Exception{
		String zte_status = request.getParameter("zte_status");
		String log_remark = request.getParameter("log_remark");
		snTaskService.saveZte(task_id,zte_status,log_remark);
		return JSON_SUCCESS;
	}
	
	public String  queryDeviceInfoByCodeAndModel() throws Exception {
		getRoot().setSimpleObj(snTaskService.queryDeviceInfoByCodeAndModel(deviceCode,deviceModel)); 
		return JSON_SIMPLEOBJ;
	}
	
	public String fillTask() throws Exception{
		String devices = request.getParameter("devices");
		Type t = new TypeToken<List<TaskFillDevice>>(){}.getType();
		List<TaskFillDevice> list = JsonHelper.gson.fromJson( devices , t);
		snTaskService.fillTask(task_id,list);
		return JSON_SUCCESS;
	}
	
	public String fillWriteOffTerminalTask() throws Exception{
		String devices = request.getParameter("devices");
		Type t = new TypeToken<List<TaskFillDevice>>(){}.getType();
		List<TaskFillDevice> list = JsonHelper.gson.fromJson( devices , t);
		snTaskService.fillWriteOffTerminalTask(task_id, list);
		return JSON_SUCCESS;
	}
	
	public String queryTaskByCustId()throws Exception{
		getRoot().setRecords(snTaskService.queryTaskByCustId(custId));
		return JSON_RECORDS;
	}
	/**
	 * @return the cust_ids
	 */
	public String[] getCust_ids() {
		return cust_ids;
	}

	/**
	 * @param cust_ids the cust_ids to set
	 */
	public void setCust_ids(String[] cust_ids) {
		this.cust_ids = cust_ids;
	}
	
	/**
	 * @return the task_ids
	 */
	public String[] getTask_ids() {
		return task_ids;
	}

	/**
	 * @param task_ids the task_ids to set
	 */
	public void setTask_ids(String[] task_ids) {
		this.task_ids = task_ids;
	}
	
	/**
	 * @return the taskService
	 */
	public ITaskService getTaskService() {
		return taskService;
	}


	/**
	 * @param taskService the taskService to set
	 */
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}

	public WTaskBaseInfo getTask() {
		return task;
	}

	public void setTask(WTaskBaseInfo task) {
		this.task = task;
	}
	
	public String getCancelRemark() {
		return cancelRemark;
	}


	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}


	public QueryTaskConditionDto getTaskCond() {
		return taskCond;
	}


	public void setTaskCond(QueryTaskConditionDto taskCond) {
		this.taskCond = taskCond;
	}


	public String[] getMaterials() {
		return materials;
	}

	public void setMaterials(String[] materials) {
		this.materials = materials;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}


	public String getBooksTime() {
		return booksTime;
	}


	public void setBooksTime(String booksTime) {
		this.booksTime = booksTime;
	}


	public String getTaskCustName() {
		return taskCustName;
	}


	public void setTaskCustName(String taskCustName) {
		this.taskCustName = taskCustName;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getBugCause() {
		return bugCause;
	}


	public void setBugCause(String bugCause) {
		this.bugCause = bugCause;
	}


	public ISnTaskService getSnTaskService() {
		return snTaskService;
	}


	public void setSnTaskService(ISnTaskService snTaskService) {
		this.snTaskService = snTaskService;
	}

	public UserServiceSN getUserServiceSN() {
		return userServiceSN;
	}


	public void setUserServiceSN(UserServiceSN userServiceSN) {
		this.userServiceSN = userServiceSN;
	}


	public String getDeptId() {
		return deptId;
	}


	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}


	public String getBugType() {
		return bugType;
	}


	public void setBugType(String bugType) {
		this.bugType = bugType;
	}


	public String getResultType() {
		return resultType;
	}


	public void setResultType(String resultType) {
		this.resultType = resultType;
	}


	public String getDeviceCode() {
		return deviceCode;
	}


	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}


	public String getDeviceModel() {
		return deviceModel;
	}


	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}


	public String getCustId() {
		return custId;
	}


	public void setCustId(String custId) {
		this.custId = custId;
	}


	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}



}

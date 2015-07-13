package com.ycsoft.sysmanager.web.action.config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.core.bill.BTaskScheduleContent;
import com.ycsoft.beans.core.bill.BTaskScheduleContentDto;
import com.ycsoft.beans.core.bill.BTaskScheduleListDto;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.beans.task.WRevisitInfo;
import com.ycsoft.business.dto.config.TaskQueryConditionDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.config.TaskComponent;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

public class TaskAction  extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6147752105205251492L;

	private TaskComponent taskComponent;
	
	private String stopTaskList;
	private String threeTaskList;
	private String status;
	private String taskCode;
	private String isBase;
	private String servType;
	private String countyId;
	private TaskQueryConditionDto taskCond;
	private String [] task_ids;
	private String [] cust_ids;
	private String work_id;
	private String books_time;
	private WRevisitInfo revisit;
	private String newRevisitInstallerId;
	private String cancel_remak;
	
	public String deleteStopTask() throws Exception {
		taskComponent.deleteStopTask(taskCode, servType, countyId);
		return JSON;
	}
	
	public String deleteThreeTask() throws Exception {
		taskComponent.deleteThreeTask(taskCode,countyId);
		return JSON;
	}

	@SuppressWarnings("unchecked")
	public String updateThreeTaskStatus() throws Exception {
		//TODO 记录异动
		Map oldTasks = taskComponent.queryTaskSachedule(optr.getCounty_id());
		taskComponent.updateThreeTaskStatus(status, taskCode, isBase, countyId);
		Map newTasks = taskComponent.queryTaskSachedule(optr.getCounty_id());
		Set<String> set = new HashSet<String>();
		set.add(taskCode);
		saveThreeTaskChanges(oldTasks,newTasks,set);
		return JSON;
	}
	
	public String updateStopTaskStatus() throws Exception {
		//TODO 增加异动
		List<BTaskScheduleContentDto> oldList = taskComponent.queryTjTaskSchedule(countyId); 
		taskComponent.updateStopTaskStatus(status, taskCode, servType, countyId);
		List<BTaskScheduleContentDto> newList = taskComponent.queryTjTaskSchedule(countyId);
		saveStopTaskChanges(oldList, newList);
		return JSON;
	}
	
	@SuppressWarnings("unchecked")
	public String saveThreeTask() throws Exception {
		//TODO 记录异动
		Type t = new TypeToken<List<BTaskScheduleListDto>>(){}.getType();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		List<BTaskScheduleListDto> list = gson.fromJson( threeTaskList , t);
		
		Map oldTasks = taskComponent.queryTaskSachedule(optr.getCounty_id());
		taskComponent.saveThreeTask(list, optr.getOptr_id());
		Map newTasks = taskComponent.queryTaskSachedule(optr.getCounty_id());
		saveThreeTaskChanges(oldTasks,newTasks,CollectionHelper.converToMap(list, "task_code").keySet());
		
		return JSON;
	}
	
	public String saveStopTask() throws Exception {
		//TODO 记录异动
		Type t = new TypeToken<List<BTaskScheduleContentDto>>(){}.getType();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		List<BTaskScheduleContentDto> list = gson.fromJson( stopTaskList , t);
		
		Set<String> countyids = CollectionHelper.converToMap(list, "county_id").keySet();
		
		List<BTaskScheduleContentDto> oldList = new ArrayList<BTaskScheduleContentDto>(); 
		for(String cid:countyids){
			List<BTaskScheduleContentDto> innerList = taskComponent.queryTjTaskSchedule(cid);
			oldList.addAll(innerList);
		}
		
		taskComponent.saveStopTask(list, optr.getOptr_id());
		List<BTaskScheduleContentDto> newList = new ArrayList<BTaskScheduleContentDto>();
		
		for(String cid:countyids){
			List<BTaskScheduleContentDto> innerList = taskComponent.queryTjTaskSchedule(cid);
			newList.addAll(innerList);
		}
		
		saveStopTaskChanges(oldList,newList);
		
		return JSON;
	}
	
	/**
	 * 保存停机任务异动.
	 * @param oldList
	 * @param newList
	 */
	private void saveStopTaskChanges(List<BTaskScheduleContentDto> oldList,List<BTaskScheduleContentDto> newList) throws ActionException{
		List<SSysChange> changes = new ArrayList<SSysChange>();
		Date createTime = new Date();
		String optrId = WebOptr.getOptr().getOptr_id();
		String changeType = SysChangeType.TASK_CFG.toString();
		String changeDesc = "任务配置异动";
		
		String [] fields = new String[]{"area_name","base_eff_date","base_exp_date","bcnt","county_name","is_real_time_text"
				,"isbase_text","notbase_text","notbase_eff_date","notbase_exp_date","schedule_time","serv_type_text","status_text"
				,"xcnt","task_code"
		};
		Map<String, String> taskCodeDescMap = new HashMap<String, String>();
		taskCodeDescMap.put("TJ", "停机");
		taskCodeDescMap.put("XJ", "巡检");
		taskCodeDescMap.put("TD", "退订");
		taskCodeDescMap.put("CJ", "催缴");
		
		try{
			Integer doneCode = taskComponent.getDoneCOde();
			
			Map<String, BTaskScheduleContentDto> oldMap = CollectionHelper.converToMapSingle(oldList, new String[] { "county_id","task_code", "serv_type" });
			Map<String, BTaskScheduleContentDto> newMap = CollectionHelper.converToMapSingle(newList, new String[] { "county_id","task_code", "serv_type" });
			
			Set<String> keySet = new HashSet<String>();
			keySet.addAll(oldMap.keySet());
			keySet.addAll(newMap.keySet());
			if(keySet.size() ==0){
				throw new ActionException("保存停机任务异动出现错误,未能正确获取数据");
			}
			
			for(String key :keySet){
				String keyDesc = null;
				BTaskScheduleContent oldTask = oldMap.get(key);
				if(oldTask!=null){
					oldTask.setTask_code(taskCodeDescMap.get(oldTask.getTask_code()));
					keyDesc = MemoryDict.getDictName(DictKey.COUNTY, oldTask.getCounty_id()) + "_"+oldTask.getTask_code() + "_" + oldTask.getServ_type_text();
				}
				BTaskScheduleContent newTask = newMap.get(key);
				if(newTask!=null){
					newTask.setTask_code(taskCodeDescMap.get(newTask.getTask_code()));
					keyDesc = MemoryDict.getDictName(DictKey.COUNTY, newTask.getCounty_id())+"_"+newTask.getTask_code() + "_" + newTask.getServ_type_text();
				}
				
				String content = BeanHelper.beanchange(oldTask, newTask, fields);
				if(StringHelper.isNotEmpty(content)){
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, content, optrId, createTime);
					changes.add(change);
				}
			}
			
			if(changes.size()>0){
				taskComponent.getSSysChangeDao().save(changes.toArray(new SSysChange[changes.size()]));
			}
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}
	
	
	/**
	 * 保存 催缴、巡检、退订任务的异动.
	 * @param oldTasks 以任务类型为key的map
	 * @param newTasks	以任务类型为key的map
	 * @param taskCodes	本次保存的任务的类型.
	 * @throws ActionException
	 */
	private void saveThreeTaskChanges(Map<String,List<BTaskScheduleListDto>> oldTasks, Map<String, List<BTaskScheduleListDto>> newTasks, Set<String> taskCodes) throws ActionException{
		Map<String, String> taskCodeDescMap = new HashMap<String, String>();
		taskCodeDescMap.put("TJ", "停机");
		taskCodeDescMap.put("XJ", "巡检");
		taskCodeDescMap.put("TD", "退订");
		taskCodeDescMap.put("CJ", "催缴");
		
		List<BTaskScheduleListDto> oldTaskList = new ArrayList<BTaskScheduleListDto>();
		List<BTaskScheduleListDto> newTaskList = new ArrayList<BTaskScheduleListDto>();
		for(String key:taskCodes){
			List<BTaskScheduleListDto> olds = oldTasks.get(key);
			if(CollectionHelper.isNotEmpty(olds)){
				for(BTaskScheduleListDto dto:olds){
					String isbase2 = dto.getIsbase();
					dto.setIsbase("T".equalsIgnoreCase(isbase2) ? taskCodeDescMap.get(dto.getTask_code())+"基本产品":taskCodeDescMap.get(dto.getTask_code())+"增值产品");
					oldTaskList.add(dto);
				}
			}
			List<BTaskScheduleListDto> news = newTasks.get(key);
			if(CollectionHelper.isNotEmpty(news)){
				for(BTaskScheduleListDto dto:news){
					String isbase2 = dto.getIsbase();
					dto.setIsbase("T".equalsIgnoreCase(isbase2) ? taskCodeDescMap.get(dto.getTask_code())+"基本产品":taskCodeDescMap.get(dto.getTask_code())+"增值产品");
					newTaskList.add(dto);
				}
			}
		}
		
		Date createTime = new Date();
		String optrId = WebOptr.getOptr().getOptr_id();
		String changeType = SysChangeType.TASK_CFG.toString();
		String changeDesc = "任务配置异动";
		
		String [] xjTdFields = new String[]{"county_name","schedule_time","status","isbase","max_prod_num","eff_date","exp_date"};
		
		String [] cjFields = new String[]{"county_name","eff_date","exp_date","hst_day","isbase","mail_title","max_prod_num","schedule_time","status","task_info"};
		
		
		List<SSysChange> changes = new ArrayList<SSysChange>();
		try{
			Integer doneCode = taskComponent.getDoneCOde();
			Map<String, BTaskScheduleListDto> oldMap = CollectionHelper.converToMapSingle(oldTaskList, new String[] {"county_id",  "task_code", "isbase" });
			Map<String, BTaskScheduleListDto> newMap = CollectionHelper.converToMapSingle(newTaskList, new String[] {"county_id",  "task_code", "isbase" });
			
			Set<String> allChangeRecordKeys = new HashSet<String>();
			allChangeRecordKeys.addAll(oldMap.keySet());
			allChangeRecordKeys.addAll(newMap.keySet());
			//汉字显示
			for(String key : allChangeRecordKeys){
				BTaskScheduleListDto oldTask = oldMap.get(key);
				if(oldTask!=null){
					String oldStatus = oldTask.getStatus();
					oldTask.setStatus("1".equals(oldStatus)?"有效":"失效");
				}
				BTaskScheduleListDto newTask = newMap.get(key);
				if(null !=newTask){
					String newStatus = newTask.getStatus();
					newTask.setStatus("1".equals(newStatus)?"有效":"失效");
				}
				
				String taskCode = oldTask ==null?newTask.getTask_code():oldTask.getTask_code();
				String [] fields = null;
				if(taskCode.equalsIgnoreCase("CJ")){
					fields = cjFields;
				}else{
					fields = xjTdFields;
				}
				String taskCodeDesc = taskCodeDescMap.get(taskCode);
				String content = BeanHelper.beanchange(oldTask, newTask, fields);
				if(StringHelper.isNotEmpty(content)){
					String keyDesc = null == oldTask ? MemoryDict.getDictName(DictKey.COUNTY, newTask.getCounty_id())+"_"+taskCodeDesc + "_" + newTask.getIsbase():
						MemoryDict.getDictName(DictKey.COUNTY, oldTask.getCounty_id())+"_"+taskCodeDesc + "_" + oldTask.getIsbase() ;
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, content, optrId, createTime);
					changes.add(change);
				}
			}
			
			if(changes.size()>0){
				taskComponent.getSSysChangeDao().save(changes.toArray(new SSysChange[changes.size()]));
			}
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}

	public String queryTjTaskSchedule() throws Exception {
		getRoot().setRecords(taskComponent.queryTjTaskSchedule(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	
	@SuppressWarnings("unchecked")
	public String queryTaskSachedule() throws Exception {
		getRoot().setOthers(taskComponent.queryTaskSachedule(optr.getCounty_id()));
		return JSON_OTHER;
	}
	
	public String queryCounty() throws Exception {
		getRoot().setRecords(taskComponent.queryCounty(optr.getCounty_id()));
		return JSON_RECORDS;
	}

	
	/**
	 * 查询待办工单
	 * @return
	 * @throws Exception
	 */
	public String queryWaitAcceptTask()throws Exception{
		if(taskCond == null){
			return JSON_PAGE;
		}
		taskCond.setStart(start);
		taskCond.setLimit(limit);
		getRoot().setPage( taskComponent.queryWaitAcceptTask(taskCond) );
		return JSON_PAGE;  
	}
	/**
	 * 查询施工队组合成ComboBox Store需要的参数
	 * @return
	 * @throws Exception
	 */
	public String queryInstallerDept()throws Exception{
		getRoot().setRecords(taskComponent.queryInstallerDept());
		return JSON_RECORDS;
	}
	
	public String queryInstaller()throws Exception{
		String team = request.getParameter("team");
		getRoot().setRecords(taskComponent.queryInstaller(team));
		return JSON_RECORDS;
	}
	
	public String cancelTask()throws Exception{
		taskComponent.cancelTask(this.task_ids,cancel_remak);
		return JSON_SUCCESS;
	}
	
	public String responseTask() throws Exception{
		if(this.cust_ids == null && this.cust_ids.length == 0){
			throw new IllegalArgumentException("客户编号不能为空!");
		}
		taskComponent.responseTask(revisit , cust_ids[0]);
		return JSON;
	}
	
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
		List<Map<String,Object>> records = taskComponent.queryPrintContent(task_types, cust_ids, task_ids);
		getRoot().setRecords(records);
		return JSON_RECORDS;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String modifyBooksTime()throws Exception{
		taskComponent.modifyBooksTime(work_id, books_time);
		return JSON;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String assignTask()throws Exception{
		String team = request.getParameter("team");
		taskComponent.assignTask(this.task_ids, team);
		return JSON;
	}
	
	public String modifyPlanOptr() throws Exception {
		taskComponent.modifyPlanOptr(work_id, newRevisitInstallerId);
		return JSON;
	}
	
	public String  queryTaskByTaskId() throws Exception{
		getRoot().setSimpleObj(taskComponent.queryTaskByTaskId(work_id));
		return JSON_SIMPLEOBJ;
	}
	
	public void setTaskComponent(TaskComponent taskComponent) {
		this.taskComponent = taskComponent;
	}

	public void setStopTaskList(String stopTaskList) {
		this.stopTaskList = stopTaskList;
	}

	public void setThreeTaskList(String threeTaskList) {
		this.threeTaskList = threeTaskList;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public void setServType(String servType) {
		this.servType = servType;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}

	public void setTaskCond(TaskQueryConditionDto taskCond) {
		this.taskCond = taskCond;
	}

	public TaskQueryConditionDto getTaskCond() {
		return taskCond;
	}

	public void setTask_ids(String[] task_ids) {
		this.task_ids = task_ids;
	}

	public void setCust_ids(String[] cust_ids) {
		this.cust_ids = cust_ids;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	public void setBooks_time(String books_time) {
		this.books_time = books_time;
	}

	public void setRevisit(WRevisitInfo revisit) {
		this.revisit = revisit;
	}

	public WRevisitInfo getRevisit() {
		return revisit;
	}

	public void setNewRevisitInstallerId(String newRevisitInstallerId) {
		this.newRevisitInstallerId = newRevisitInstallerId;
	}

	public void setCancel_remak(String cancel_remak) {
		this.cancel_remak = cancel_remak;
	}

}
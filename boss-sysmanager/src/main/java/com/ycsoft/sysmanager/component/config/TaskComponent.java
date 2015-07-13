package com.ycsoft.sysmanager.component.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BTaskInfo;
import com.ycsoft.beans.core.bill.BTaskSchedule;
import com.ycsoft.beans.core.bill.BTaskScheduleContent;
import com.ycsoft.beans.core.bill.BTaskScheduleContentDto;
import com.ycsoft.beans.core.bill.BTaskScheduleList;
import com.ycsoft.beans.core.bill.BTaskScheduleListDto;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.task.WLog;
import com.ycsoft.beans.task.WRevisitInfo;
import com.ycsoft.beans.task.WWork;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dao.core.bill.BTaskInfoDao;
import com.ycsoft.business.dao.core.bill.BTaskScheduleContentDao;
import com.ycsoft.business.dao.core.bill.BTaskScheduleDao;
import com.ycsoft.business.dao.core.bill.BTaskScheduleListDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.task.WRevisitInfoDao;
import com.ycsoft.business.dao.task.WTaskServDao;
import com.ycsoft.business.dao.task.WWorkDao;
import com.ycsoft.business.dto.config.TaskQueryConditionDto;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.business.service.externalImpl.ITaskServiceExternal;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Component
public class TaskComponent extends  BaseComponent {
	private BTaskScheduleDao bTaskScheduleDao;
	private BTaskScheduleContentDao bTaskScheduleContentDao;
	private BTaskScheduleListDao bTaskScheduleListDao;
	private BTaskInfoDao bTaskInfoDao;
	private WTaskServDao wTaskServDao;
	private SCountyDao sCountyDao;
	private WWorkDao wWorkDao;
	private WRevisitInfoDao wRevisitInfoDao;
	private ITaskServiceExternal taskService;
	private SDeptDao sDeptDao;
	
	
	public void deleteThreeTask(String taskCode,String countyId) throws Exception {
		bTaskScheduleDao.deleteTaskSchedule(taskCode, countyId);
		bTaskScheduleListDao.deleteTaskScheduleList(taskCode, countyId);
	}
	
	public void deleteStopTask(String taskCode,String servType,String countyId) throws Exception {
		bTaskScheduleDao.deleteTaskSchedule(taskCode, countyId);
		bTaskScheduleContentDao.deleteTaskContent(taskCode, countyId, servType);
	}
	
	/**
	 * 修改 催缴、巡检、退订 状态
	 * @param status
	 * @param taskCode
	 * @param countyId
	 * @throws Exception
	 */
	public void updateThreeTaskStatus(String status, String taskCode,
			String isBase, String countyId) throws Exception {
		bTaskScheduleListDao.updateThreeTaskStatus(status, taskCode, isBase, countyId);
	}
	
	/**
	 * 修改 停机 状态
	 * @param status
	 * @param taskCode
	 * @param servType
	 * @param countyId
	 * @throws Exception
	 */
	public void updateStopTaskStatus(String status, String taskCode,
			String servType, String countyId) throws Exception {
		bTaskScheduleContentDao.updateStopTaskStatus(status, taskCode, servType, countyId);
	}
	
	/**
	 * 保存 催缴、巡检、退订 
	 * @param status
	 * @param taskCode
	 * @param countyId
	 * @throws Exception
	 */
	public void saveThreeTask(List<BTaskScheduleListDto> list,String optrId) throws Exception {
		//查看list 内容
		Map<String, List<BTaskScheduleListDto>> map = CollectionHelper.converToMap(list, new String[] { "task_code", "county_id" });
		BTaskScheduleListDto bstDto = list.get(0);
		//退订不添加执行计划数据
		if( !bstDto.getTask_code().equals(SystemConstants.TASK_TD) ){
			for(String key : map.keySet()){
				List<BTaskScheduleListDto> tsList = map.get(key);
				BTaskScheduleListDto tsc = tsList.get(0);
				BTaskSchedule ts = new BTaskSchedule();
				BeanUtils.copyProperties(tsc, ts);
				ts.setOptr_id(optrId);
				bTaskScheduleDao.saveTaskSchedule(ts);
				//查看 tsList
				for(BTaskScheduleListDto dto : tsList){
					if(StringHelper.isNotEmpty(dto.getTask_info())){
						BTaskInfo taskInfo = new BTaskInfo();
						BeanUtils.copyProperties(dto, taskInfo);
						bTaskInfoDao.saveOrUpdateTaskInfo(taskInfo);
					}
				}
			}
		}
		for(BTaskScheduleListDto dto : list){
			BTaskScheduleList bsl = new BTaskScheduleList();
			BeanUtils.copyProperties(dto, bsl);
			bTaskScheduleListDao.saveTaskScheduleList(bsl);
		}
	}
	
	
	/**
	 * 停机配置
	 * @param list
	 * @param optr
	 * @throws Exception
	 */
	public void saveStopTask(List<BTaskScheduleContentDto> list,String optrId) throws Exception {
		Map<String, List<BTaskScheduleContentDto>> map = CollectionHelper.converToMap(list, new String[] { "task_code", "county_id" });
		//查看list
		for(String key : map.keySet()){
			List<BTaskScheduleContentDto> tscList = map.get(key);
			BTaskScheduleContentDto tsc = tscList.get(0);
			BTaskSchedule ts = new BTaskSchedule();
			BeanUtils.copyProperties(tsc, ts);
			ts.setOptr_id(optrId);
			bTaskScheduleDao.saveTaskSchedule(ts);
		}
		Map<String, List<BTaskScheduleContentDto>> servMap = CollectionHelper.converToMap(list, new String[] { "task_code", "county_id", "serv_type" });
		//查看servMap
		for(String key : servMap.keySet()){
			bTaskScheduleContentDao.deleteTaskContent(SystemConstants.TASK_TJ,
					key.substring(key.indexOf("_")+1, key.lastIndexOf("_")),
					key.substring(key.lastIndexOf("_")+1, key.length()));
		}
		for(BTaskScheduleContentDto dto : list){
			BTaskScheduleContent content = new BTaskScheduleContent();
			BeanUtils.copyProperties(dto, content);
			bTaskScheduleContentDao.saveTaskScheduleContent(content);
		}
	}
	
	
	/**
	 * 催缴、退订、巡检
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Map queryTaskSachedule(String countyId) throws Exception {
	
		List<BTaskInfo> taskInfoList = bTaskInfoDao.queryTaskInfoByCountyId(countyId);
		List<BTaskScheduleListDto> tasklist = bTaskScheduleDao.queryTaskSchedule(countyId);
		Map<String,List<BTaskScheduleListDto>> map = new HashMap<String,List<BTaskScheduleListDto>>();
		if(tasklist.size() > 0){
			map = CollectionHelper.converToMap(tasklist, "task_code");
			
			List<BTaskScheduleListDto> cjList = map.get(SystemConstants.TASK_CJ);//催缴集合
			for(BTaskScheduleListDto tslDto : cjList){
				for(BTaskInfo taskInfo : taskInfoList){
					if(tslDto.getCounty_id().equals(taskInfo.getCounty_id())){
						tslDto.setTask_info(taskInfo.getTask_info());
						tslDto.setMail_title(taskInfo.getMail_title());
					}
				}
			}
		}
		return map;
	}

	/**
	 * 停机
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<BTaskScheduleContentDto> queryTjTaskSchedule(String countyId) throws Exception {
		return bTaskScheduleDao.queryTjTaskSchedule(countyId);
	}
	
	public List<SCounty> queryCounty(String countyId) throws Exception {
		List<SCounty> list = new ArrayList<SCounty>();
		if(countyId.equals(SystemConstants.COUNTY_ALL)){
			list = sCountyDao.findAll();
		}else{
			list.add(sCountyDao.getCountyById(countyId).get(0));
		}
		for(SCounty county : list){
			if(county.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
				list.remove(county);
				break;
			}
		}
		return list;
	}
	
	/**
	 * 多功能查询工单
	 * @param cond
	 * @return
	 * @throws Exception
	 */
	public Pager<TaskQueryWorkDto> queryWaitAcceptTask(TaskQueryConditionDto cond) throws Exception {
		String dataRight = this.queryDataRightCon();
		return wWorkDao.queryTasks(cond,dataRight, WebOptr.getOptr().getCounty_id());
	}
	
	/**
	 * 查询施工部门
	 * @return
	 * @throws Exception
	 */
	public List<?> queryInstallerDept() throws Exception{
		return wWorkDao.queryInstallerDept();
	}
	
	/**
	 * 查询安装人员
	 * @return
	 * @throws Exception
	 */
	public List<SItemvalue> queryInstaller(String team)throws Exception{
		return wWorkDao.queryInstaller(team);
	}
	
	/**
	 * 查询打印内容，并修改工单状态为施工中
	 * @param task_types
	 * @param cust_ids
	 * @param task_ids
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryPrintContent(String[] task_types,
			String[] cust_ids, String[] task_ids) throws Exception{ 
		BusiParameter p = new BusiParameter();
		p.setOptr(WebOptr.getOptr());
		List<Map<String,Object>> list = taskService.queryPrintContent(p, task_types, cust_ids, task_ids);
		return list;
	}
	
	/**
	 * 修改预约时间
	 * @param task_id
	 * @param newPlanTime
	 * @return
	 * @throws Exception
	 */
	public void modifyBooksTime(String task_id, String newPlanTime) throws Exception{
		WWork work = wWorkDao.findByKey(task_id);
		work.setBooks_time(newPlanTime);
		wWorkDao.update(work);
		
		//记录操作
		String info = "预约时间:"+work.getBooks_time()+"->"+newPlanTime;
		saveWorkLog(SystemConstants.WORK_CHANGE_TIME,task_id.split(","),WebOptr.getOptr(),info);
	}
	
	/**
	 * 更新派单内容
	 * @param task_ids
	 * @param installerTeam
	 * @throws Exception
	 */
	public void assignTask(String[] task_ids, String installerTeam)throws Exception {
		Map<String, WWork> map = CollectionHelper.converToMapSingle(wWorkDao.queryWork(task_ids), "work_id");
		List<WLog> logList = new ArrayList<WLog>();
		List<WWork> wList = new ArrayList<WWork>();
		Integer sDoneCode = getLogDonecode();
		SDept dept = sDeptDao.findByKey(installerTeam);
		if(dept == null){
			throw new ComponentException("安装部门不存在");
		}
		for(String dto:task_ids){
			WWork work = map.get(dto);
			if(work!=null){
				//新建派单记录
				WLog log = new WLog();
				log.setDone_type(SystemConstants.WORK_ASSIGN);
				log.setArea_id(WebOptr.getOptr().getArea_id());
				log.setCounty_id(WebOptr.getOptr().getCounty_id());
				log.setDept_id(WebOptr.getOptr().getDept_id());
				log.setOptr_id(WebOptr.getOptr().getOptr_id());
				log.setWork_id(dto);
				log.setDone_code(sDoneCode);
				String info = "安装部门:"+work.getAssign_dept_text()+"->"+dept.getDept_name();
				log.setInfo(info);
				logList.add(log);
				
				if(work.getTask_status().equals(StatusConstants.TASK_CREATE)){
					work.setTask_status(StatusConstants.TASK_INIT);
				}
				work.setAssign_dept(installerTeam);
				work.setAssign_time(new Date());
				wList.add(work);
			}
		}
		//记录操作
		wLogDao.save(logList.toArray(new WLog[logList.size()]));
		//更新工单
		wWorkDao.update(wList.toArray(new WWork[wList.size()]));
		
	}
	
	/**
	 * 记录回单，并更新工单状态为完成
	 * @param revisit
	 * @param custId
	 * @throws Exception
	 */
	public void responseTask(WRevisitInfo revisit, String custId) throws Exception {
		//变更工单状态
		String workId = revisit.getWork_id();
		WWork work = wWorkDao.findByKey(workId);
		work.setTask_status(StatusConstants.TASK_END);
		wWorkDao.update(work);

		//记录回单信息
		revisit.setRevisit_optr(WebOptr.getOptr().getOptr_id());
		wRevisitInfoDao.save(revisit);
		
		//记录操作
		saveWorkLog(SystemConstants.WORK_REVISIT,workId.split(","),WebOptr.getOptr(),null);
	}
	
	/**
	 * 作废工单
	 * @param task_ids
	 * @throws Exception
	 */
	public void cancelTask(String[] task_ids,String cancel_remak)throws Exception {
		//变更工单状态
		wWorkDao.updateStatus(task_ids, StatusConstants.TASK_CANCEL);
		
		//记录操作
		saveWorkLog(SystemConstants.WORK_CANCEL,task_ids,WebOptr.getOptr(),cancel_remak);
	}
	
	
	/**
	 * 根据工单编号，查询工单详细信息
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	public TaskQueryWorkDto queryTaskByTaskId(String workId) throws Exception {
		TaskQueryWorkDto workInfo = new TaskQueryWorkDto();
		workInfo = wWorkDao.queryaskByTaskId(workId);
		//查询服务类型
		workInfo.setTaskServList(wTaskServDao.queryTaskServByTaskId(workId));
		return workInfo;
	}
	
	/**
	 * 工单变更安装人员
	 * @param taskId
	 * @param newRevisitInstallerIds
	 * @throws Exception
	 */
	public void modifyPlanOptr(String taskId, String newRevisitInstallerIds) throws Exception {
		WRevisitInfo winfo = wRevisitInfoDao.findByKey(taskId);
		winfo.setInstaller_optr(newRevisitInstallerIds);
		wRevisitInfoDao.update(winfo);
		
		//记录操作
		String info = "安装人员:"+winfo.getInstaller_optr_text();
		saveWorkLog(SystemConstants.WORK_CHANGE_OPTR,taskId.split(","),WebOptr.getOptr(),info);
	}
	
//	/**
//	 * 记录工单操作
//	 * @param doneCode
//	 * @param doneType
//	 * @param workId
//	 * @param info
//	 * @throws Exception
//	 */
//	public void saveWorkLog(String doneType,String[] workIds,String info  ) throws Exception {
//		Integer sDoneCode = getLogDonecode();
//		List<WLog> logList = new ArrayList<WLog>();
//		for(String dto : workIds){
//			WLog log = new WLog();
//			log.setDone_type(doneType);
//			log.setArea_id(WebOptr.getOptr().getArea_id());
//			log.setCounty_id(WebOptr.getOptr().getCounty_id());
//			log.setDept_id(WebOptr.getOptr().getDept_id());
//			log.setOptr_id(WebOptr.getOptr().getOptr_id());
//			log.setWork_id(dto);
//			log.setDone_code(sDoneCode);
//			log.setInfo(info);
//			logList.add(log);
//		}
//		wLogDao.save(logList.toArray(new WLog[logList.size()]));
//	}
	
	private String queryDataRightCon() {
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(WebOptr.getOptr(),DataRight.QUERY_CUST.toString());
		} catch (Exception e) {
			dataRight=SystemConstants.DEFAULT_DATA_RIGHT;
		}
		if(!SystemConstants.DEFAULT_DATA_RIGHT.equals(dataRight)){
			dataRight = "t."+dataRight.trim();
		}
		return dataRight;
	}
	
	public void setBTaskScheduleDao(BTaskScheduleDao taskScheduleDao) {
		bTaskScheduleDao = taskScheduleDao;
	}

	public void setBTaskInfoDao(BTaskInfoDao taskInfoDao) {
		bTaskInfoDao = taskInfoDao;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public void setBTaskScheduleContentDao(
			BTaskScheduleContentDao taskScheduleContentDao) {
		bTaskScheduleContentDao = taskScheduleContentDao;
	}

	public void setBTaskScheduleListDao(BTaskScheduleListDao taskScheduleListDao) {
		bTaskScheduleListDao = taskScheduleListDao;
	}

	public void setWWorkDao(WWorkDao workDao) {
		wWorkDao = workDao;
	}

	public void setTaskService(ITaskServiceExternal taskService) {
		this.taskService = taskService;
	}

	public void setWRevisitInfoDao(WRevisitInfoDao revisitInfoDao) {
		wRevisitInfoDao = revisitInfoDao;
	}

	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}

	public void setWTaskServDao(WTaskServDao taskServDao) {
		wTaskServDao = taskServDao;
	}




}
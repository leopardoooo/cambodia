package com.ycsoft.business.component.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.TTaskDetailType;
import com.ycsoft.beans.task.WCustInfo;
import com.ycsoft.beans.task.WPrint;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskServ;
import com.ycsoft.beans.task.WWork;
import com.ycsoft.business.cache.PrintContentConfiguration;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TTemplateDao;
import com.ycsoft.business.dao.task.TTaskDetailTypeDao;
import com.ycsoft.business.dao.task.TTaskTemplatefileDao;
import com.ycsoft.business.dao.task.WCustInfoDao;
import com.ycsoft.business.dao.task.WLogDao;
import com.ycsoft.business.dao.task.WPrintDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskServDao;
import com.ycsoft.business.dao.task.WWorkDao;
import com.ycsoft.business.dto.config.TaskDetailTypeDto;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.business.dto.config.TaskWorkDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.cust.QueryTaskConditionDto;
import com.ycsoft.business.dto.core.cust.QueryTaskResultDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.daos.core.Pager;


/**
 * 工单类型业务组件封装类
 *
 * @author hh
 * @date Mar 4, 2010 4:39:08 PM
 */
@Component
public class TaskComponent extends BaseBusiComponent {

	private TTaskDetailTypeDao tTaskDetailTypeDao;
	private WTaskBaseInfoDao wTaskBaseInfoDao;

	private WWorkDao wWorkDao;
	private WCustInfoDao wCustInfoDao;
	private WPrintDao wPrintDao;
	private WTaskServDao wTaskServDao;

	private TTaskTemplatefileDao tTaskTemplatefileDao;
	private TTemplateDao tTemplateDao;
	/**
	 * 当前地区的工单类型
	 * @return
	 * @throws Exception
	 */
	public List<TaskDetailTypeDto> queryTaskTypes()throws Exception{
		return tTaskDetailTypeDao.getTaskTypes(getOptr().getCounty_id());
	}

	/**
	 * 获取业务代码对应的工单类型
	 * @param busiCode 业务代码
	 * @return
	 * @throws Exception
	 */
	public List<TTaskDetailType> queryTaskTypes(String busiCode)throws Exception{
		return tTaskDetailTypeDao.getTaskTypes(busiCode,getOptr().getCounty_id());
	}
	/**
	 * 生成taskid
	 * @return
	 * @throws Exception
	 */
	public String gTaskId()throws Exception{
		return wWorkDao.findSequence(SequenceConstants.SEQ_TASK).toString();
	}

	/**
	 * 创建业务工单
	 * @param taskIds     业务需要生成的工单数组
	 * @param doneCode    流水号
	 * @param cua         完整客户信息
	 * @param newAddr     客户新地址,只有客户地址发生变动的业务需要传递
	 * @throws Exception
	 */
	public void createTask(String[] taskIds,Integer doneCode,CustFullInfoDto cust,List<CUser> users,String newAddr) throws Exception{
		if (taskIds != null && taskIds.length>0){
			WTaskBaseInfo[] tasks = new WTaskBaseInfo[taskIds.length];
			WTaskBaseInfo tTask = null ;
			for(int i = 0; i< taskIds.length ; i++){
				tTask = new WTaskBaseInfo();
				tTask.setTask_id(gTaskId());
				tTask.setTask_detail_type_id( taskIds[i] );
				tTask.setCust_id(cust.getCust().getCust_id());
				tTask.setDone_code(doneCode);
				//设置工单对应的用户数,如果用户信息为空，则设置用户数为1
				if (users != null && users.size()>0)
					tTask.setUser_count(users.size());
				else
					tTask.setUser_count(1);
				tTask.setCust_name(cust.getCust().getCust_name());
				//设置工单的施工地址
				if (StringHelper.isNotEmpty(newAddr)){
					tTask.setNew_addr(newAddr);
					tTask.setOld_addr(cust.getCust().getAddress());
				} else {
					tTask.setNew_addr(cust.getCust().getAddress());
				}
				tTask.setMobile(cust.getLinkman().getMobile());
				tTask.setTel(cust.getLinkman().getTel());
				setUserType( users , tTask );
				//设置地区县市信息
				tTask.setCounty_id(cust.getCust().getCounty_id());
				tTask.setArea_id(cust.getCust().getArea_id());
				tasks[i] = tTask;
			}
			wTaskBaseInfoDao.save(tasks);
		}
	}
	
	
	/**
	 * 新建工单（系统,手工生成）
	 * @param taskIds
	 * @param doneCode
	 * @param cust
	 * @param users
	 * @param newAddr
	 * @param booksTime
	 * @param taskCustName
	 * @param mobile
	 * @param bugCause
	 * @param remark
	 * @param optr
	 * @throws Exception
	 */
	public void createTask(String[] taskIds,Integer doneCode,CustFullInfoDto cust,List<CUser> users,String newAddr,
			String booksTime, String taskCustName, String mobile, String bugCause,String remark,SOptr optr) throws Exception{
		if (taskIds != null && taskIds.length>0){
			WWork[] tasks = new WWork[taskIds.length];
			List<WCustInfo> wCustList = new ArrayList<WCustInfo>();
			List<WTaskServ> wTaskServs = new ArrayList<WTaskServ>();
			WWork tTask = null ;
			List<SDept> list = wWorkDao.queryInstallerDeptById(cust.getCust().getAddr_id());
			Map<String, TTaskDetailType> tasktypesmap = CollectionHelper.converToMapSingle(tTaskDetailTypeDao.querytask(), "detail_type_id");
			for(int i = 0; i< taskIds.length ; i++){
				//记录工单
				tTask = new WWork();
				String[] taskInfo = taskIds[i].split("#");
				String workId = gTaskId();
				tTask.setWork_id(workId);
				tTask.setTask_type(taskInfo[0] );
				//设置故障单
				if(bugCause != null && !"".equals(bugCause)){
					tTask.setBug_cause(bugCause);
				}else{
					tTask.setBug_cause(tasktypesmap.get(tTask.getTask_type()).getBug_cause());
				}
				tTask.setCreate_type("MANUAL");
				tTask.setBooks_optr(optr.getOptr_id());
				if(StringHelper.isNotEmpty(booksTime))
					tTask.setBooks_time(booksTime);
				tTask.setCreate_done_code(""+doneCode);
				tTask.setRemark(remark);
				tTask.setTask_status(StatusConstants.TASK_CREATE);
				tTask.setCounty_id(cust.getCust().getCounty_id());
				if(list.size() == 1){
					tTask.setAssign_dept(list.get(0).getDept_id());
					tTask.setAssign_time(new Date());
				}
				
				tasks[i] = tTask;
				
				for(int j=1 ; j < taskInfo.length ;j ++){
					wTaskServs.add(new WTaskServ(tTask.getWork_id(), taskInfo[j]));
				}
				
				//记录工单客户信息
				WCustInfo info = new WCustInfo();
				info.setCust_id(cust.getCust().getCust_id());
				info.setTask_cust_name(taskCustName);
				//施工地址
				if (StringHelper.isNotEmpty(newAddr)){
					info.setInstall_addr(newAddr);
					info.setOld_addr(cust.getCust().getAddress());
				}else{
					info.setInstall_addr(cust.getCust().getAddress());
				}
				//联系方式
				if(StringHelper.isNotEmpty(mobile)){
					info.setTel(mobile);
				}else{
					if(StringHelper.isNotEmpty(cust.getLinkman().getMobile())){
						info.setTel(cust.getLinkman().getMobile());
					}else{
						info.setTel(cust.getLinkman().getTel());
					}
				}
				if (users.size()==1)
					info.setUser_id(users.get(0).getUser_id());
				info.setWork_id(workId);
				wCustList.add(info);
			}
			//记录工单操作记录
			saveWorkLog(SystemConstants.WORK_CREATE,tTask.getWork_id().split(","),getOptr(),null);
			
			wTaskServDao.save(wTaskServs.toArray(new WTaskServ[wTaskServs.size()]));
			//记录工单客户信息
			wCustInfoDao.save(wCustList.toArray(new WCustInfo[wCustList.size()]));
			//记录工单基础信息
			wWorkDao.save(tasks);
		}
	}

	/**
	 * 查询打印内容
	 * @return
	 * @throws Exception
	 */
	public String queryPrintContent(String taskDetailType)throws Exception{
		String id = tTemplateDao.getTemplateId(getOptr().getCounty_id(), TemplateConfig.Template.TASK.toString());
		String fileName = tTaskTemplatefileDao.findPrintXml(taskDetailType, id);
		
		//获取打印内容
		String content = PrintContentConfiguration.getTemplate(fileName);
		if(null == content){
			throw new IllegalArgumentException("[" + fileName + "]文件不存在");
		}
		return content;
	}
	
	/**
	 * 查询工单基础信息
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TaskQueryWorkDto queryTaskByTaskId(String taskId) throws Exception {
		TaskQueryWorkDto workDto = new TaskQueryWorkDto();
		workDto = wWorkDao.queryBillTaskByTaskId(taskId);
		//查询服务类型
		workDto.setTaskServList(wTaskServDao.queryTaskServByTaskId(taskId));
		return workDto;
	}
	/**
	 * 记录工单打印时间
	 * @param taskList
	 * @throws Exception
	 */
	public void saveTaskPrintTime(List<WPrint> taskList) throws Exception {
		wPrintDao.save(taskList.toArray(new WPrint[taskList.size()]));
	}
	
	/**
	 * 更新工单状态
	 * @param wList
	 * @throws Exception
	 */
	public void updateWork(List<WWork> wList) throws Exception {
		wWorkDao.update(wList.toArray(new WWork[wList.size()]));
		
		String[] workIds = CollectionHelper.converValueToArray(wList, "work_id"); 
		//记录工单操作
		String info = "工单打印:"+StatusConstants.TASK_CREATE+"->"+StatusConstants.TASK_INIT;
		saveWorkLog(SystemConstants.WORK_CANCEL,workIds,getOptr(),info);
	}
	
	/**
	 * 作废工单
	 * @param doneCode
	 * @throws Exception
	 */
	public void cancelTaskByDoneCode(Integer doneCode) throws Exception{
		List<WWork> list = wWorkDao.queryWorkByDoneCode(doneCode);
		String[] workIds = CollectionHelper.converValueToArray(list, "work_id");
		wWorkDao.cancelTaskByDoneCode(doneCode);
		
		//记录工单操作
		String info = "业务冲正,流水号:"+doneCode;
		saveWorkLog(SystemConstants.WORK_CANCEL,workIds,getOptr(),info);
	}

	/**
	 * 查询客户的工单
	 * @param custId
	 * @return
	 */
	public List<TaskQueryWorkDto> queryTaskByCustId(String custId) throws Exception {
		return wWorkDao.queryTaskByCustId(custId,getOptr().getCounty_id());
	}

	public List<TaskWorkDto> queryTaskType() throws Exception{
		return wWorkDao.getTaskTypes(getOptr().getCounty_id());
	}
	
	public Pager<QueryTaskResultDto> queryTasks(QueryTaskConditionDto cond)throws Exception{
		String dataRight = this.queryDataRightCon();
		return wWorkDao.queryTasks(cond,dataRight, getOptr().getCounty_id());
	}

	private String queryDataRightCon() {
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(),DataRight.QUERY_CUST.toString());
		} catch (Exception e) {
			dataRight=SystemConstants.DEFAULT_DATA_RIGHT;
		}
		if(!SystemConstants.DEFAULT_DATA_RIGHT.equals(dataRight)){
			dataRight = "t."+dataRight.trim();
		}
		return dataRight;
	}
	
	
	/**
	 * @param task_ids
	 */
	public void cancelTask(String[] task_ids,String cancelRemark)throws Exception {
		wWorkDao.cancelTask(task_ids, StatusConstants.TASK_CANCEL,cancelRemark);
	}
	
	/**
	 * @param task_ids
	 */
	public void assignTask(String[] task_ids)throws Exception {
		wWorkDao.assignTask(task_ids, StatusConstants.TASK_INIT);
	}
	
	
	public void setTTaskDetailTypeDao(TTaskDetailTypeDao taskDetailTypeDao) {
		tTaskDetailTypeDao = taskDetailTypeDao;
	}

	public void setWTaskBaseInfoDao(WTaskBaseInfoDao taskBaseInfoDao) {
		wTaskBaseInfoDao = taskBaseInfoDao;
	}

	public void setWWorkDao(WWorkDao workDao) {
		wWorkDao = workDao;
	}

	public void setWCustInfoDao(WCustInfoDao custInfoDao) {
		wCustInfoDao = custInfoDao;
	}

	public void setWLogDao(WLogDao logDao) {
		wLogDao = logDao;
	}

	public void setTTaskTemplatefileDao(TTaskTemplatefileDao taskTemplatefileDao) {
		tTaskTemplatefileDao = taskTemplatefileDao;
	}

	public void setTTemplateDao(TTemplateDao templateDao) {
		tTemplateDao = templateDao;
	}

	public void setWPrintDao(WPrintDao printDao) {
		wPrintDao = printDao;
	}

	public void setWTaskServDao(WTaskServDao taskServDao) {
		wTaskServDao = taskServDao;
	}

	/**
	 * @param taskId
	 * @param success
	 * @param failureCause
	 * @param finishTime
	 */
	public void saveTaskFinish(String taskId, int success, String failureCause,
			Date finishTime) throws Exception{
		WTaskBaseInfo task = new WTaskBaseInfo();
		task.setTask_id(taskId);
		task.setTask_finish_teime(finishTime);
		task.setTask_finish_type(String.valueOf(success));
		task.setRemark(failureCause);
		wTaskBaseInfoDao.save(task);
		
		wWorkDao.updateStatus(new String[]{taskId}, "COMPLETE");
	}

	/**
	 * @param task_id
	 * @param booksTime
	 * @param taskCustName
	 * @param tel
	 * @param remark
	 */
	public void updateTask(String task_id, String booksTime,
			String taskCustName, String tel, String remark,String bugCause) throws Exception{
		
		wWorkDao.updateTask(task_id,booksTime,remark,bugCause);
		wCustInfoDao.updateTask(task_id,taskCustName,tel);
	}

	/**
	 * 查询工单类型，canAddManual =T 手工添加工单进行过滤
	 * @param booleanTrue
	 * @return
	 */
	public List<TTaskDetailType> getTaskType(String canAddManual) throws Exception{
		return tTaskDetailTypeDao.getTaskType(canAddManual);
	}

}

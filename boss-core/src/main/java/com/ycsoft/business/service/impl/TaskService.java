/**
 * 
 */
package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.task.TTaskDetailType;
import com.ycsoft.beans.task.WPrint;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WWork;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.cust.QueryTaskConditionDto;
import com.ycsoft.business.dto.core.cust.QueryTaskResultDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.service.ITaskService;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.helper.StringHelper;

/**
 * @author allex
 * 
 */
@Component
public class TaskService extends BaseBusiService implements ITaskService {

	static final Integer VIRTUAL_BUSI_CODE = 111111;

	public void reAddTask() throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		saveAllPublic(doneCode, getBusiParam());
	}


	public void saveBugTask(String bugCause) throws Exception {
		if (StringHelper.isEmpty(bugCause)) {
			throw new ComponentException("故障原因不能为空!");
		}
		BusiParameter p = getBusiParam();
		Integer doneCode = doneCodeComponent.gDoneCode();
		// 保存流水
		doneCodeComponent
				.saveDoneCode(doneCode, p.getBusiCode(), p.getTask_remark(), p
						.getCust().getCust_id(), p.getSelectedUserIds());
		// 保存故障单
		taskComponent.createTask(p.getTaskIds(), doneCode, p.getCustFullInfo(),
				p.getSelectedUsers(), null, p.getTask_books_time(), p
						.getTask_cust_name(), p.getTask_mobile(), bugCause,p.getTask_remark(),getOptr());
	}


	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.ITaskService#queryPrintContent(java.lang.String[], java.lang.String[], java.lang.String[])
	 */
	public List<Map<String,Object>> queryPrintContent(String []  taskTypes,String[] cust_id, String [] task_id) throws Exception {
		List<WPrint> printList = new ArrayList<WPrint>();
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		List<WWork> wList = new ArrayList<WWork>();
		int rowLen=20;
		for (int i = 0; i < task_id.length; i++) {
			String tpl = taskComponent.queryPrintContent(taskTypes[i]);
			CustFullInfoDto cust = custComponent.searchCustInfoById(cust_id[i]);
			TaskQueryWorkDto task = taskComponent.queryTaskByTaskId(task_id[i]);
			CUser  user = userComponent.queryUserById(task.getUser_id());
			Map<String,Object> map = new HashMap<String,Object>();
			
			//更新工单状态为施工中
			WWork work = new WWork();
			BeanUtils.copyProperties(task, work);
			if(work.getTask_status().equals(StatusConstants.TASK_CREATE) && StringHelper.isNotEmpty(work.getAssign_dept())){
				work.setTask_status(StatusConstants.TASK_INIT);
				wList.add(work);
			}
			
			if(null == cust.getCust()){
				CCust custinfo = new CCust();
				custinfo.setCust_id(task.getCust_id());
				custinfo.setCust_name(task.getCust_name());
				custinfo.setAddress(task.getInstall_addr());
				cust.setCust(custinfo);
				CCustLinkman linkman = new CCustLinkman();
				cust.setLinkman(linkman);
			}
			map.put("tpl", tpl);
			map.put("custInfo", cust);
			map.put("userDto", user);
			
			//如果工单类型是增机,判断是否为第一台开户机，如果是打印为 新装开户
			if(task.getTask_type().equals("ZJ")  &&  null==user){
				user = new UserDto();user.setTerminal_type_text("主终端");
			}
			if(task.getTask_type().equals("ZJ") && isFirstUser(user.getUser_id(),user.getCust_id()) ){
				task.setTask_type("XZKH");
			}
			//由于施工单有两个备注，一个是业务备注，一个是工单备注
			String remark = task.getRemark()==null?"":task.getRemark();
			task.setRemark(remark);
			int num = remark.length()%rowLen;
			//如果备注长度小于rowLen 存在 remark 中
			int rowNum = remark.length()/rowLen;;
			if(num>0 && rowNum>=1){
				rowNum++;
				task.setRemark(task.getRemark().substring(0,rowLen));
			}
			//如果备注长度大于rowLen ，超过部分存在 remarkList 中
			List<Object> remarkList = new ArrayList<Object>();
			WTaskBaseInfo rinfo = null;
			for(int j=2;j<=rowNum;j++){
				rinfo = new WTaskBaseInfo();
				int c = j-1;
				if(j==rowNum){
					rinfo.setRemark(remark.substring(c*rowLen));
				}else{
					rinfo.setRemark(remark.substring((j-1)*rowLen,j*rowLen));
				}
				remarkList.add(rinfo);
			}
			task.setRemarkList(remarkList);
			map.put("task", task);
			
			results.add(map);
			
			WPrint tbi = new WPrint();
			tbi.setWork_id(task.getWork_id());
			tbi.setPrint_time(new Date());
			tbi.setPrint_optr(getOptr().getOptr_id());
			printList.add(tbi);
		}
		taskComponent.saveTaskPrintTime(printList);	//记录打印时间
		//更新状态为施工中
		if(wList.size()>0){
			taskComponent.updateWork(wList);
		}
		return results;
	}
	public boolean isFirstUser(String userId, String custId){
		try {
			String minUserId = userComponent.queryMinUserId(custId);
			if(userId.equals(minUserId)){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public Pager<QueryTaskResultDto> queryTask(QueryTaskConditionDto cond)
			throws Exception {
		return taskComponent.queryTasks(cond);
	}

	public void cancelTask(String[] task_ids,String cancelRemark)
			throws Exception {
		taskComponent.cancelTask(task_ids,cancelRemark);		
	}

	public void assignTask(String[] task_ids)
			throws Exception {
		taskComponent.assignTask(task_ids);
	}
	
	public void saveNewTask() throws Exception {
		BusiParameter p = getBusiParam();
		Integer doneCode = doneCodeComponent.gDoneCode();
		// 保存流水
		doneCodeComponent
				.saveDoneCode(doneCode, p.getBusiCode(), p.getTask_remark(), p
						.getCust().getCust_id(), p.getSelectedUserIds());
		// 保存故障单
		taskComponent.createTask(p.getTaskIds(), doneCode, p.getCustFullInfo(),
				p.getSelectedUsers(), null, p.getTask_books_time(), p
						.getTask_cust_name(), p.getTask_mobile(), p.getTask_bug_cause(),p.getTask_remark(),getOptr());
	}

	public void modifyTask(String task_id, String booksTime,String taskCustName, String tel, String remark, String bugCause) throws Exception {
		taskComponent.updateTask(task_id,booksTime,taskCustName,tel,remark,bugCause);
	}
	
	public List<TTaskDetailType> getTaskType() throws Exception {
		return taskComponent.getTaskType(SystemConstants.BOOLEAN_TRUE);
	}
}

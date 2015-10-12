package com.yaochen.boss.job.component;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.system.SBullentionWorkCount;
import com.ycsoft.beans.system.SBulletinWorktask;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.TaskCustExtInfo;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.beans.task.WTeam;
import com.ycsoft.boss.remoting.cfocn.WorkOrderClient;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.system.SBulletinDao;
import com.ycsoft.business.dao.system.SBulletinWorktaskDao;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.business.dao.task.WTeamDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.core.JDBCException;
@Component
public class TaskComponent extends BaseComponent {
	@Autowired
	private WTaskBaseInfoDao wTaskBaseInfoDao;
	@Autowired
	private WTaskUserDao wTaskUserDao;
	@Autowired
	private WTaskLogDao wTaskLogDao;
	@Autowired
	private SOptrDao sOptrDao;
	@Autowired
	private CCustDao cCustDao;
	@Autowired
	private WTeamDao wTeamDao;
	@Autowired
	private SBulletinWorktaskDao sBulletinWorktaskDao;
	@Autowired
	private SBulletinDao sBulletinDao;
	
	//查找需要执行的工单任务
	public List<WTaskLog> querySynTaskLog() throws Exception{
		return wTaskLogDao.querySynLog();
		
	}
	/**
	 * 从cfocn撤回工单
	 * @param client
	 * @param taskId
	 * @param doneCode
	 * @throws Exception
	 */
	public void cancelTaskService(WorkOrderClient client,String taskId,Integer doneCode) throws Exception{
		WTaskBaseInfo task= wTaskBaseInfoDao.queryForLock(taskId);
		if(task==null){
			throw new Exception("工单不存在");
		}
		if(!task.getTask_status().equals(StatusConstants.TASK_INIT)){
			throw new Exception("工单状态非施工中");
		}
		//调用接口撤回
		client.cancelTaskService(doneCode, taskId);
		//更新工单状态为待派单
		this.updateTaskBaseInfoStatus(taskId, StatusConstants.TASK_CREATE);
		wTaskBaseInfoDao.updateTaskSyncStatus(taskId, StatusConstants.SUCCESS);
	}
	/**
	 * 同步工单给cfocn
	 * @param client
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public boolean sendNewWorkOrder(WorkOrderClient client, String taskId,String cfonTeamId) throws Exception{
		WTaskBaseInfo task=wTaskBaseInfoDao.queryForLock(taskId);
		//工单状态判断
		if(!task.getTask_status().equals(StatusConstants.TASK_CREATE)
				&&!task.getTask_status().equals(StatusConstants.TASK_ENDWAIT)){
			throw new Exception("工单状态非可派单派单");
		}

		if(!cfonTeamId.equals(task.getTeam_id())){
			throw new Exception("非cfocn的工单");
		}
		List<WTaskUser> userList = queryTaskUser(taskId);
		TaskCustExtInfo extInfo = queryCustInfo(task.getCust_id());
		
		boolean sign= client.createTaskService(task, userList, extInfo);
		//更新工单状态为施工中
		this.updateTaskBaseInfoStatus(taskId, StatusConstants.TASK_INIT);
		//同步标志成功
		wTaskBaseInfoDao.updateTaskSyncStatus(taskId, StatusConstants.SUCCESS);
		return sign;
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
	public void updateTaskBaseInfoStatus(String taskId,String status)throws Exception{
		wTaskBaseInfoDao.updateTaskStatus(taskId, status);
	}
	public WTaskBaseInfo queryTaskBaseInfo(String taskId) throws Exception{
		return wTaskBaseInfoDao.findByKey(taskId);
	}
	
	public List<WTaskUser> queryTaskUser(String taskId) throws Exception{
		return wTaskUserDao.queryByTaskId(taskId);
	}
	
	public TaskCustExtInfo queryCustInfo(String custId)throws Exception{
		CCust cust = cCustDao.findByKey(custId);
		String areaCode = wTaskBaseInfoDao.queryTaskProvinceCode(custId);
		SOptr manager = sOptrDao.findByKey(cust.getStr9());
		
		return new TaskCustExtInfo(cust.getCust_no(),areaCode,manager);
	}
	
	public void saveTaskSynResult(WTaskLog log,Result result) throws Exception{
		if(result.isSuccess()){
			log.setSyn_status(StatusConstants.SUCCESS);
		}else if (result.isUndefinedError() || result.isConnectionError()){
			//网络错误或者未知严重错误需要重发,所有不设置已发状态
			log.setSyn_status(StatusConstants.NOT_EXEC);
			wTaskBaseInfoDao.updateTaskSyncStatus(log.getTask_id(), StatusConstants.FAILURE);
		}else {
			log.setSyn_status(StatusConstants.FAILURE);
			wTaskBaseInfoDao.updateTaskSyncStatus(log.getTask_id(), StatusConstants.FAILURE);
		}
		
		log.setError_code(result.getErr());
		log.setError_remark(result.getReason());
		log.setSyn_time(new Date());
		wTaskLogDao.update(log);
		
	}
	/**
	 * 查询公告提醒统计信息
	 */
	public List<SBullentionWorkCount> queryBullentionWorkCount(Date currentTimeStamp)throws Exception{
		String supernetTreamId=this.getTeamId(SystemConstants.TEAM_TYPE_SUPERNET);
		return wTaskBaseInfoDao.queryBullentionWorkCount(supernetTreamId, currentTimeStamp);
	}
	/**
	 * 查询部门的公告配置
	 * @return 
	 * @throws Exception 
	 */
	public List<SBulletinWorktask> queryDeptBullentionConfig() throws Exception{
		return sBulletinWorktaskDao.findAll();
	}
	/**
	 * 更新公告内容和操作员查看信息
	 * @throws Exception 
	 */
	public int updateBullentin(String bullentinId,String bullentinText) throws Exception{
		return sBulletinDao.updateBulletinByWorkTask(bullentinId,bullentinText);
	}
}

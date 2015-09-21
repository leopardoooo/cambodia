package com.yaochen.boss.job.component;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.TaskCustExtInfo;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;

public class TaskComponent extends BaseComponent {
	@Autowired
	private WTaskBaseInfoDao wTaskBaseInfoDao;
	@Autowired
	private WTaskUserDao wTaskUserDao;
	@Autowired
	private WTaskLogDao wTaskLogDao;
	@Autowired
	private SOptrDao SOptrDao;
	@Autowired
	private CCustDao cCustDao;
	
	//查找需要执行的工单任务
	public List<WTaskLog> querySynTaskLog() throws Exception{
		return wTaskLogDao.querySynLog();
		
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
		}else {
			log.setSyn_status(StatusConstants.FAILURE);
		}
		
		log.setError_code(result.getErr());
		log.setError_remark(result.getReason());
		log.setSyn_time(new Date());
		wTaskLogDao.save(log);
		
	}
}

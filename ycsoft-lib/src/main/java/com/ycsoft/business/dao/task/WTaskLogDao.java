package com.ycsoft.business.dao.task;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskLog;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class WTaskLogDao extends BaseEntityDao<WTaskLog> {

	private static final long serialVersionUID = 8483966061975110281L;
	
	public List<WTaskLog> queryByTaskId(String taskId) throws JDBCException {
		String sql = "select * from w_task_log where task_id=? order by log_sn desc";
		return this.createQuery(sql, taskId).list();
	}
	
	public List<WTaskLog> queryUnSynLogByTaskId(String taskId) throws JDBCException{
		String sql = "select * from w_task_log where task_id=?  and syn_status=? ";
		return this.createQuery(sql, taskId,StatusConstants.NOT_EXEC).list();
	}
	
	public void updateUnSynLogToNone(String taskId,String remark) throws JDBCException{
		String sql=" update w_task_log set syn_status=? ,error_remark=? where task_id=? and syn_status=? ";
		this.executeUpdate(sql, StatusConstants.INVALID,remark,taskId,StatusConstants.NOT_EXEC);
	}
	public List<WTaskLog> querySynLog() throws JDBCException {
		String sql = " select * from (select * from w_task_log t where "
				+" syn_status =? "+
				" and  (t.delay_time is  null or t.delay_time<=0 or (t.delay_time>0 and t.log_time <sysdate-t.delay_time/(24*60))) "
				+" order by t.log_sn ) where rownum<500 ";
		return this.createQuery(sql, StatusConstants.NOT_EXEC).list();
	}
	
	public void updateExecResult(WTaskLog log) throws JDBCException{
		String sql=" update w_task_log t set syn_status=? ,error_code=? ,error_remark=?,syn_time=? where log_sn=? ";
		this.executeUpdate(sql, log.getSyn_status(),log.getError_code(),log.getError_remark(),log.getSyn_time(),log.getLog_sn());
	}
}

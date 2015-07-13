package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.report.bean.RepTask;
@Component
public class RepTaskDao extends BaseEntityDao<RepTask> {
	public RepTaskDao(){}
	
	public void updateKeylist(Integer taskId, String keylist) throws ReportException {
		try {
			String sql = "update rep_task set keylist=? where task_id=?";
			this.executeUpdate(sql, keylist, taskId);
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	
	/**
	 * 查询几天前的已执行的一次性任务
	 * @throws ReportException 
	 */
	public List<RepTask> queryTaskTypeIsOne(Integer day) throws ReportException{
		
		String sql="select * from  rep_task where task_type='one' and exec_end_time is not null and (sysdate-trunc(exec_end_time))>? ";
		try {
			return this.createQuery(sql, day).list();
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	
	/**
	 * 查询所有任务
	 * @param query
	 * @param start
	 * @param limit
	 * @return
	 * @throws ReportException
	 */
	public Pager<RepTask> queryAllTask(String query, Integer start, Integer limit) throws ReportException{
		try {
			String sql=StringHelper.append(
				"select  t.*,",
				" (select op.optr_name from s_optr op where op.optr_id=t.optr_id) optr_name,",
				" (select si.item_name from busi.s_itemvalue si where si.item_key='REPORT_TASK_STATUS' and si.item_value=t.status) status_text,",
				" (select si.item_name from busi.s_itemvalue si where si.item_key='REPORT_TASK_TYPE' and si.item_value=t.task_type) task_type_text,",
				" case when (t.exec_end_time is null or t.exec_end_time<trunc(sysdate)) ",
				" and t.task_type<>'one' and t.status <>'exec' then 'T' else 'F' end is_waitexec ",
			    " from rep_task t where 1=1");
		
			if(StringHelper.isNotEmpty(query)){
				sql += " and (task_name like '%"+query+"%' or rep_name like '%"+query+"%') ";
			}
			sql += " order by task_id desc";
			return this.createQuery(sql).setStart(start).setLimit(limit).page();
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}

	/**
	 * 保存执行结果
	 * @param task
	 * @throws ReportException
	 */
	public void updateResult(RepTask task) throws ReportException{
		try {
			RepTask update=new RepTask();
			update.setTask_id(task.getTask_id());
			update.setExec_query_id(task.getExec_query_id());
			update.setExec_result(task.getExec_result());
			update.setExec_start_time(task.getExec_start_time());
			update.setExec_end_time(task.getExec_end_time());
			update.setStatus(task.getStatus());
			this.update(update);
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	
	/**
	 * 查询需要执行的任务
	 * @throws JDBCException 
	 */
	public RepTask queryExecTask() throws JDBCException{
		String sql=StringHelper.append("select * from busi.rep_task t ",
				" where  (t.exec_end_time is null or t.exec_end_time<trunc(sysdate)) ",
				" and (t.task_type='day' ",
				" or (t.task_type='week'  and t.task_execday=to_char(sysdate,'D')         ) ",
				" or (t.task_type='month' and t.task_execday=to_char(sysdate,'DD')        ) ",
				" or (t.task_type='one'   and t.task_execday=to_char(sysdate,'yyyy-mm-dd')) ",
				" ) order by task_id");
		return this.createQuery(sql).first();
	}
	
}

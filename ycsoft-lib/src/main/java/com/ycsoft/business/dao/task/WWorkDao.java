/**
 * WWorkDao.java	2013/08/23
 */
 
package com.ycsoft.business.dao.task; 

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.task.WWork;
import com.ycsoft.business.dto.config.TaskQueryConditionDto;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.business.dto.config.TaskWorkDto;
import com.ycsoft.business.dto.core.cust.QueryTaskConditionDto;
import com.ycsoft.business.dto.core.cust.QueryTaskResultDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * WWorkDao -> W_WORK table's operator
 */
@Component
public class WWorkDao extends BaseEntityDao<WWork> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1158829798775588692L;

	/**
	 * default empty constructor
	 */
	public WWorkDao() {}

	/**
	 * 查询客户的工单
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public List<TaskQueryWorkDto> queryTaskByCustId(String custId, String countyId) throws JDBCException {
		String sql = " select t.*,t2.installer_time,t1.cust_id from w_work t,w_cust_info t1,w_revisit_info t2 where t.work_id =t1.work_id and t1.work_id =t2.work_id(+) and t1.cust_id=? and  t.county_id =?  order by t.create_time desc";
		return createQuery(TaskQueryWorkDto.class,sql, custId,countyId).list();
	}
	
	
	public List<TaskWorkDto> getTaskTypes(String countyId)throws Exception {
		String sql = "SELECT t2.detail_type_id task_type, t2.busi_code  "
				+ " FROM t_template_county t1, t_busi_code_task t2"
				+ " WHERE  t2.template_id = t1.template_id   and t1.county_id = ?"
				+ " and t1.template_type = ?";
		return createQuery(TaskWorkDto.class, sql, countyId,
				SystemConstants.TEMPLATE_TYPE_TASK).list();
	}
	
	/**
	 * 根据条件查询工单
	 * @param string 
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public Pager<TaskQueryWorkDto> queryTasks(TaskQueryConditionDto cond, String dataRight, String countyId) throws JDBCException {
		String sql = "select t.*,t1.installer_dept,t1.installer_optr,t1.installer_time, t2.cust_id,t2.user_id," +
				" t2.install_addr,t2.task_cust_name,t2.tel,t2.old_addr,t2.net_type, t4.busi_code,t3.cust_no,addr.addr_pid addr_pid " +
				" from w_work t,w_revisit_info t1, w_cust_info t2, c_cust t3,c_done_code t4,t_address addr " +
				" where t.work_id = t2.work_id and t.work_id = t1.work_id(+) and t2.cust_id = t3.cust_id  and addr.addr_id=t3.addr_id " +
				" and t.create_done_code = t4.done_code and t.county_id = t3.county_id(+) "
				+"  AND t.create_time >= to_date(?, 'yyyy/MM/dd') "
				+"  AND t.create_time < to_date(?, 'yyyy/MM/dd') ";
		List<Object> params = new ArrayList<Object>();
		params.add(cond.getCStart());
		params.add(cond.getCEnd());
		
		if(SystemConstants.DEFAULT_DATA_RIGHT.equals(dataRight)){
			sql += " and t.county_id=? ";
			
		}else{
			sql += " and (t.county_id=? or "+dataRight+")";
		}
		params.add(countyId);
		
		if(StringHelper.isNotEmpty(cond.getStatus())){
			sql += "  AND T.TASK_STATUS = ? ";
			params.add(cond.getStatus());
		}
		if(StringHelper.isNotEmpty(cond.getCustName())){
			sql += "  AND t2.cust_name like ? ";
			params.add("%" + cond.getCustName() + "%");
		}
		if(StringHelper.isNotEmpty(cond.getNewAddr())){
			sql += "  AND t2.install_addr like ? ";
			params.add("%" + cond.getNewAddr() + "%");
		}
		if(StringHelper.isNotEmpty(cond.getTel())){
			sql += "  AND t2.tel like ? ";
			params.add("%" +cond.getTel()+ "%");
		}
		if(StringHelper.isNotEmpty(cond.getCustNo())){
			sql += "  AND t3.cust_no = ? ";
			params.add(cond.getCustNo());
		}
		if(StringHelper.isNotEmpty(cond.getInstallDept())){
			sql += "  AND  t.assign_dept=? ";
			params.add(cond.getInstallDept());
		}
		if(StringHelper.isNotEmpty(cond.getTaskType())){
			sql += "  AND  t.task_type=? ";
			params.add(cond.getTaskType());
		}
		sql += " ORDER BY create_time DESC ";
		return this.createQuery(TaskQueryWorkDto.class, sql, params.toArray(new Object[params.size()]))
			.setLimit(cond.getLimit())
			.setStart(cond.getStart())
			.page();
	}
	
	public List<SDept> queryInstallerDeptById(String addrId) throws Exception{
		final String sql = "select t2.* from s_dept_addr t,t_address t1,s_dept t2 " +
				" where t.addr_id = t1.addr_pid and t.dept_id =t2.dept_id and t2.dept_type in (?,?) and t1.addr_id = ? ";
		return createQuery(SDept.class, sql,SystemConstants.DEPT_TYPE_FGS,SystemConstants.DEPT_TYPE_KFB,addrId).list();
	}
	
	public List<SItemvalue> queryInstallerDept() throws Exception{
		final String sql = "SELECT dept_id item_value , dept_name item_name FROM s_dept t" +
				" WHERE t.dept_type in ('"+SystemConstants.DEPT_TYPE_FGS+"','"+SystemConstants.DEPT_TYPE_KFB+"') order by dept_id desc  ";
		return createQuery(SItemvalue.class, sql).list();
	}
	public List<SItemvalue> queryInstaller(String team)throws Exception {
		String sql = " SELECT t.optr_id item_value, t.optr_name item_name FROM s_optr t WHERE t.dept_id=? and t.status = ? ";
		return createQuery(SItemvalue.class, sql , team,StatusConstants.ACTIVE).list();
	}
	
	public TaskQueryWorkDto queryBillTaskByTaskId(String taskId) throws JDBCException {
		String sql = "select t.*,t1.cust_id,t1.user_id,t1.install_addr,t1.task_cust_name,t1.tel,t1.old_addr,t1.net_type" +
				"  from w_work t,  w_cust_info t1 where t.work_id = t1.work_id and t.work_id = ? ";
		return createQuery(TaskQueryWorkDto.class,sql, taskId).list().get(0);
	}
	
	public int modifyBooksTime(String task_id, String newPlanTime)throws Exception {
		String sql = "UPDATE w_work t set t.books_time= ? WHERE t.work_id=?";
		return executeUpdate(sql, newPlanTime, task_id);
	}

	public TaskQueryWorkDto queryaskByTaskId(String workId) throws Exception {
		String sql = "select t.*, t1.installer_dept,t1.installer_optr,t1.succeed,t1.fail_remark,t1.satisfaction,t1.satisfaction_remak," +
				" t1.fail_cause,t1.revisit_optr,t1.revisit_create_time,t1.installer_time, t2.cust_id,t2.user_id,t2.install_addr," +
				" t2.task_cust_name,t2.tel,t2.old_addr,t2.net_type," +
				" t3.cust_no,t3.cust_name, t4.dept_id,t4.busi_code  " +
				" from w_work t,w_revisit_info t1, w_cust_info t2, c_cust t3,c_done_code t4 " +
				" where  t.work_id = t2.work_id and t.work_id = t1.work_id(+) and t2.cust_id = t3.cust_id and t.create_done_code = t4.done_code " +
				" and t.county_id = t3.county_id(+) and t4.county_id = t.county_id and t.work_id = ? ";
		return createQuery(TaskQueryWorkDto.class,sql,workId).list().get(0);
	}
	
	public List<WWork> queryWork(String[] task_ids)throws Exception {
		String sql = "select * from w_work where "+getSqlGenerator().setWhereInArray("work_id",task_ids)+"";
		return createQuery(sql).list();
	}
	
	public void updateStatus(String[] task_ids, String status)throws Exception {
		String sql = "UPDATE w_work t SET t.task_status=?  WHERE t.work_id=?";
		List<Object[]> params = new ArrayList<Object[]>();
		
		Object[] o = null;
		for(int i =0 ;i<task_ids.length; i++){
			o = new Object[2];
			o[0] = status;
			o[1] = task_ids[i];
			params.add(o);
		}
		executeBatch(sql, params);
	}
	
	public List<WWork> queryWorkByDoneCode(Integer doneCode)throws Exception {
		String sql = "select * from w_work where create_done_code=? ";
		return createQuery(sql,doneCode).list();
	}
	
	public void cancelTaskByDoneCode(Integer doneCode) throws JDBCException {
		String sql = "UPDATE w_work t SET t.task_status=?  where t.create_done_code=?";

		executeUpdate(sql, StatusConstants.TASK_CANCEL,doneCode);
	}

	public List<TaskQueryWorkDto> queryUnSyncWork(Integer num) throws JDBCException {
		String sql = "select t.*,t1.installer_dept,t1.installer_optr,t1.installer_time, t2.cust_id,t2.user_id," +
		" t2.install_addr,t2.task_cust_name,t2.tel,t2.old_addr,t2.net_type, t4.busi_code,t3.cust_no,t3.cust_name,addr.addr_pid addr_pid,t5.task_type_id  " +
		" from w_work t,w_revisit_info t1, w_cust_info t2, c_cust t3,c_done_code t4,t_address addr ,t_task_detail_type t5" +
		" where t.work_id = t2.work_id and t.work_id = t1.work_id(+) and t2.cust_id = t3.cust_id and addr.addr_id=t3.addr_id and  t5.detail_type_id=t.task_type" +
		" and t.create_done_code = t4.done_code and t.county_id = t3.county_id(+) and t.task_status = 'INIT'  and t.sync_status is null and  ROWNUM <= ?  order by t.work_id ";
		return createQuery(TaskQueryWorkDto.class,sql,num).list();		
	}

	public int syncWork(String workid,String syncStatus) throws JDBCException {
		String sql = "UPDATE w_work t SET t.sync_status=?  where t.work_id=?";
		return executeUpdate(sql,syncStatus, workid);
	}

	
	public void cancelTask(String[] task_ids, String status,String cancelRemark)throws Exception {
		String sql = "UPDATE w_work t SET t.task_status=?,t.remark= t.remark||'|'||?  WHERE t.work_id=?";
		List<Object[]> params = new ArrayList<Object[]>();
		
		Object[] o = null;
		for(int i =0 ;i<task_ids.length; i++){
			o = new Object[3];
			o[0] = status;
			o[1] = cancelRemark;
			o[2] = task_ids[i];
		
			params.add(o);
		}
		executeBatch(sql, params);
	}
	public void assignTask(String[] task_ids, String status)throws Exception {
		String sql = "UPDATE w_work t SET t.task_status=?,assign_time=sysdate  WHERE t.work_id=?";
		List<Object[]> params = new ArrayList<Object[]>();
		
		Object[] o = null;
		for(int i =0 ;i<task_ids.length; i++){
			o = new Object[2];
			o[0] = status;			
			o[1] = task_ids[i];
			params.add(o);
		}
		executeBatch(sql, params);
	}

	public Pager<QueryTaskResultDto> queryTasks(QueryTaskConditionDto cond,
			String dataRight, String countyId) throws Exception {
		String sql = "select t.*,wc.cust_id,c.cust_no,wc.install_addr,wc.tel,wc.task_cust_name,cd.optr_id,cd.busi_code "
				+ " from w_work t, w_cust_info wc, C_CUST c, c_done_Code cd "
				+ " where t.work_id = wc.work_id and wc.cust_id = c.cust_id and  t.create_done_code = cd.done_code "
				+ " and t.county_id = c.county_id"
				+ "  AND t.create_time >= to_date(?, 'yyyy-MM-dd') "
				+ "  AND t.create_time < to_date(?, 'yyyy-MM-dd') ";
		List<Object> params = new ArrayList<Object>();
		params.add(cond.getStartTime());
		params.add(cond.getEndTime());
		
		
		if(SystemConstants.DEFAULT_DATA_RIGHT.equals(dataRight)){
			sql += " and t.county_id=? ";
			
		}else{
			sql += " and (t.county_id=? or "+dataRight+")";
		}
		params.add(countyId);
		
		if(StringHelper.isNotEmpty(cond.getStatus())){
			sql += "  AND T.TASK_STATUS = ? ";
			params.add(cond.getStatus());
		}
		if(StringHelper.isNotEmpty(cond.getLinkman())){
			sql += "  AND wc.task_cust_name like ? ";
			params.add("%" + cond.getLinkman() + "%");
		}
		if(StringHelper.isNotEmpty(cond.getAddr())){
			sql += "  AND wc.install_addr like ? ";
			params.add("%" + cond.getAddr() + "%");
		}
		if(StringHelper.isNotEmpty(cond.getMobile())){
			sql += "  AND wc.tel like ? ";
			params.add("%" +cond.getMobile()+ "%");
		}
		if(StringHelper.isNotEmpty(cond.getCustNo())){
			sql += "  AND c.cust_no = ? ";
			params.add(cond.getCustNo());
		}		
		if(StringHelper.isNotEmpty(cond.getTaskType())){
			sql += "  AND  t.task_type =? ";
			params.add(cond.getTaskType());
		}
		sql += " ORDER BY create_time DESC ";
		return this.createQuery(QueryTaskResultDto.class, sql, params.toArray(new Object[params.size()]))
			.setLimit(cond.getLimit())
			.setStart(cond.getStart())
			.page();
	}

	public void updateTask(String task_id, String booksTime, String remark,String bugCause) throws Exception{
		String sql = "UPDATE w_work t SET t.books_time=?,t.remark = ?,t.bug_cause =?  where t.work_id=?";
		this.executeUpdate(sql, booksTime,remark,bugCause,task_id);
	}
	
	
}

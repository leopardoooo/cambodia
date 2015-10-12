/**
 * WTaskBaseInfoDao.java	2010/03/16
 */

package com.ycsoft.business.dao.task;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.business.dto.config.TaskBaseInfoDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * WTaskBaseInfoDao -> W_TASK_BASE_INFO table's operator
 */
@Component
public class WTaskBaseInfoDao extends BaseEntityDao<WTaskBaseInfo> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4530530259017878299L;

	/**
	 * default empty constructor
	 */
	public WTaskBaseInfoDao() {}
	/**
	 * 加锁查询
	 * @param taskId
	 * @return
	 * @throws JDBCException
	 */
	public WTaskBaseInfo queryForLock(String taskId) throws JDBCException{
		String sql=" select * from w_task_base_info where task_id=? for update ";
		return this.createQuery(sql, taskId).first();
	}

	public void updateTaskStatus(String taskId,String status) throws JDBCException{
		String sql="update w_task_base_info set task_status=? ,task_status_date=sysdate where task_id=? ";
		this.executeUpdate(sql, status,taskId);
	}
	
	public void updateTaskSyncStatus(String taskId,String status)throws JDBCException{
		String sql="update w_task_base_info set sync_status=? ,sync_status_date=sysdate where task_id=? ";
		this.executeUpdate(sql, status,taskId);
	}
	/**
	 * 查询客户的工单
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public List<TaskBaseInfoDto> queryTaskByCustId(String custId) throws JDBCException {
		String sql = " select t.* " +
				" from w_task_base_info t  " +
				" where  t.cust_id=?  order by t.task_create_time desc";
		return createQuery(TaskBaseInfoDto.class,sql, custId).list();
	}

	/**
	 * @param doneCode
	 */
	public void delete(Integer doneCode) throws JDBCException {
		String sql = "delete w_task_base_info where done_code=?";

		executeUpdate(sql, doneCode);

	}

	public Pager<TaskBaseInfoDto> queryTask(String taskTypes, String addrIds, String beginDate, String endDate,
			String taskId, String teamId, String status, String custNo, String custName, String custAddr,String mobile, String zteStatus, Integer start, Integer limit) throws Exception {
		
		String sql = "select t.*,wt.team_type,case when s.tel is null and s.mobile is null then '' "
				+ " when s.mobile is null then  s.tel  "
				+ " when s.tel is null then s.mobile  else s.tel||','||s.mobile end linkman_tel ,c.cust_name linkman_name "
				+ " from w_task_base_info t, C_CUST c ,w_team wt,s_optr s "+(StringHelper.isEmpty(addrIds)?"":", t_district td, t_address ta,t_province tp")
				+ " where t.cust_id = c.cust_id and c.str9 = s.optr_id(+) and t.team_id = wt.dept_id(+) "+(StringHelper.isEmpty(addrIds)?"":" and c.addr_id = ta.addr_id "
				+ "and ta.district_id = td.district_id  and td.province_id=tp.id and  tp.id in ("+sqlGenerator.in(addrIds.split(","))+")") ;
		List<Object> params = new ArrayList<Object>();
		
		if(StringHelper.isNotEmpty(beginDate)){
			sql += " AND t.task_create_time >= to_date(?, 'yyyy-MM-dd') ";
			params.add(beginDate);
		}
		if(StringHelper.isNotEmpty(endDate)){
			sql += " AND t.task_create_time < to_date(?, 'yyyy-MM-dd') ";
			params.add(endDate);
		}	
		if(StringHelper.isNotEmpty(taskTypes)){
			sql += "  AND  t.task_type_id in ("+sqlGenerator.in(taskTypes.split(","))+")";
		}
		if(StringHelper.isNotEmpty(zteStatus)){
			sql += "  AND  t.zte_status in ("+sqlGenerator.in(zteStatus.split(","))+")";
		}
		if(StringHelper.isNotEmpty(status)){
			sql += "  AND T.TASK_STATUS in ("+sqlGenerator.in(status.split(","))+")";
		}
		if(StringHelper.isNotEmpty(taskId)){
			sql += "  AND t.task_id = ? ";
			params.add(taskId);
		}
		if(StringHelper.isNotEmpty(teamId)){
			sql += "  AND t.team_id in ("+sqlGenerator.in(teamId.split(","))+")";
		}
		if(StringHelper.isNotEmpty(custNo)){
			sql += "  AND c.cust_no = ? ";
			params.add(custNo);
		}
		if(StringHelper.isNotEmpty(custName)){
			sql += "  AND t.cust_name = ? ";
			params.add(custName);
		}		
		if(StringHelper.isNotEmpty(mobile)){
			sql += "  AND t.tel like ? ";
			params.add("%" +mobile+ "%");
		}
		if(StringHelper.isNotEmpty(custAddr)){
			sql += "  AND t.old like ? ";
			params.add("%" +custAddr+ "%");
		}
		
		sql += " ORDER BY t.task_create_time DESC ";
		return this.createQuery(TaskBaseInfoDto.class,sql, params.toArray(new Object[params.size()]))
			.setLimit(limit)
			.setStart(start)
			.page();
	}
	
	public String queryTaskProvinceCode(String custId) throws JDBCException{
		String sql = "select d.task_code "+
					"  from c_cust a, t_address b, t_district c, t_province d "+
					" where a.addr_id = b.addr_id "+
					"   and b.district_id = c.district_id "+
					"   and c.province_id = d.id "+
					"   and a.cust_id = ?";
		return this.findUnique(sql, custId);
	}

	public List<WTaskBaseInfo> queryTaskByDoneCode(Integer doneCode) throws JDBCException{
		String sql = "select * from w_task_base_info where done_code = ?";
		return this.createQuery(sql,doneCode).list();
	}

	public Pager<TaskBaseInfoDto> queryUnProcessTask(String deptId, Integer start,
			Integer limit) throws JDBCException {
		
		String sql = "select t.* ,wt.team_type,case when s.tel is null and s.mobile is null then '' "
				+ " when s.mobile is null then  s.tel  "
				+ " when s.tel is null then s.mobile  else s.tel||','||s.mobile end linkman_tel ,c.cust_name linkman_name "
				+ " from w_task_base_info t, C_CUST c  ,w_team wt,s_optr s "
				+ " where t.cust_id = c.cust_id and c.str9 = s.optr_id(+) and t.team_id = wt.dept_id(+) " 
				+" and (( t.task_status =? and t.team_id =?) or t.task_status=? or (t.task_status=? and t.zte_status=?)) "
				+" ORDER BY t.task_create_time DESC ";
		return this.createQuery(TaskBaseInfoDto.class,sql,StatusConstants.TASK_CREATE,deptId,StatusConstants.TASK_ENDWAIT
				,StatusConstants.TASK_INIT,StatusConstants.NOT_EXEC)
			.setLimit(limit)
			.setStart(start)
			.page();
	}

	
	/**
	 * 查询一个部门相关的新增派单
	 * @throws JDBCException 
	 */
	public int queryStatusAddCntByDeptTimeStamp(String deptId,Date currentTimeStamp,String status) throws JDBCException{
		String sql="select count(1) from w_task_base_info t,s_optr op "
					+" where t.task_status=? and t.task_status_date>? "
					+" and t.optr_id=op.optr_id and op.dept_id=?";
		//this.findUnique(sql, params)
		return this.count(sql, status,currentTimeStamp,deptId);
	}
	/**
	 * 查询一个部门相关的工单新增派单同步失败
	 */
	public int querySyncErrorAddCntByDeptTimeStamp(String deptId,Date currentTimeStamp)throws JDBCException{
		String sql="select count(1) from w_task_base_info t,s_optr op "
				+" where t.task_status=? and t.sync_status=? and t.sync_status_date>? "
				+" and t.optr_id=op.optr_id and op.dept_id=?";
		return this.count(sql, StatusConstants.TASK_CREATE,StatusConstants.FAILURE,currentTimeStamp,deptId);
	}

}

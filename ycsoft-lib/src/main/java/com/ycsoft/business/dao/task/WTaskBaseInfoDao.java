/**
 * WTaskBaseInfoDao.java	2010/03/16
 */

package com.ycsoft.business.dao.task;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskBaseInfo;
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
	 * 查询客户的工单
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public List<WTaskBaseInfo> queryTaskByCustId(String custId, String countyId) throws JDBCException {
		String sql = " select t.*,tt.task_type_name,ttd.detail_type_name task_detail_type_name " +
				" from w_task_base_info t ,t_task_detail_type ttd,t_task_type tt  " +
				" where ttd.task_type_id=tt.task_type_id and ttd.detail_type_id =t.task_detail_type_id  " +
				" and t.cust_id=? and t.county_id=? order by t.task_create_time desc";
		return createQuery(sql, custId,countyId).list();
	}

	/**
	 * @param doneCode
	 */
	public void delete(Integer doneCode) throws JDBCException {
		String sql = "delete w_task_base_info where done_code=?";

		executeUpdate(sql, doneCode);

	}

	public Pager<WTaskBaseInfo> queryTask(String taskTypes, String addrIds, String beginDate, String endDate,
			String taskId, String teamId, String status, String custNo, String custName, String custAddr,String mobile, Integer start, Integer limit) throws Exception {
		String sql = "select t.* "
				+ " from w_task_base_info t, C_CUST c "
				+ " where t.cust_id = c.cust_id "
				+ "  AND t.task_create_time >= to_date(?, 'yyyy-MM-dd') "
				+ "  AND t.task_create_time < to_date(?, 'yyyy-MM-dd') ";
		List<Object> params = new ArrayList<Object>();
		params.add(beginDate);
		params.add(endDate);
		
		if(StringHelper.isNotEmpty(taskTypes)){
			sql += "  AND  t.task_type_id in ("+sqlGenerator.in(taskTypes.split(","))+")";
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
		return this.createQuery(sql, params.toArray(new Object[params.size()]))
			.setLimit(limit)
			.setStart(start)
			.page();
	}

}

/**
 * WTaskBaseInfoDao.java	2010/03/16
 */

package com.ycsoft.business.dao.task;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


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

}

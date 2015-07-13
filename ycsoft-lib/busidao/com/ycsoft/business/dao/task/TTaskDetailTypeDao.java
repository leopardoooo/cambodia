/**
 * TTaskDetailTypeDao.java	2010/03/04
 */

package com.ycsoft.business.dao.task;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.TTaskDetailType;
import com.ycsoft.business.dto.config.TaskDetailTypeDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;

/**
 * TTaskDetailTypeDao -> T_TASK_DETAIL_TYPE table's operator
 */
@Component
public class TTaskDetailTypeDao extends BaseEntityDao<TTaskDetailType> {

	/**
	 *
	 */
	private static final long serialVersionUID = -44013093603054958L;

	/**
	 * default empty constructor
	 */
	public TTaskDetailTypeDao() {
	}

	/**
	 * 通过业务代码及县市编号，获取对应的施工单类型
	 *
	 * @param countyId
	 *            县市编号
	 * @throws Exception
	 */
	public List<TaskDetailTypeDto> getTaskTypes(String countyId)
			throws Exception {
		String sql = "SELECT t3.*, t2.busi_code  "
				+ " FROM t_template_county t1, t_busi_code_task t2, t_Task_Detail_Type t3"
				+ " WHERE t3.detail_type_id = t2.detail_type_id "
				+ " and t2.template_id = t1.template_id   and t1.county_id = ?"
				+ " and t1.template_type = ?";
		return createQuery(TaskDetailTypeDto.class, sql, countyId,
				SystemConstants.TEMPLATE_TYPE_TASK).list();
	}

	/**
	 * 通过业务代码及县市编号，获取对应的施工单类型
	 *
	 * @param busiCode
	 *            业务代码
	 * @param countyId
	 *            县市编号
	 * @throws Exception
	 */
	public List<TTaskDetailType> getTaskTypes(String busiCode, String countyId)
			throws Exception {
		String sql = "SELECT * FROM t_Task_Detail_Type t3 "
				+ " WHERE t3.detail_type_id in( "
				+ "   SELECT t2.detail_type_id "
				+ "      FROM t_busi_code_task t2 "
				+ "     WHERE t2.busi_code = ? "
				+ "       and t2.template_id in "
				+ "           (SELECT t1.template_id "
				+ "              FROM t_template_county t1 "
				+ "             WHERE t1.county_id = ? "
				+ "               and t1.template_type = ?)) ";
		return createQuery(sql, busiCode, countyId,
				SystemConstants.TEMPLATE_TYPE_TASK).list();
	}
	public List<TTaskDetailType> querytask()throws Exception {
		return findList(" select * from t_task_detail_type");
	}

	public List<TTaskDetailType> getTaskType(String canAddManual) throws Exception{
		String sql = "select * from t_task_detail_type where can_add_manual = ? ";
		return createQuery(sql, canAddManual).list();
	}
}

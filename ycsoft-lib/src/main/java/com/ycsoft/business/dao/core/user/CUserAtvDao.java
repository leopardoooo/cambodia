/**
 * CUserAtvDao.java	2010/03/17
 */

package com.ycsoft.business.dao.core.user;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CUserAtv;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CUserAtvDao -> C_USER_ATV table's operator
 */
@Component
public class CUserAtvDao extends BaseEntityDao<CUserAtv> {


	private static final long serialVersionUID = -3605706965402342614L;

	/**
	 * default empty constructor
	 */
	public CUserAtvDao() {
	}


	/**
	 * 根据ID查询模拟用户信息
	 * @param userId
	 * @return
	 */
	public CUserAtv queryAtvById(String userId) throws JDBCException{
		String sql = "select * from c_user_atv d,c_user u where d.user_id =u.user_id and " +
				" d.user_id =?";
		return createQuery(sql, userId).first();
	}

	/**
	 * 根据客户ID查询模拟用户信息
	 * @param custId
	 * @return
	 */
	public List<CUserAtv> queryAtvByCustId(String custId) throws JDBCException{
		String sql = "select * from c_user_atv d,c_user u where d.user_id =u.user_id and " +
				" u.cust_id = ?";
		List<CUserAtv> users = createQuery(sql, custId).list();
		return users;
	}
	
	public List<CUserAtv> queryAtvHisByCustId(String custId) throws JDBCException{
		String sql = "select * from c_user_atv_his d,c_user_his u where d.user_id =u.user_id and " +
				" u.cust_id = ?";
		List<CUserAtv> users = createQuery(sql, custId).list();
		return users;
	}
	
	public List<CUserAtv> queryAtvByUserIds(String[] userIds) throws JDBCException{
		String sql = "select * from c_user_atv d,c_user u where d.user_id =u.user_id and ( "+getSqlGenerator().setWhereInArray("u.user_id",userIds)+")";
		List<CUserAtv> users = createQuery(sql).list();
		return users;
	}
	
	public List<CUserAtv> queryAtvHisByUserIds(String[] userIds) throws JDBCException{
		String sql = "select * from c_user_atv_his d,c_user_his u where d.user_id =u.user_id and ( "+getSqlGenerator().setWhereInArray("u.user_id",userIds)+")";
		List<CUserAtv> users = createQuery(sql).list();
		return users;
	}
}

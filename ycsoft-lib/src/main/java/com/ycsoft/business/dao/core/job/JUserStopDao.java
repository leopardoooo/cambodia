/**
 * JUserStopDao.java	2010/06/08
 */

package com.ycsoft.business.dao.core.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JUserStopDao -> J_USER_STOP table's operator
 */
@Component
public class JUserStopDao extends BaseEntityDao<JUserStop> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2345715193938087952L;

	/**
	 * default empty constructor
	 */
	public JUserStopDao() {}

	/**
	 * @param doneCode
	 * @return
	 */
	public List<JUserStop> queryByDoneCode(Integer doneCode) throws Exception{
		String sql = "select * from J_USER_STOP where done_code =?";
		return createQuery(sql, doneCode).list();
	}
	public List<JUserStop> queryStopByUserId(String userId) throws Exception{
		String sql = "select * from J_USER_STOP where user_id =?";
		return createQuery(sql, userId).list();
	}
	/**
	 * @param doneCode
	 */
	public void removeByDoneCode(Integer doneCode) throws Exception{
		String sql = "delete J_USER_STOP where done_code =?";
		executeUpdate(sql, doneCode);
	}

	public void removeByUserId(String userId) throws Exception{
		String sql = "delete J_USER_STOP where user_id =?";
		executeUpdate(sql, userId);
	}
	
	public void cancelStopUser(String[] userall) throws Exception{
		String sql = "delete J_USER_STOP where user_id =?";
		executeBatch(sql, userall); 
	}
	
	public void updateByUserId(String oldUserId, String newUserId) throws Exception {
		String sql = "update J_USER_STOP set user_id=? where user_id=?";
		this.executeUpdate(sql, newUserId, oldUserId);
	}

	public List<JUserStop> queryStopByCustId(String custId) throws Exception {
		String sql ="select j.* from j_user_stop j,c_user u where j.user_id=u.user_id and u.cust_id=?";
		return this.createQuery(sql, custId).list();
	}
}

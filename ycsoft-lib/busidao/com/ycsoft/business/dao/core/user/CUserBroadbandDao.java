/**
 * CUserBroadbandDao.java	2010/03/17
 */

package com.ycsoft.business.dao.core.user;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserBroadband;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CUserBroadbandDao -> C_USER_BROADBAND table's operator
 */
@Component
public class CUserBroadbandDao extends BaseEntityDao<CUserBroadband> {

	private static final long serialVersionUID = -6174030563177171309L;

	/**
	 * default empty constructor
	 */
	public CUserBroadbandDao() {
	}

	/**
	 * 保存宽带用户信息
	 * @param bBand
	 * @throws Exception
	 */
//	public void save(CUserBroadband bBand) throws Exception{
//		String sql = "insert into c_user_broadband "+
//					 " (user_id, check_type, login_name, login_password,  bind_type, max_connection, area_id, county_id) "+
//					 " values "+
//					 " (?, ?, ?, ?, ?, ?, ?, ?) ";
//		executeUpdate(sql,bBand.getUser_id(),bBand.getCheck_type(),bBand.getLogin_name(),
//				bBand.getLogin_password(),bBand.getBind_type(),bBand.getMax_connection(),
//				bBand.getArea_id(),bBand.getCounty_id());
//	}

	/**
	 * 根据客户ID查询宽带信息
	 * @param custId
	 * @return
	 */
	public List<CUserBroadband> queryBandByCustId(String custId) throws JDBCException{
		String sql = "select * from c_user_broadband d,c_user u where d.user_id =u.user_id and " +
		" d.user_id in (select user_id from c_user where cust_id = ? )";
		List<CUserBroadband> users = createQuery(sql, custId).list();
		return users;
	}
	public List<CUserBroadband> queryBandByUserIds(String[] userIds) throws JDBCException{
		String sql = "select * from c_user_broadband d,c_user u where d.user_id =u.user_id and ( "+getSqlGenerator().setWhereInArray("u.user_id",userIds)+")";
		List<CUserBroadband> users = createQuery(sql).list();
		return users;
	}
	
	public List<CUserBroadband> queryBandHisByUserIds(String[] userIds) throws JDBCException{
		String sql = "select * from c_user_broadband_his d,c_user_his u where d.user_id =u.user_id and ( "+getSqlGenerator().setWhereInArray("u.user_id",userIds)+")";
		List<CUserBroadband> users = createQuery(sql).list();
		return users;
	}
	public List<CUserBroadband> queryBandHisByCustId(String custId) throws JDBCException{
		String sql = "select * from c_user_broadband_his d,c_user_his u where d.user_id =u.user_id and " +
		" u.cust_id = ? ";
		List<CUserBroadband> users = createQuery(sql, custId).list();
		return users;
	}


	/**
	 * 根据ID查询宽带信息
	 * @param userId
	 * @return
	 */
	public CUserBroadband queryBandById(String userId) throws JDBCException{
		String sql = "select * from c_user_broadband d,c_user u where d.user_id =u.user_id and " +
		" d.user_id =?";
		return createQuery(sql, userId).first();
	}
	/**
	 * 根据MAC查询宽带信息
	 * @param userId
	 * @return
	 */
	public CUserBroadband queryBandByDeviceId(String deviceId) throws JDBCException{
		String sql = "select * from c_user_broadband d,c_user u where d.user_id =u.user_id and " +
		" u.modem_mac =?";
		return createQuery(sql, deviceId).first();
	}

	//宽带用户名所有地区都不能重复
	public CUser queryUserByLoginName(String loginName, String countyId) throws JDBCException {
//		String sql = "select * from c_user_broadband c where c.login_name=? and c.county_id=?";
		String sql = "select * from c_user_broadband c where c.login_name=?";
		return createQuery(CUser.class, sql, loginName).first();
	}
}

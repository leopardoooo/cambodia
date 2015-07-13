/**
 * CUserDtvDao.java	2010/03/17
 */

package com.ycsoft.business.dao.core.user;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CUserDtv;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CUserDtvDao -> C_USER_DTV table's operator
 */
@Component
public class CUserDtvDao extends BaseEntityDao<CUserDtv> {

	private static final long serialVersionUID = 3356589403502541765L;

	/**
	 * default empty constructor
	 */
	public CUserDtvDao() {
	}

	/**
	 * 保存数字电视用户信息
	 * @param dtv
	 * @throws Exception
	 */
//	public void save(CUserDtv dtv) throws Exception{
//		String sql = "insert into c_user_dtv "+
//					 " (user_id, terminal_type, serv_type, area_id, county_id) "+
//					 " values " +
//					 "  (?, ?, ?, ?, ?)";
//		executeUpdate(sql, dtv.getUser_id(),dtv.getTerminal_type(),dtv.getServ_type(),dtv.getArea_id(),dtv.getCounty_id());
//	}

	/**
	 * 根据客户ID查询数字用户信息
	 * @param custId
	 * @return
	 */
	public List<CUserDtv> queryDtvByCustId(String custId) throws JDBCException{
		String sql = "select * from c_user_dtv d,c_user u where d.user_id =u.user_id and " +
				" d.user_id in (select user_id from c_user where cust_id=? )";
		List<CUserDtv> users = createQuery(sql, custId).list();
		return users;
	}
	
	public List<CUserDtv> queryDtvByUserIds(String[] userIds) throws JDBCException{
		String sql = "select * from c_user_dtv d,c_user u where d.user_id =u.user_id and ( "+getSqlGenerator().setWhereInArray("u.user_id",userIds)+" )";
		List<CUserDtv> users = createQuery(sql).list();
		return users;
	}
	
	public List<CUserDtv> queryDtvHisByUserIds(String[] userIds) throws JDBCException{
		String sql = "select * from c_user_dtv_his d,c_user_his u where d.user_id =u.user_id and ( "+getSqlGenerator().setWhereInArray("u.user_id",userIds)+" )";
		List<CUserDtv> users = createQuery(sql).list();
		return users;
	}
	
	public List<CUserDtv> queryDtvHisByCustId(String custId) throws JDBCException{
		String sql = "select * from c_user_dtv_his d,c_user_his u where d.user_id =u.user_id and " +
				" u.cust_id=? ";
		List<CUserDtv> users = createQuery(sql, custId).list();
		return users;
	}


	/**
	 * 根据用户ID查询数字用户信息
	 * @param userId
	 * @return
	 */
	public CUserDtv queryDtvById(String userId) throws JDBCException{
		String sql = StringHelper.append("select d.*,u.*,rs.device_model stbModel,rc.device_model cardModel",
				" from c_user_dtv d, c_user u,r_stb rs,r_card rc where d.user_id = u.user_id",
				" and rs.stb_id(+)=u.stb_id and rc.card_id(+)=u.card_id and d.user_id = ?");
		return createQuery(sql, userId).first();
	}
	/**
	 * 根据智能卡号 或 机顶盒号 查询数字用户信息
	 * @param userId
	 * @return
	 */
	public CUserDtv queryDtvByDeviceId(String deviceId) throws JDBCException{
		String sql = "select * from c_user_dtv d,c_user u where d.user_id =u.user_id and " +
				" u.card_id = ? union select * from c_user_dtv d,c_user u where d.user_id =u.user_id and " +
				" u.stb_id = ? ";
		return createQuery(sql, deviceId,deviceId).first();
	}
}

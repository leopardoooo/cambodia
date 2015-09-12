package com.ycsoft.business.dao.core.user;

/**
 * CUserPropChangeDao.java	2010/05/19
 */


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;



/**
 * CUserPropChangeDao -> C_USER_PROP_CHANGE table's operator
 */
@Component
public class CUserPropChangeDao extends BaseEntityDao<CUserPropChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5743594974364880497L;

	/**
	 * default empty constructor
	 */
	public CUserPropChangeDao() {}
	
	public CUserPropChange queryLastStatus(String userId, String countyId) throws Exception{
		String sql = "select * from c_user_prop_change where user_id=? and county_id=?" +
				" and column_name='status' order by change_time desc";
		return this.createQuery(sql, userId,countyId).first();
	}
	
	/**
	 * 查询开通双向的用户异动信息
	 * @param userId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CUserPropChange> queryNearUserPropByBusiCode(String userId,
			String countyId, String busiCode) throws JDBCException {
		String sql = "select * from c_user_prop_change t where t.done_code in ("
			+" select max(c.done_code) from c_done_code c,c_done_code_detail d"
			+" where c.done_code=d.done_code and d.user_id=?"
			+" and c.busi_code=? and c.county_id=? and d.county_id=?"
			+" ) and t.user_id=?";
		return this.createQuery(sql, userId,busiCode, countyId, countyId, userId).list();
	}

	/**
	 * 根据用户ID 查询用户异动信息
	 * @param userId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CUserPropChange> queryByUserId (String userId,String userType, String countyId) throws Exception{
		String tableName="";
		
		List<CUserPropChange> userPropChangeList = null;
		String sql = "select * from (" +
				"select a.column_name,a.old_value,a.new_value,a.change_time," +
				" b.comments column_name_text,b.param_name,c.busi_code,c.optr_id " +
				" from c_user_prop_change a,t_tab_define b ,c_done_code c " +
				" where a.column_name=b.column_name and b.table_name in ('CUSER',?) and b.status=? " +
				" and a.done_code=c.done_code "+
				" and a.user_id= ? and a.county_id= ?" +
				" union " +
				"select a.column_name,a.old_value,a.new_value,a.change_time," +
				" t.attribute_name column_name_text,t.param_name,c.busi_code,c.optr_id" +
				" from c_user_prop_change a, t_extend_attribute t, c_done_code c,t_extend e"+
				" where a.column_name=t.col_name and a.done_code = c.done_code" +
				" and t.extend_id=e.extend_id and e.extend_table='C_USER'"+
				" and a.user_id = ? and a.county_id = ?) order by change_time desc";
		
		userPropChangeList = this.createQuery(CUserPropChange.class, sql,
				tableName,StatusConstants.ACTIVE, userId, countyId, userId, countyId).list();
		return userPropChangeList;
	}

	/**
	 * @param userId 可为空，为空则查询档次所有用户的变更记录.
	 * @param county_id
	 * @return
	 */
	public List<CUserPropChange> queryByDoneCode(String userId,Integer doneCode, String countyId) throws Exception{
		List<CUserPropChange> userPropChangeList = null;
		List<Object> params = new ArrayList<Object>();
		String sql = "select a.column_name,a.old_value,a.new_value,a.change_time,b.comments column_name_text,b.param_name ,e.busi_name " +
				" from c_user_prop_change a,t_tab_define b ,c_done_code c,t_busi_code e " +
				" where a.column_name=b.column_name and b.table_name='CUSER' " +
				" and b.status=? and a.done_code=c.done_code and c.busi_code=e.busi_code "+
				" and a.done_code=? and a.county_id= ?";
		params.add(StatusConstants.ACTIVE);
		params.add(doneCode);
		params.add(countyId);
		if(StringHelper.isNotEmpty(userId)){
			sql += " and a.user_id= ?  ";
			params.add(userId);
		}
		userPropChangeList = this.createQuery(CUserPropChange.class,sql,params.toArray()).list();
		return userPropChangeList;
	}

	/**
	 * @param userId
	 * @param doneCode
	 * @param county_id
	 */
	public void removeByDoneCode(String userId, Integer doneCode,String countyId) throws Exception{
		String sql = "delete c_user_prop_change " +
				" where user_id =? " +
				" and done_code=? " +
				" and county_id=? ";
		executeUpdate(sql,  userId,doneCode,countyId);
	}

	public String queryUserLastStatus(String userId, String countyId) throws JDBCException {
		String sql = "select old_value from C_USER_PROP_CHANGE where user_id=? and county_id=? and COLUMN_NAME='status' order by change_time desc";
		return this.findUnique(sql, userId,countyId);
	}

}

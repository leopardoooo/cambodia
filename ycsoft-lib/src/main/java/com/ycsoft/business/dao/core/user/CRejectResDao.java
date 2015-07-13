/**
 * CRejectResDao.java	2010/12/02
 */
 
package com.ycsoft.business.dao.core.user; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CRejectRes;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CRejectResDao -> C_REJECT_RES table's operator
 */
@Component
public class CRejectResDao extends BaseEntityDao<CRejectRes> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7730936256259254344L;

	/**
	 * default empty constructor
	 */
	public CRejectResDao() {}
	
	/**
	 * 删除用户下的所有排斥资源
	 * @param userId
	 * @param custId
	 * @throws Exception
	 */
	public void deleteByUserIdAndCustId(String userId,String custId) throws Exception {
		String sql = "delete from c_reject_res where user_id=? and cust_id=?";
		this.executeUpdate(sql, userId, custId);
	}
	
	/**
	 * 查询用户的排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryRejectRes(String userId,String custId) throws Exception {
		String sql = "select * from p_res a,c_reject_res b where a.res_id=b.res_id and b.user_id=? and b.cust_id=? order by to_number(a.res_id)";
		return this.createQuery(PRes.class, sql, userId, custId).list();
	}
	
	public List<UserRes> queryRejectResByCustId(String custId) throws Exception {
		String sql = "select a.*,b.user_id from p_res a,c_reject_res b where a.res_id=b.res_id and b.cust_id=? order by to_number(a.res_id)";
		return this.createQuery(UserRes.class, sql, custId).list();
	}
	
	/**
	 * 查询用户未排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryUnRejectRes(String userId,String custId) throws Exception {
		String sql = "select p.* from p_res p where p.res_id not in (select r.res_id from c_reject_res r where  r.user_id=? and r.cust_id=?) order by to_number(p.res_id)";
		return this.createQuery(PRes.class, sql, userId, custId).list();
	}

}

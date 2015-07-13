/**
 * PResgroupResDao.java	2010/12/02
 */
 
package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PResgroupRes;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PResgroupResDao -> P_RESGROUP_RES table's operator
 */
@Component
public class PResgroupResDao extends BaseEntityDao<PResgroupRes> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6418249842583267551L;

	/**
	 * default empty constructor
	 */
	public PResgroupResDao() {}
	public List<PResgroupRes> queryResByGroupId(String groupId) throws JDBCException {
		String sql = "select * from p_resgroup_res where group_id = ? ";
		return this.createQuery(PResgroupRes.class, sql,groupId).list();
	}
	
	public void deleteRes (String groupId) throws Exception {
		String	sql = "delete p_resgroup_res where group_id = ? ";
		executeUpdate(sql,groupId);
	}
}

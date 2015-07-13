/**
 * CBankAgreeDao.java	2010/11/01
 */

package com.ycsoft.business.dao.core.bank;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CBankAgreeDao -> C_BANK_AGREE table's operator
 */
@Component
public class CBankAgreeDao extends BaseEntityDao<CBankAgree> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3651243173636367393L;
	/**
	 * default empty constructor
	 */
	public CBankAgreeDao() {}
	public void removeByCustId(String custId) throws Exception{
		String sql = "delete c_bank_agree where cust_id=?";
		executeUpdate(sql, custId);
	}
	public List<CBankAgree> queryByCustId(String custId) throws Exception{
		String sql = "select * from  c_bank_agree where cust_id=?";
		return createQuery(sql, custId).list();
	}
	public void updateProcStatus(Integer agree_id, String booleanFalse,
			String message) throws JDBCException {
		String sql = "update c_bank_agree set proc_status=?,proc_result=?,proc_date=sysdate where agree_id=?";
		executeUpdate(sql, booleanFalse,message,agree_id);
	}
	public List<CBankAgree> queryUnProc() throws JDBCException {
		String sql = "select * from  c_bank_agree where proc_status is null order by agree_id";
		return createQuery(sql).list();
	}

}

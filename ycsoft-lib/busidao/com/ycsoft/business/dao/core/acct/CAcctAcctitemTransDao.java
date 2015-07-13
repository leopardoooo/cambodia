package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemTrans;
import com.ycsoft.daos.abstracts.BaseEntityDao;

/**
 * CAcctAcctitemTransDao -> C_ACCT_ACCTITEM_TRANS table's operator
 */
@Component
public class CAcctAcctitemTransDao extends BaseEntityDao<CAcctAcctitemTrans> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4349025848689354652L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemTransDao() {}

	/**
	 * @param doneCode
	 * @return
	 */
	public List<CAcctAcctitemTrans> queryByDoneCode(Integer doneCode) throws Exception{
		String sql = "select * from C_ACCT_ACCTITEM_TRANS where done_code=?";
		return this.createQuery(sql, doneCode).list();
	}

	/**
	 * @param doneCode
	 */
	public void removeByDoneCode(Integer doneCode) throws Exception{
		String sql = "delete C_ACCT_ACCTITEM_TRANS where done_code=?";
		executeUpdate(sql, doneCode);

	}
}

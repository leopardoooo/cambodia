/**
 * CBankPayDao.java	2010/11/01
 */

package com.ycsoft.business.dao.core.bank;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bank.CBankPay;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CBankPayDao -> C_BANK_PAY table's operator
 */
@Component
public class CBankPayDao extends BaseEntityDao<CBankPay> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8643383539120128216L;

	/**
	 * default empty constructor
	 */
	public CBankPayDao() {}
	public CBankPay queryBankPayByLogId(String banklogid) throws Exception{
		String sql = "select * from  c_bank_pay where banklogid=?";
		return createQuery(sql, banklogid).first();
	}
	public void updateBankPayByDoneCode(Integer done_code) throws JDBCException{
		String sql="update c_bank_pay t set t.reverse=1,t.reverse_date=sysdate where t.done_code=?";
		this.executeUpdate(sql, done_code);
	}
}

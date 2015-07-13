/**
 * CFeeAcctDao.java	2010/10/20
 */

package com.ycsoft.business.dao.core.fee;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CFeeAcctDao -> C_FEE_ACCT table's operator
 */
@Component
public class CFeeAcctDao extends BaseEntityDao<CFeeAcct> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4104938442025895824L;

	/**
	 * default empty constructor
	 */
	public CFeeAcctDao() {}

	public void updateInvalidDate(String feeSn,String prodSn, Date invalidDate) throws JDBCException{
		String sql = "update c_fee_acct set prod_invalid_date=to_date(?,'yyyy-mm-dd') where fee_sn=? and prod_sn=?";
		this.executeUpdate(sql, DateHelper.dateToStr(invalidDate),feeSn,prodSn);
		
	}

}

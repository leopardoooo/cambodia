/**
 * CFeeBusiDao.java	2010/04/08
 */

package com.ycsoft.business.dao.core.fee;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFeeBusi;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CFeeBusiDao -> C_FEE_BUSI table's operator
 */
@Component
public class CFeeBusiDao extends BaseEntityDao<CFeeBusi> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4712258675702390565L;

	/**
	 * default empty constructor
	 */
	public CFeeBusiDao() {}
	
	public CFeeBusi queryCFeeBusi(String feeSn) throws JDBCException{
		String sql="select * from c_fee c,c_fee_busi b where c.fee_sn=b.fee_sn and b.fee_sn=? ";
		return this.createQuery(sql, feeSn).first();
		
	}
}

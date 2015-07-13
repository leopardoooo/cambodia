/**
 * JCustWriteOffDao.java	2010/06/08
 */

package com.ycsoft.business.dao.core.job;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JCustWriteoff;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * JCustWriteOffDao -> J_CUST_WRITE_OFF table's operator
 */
@Component
public class JCustWriteoffDao extends BaseEntityDao<JCustWriteoff> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2519376610108710075L;

	/**
	 * default empty constructor
	 */
	public JCustWriteoffDao() {}
	
	public JCustWriteoff queryByDoneCode(Integer doneCode) throws JDBCException{
		String sql = "select * from j_cust_writeoff j where j.done_code=?";
		return createQuery(sql, doneCode).first();
	}

}

/**
 * RInvoiceFeelistDao.java	2012/10/08
 */
 
package com.ycsoft.business.dao.resource.invoice; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.invoice.RInvoiceFeelist;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RInvoiceFeelistDao -> R_INVOICE_FEELIST table's operator
 */
@Component
public class RInvoiceFeelistDao extends BaseEntityDao<RInvoiceFeelist> {
	
	/**
	 * default empty constructor
	 */
	public RInvoiceFeelistDao() {}
	
	public void updateStatus(Integer feeDoneCode, String status) throws JDBCException {
		String sql = "update R_INVOICE_FEELIST set status=?,status_time=sysdate where done_code=?";
		this.executeUpdate(sql, status, feeDoneCode);
	}

}

/**
 * CInvoicePropChangeDao.java	2012/10/30
 */

package com.ycsoft.business.dao.core.print;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.print.CInvoice;
import com.ycsoft.beans.core.print.CInvoicePropChange;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CInvoicePropChangeDao -> C_INVOICE_PROP_CHANGE table's operator
 */
@Component
public class CInvoicePropChangeDao extends BaseEntityDao<CInvoicePropChange> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3132455148427665492L;

	/**
	 * default empty constructor
	 */
	public CInvoicePropChangeDao() {
	}
	
}

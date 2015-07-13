/**
 * CInvoiceItemDao.java	2010/04/15
 */

package com.ycsoft.business.dao.core.print;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.print.CInvoiceItem;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CInvoiceItemDao -> C_INVOICE_ITEM table's operator
 */
@Component
public class CInvoiceItemDao extends BaseEntityDao<CInvoiceItem> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6264905550682487760L;

	/**
	 * default empty constructor
	 */
	public CInvoiceItemDao() {}

}

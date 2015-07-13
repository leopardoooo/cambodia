/**
 * RInvoiceDetailDao.java	2010/09/17
 */

package com.ycsoft.business.dao.resource.invoice;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.invoice.RInvoiceDetail;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * RInvoiceDetailDao -> R_INVOICE_DETAIL table's operator
 */
@Component
public class RInvoiceDetailDao extends BaseEntityDao<RInvoiceDetail> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5923410957033662380L;

	/**
	 * default empty constructor
	 */
	public RInvoiceDetailDao() {}

	/**
	 * 设备调拨中 发票本号段内的所有发票 插入发票详细表(状态为使用和未使用状态)
	 * @param doneCode
	 * @param depotId
	 * @param startInvoiceBook
	 * @param endInvoiceBook
	 * @param startInvoiceId
	 * @param endInvoiceId
	 * @throws Exception
	 */
	public void insertInvoiceDetail(Integer doneCode,String depotId,String startInvoiceBook,
			String endInvoiceBook,String startInvoiceId,String endInvoiceId) throws Exception {
		String sql = "insert into r_invoice_detail(done_code,invoice_id,invoice_code) " +
				"select ?,t.invoice_id,invoice_code from r_invoice t where t.invoice_book_id between ? and ?" +
				" and t.finance_status in (?,?) and t.depotId=? and t.status=?";
		if(StringHelper.isNotEmpty(startInvoiceId)){
			sql += " and t.invoice_id >='"+startInvoiceId+"'";
		}
		if(StringHelper.isNotEmpty(endInvoiceId)){
			sql += " and t.invoice_id <='"+startInvoiceId+"'";
		}
		executeUpdate(sql, doneCode,startInvoiceBook,endInvoiceBook,
				SystemConstants.INVOICE_STATUS_IDLE,SystemConstants.INVOICE_STATUS_USE,
				depotId,StatusConstants.ACTIVE);
	}

}

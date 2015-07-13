/**
 * RInvoiceInputDao.java	2010/09/17
 */

package com.ycsoft.business.dao.resource.invoice;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.invoice.RInvoiceInput;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;


/**
 * RInvoiceInputDao -> R_INVOICE_INPUT table's operator
 */
@Component
public class RInvoiceInputDao extends BaseEntityDao<RInvoiceInput> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8575385452699564128L;


	/**
	 * default empty constructor
	 */
	public RInvoiceInputDao() {}


	/**
	 * 查询入库信息
	 * @param status
	 * @param optrId
	 * @param isToday
	 * @return
	 * @throws Exception
	 */
	public List<InvoiceDto> queryInvoiceInput(String countyId,boolean isToday)
		throws Exception {
		StringBuilder bul = new StringBuilder("select d.invoice_code,i.invoice_type,MIN(d.invoice_id) start_invoice_id,");
		bul.append(" MAX(d.invoice_id) end_invoice_id,i.optr_id,COUNT(i.invoice_count) invoice_count");
		bul.append(" from r_invoice_input i,r_invoice_detail d");
		bul.append(" where i.done_code=d.done_code and i.county_id=?");

		if(isToday)
			bul.append(" and i.create_time BETWEEN to_date(to_char(SYSDATE,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') and  to_date(to_char(SYSDATE,'yyyymmdd')||'23:59:59','yyyymmdd hh24:mi:ss')");
		bul.append("GROUP BY d.invoice_code,i.invoice_type,i.optr_id order by d.invoice_code");
		return this.createQuery(InvoiceDto.class, bul.toString(), countyId).list();
	}

}

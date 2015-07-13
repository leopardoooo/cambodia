/**
 * RInvoiceTransferDao.java	2010/09/17
 */

package com.ycsoft.business.dao.resource.invoice;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.invoice.RInvoiceTransfer;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;


/**
 * RInvoiceTransferDao -> R_INVOICE_TRANSFER table's operator
 */
@Component
public class RInvoiceTransferDao extends BaseEntityDao<RInvoiceTransfer> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2438488335919320649L;

	/**
	 * default empty constructor
	 */
	public RInvoiceTransferDao() {}

	

	/** 查询调拨信息
	 * @param status
	 * @param optrId
	 * @param isToday
	 * @return
	 * @throws Exception
	 */
	public List<InvoiceDto> queryInvoiceTransfer(String countyId,boolean isToday)
		throws Exception {
		StringBuilder bul= new StringBuilder("select d.invoice_code,t.invoice_type,");
		bul.append(" MIN(d.invoice_id) start_invoice_id,MAX(d.invoice_id) end_invoice_id, t.create_time,");
		bul.append(" t.optr_id,COUNT(t.invoice_count) invoice_count,");
		bul.append(" t.source_county_id,t.source_dept_id,t.source_optr_id,");
        bul.append(" t.order_county_id,t.order_dept_id,t.order_optr_id");
		bul.append(" from r_invoice_transfer t,r_invoice_detail d");
		bul.append(" where t.done_code=d.done_code and t.order_county_id=?");
		if(isToday)
			bul.append(" and t.create_time BETWEEN to_date(to_char(SYSDATE,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') and  to_date(to_char(SYSDATE,'yyyymmdd')||'23:59:59','yyyymmdd hh24:mi:ss')");
		bul.append(" GROUP BY d.invoice_code,t.invoice_type,t.create_time,t.optr_id,");
		bul.append(" t.source_county_id,t.source_dept_id,t.source_optr_id,");
        bul.append(" t.order_county_id,t.order_dept_id,t.order_optr_id");
        bul.append(" order by t.create_time desc,to_number(d.invoice_code) asc");
		return this.createQuery(InvoiceDto.class, bul.toString(), countyId).list();
	}
	
	/**
	 * 发票本号段内的调拨
	 * @param startInvoiceBook
	 * @param endInvoiceBook
	 * @param startInvoiceId
	 * @param endInvoiceId
	 * @throws Exception
	 */
	public void saveRangeTransfer(String startInvoiceBook,String endInvoiceBook,
				String startInvoiceId,String endInvoiceId,String optrId,String sourceDepotId,
				String orderDepotId,String doneCode) 
					throws Exception {
		String sql = "insert into r_transfer_invoice t(done_code,optr_id,create_time," +
				"source_depot_id,order_depot_id,invoice_count,r.invoice_type) " +
				"select ?,?,?,?,?,count(),invoice_type from r_invoice r where " +
				"r.invoice_book_id between ? and ? and r.depot_id=?";
	}

}

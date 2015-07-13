/**
 * RInvoiceOptrDao.java	2010/11/08
 */
 
package com.ycsoft.business.dao.resource.invoice; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.invoice.RInvoiceOptr;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;


/**
 * RInvoiceOptrDao -> R_INVOICE_OPTR table's operator
 */
@Component
public class RInvoiceOptrDao extends BaseEntityDao<RInvoiceOptr> {
	
	/**
	 * default empty constructor
	 */
	public RInvoiceOptrDao() {}
	
	/**
	 * 根据发票操作类型查询发票信息
	 * @param countyId
	 * @param optrType
	 * @param isToday
	 * @return
	 * @throws Exception
	 */
	public List<InvoiceDto> queryInvoiceByOptrType(String countyId,String optrType,boolean isToday) throws Exception {
		String sql = "select * from r_invoice_optr where optr_type=? and countyId=?";
		if(isToday)
			sql +=" and create_time BETWEEN to_date(to_char(SYSDATE,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') and  to_date(to_char(SYSDATE,'yyyymmdd')||'23:59:59','yyyymmdd hh24:mi:ss')";

		return this.createQuery(InvoiceDto.class, sql, optrType, countyId).list();
	}

}

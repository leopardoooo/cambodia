/**
 * CInvoiceDao.java	2010/04/15
 */

package com.ycsoft.business.dao.core.print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.print.CInvoice;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.business.dto.core.print.InvoiceFromDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CInvoiceDao -> C_INVOICE table's operator
 */
@Component
public class CInvoiceDao extends BaseEntityDao<CInvoice> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5253287596495358944L;

	/**
	 * default empty constructor
	 */
	public CInvoiceDao() {
	}

	/**
	 * 取消与费用相关的发票
	 *
	 * @param feeSns
	 */
	public void cancelAutoInvoice(CFee fee) throws JDBCException {
		// 作废发票
		String sql = "update r_invoice set status=? ,use_time = sysdate "
				+ " where invoice_id=? and invoice_code=? and invoice_mode=? and finance_status=?";
		executeUpdate(sql, StatusConstants.INVALID, fee.getInvoice_id(), fee
				.getInvoice_code(), SystemConstants.INVOICE_MODE_AUTO,
				StatusConstants.IDLE);
		
		//取消
		sql ="update c_invoice set status=? where invoice_id=? and invoice_code=?";
//		sql = "update c_invoice set status=? where doc_sn = "
//				+ " (select max(to_number(pi.doc_sn)) from c_doc_item pi, "
//				+ " c_doc_fee pf where pi.docitem_sn = pf.docitem_sn "
//				+ " and pf.fee_sn=?)";
		executeUpdate(sql, StatusConstants.INVALID, fee.getInvoice_id(), fee.getInvoice_code());
		
		// 修改同张发票的费用状态为未打印(自动票)
		sql = "update c_fee t set is_doc =? ,invoice_id=null,invoice_code=null,invoice_book_id=null,invoice_mode=null"
				+ " where invoice_id=? and invoice_code=? and invoice_mode=?";
		executeUpdate(sql, SystemConstants.BOOLEAN_FALSE, fee.getInvoice_id(),fee.getInvoice_code(),
				SystemConstants.INVOICE_MODE_AUTO);
	}
	
	public void cancelManualInvoice(CFee fee) throws JDBCException {
		// 修改为未打印状态
		String sql = "update c_fee t set is_doc =? ,invoice_id=null,invoice_code=null,invoice_book_id=null,invoice_mode=null"
				+ " where  fee_sn= ? and invoice_mode= ? ";
		executeUpdate(sql, SystemConstants.BOOLEAN_FALSE, fee.getFee_sn(),
				SystemConstants.INVOICE_MODE_MANUAL);
		
		// 发票	
		sql = "update r_invoice set amount = amount - ? "
				+ " where invoice_id=? and invoice_code=? and invoice_mode=? and finance_status=?";
		executeUpdate(sql, fee.getReal_pay(), fee.getInvoice_id(), fee
				.getInvoice_code(), SystemConstants.INVOICE_MODE_MANUAL,StatusConstants.IDLE);
		
		executeUpdate("update r_invoice set status=? where amount=0 and invoice_id=? and invoice_code=?",
					StatusConstants.IDLE, fee.getInvoice_id(),fee.getInvoice_code());
	}

	/**
	 * 根据单据编号查询发票信息
	 *
	 * @param docSn
	 *            单据编号
	 * @return
	 */
	public List<InvoiceFromDto> queryInvoiceByDocSn(String docSn, String invoiceId, String invoiceCode)
			throws JDBCException {
		String sql = "select doc_sn, invoice_code, invoice_id, status, amount , docitem_data"
				+ " from c_invoice " + " where doc_sn=?";
		if(StringHelper.isNotEmpty(invoiceId)){
			sql += " and invoice_id='"+invoiceId+"' and invoice_code='"+invoiceCode+"'";
		}
		return createQuery(InvoiceFromDto.class, sql, docSn).list();
	}
	
	/**
	 * 发票重打，查找需要作废的发票
	 * @param donecode
	 * @param docSn
	 * @return
	 * @throws JDBCException
	 */
	public List<InvoiceFromDto> queryOldInvoiceByDocSn(Integer donecode,String docSn)
		throws JDBCException {
		String sql = "select doc_sn, invoice_code, invoice_id, status, amount , docitem_data"
				+ " from c_invoice c where doc_sn=? and c.done_code <> ?  ";
		return createQuery(InvoiceFromDto.class, sql, docSn,donecode).list();
	}

	/**
	 * 根据收费编号查询所在发票的信息 没有相关发票返回 null
	 *
	 * @param feeSn 收费编号
	 * @return
	 */
	public InvoiceFromDto queryInvoiceByFeeSn(String feeSn)
			throws JDBCException {
		String sql = "select  doc_sn, invoice_code, invoice_id, status, amout , docitem_data  "
				+ " from c_invoice where doc_sn = "
				+ " (select pi.doc_sn from c_doc_item pi, "
				+ " c_doc_fee pf where pi.docitem_sn = pf.docitem_sn "
				+ " and pf.fee_sn=?)";

		return createQuery(InvoiceFromDto.class, sql, feeSn)
				.first();
	}

	/**
	 * 查询客户的所有发票信息
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CInvoiceDto> queryInvoiceByCustId(String custId,String countyId) throws JDBCException {
		String sql = " select distinct 'A' invoice_mode, i.doc_sn, i.optr_id,i.invoice_code, i.invoice_id, ri.status,ri.finance_status, i.amount,  print_date, i.invoice_book_id,"
				+ " ri.use_time,ri.invoice_type doc_type,o.optr_name,"
				+ " (select max(cf.create_time) from c_fee cf where cf.cust_id=:CUSTID and cf.county_id=:COUNTYID and cf.invoice_id=i.invoice_id and cf.invoice_code=i.invoice_code) fee_create_time"
				+ " from c_invoice i ,c_doc d,r_invoice ri,s_optr o"
				+ " where i.doc_sn=d.doc_sn AND ri.status=:STATUS"
				+ " AND  i.invoice_code=ri.invoice_code and i.invoice_id=ri.invoice_id and i.optr_id=o.optr_id"
				+ " and d.cust_id=:CUSTID and i.county_id=:COUNTYID and d.county_id=:COUNTYID and i.status=:ACTIVE"
				+ " UNION ALL"
				+ " SELECT 'M' invoice_mode,'',c.OPTR_ID,"
				+ " c.INVOICE_CODE,c.INVOICE_ID,ri.status, RI.FINANCE_STATUS, SUM(c.real_pay) AMOUNT, NULL PRINT_DATE, c.INVOICE_BOOK_ID,"
				+ " ri.use_time,ri.invoice_type doc_type,o.optr_name,"
				+ " (select max(cf.create_time) from c_fee cf where cf.cust_id=:CUSTID and cf.county_id=:COUNTYID and cf.invoice_id=c.invoice_id and cf.invoice_code=c.invoice_code) fee_create_time"
				+ " FROM c_fee c,r_invoice ri,s_optr o"
				+ " WHERE c.invoice_mode='M' AND c.CUST_ID =:CUSTID AND c.COUNTY_ID =:COUNTYID"
				+ " AND ri.invoice_id=c.invoice_id AND ri.invoice_code=c.invoice_code and c.optr_id=o.optr_id"
				+ " and c.status=:PAY and c.busi_code <> :UNITPAY"
				+ " GROUP BY c.OPTR_ID,c.INVOICE_CODE, c.INVOICE_ID,ri.status, RI.FINANCE_STATUS,c.INVOICE_BOOK_ID,ri.use_time,ri.invoice_type,o.optr_name"
				+ " UNION ALL"
				+ " SELECT 'M' invoice_mode,'',c.OPTR_ID,c.INVOICE_CODE,c.INVOICE_ID,ri.status,RI.FINANCE_STATUS,SUM(c.real_pay) AMOUNT,"
				+ " NULL PRINT_DATE,c.INVOICE_BOOK_ID,ri.use_time,ri.invoice_type doc_type,o.optr_name,"
				+ " (select max(cf.create_time) from c_fee cf where cf.cust_id=:CUSTID and cf.county_id=:COUNTYID and cf.invoice_id=c.invoice_id and cf.invoice_code=c.invoice_code) fee_create_time"
				+ " FROM c_fee c, r_invoice ri,c_done_code_detail cd,s_optr o"
				+ " where c.create_done_code=cd.done_code AND ri.invoice_id = c.invoice_id AND ri.invoice_code = c.invoice_code and c.optr_id=o.optr_id"
				+ " and ri.invoice_mode='M' AND c.COUNTY_ID = :COUNTYID and c.status = :PAY and c.busi_code=:UNITPAY and cd.cust_id=:CUSTID "
				+ " GROUP BY   c.OPTR_ID,c.INVOICE_CODE, c.INVOICE_ID,ri.status, RI.FINANCE_STATUS,c.INVOICE_BOOK_ID,ri.use_time,ri.invoice_type,o.optr_name ORDER BY use_time DESC";
		Map<String,String> params = new HashMap<String, String>();
		params.put("COUNTYID", countyId);
		params.put("CUSTID", custId);
		params.put("STATUS", StatusConstants.USE);
		params.put("ACTIVE",StatusConstants.ACTIVE);
		params.put("PAY", StatusConstants.PAY);
		params.put("UNITPAY", BusiCodeConstants.Unit_ACCT_PAY);
		return createNameQuery(CInvoiceDto.class, sql, params).list();
	}

	/**
	 * 修改发票
	 * @param oldInvoiceId
	 * @param oldInvoiceCode
	 * @param newInvoiceId
	 * @param newInvoiceCode
	 */
	public void changeInvoice(CInvoiceDto oldInvoice,CInvoiceDto newInvoice,String docSn) throws JDBCException {
		String sql = "update c_invoice set invoice_id=?,invoice_code=?,invoice_book_id=? where doc_sn=? AND status=? and invoice_id=? and invoice_code=?";
		executeUpdate(sql, newInvoice.getInvoice_id(), newInvoice.getInvoice_code(),newInvoice.getInvoice_book_id(),
				docSn,StatusConstants.ACTIVE,oldInvoice.getInvoice_id(),oldInvoice.getInvoice_code());
	}
	
	public void changeInvoiceItem(CInvoiceDto oldInvoice,CInvoiceDto newInvoice,String docSn) throws JDBCException {
		String sql = "update c_invoice_item set invoice_id=?,invoice_code=? where invoice_id=? and invoice_code=? " +
				" and docitem_sn IN (SELECT a.docitem_sn FROM c_doc_item a WHERE a.doc_sn=? )";
		executeUpdate(sql, newInvoice.getInvoice_id(), newInvoice.getInvoice_code(), oldInvoice.getInvoice_id(),
				oldInvoice.getInvoice_code(),docSn);
	}

	public CInvoiceDto queryReprintInvoice(String invoiceId, String invoiceCode,
			String countyId) throws JDBCException {
		String sql = " select i.*,bd.*,d.done_code from c_invoice i ,c_doc d,t_busi_doc bd "
				+ " where i.doc_sn=d.doc_sn and d.doc_type=bd.doc_type"
				+ " and i.county_id=? and d.county_id=? and invoice_id=? and invoice_code=? "
				+ " and i.status=?";
		return createQuery(CInvoiceDto.class, sql, countyId, countyId,
				invoiceId, invoiceCode,StatusConstants.ACTIVE).first();
	}
	
	/**
	 * 1.修改 r_invoice ，c_invoice 表status=INVALID
	 * 2.与c_fee相关联的发票信息清除，is_doc='F',invoice_mode=null,invoice_id=null,invoice_book_id=null,invoice_code=null
	 * @param invoiceId
	 * @param invoiceCode
	 * @throws JDBCException
	 */
	public void invalidInvoiceAndClearFeeInfo(String invoiceId, String invoiceCode) throws JDBCException {
		executeUpdate( "UPDATE c_invoice SET status=? WHERE invoice_id=? AND invoice_code=?",
				StatusConstants.INVALID, invoiceId, invoiceCode);
		executeUpdate( "UPDATE r_invoice SET status=? WHERE invoice_id=? AND invoice_code=?",
				StatusConstants.INVALID, invoiceId, invoiceCode);
		
		executeUpdate( "UPDATE c_fee SET is_doc='F',invoice_mode=null,invoice_id=null,invoice_book_id=null,invoice_code=null " +
				" WHERE invoice_id=? AND invoice_code=?", invoiceId, invoiceCode);
	}

	public void invalidInvoice(String invoiceId, String invoiceCode) throws JDBCException {
		executeUpdate(
				"UPDATE c_invoice SET status=? WHERE invoice_id=? AND invoice_code=?",
				StatusConstants.INVALID, invoiceId, invoiceCode);
	}

}

/**
 * CDocDao.java	2010/04/09
 */

package com.ycsoft.business.dao.core.print;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.print.CDoc;
import com.ycsoft.business.dto.core.print.DocDto;
import com.ycsoft.business.dto.core.print.PrintItemDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CDocDao -> C_DOC table's operator
 */
@Component
public class CDocDao extends BaseEntityDao<CDoc> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4648511512269847116L;

	/**
	 * default empty constructor
	 */
	public CDocDao() {}

	/**
	 * 根据单据更新状态
	 * @param feeSn
	 * @param valid
	 * @throws JDBCException
	 */
	public void updateDocValid(String[] feeSns,String valid) throws JDBCException {
		String sql = "update c_doc set is_valid=? where doc_sn in "
				+ " (select pi.doc_sn from c_doc_item pi,c_doc_fee pf where pi.docitem_sn = pf.docitem_sn "
				+ " and pf.fee_sn in("+getSqlGenerator().in(feeSns)+"))";
		executeUpdate(sql, valid );
	}

	/**
	 * 查询客户下的已支付单据
	 *
	 * @param custId
	 * @return
	 */
	public Pager<DocDto> queryDoc(String custId,Integer start,Integer limit) throws JDBCException {
		String sql = "select d.doc_sn,i.status from c_done_code_detail dc, c_doc d,c_invoice i" +
				" where d.is_valid=:STATUS and dc.cust_id = :CUSTID and dc.done_code = d.done_code" +
				" and i.doc_sn(+) = d.doc_sn order by d.done_code desc";

		Map<String, Serializable> paramers = new HashMap<String, Serializable>();
		paramers.put("CUSTID", custId);
		paramers.put("STATUS", SystemConstants.BOOLEAN_TRUE);

		return createNameQuery(DocDto.class, sql, paramers).setStart(start).setLimit(limit).page();
	}

	/**
	 * 根据发票查询票据
	 * @param oldInvoiceId
	 * @param oldInvoiceCode
	 */
	public CDoc queryByInvoice(String oldInvoiceId, String oldInvoiceCode)
			throws JDBCException {
		String sql = "select * from c_doc d ,c_invoice i where d.doc_sn=i.doc_sn and i.invoice_id=? and i.invoice_code=?";
		return createQuery(sql, oldInvoiceId, oldInvoiceCode).first();
	}

	public List<DocDto> queryUnPrintUnitPre(String feeSn) throws JDBCException {
			String sql = "select d.doc_sn,d.done_code,d.doc_type,bd.doc_name, bd.is_invoice,dcode.busi_code,dcode.done_date,dcode.optr_id,optr.optr_name"
				+ " from  c_doc d, c_doc_fee dfee, t_busi_doc bd, c_done_code dcode ,s_optr optr"
				+ " where optr.optr_id = dcode.optr_id"
				+ "   and dcode.done_code = d.done_code "
				+ "   and bd.doc_type = d.doc_type  "
				+ "   AND dfee.doc_sn=d.doc_sn"
				+ "   AND dfee.fee_sn=?"
				+ "   and not exists (select 1"
				+ "          from c_invoice i"
				+ "         where i.doc_sn = d.doc_sn"
				+ "           and i.status = ?)";


		return createQuery(DocDto.class, sql,feeSn, StatusConstants.ACTIVE).list();
	}

	public List<CDoc> queryByDoneCodes(CDoneCode[] doneCodes) throws JDBCException {
		String sql = "select * from c_doc where done_code in ("
				+ getSqlGenerator().in(doneCodes) + ")";
		return findList(sql);
	}



	public List<DocDto> queryLastUnPrintInvoice(String custId)
			throws JDBCException {
		String sql = StringHelper
				.append(
						" select d.doc_sn,d.done_code,d.doc_type,bd.doc_name,bd.is_invoice,",
						" dcode.busi_code,dcode.done_date,dcode.optr_id,optr.optr_name",
						" from c_done_code_detail dc, c_doc d, t_busi_doc bd, c_done_code dcode ,s_optr optr",
						" where optr.optr_id = dcode.optr_id",
						" AND dc.done_code = d.done_code",
						" AND dcode.done_code = d.done_code ",
						" AND bd.doc_type = d.doc_type and d.doc_type=?",
						" AND not exists (select 1 from c_invoice i where i.doc_sn = d.doc_sn" +
						" AND i.status = ?)",
						" AND dcode.done_code =",
						" (SELECT MAX(t.done_code) FROM c_doc t ",
						" WHERE t.cust_id=?)");

		return createQuery(DocDto.class, sql, SystemConstants.DOC_TYPE_INVOICE,
				StatusConstants.ACTIVE, custId)
				.list();
	}

//	public List<DocDto> queryPrintConfig(String[] doneCodes) throws JDBCException {
//		String sql = "select d.doc_sn,d.done_code,d.doc_type,bd.doc_name, bd.is_invoice,"
//				+ " dcode.busi_code,dcode.done_date,dcode.optr_id,optr.optr_name,dcode.busi_code"
//				+ " from  c_doc d, t_busi_doc bd, c_done_code dcode ,s_optr optr"
//				+ " where optr.optr_id = dcode.optr_id and dcode.done_code in ("
//				+ getSqlGenerator().in(doneCodes)
//				+ ")"
//				+ "   and dcode.done_code = d.done_code "
//				+ "   and bd.doc_type = d.doc_type and d.doc_type=?";
//		return createQuery(DocDto.class, sql, SystemConstants.DOC_TYPE_CONFIG)
//				.list();
//	}
	
	public void updateLastPrintDate(String[] doneCode) throws JDBCException {
		String sql = "update c_done_code_info set last_print=sysdate where done_code in ("
				+ getSqlGenerator().in(doneCode) + ")";
		executeUpdate(sql);
	}

	public List<DocDto> queryDocInvoice(Integer doneCode) throws JDBCException {
		String sql = StringHelper
				.append(
						" select d.doc_sn,d.done_code,d.doc_type,d.cust_id,bd.doc_name,bd.is_invoice,d.create_time done_date",
						" from  c_doc d, t_busi_doc bd",
						" where bd.doc_type = d.doc_type ",
						" AND d.done_code =?");
		
		return createQuery(DocDto.class, sql, doneCode).list();
	}
	

	public List<DocDto> queryInvoiceType(Integer doneCode,String custId,String countyId) throws JDBCException {
		String sql = StringHelper.append(" select '",doneCode,"' done_code,'",custId,"' cust_id,bd.* ",
						" from   t_busi_doc bd",
						" where bd.is_invoice = 'T'",
						" and bd.doc_type <> '2'",		//去掉手工发票
						" and (bd.show_county_id like '%", 
						countyId,
						"%' or bd.show_county_id='4501')");
		return createQuery(DocDto.class, sql).list();
	}

	public List<PrintItemDto> queryUnPrintItemByDoneCode(String doneCode) throws JDBCException{
		String sql = StringHelper
		.append(
				"select c.docitem_sn, c.doc_sn, c.amount, d.printitem_name,a.doc_type ",
				"  from c_doc a, c_invoice b, c_doc_item c, t_printitem d ",
				" where a.done_code = b.done_code(+) ",
				"   and a.doc_sn = c.doc_sn ",
				"   and b.invoice_id is null ",
				"   and c.printitem_id = d.printitem_id ",
				"   and a.done_code = ? "); 
		return createQuery(PrintItemDto.class, sql,doneCode).list();
	}

	public void deleteDoc(String doneCode)  throws JDBCException{
		String sql ="delete c_doc where done_code=?";
		this.executeUpdate(sql,doneCode);
		
	}
	
	public void updateDocType(String docSn, String docType) throws JDBCException {
		String sql = "update c_doc set doc_type=? where doc_sn=?";
		this.executeUpdate(sql, docType, docSn);
	}

	public void updateDocItem(String[] docItems, String docSn) throws JDBCException {
		String sql = "update c_doc_item set doc_sn=? where docitem_sn in ("+getSqlGenerator().in(docItems)+")";
		this.executeUpdate(sql,docSn);
		
		sql = "update c_doc_fee set doc_sn=? where docitem_sn in ("+getSqlGenerator().in(docItems)+")";
		this.executeUpdate(sql,docSn);
	}
}

/**
 * CDocItemDao.java	2010/04/09
 */

package com.ycsoft.business.dao.core.print;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.print.CDocItem;
import com.ycsoft.business.dto.core.print.PrintItemDto;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CDocItemDao -> C_DOC_ITEM table's operator
 */
@Component
public class CDocItemDao extends BaseEntityDao<CDocItem> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4622447555519351187L;


	/**
	 * default empty constructor
	 */
	public CDocItemDao() {}


	/**
	 * 通过DocSN查询打印项记录
	 * @param docSn
	 * @param custType 
	 * @return
	 * @throws JDBCException
	 */
	public List<PrintItemDto> queryBySn(String docSn, String custType, String invoiceId, String invoiceCode) throws JDBCException{
		String invoiceSql = "";
		if(StringHelper.isNotEmpty(invoiceId)){
			invoiceSql = " and f.invoice_id='"+invoiceId+"' and f.invoice_code='"+invoiceCode+"'";
		}
		String sql = StringHelper.append("SELECT distinct t.docitem_sn,t.amount,t2.printitem_name,u.card_id  ,d.doc_type",
										 " FROM c_doc_item t, t_printitem t2,c_doc_fee t3,c_fee f,c_user u ,c_doc d " +
										 " WHERE t3.doc_sn=d.doc_sn and t.doc_sn=t3.doc_sn AND t3.docitem_sn =t.docitem_sn AND f.fee_sn=t3.fee_sn AND u.user_id(+)=f.user_id AND ",
										 " t.doc_sn= ? and t2.printitem_id=t.printitem_id", invoiceSql,
										 " union all",
										 " SELECT distinct t.docitem_sn, t.amount, t2.printitem_name,'' card_id ,d.doc_type",
										 " FROM c_doc_item t, t_printitem t2, c_doc_fee t3, c_prom_fee ff,p_prom_fee pf ,c_doc d",
										 " WHERE t3.doc_sn=d.doc_sn and t.doc_sn = t3.doc_sn",
										 " AND t3.docitem_sn = t.docitem_sn",
										 " AND ff.prom_fee_sn = t3.fee_sn",
										 " and ff.prom_fee_id=pf.prom_fee_id",
										 " AND t.doc_sn = ? and t2.printitem_id = pf.printitem_id"		
										 );
		//单位批量缴费，不需要card_id
		if(StringHelper.isNotEmpty(custType)){
			sql = StringHelper.append("SELECT distinct t.docitem_sn,t.amount,t2.printitem_name ",
					 " FROM c_doc_item t, t_printitem t2,c_doc_fee t3,c_fee f,c_user u ",
					 " WHERE t.doc_sn=t3.doc_sn AND t3.docitem_sn =t.docitem_sn AND f.fee_sn=t3.fee_sn AND u.user_id(+)=f.user_id AND ",
					 " t.doc_sn= ? and t2.printitem_id=t.printitem_id", invoiceSql,
					 " union all ",
					 "SELECT distinct t.docitem_sn,t.amount,t2.printitem_name ",
					 " FROM c_doc_item t, t_printitem t2,c_doc_fee t3,c_prom_fee ff,p_prom_fee pf",
					 " WHERE t.doc_sn=t3.doc_sn AND t3.docitem_sn =t.docitem_sn AND ff.prom_fee_sn=t3.fee_sn",
					 " and ff.prom_fee_id=pf.prom_fee_id ",
					 " and t.doc_sn= ? and t2.printitem_id=pf.printitem_id"
			);
		}
		return createQuery(PrintItemDto.class,sql, docSn, docSn).list();
	}
	
	//非居民打印项
	public List<PrintItemDto> queryNonresCustBySn(String docSn) throws JDBCException{
		String sql = "select distinct t.amount, t2.printitem_name,t.docitem_sn"
			+" from c_doc_item t, t_printitem t2, c_doc_fee t3, c_fee f"
			+" where t.doc_sn = t3.doc_sn"
			+" and t3.docitem_sn = t.docitem_sn"
			+" and f.fee_sn = t3.fee_sn"
			+" and t.doc_sn = ?"
			+" and t2.printitem_id = t.printitem_id"
			+" union all "
			+" select distinct t.amount, t2.printitem_name,t.docitem_sn"
			+" from c_doc_item t, t_printitem t2, c_doc_fee t3, c_fee f,c_prom_fee ff,p_prom_fee pf"
			+" where t.doc_sn = t3.doc_sn"
			+" and t3.docitem_sn = t.docitem_sn"
			+" and ff.prom_fee_sn = t3.fee_sn and ff.prom_fee_id=pf.prom_fee_id and f.create_done_code=ff.done_code"
			+" and t.doc_sn = ?"
			+" and t2.printitem_id = pf.printitem_id";
		return createQuery(PrintItemDto.class,sql, docSn, docSn).list();
	}


	public List<String> queryRemarkBySn(String doc_sn) throws JDBCException {
		String sql  = "  SELECT d.remark FROM c_fee t, c_done_code d where " +
				" d.done_code=t.create_done_code and  " +
				" t.fee_sn in(  SELECT fee_sn FROM c_doc_fee  where doc_sn=?)";
		return findUniques(sql, doc_sn);
	}

}

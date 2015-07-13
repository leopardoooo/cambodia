/**
 * TInvoicePrintitemDao.java	2010/04/12
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TInvoicePrintitem;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TInvoicePrintitemDao -> T_INVOICE_PRINTITEM table's operator
 */
@Component
public class TInvoicePrintitemDao extends BaseEntityDao<TInvoicePrintitem> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5370192975201785273L;

	/**
	 * default empty constructor
	 */
	public TInvoicePrintitemDao() {}

	/**
	 *
	 * @param countyId 地区
	 * @param printitemId 打印编号
	 */
	public List<TInvoicePrintitem> queryPrintitem(String countyId,
			String[] printitemId) throws Exception {
		String sql = "SELECT p.printitem_id,p.doc_type FROM t_template_county c ,t_invoice_printitem p"
				+ " where c.template_id = p.template_id and c.county_id= ? and p.printitem_id in("+getSqlGenerator().in(printitemId)+") ";
		return createQuery(sql, countyId)
				.list();
	}

	/**
	 * 根据模板ID查询发票打印模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TInvoicePrintitem> queryInvoiceTpls(String templateId) throws JDBCException{
		String sql = "SELECT t1.*,t2.printitem_name,t3.doc_name FROM t_invoice_printitem  t1 ,"
			 + " t_printitem t2 ,t_busi_doc t3 where t1.template_id=? "
			 + " and t1.printitem_id = t2.printitem_id and t1.doc_type = t3.doc_type ";
		return createQuery(TInvoicePrintitem.class, sql, templateId).list();
	}

	/**
	 * 根据模板ID删除记录
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException{
		String sql = "delete from t_invoice_printitem t where t.template_id=?";
		executeUpdate(sql, templateId);
	}
}

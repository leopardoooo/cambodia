/**
 * CDoneCodeDao.java	2010/03/16
 */

package com.ycsoft.business.dao.core.common;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.common.CDoneCodeInfo;
import com.ycsoft.beans.core.print.CDoc;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CDoneCodeInfoDao -> C_DONE_CODE_INFO table's operator
 */
@Component
public class CDoneCodeInfoDao extends BaseEntityDao<CDoneCodeInfo> {

	private static final long serialVersionUID = 142913821221838946L;

	/**
	 * default empty constructor
	 */
	public CDoneCodeInfoDao() {
	}

	
	public void updateLastPrintByDoneCode(Integer doneCode,Date lastPrint) throws JDBCException {
		String sql = "update c_done_code_info t set t.last_print=? where done_code=?";
		this.executeUpdate(sql, lastPrint, doneCode);
	}
	
	public List<CDoneCodeInfo> queryPrintConfig(String[] doneCodes)
			throws JDBCException {
		String sql = "SELECT * FROM c_done_code_info t,c_done_code d "
				+ " WHERE d.done_code=t.done_code AND d.status=? AND d.done_code in ("
				+ getSqlGenerator().in(doneCodes) + ") ORDER BY D.DONE_CODE ";

		return createQuery(sql,StatusConstants.ACTIVE).list();
	}

	/**
	 * 查询客户的单据
	 * 
	 * @param custId
	 * @param countyId
	 * @return
	 */
	public List<CDoneCodeInfo> queryDocByCustId(String custId, String countyId)
			throws JDBCException {
		String sql = " SELECT distinct d.*,i.last_print FROM c_done_code d,c_done_code_detail dd,c_done_code_info i "
				+ " WHERE dd.done_code=d.done_code AND d.done_code=i.done_code "
				+ " AND d.county_id=? AND dd.cust_id=? AND d.status=?"
				+ " order by d.done_code desc";
		return createQuery(sql, countyId, custId, StatusConstants.ACTIVE)
				.list();
	}
	
	/**
	 * 查询业务受理单.
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CDoc> queryBusiConfirmDocByCustId(String custId, String countyId)
			throws JDBCException {
		String sql = "select distinct c.busi_code,doc.done_code,doc.doc_sn,"+
				" doc.optr_id,i.last_print create_time " +
				" from c_doc doc,c_doc_item di,busi.c_done_code c ,c_done_code_info i "
				+ " where doc.doc_sn=di.doc_sn and i.done_code= c.done_code and c.done_code = di.docitem_sn and doc.cust_id=? and doc.county_id=?" +
				" and doc.busi_code=? and doc.doc_type=? order by doc.done_code desc ";
		return createQuery(CDoc.class, sql, custId,countyId,
				BusiCodeConstants.SERVICE_PRINT,
				SystemConstants.DOC_TYPE_SERVICE).list();
	}
	

	/**
	 * 查询客户下未打印的确认单
	 * 
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CDoneCodeInfo> queryUnPrintConfig(String countyId, String custId)
			throws JDBCException {
		String sql = " SELECT d.*,i.* FROM c_done_code d,c_done_code_detail dd,c_done_code_info i "
				+ " WHERE dd.done_code=d.done_code AND d.done_code=i.done_code AND i.last_print is null "
				+ " AND d.done_date BETWEEN to_date(to_char(SYSDATE,'yyyymmdd'),'yyyymmdd')  "
				+ " AND to_date(to_char(SYSDATE,'yyyymmdd')||'23:59:59','yyyymmdd hh24:mi:ss')"
				+ " AND d.county_id=? AND dd.cust_id=? AND d.status=?"
				+ " order by d.done_code";
		return createQuery(sql, countyId, custId, StatusConstants.ACTIVE)
				.list();
	}
	
	/**
	 * 查询客户的单据
	 * 
	 * @param custId
	 * @param countyId
	 * @return
	 */
	public List<CDoc> queryDocByDocSn(String docSn)
			throws JDBCException {
 
		String sql = "select distinct doc.done_code,doc.doc_sn,doc.optr_id,doc.create_time "+
					" from c_doc doc where doc.doc_sn=?  ";
		return createQuery(CDoc.class, sql, docSn).list();
	}
}

/**
 * TBusiDocDao.java	2010/03/08
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.business.dto.config.BusiDocDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * TBusiDocDao -> T_BUSI_DOC table's operator
 */
@Component
public class TBusiDocDao extends BaseEntityDao<TBusiDoc> {

	/**
	 *
	 */
	private static final long serialVersionUID = -919112760079243780L;

	/**
	 * default empty constructor
	 */
	public TBusiDocDao() {
	}

	public List<BusiDocDto> queryByBusiCodeTemplate(String template_id)
			throws Exception {
		String sql = "select d.*,bd.busi_code from t_busi_code_doc bd,t_busi_doc d where bd.doc_type=d.doc_type and bd.template_id=? and d.is_invoice=?";
		List<BusiDocDto> result = createQuery(BusiDocDto.class, sql,
				template_id, SystemConstants.BOOLEAN_FALSE).list();
		return result;
	}

	public List<TBusiDoc> querydoc()throws Exception {
		return findList(" select * from t_busi_doc where is_invoice='F'");
	}

	public List<TBusiDoc> queryCountyByIds(String[] docType)throws Exception {
		return this.createQuery("select * from t_busi_doc where doc_type in (" +sqlGenerator.in(docType)+")").list();

	}
	
	public List<TBusiDoc> queryInvoiceType(String countyId) throws JDBCException{
		String sql = StringHelper.append(" select * from t_busi_doc where is_invoice='T'  and (show_county_id like '%", 
						countyId,"%' or show_county_id='4501')");
		return createQuery(TBusiDoc.class, sql).list();
	}
}

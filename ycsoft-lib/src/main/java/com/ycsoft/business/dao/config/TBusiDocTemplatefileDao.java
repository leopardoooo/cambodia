/**
 * TBusiDocTemplatefileDao.java	2010/11/23
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiDocTemplatefile;
import com.ycsoft.business.dto.print.BusiDocPrintConfigDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * TBusiDocTemplatefileDao -> T_BUSI_DOC_TEMPLATEFILE table's operator
 */
@Component
public class TBusiDocTemplatefileDao extends
		BaseEntityDao<TBusiDocTemplatefile> {

	/**
	 * default empty constructor
	 */
	public TBusiDocTemplatefileDao() {
	}

	public TBusiDocTemplatefile queryByDocType(String docType, String countyId)
			throws JDBCException {
		return createQuery(
				"select * from t_busi_doc_templatefile b,t_template_county c "
						+ "WHERE  b.temlate_id=c.template_id AND c.county_id=? AND b.doc_type=? AND c.template_type=?",
				countyId, docType, SystemConstants.TEMPLATE_TYPE_PRINT).first();
	}

	public List<TBusiDocTemplatefile> queryAll(String countyId) throws JDBCException {
		return createQuery(
				"select * from t_busi_doc_templatefile b,t_template_county c "
						+ "WHERE  b.temlate_id=c.template_id AND c.county_id=? AND c.template_type=?",
				countyId, SystemConstants.TEMPLATE_TYPE_PRINT).list();
	}
	
	/**
	 * 获取模板文件
	 * @param county_id
	 * @param template_type
	 * @return
	 * @throws Exception
	 */
	public List<BusiDocPrintConfigDto> getTemplate(String county_id, String template_type)throws Exception{
		final String sql = "SELECT T1.* "
						 +"  FROM T_BUSI_DOC_TEMPLATEFILE T1, T_TEMPLATE_COUNTY T2"
						 +" WHERE T1.TEMLATE_ID = T2.TEMPLATE_ID"
						 +"   AND T2.COUNTY_ID = ? "
						 +"   AND T2.TEMPLATE_TYPE = ?";
		return createQuery(BusiDocPrintConfigDto.class, sql, county_id, template_type).list();
		
	}
}

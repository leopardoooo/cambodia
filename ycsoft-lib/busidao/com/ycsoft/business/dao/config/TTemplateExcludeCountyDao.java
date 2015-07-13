/**
 * TTemplateExcludeCountyDao.java	2013/01/23
 */
 
package com.ycsoft.business.dao.config; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TTemplateExcludeCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TTemplateExcludeCountyDao -> T_TEMPLATE_EXCLUDE_COUNTY table's operator
 */
@Component
public class TTemplateExcludeCountyDao extends BaseEntityDao<TTemplateExcludeCounty> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4901852341293619027L;

	/**
	 * default empty constructor
	 */
	public TTemplateExcludeCountyDao() {
	}
	
	public TTemplateExcludeCounty query(String templateId, String countyId) throws Exception {
		String sql = "select * from T_TEMPLATE_EXCLUDE_COUNTY where template_id=? and county_id=?";
		return this.createQuery(sql, templateId, countyId).first();
	}
}
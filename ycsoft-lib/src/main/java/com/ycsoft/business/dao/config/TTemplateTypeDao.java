/**
 * TTemplateTypeDao.java	2010/02/25
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TTemplateType;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TTemplateTypeDao -> T_TEMPLATE_TYPE table's operator
 */
@Component
public class TTemplateTypeDao extends BaseEntityDao<TTemplateType> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3544662570266850530L;

	/**
	 * default empty constructor
	 */
	public TTemplateTypeDao() {}

}

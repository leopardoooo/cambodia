/**
 * TOptrLoginDao.java	2010/11/26
 */
 
package com.ycsoft.business.dao.system; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptrLogin;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TOptrLoginDao -> T_OPTR_LOGIN table's operator
 */
@Component
public class SOptrLoginDao extends BaseEntityDao<SOptrLogin> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8905941595413800097L;

	/**
	 * default empty constructor
	 */
	public SOptrLoginDao() {}

}

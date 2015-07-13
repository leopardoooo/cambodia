/**
 * CProdSynDao.java	2011/10/19
 */
 
package com.ycsoft.business.dao.core.prod;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdSyn;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CProdSynDao -> C_PROD_SYN table's operator
 */
@Component
public class CProdSynDao extends BaseEntityDao<CProdSyn> {
	
	/**
	 * default empty constructor
	 */
	public CProdSynDao() {}

}

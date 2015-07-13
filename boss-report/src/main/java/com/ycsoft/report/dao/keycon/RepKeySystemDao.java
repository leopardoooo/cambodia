/**
 * RepKeySystemDao.java	2010/06/21
 */
 
package com.ycsoft.report.dao.keycon; 

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.report.bean.RepKeySystem;


/**
 * RepKeySystemDao -> REP_KEY_SYSTEM table's operator
 */
@Component
public class RepKeySystemDao extends BaseEntityDao<RepKeySystem> {
	
	/**
	 * default empty constructor
	 */
	public RepKeySystemDao() {}

}

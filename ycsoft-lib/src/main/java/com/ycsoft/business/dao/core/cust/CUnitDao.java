/**
 * CUnitDao.java	2015/09/18
 */
package com.ycsoft.business.dao.core.cust;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CUnit;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CUnitDao -> C_UNIT table's operator
 */
@Component
public class CUnitDao extends BaseEntityDao<CUnit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1619685338737976074L;

	/**
	 * default empty constructor
	 */
	public CUnitDao() {}
	
}
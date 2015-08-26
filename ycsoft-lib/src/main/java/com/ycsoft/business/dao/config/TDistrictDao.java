/**
 * TDistrictDao.java	2015/08/24
 */
 
package com.ycsoft.business.dao.config; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TDistrictDao -> T_DISTRICT table's operator
 */
@Component
public class TDistrictDao extends BaseEntityDao<TDistrict> {
	
	/**
	 * default empty constructor
	 */
	public TDistrictDao() {}

}

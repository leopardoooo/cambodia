/**
 * TProvinceDao.java	2015/08/24
 */
 
package com.ycsoft.business.dao.config; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TProvince;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TProvinceDao -> T_PROVINCE table's operator
 */
@Component
public class TProvinceDao extends BaseEntityDao<TProvince> {
	
	/**
	 * default empty constructor
	 */
	public TProvinceDao() {}

}

/**
 * TProvinceDao.java	2015/08/24
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

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
	
	
	public List<TProvince> queryProvince() throws Exception{
		String sql = "select * from t_province where cust_code is not null ";
		return createQuery(sql).list();
	}

}

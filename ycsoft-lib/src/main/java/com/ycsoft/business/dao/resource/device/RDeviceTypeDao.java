/**
 * RStbModelDao.java	2015/08/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceType;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class RDeviceTypeDao extends BaseEntityDao<RDeviceType> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8694524620476884589L;

	/**
	 * default empty constructor
	 */
	public RDeviceTypeDao() {}

	public List<RDeviceType> queryDeviceType() throws JDBCException{
		String sql = "SELECT * FROM r_device_type";
		return this.createQuery(RDeviceType.class, sql).list();
	}

}

/**
 * RDeviceUseRecordsDao.java	2013/01/05
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceUseRecords;
import com.ycsoft.daos.abstracts.BaseEntityDao;

/**
 * RDeviceUseRecordsDao -> R_DEVICE_USE_RECORDS table's operator
 */
@Component
public class RDeviceUseRecordsDao extends BaseEntityDao<RDeviceUseRecords> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3004515240105411450L;

	/**
	 * default empty constructor
	 */
	public RDeviceUseRecordsDao() {}
	
	public List<RDeviceUseRecords> queryUseRecordByDeviceCode(String deviceCode) throws Exception {
		String sql = "select u.*,c.cust_name from r_device_use_records u,c_cust c" +
				" where u.cust_id=c.cust_id and u.device_code=?";
		return this.createQuery(sql, deviceCode).list();
	}
	
}
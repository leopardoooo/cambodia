/**
 * CCustDeviceHisDao.java	2011/04/14
 */
 
package com.ycsoft.business.dao.core.cust; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustDeviceHis;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CCustDeviceHisDao -> C_CUST_DEVICE_HIS table's operator
 */
@Component
public class CCustDeviceHisDao extends BaseEntityDao<CCustDeviceHis> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7381998125936106551L;

	/**
	 * default empty constructor
	 */
	public CCustDeviceHisDao() {}
	
	/**
	 * 删除客户单个设备
	 * @param custId
	 * @param deviceId
	 * @throws Exception
	 */
	public void removeDeviceHis(String custId,String deviceId) throws Exception{
		String sql = "delete c_cust_device_his where cust_id=? and device_id=?";
		executeUpdate(sql, custId,deviceId);
	}
	
	public void updateRecliam(String custId,String deviceId) throws Exception {
		String sql = "update c_cust_device_his set is_reclaim=?,status_date=sysdate where cust_id=? and device_id=? and is_reclaim=?";
		this.executeUpdate(sql, SystemConstants.BOOLEAN_TRUE, custId, 
				deviceId, SystemConstants.BOOLEAN_FALSE);
	}

}

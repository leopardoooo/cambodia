/**
 * RDeviceDoneDeviceidDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceDoneDeviceid;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RDeviceDoneDeviceidDao -> R_DEVICE_DONE_DEVICEID table's operator
 */
@Component
public class RDeviceDoneDeviceidDao extends BaseEntityDao<RDeviceDoneDeviceid> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3635142011296660029L;

	/**
	 * default empty constructor
	 */
	public RDeviceDoneDeviceidDao() {}
	
	public void removeByDeviceDoneCode(Integer deviceDoneCode) throws JDBCException {
		String sql = "delete from r_device_done_deviceid t where t.device_done_code=?";
		this.executeUpdate(sql, deviceDoneCode);
	}
	
	public void removeByDeviceDoneCodeAndId(Integer deviceDoneCode,String deviceId) throws JDBCException {
		String sql = "delete from r_device_done_deviceid t where t.device_done_code=? and t.device_id=?";
		this.executeUpdate(sql, deviceDoneCode, deviceId);
	}
	
	public List<RDeviceDoneDeviceid> queryByDeviceDoneCode(Integer deviceDoneCode) throws JDBCException {
		String sql = "select * from r_device_done_deviceid t where t.device_done_code=?";
		return this.createQuery(sql, deviceDoneCode).list();
	}
	
}

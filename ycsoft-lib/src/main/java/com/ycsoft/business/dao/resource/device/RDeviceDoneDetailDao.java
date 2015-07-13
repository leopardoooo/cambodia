/**
 * RDeviceDoneDetailDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceDoneDetail;
import com.ycsoft.beans.device.RDeviceDoneDeviceid;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.resource.DeviceInputDetailDto;

/**
 * RDeviceDoneDetailDao -> R_DEVICE_DONE_DETAIL table's operator
 */
@Component
public class RDeviceDoneDetailDao extends BaseEntityDao<RDeviceDoneDetail> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1825191940724221393L;

	/**
	 * default empty constructor
	 */
	public RDeviceDoneDetailDao() {
	}
	/**
	 * 查询设备操作明细
	 *
	 * @param deviceDoneCode
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceDoneDetail> queryByDeviceDoneCode(int deviceDoneCode)
			throws JDBCException {
		String sql = "SELECT * FROM r_device_done_detail t WHERE t.device_done_code=?";
		return createQuery(sql, deviceDoneCode).list();
	}

	/**
	 * 根据设备明细更新设备的概要
	 *
	 * @param deviceDoneDode
	 * @throws JDBCException
	 */
	public void updateByDoneDeviceid(Integer deviceDoneDode)
			throws JDBCException {
		String sql = "INSERT INTO r_device_done_detail (device_done_code, device_type, device_model, COUNT) "
				+ " SELECT a.device_done_code,b.device_type,b.device_model,COUNT(1) "
				+ " FROM r_device_done_deviceid a ,r_device b "
				+ " WHERE a.device_id = b.device_id AND a.device_done_code = ? "
				+ " GROUP BY a.device_done_code,b.device_model,b.device_type";
		executeUpdate(sql, deviceDoneDode);
	}

	public List<DeviceInputDetailDto> queryInputByDoneCode(int deviceDoneCode)
			throws JDBCException {
		String sql = "SELECT  d.*,t.create_time FROM r_device_done_detail d,r_device_input t "
				+ " WHERE  d.device_done_code = t.device_done_code AND t.order_done_code = ?  ORDER BY t.device_done_code desc";
		return createQuery(DeviceInputDetailDto.class, sql, deviceDoneCode)
				.list();
	}
	
	public RDeviceDoneDetail findByDoneCode(Integer deviceDoneCode,String deviceModel) throws JDBCException {
		String sql = "select * from r_device_done_detail t where t.device_done_code=? and t.device_model=?";
		return this.createQuery(sql, deviceDoneCode, deviceModel).first();
	}
	
	public void removeByDeviceDoneCode(Integer deviceDoneCode) throws JDBCException {
		String sql = "delete from r_device_done_detail t where t.device_done_code=?";
		this.executeUpdate(sql, deviceDoneCode);
	}
	
	public void updateCountByDoneCode(Integer deviceDoneCode, int count) throws JDBCException {
		String sql = "update r_device_done_detail t set t.count=? where t.device_done_code=?";
		this.executeUpdate(sql, count, deviceDoneCode);
	}

}

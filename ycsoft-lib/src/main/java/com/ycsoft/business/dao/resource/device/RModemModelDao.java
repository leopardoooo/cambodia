/**
 * RModemModelDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RModemModel;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RModemModelDao -> R_MODEM_MODEL table's operator
 */
@Component
public class RModemModelDao extends BaseEntityDao<RModemModel> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2837337462082513100L;

	/**
	 * default empty constructor
	 */
	public RModemModelDao() {}

	/**
	 * 根据设备id查找modem类型信息
	 * @param device_id 设备id
	 * @return
	 */
	public RModemModel findByDeviceId(String deviceId) throws JDBCException {
		String sql = "select * from r_modem_model m,r_device s where m.device_model=s.device_model and s.device_id=?";
		return createQuery(sql, deviceId).first();
	}
	public List<RModemModel> queryAll() throws JDBCException {
		String sql = "SELECT m.*,sup.supplier_name "
				+ " FROM r_modem_model m,r_device_supplier sup "
				+ " WHERE m.supplier_id=sup.supplier_id ";
		return createQuery(sql).list();
	}
	
	public List<RModemModel> queryModemModelByCountyId(String countyId) throws Exception {
		String sql = "select t.* from r_modem_model t,r_device_model_county dc"
			+ " where t.device_model=dc.device_model and dc.device_type=?";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and dc.county_id='"+countyId+"'";
		}
		return this.createQuery(sql, SystemConstants.DEVICE_TYPE_MODEM).list();
	}

	public List<RModemModel> queryModemModel() throws Exception{
		String sql = "select t.* from r_modem_model t";
		return this.createQuery(sql).list();
	}
	
	public RModemModel queryByModemMac(String modemMac) throws JDBCException{
		String sql = "select b.* from r_modem a,r_modem_model b where a.modem_mac=? and a.device_model= b.device_model";
		return createQuery(sql, modemMac).first();
	}
}

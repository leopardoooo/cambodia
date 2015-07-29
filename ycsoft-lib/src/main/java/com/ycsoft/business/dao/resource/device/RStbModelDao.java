/**
 * RStbModelDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.resource.StbModelDto;


/**
 * RStbModelDao -> R_STB_MODEL table's operator
 */
@Component
public class RStbModelDao extends BaseEntityDao<RStbModel> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6550291486224648612L;

	/**
	 * default empty constructor
	 */
	public RStbModelDao() {}

	/**
	 * 根据设备id查找stb类型信息
	 * @param device_id 设备id
	 * @return
	 */
	public RStbModel findByDeviceId(String deviceId) throws JDBCException {
		String sql = "select * from r_stb_model m,r_device s where m.device_model=s.device_model and s.device_id=?";
		return createQuery(sql, deviceId).first();
	}
	
	/**
	 * 根据机顶盒编号查询机顶盒类型信息
	 * @param stbId
	 * @return
	 * @throws JDBCException
	 */
	public RStbModel queryByStbId(String stbId) throws JDBCException{
		String sql = "select b.* from r_stb a,r_stb_model b where a.stb_id=? and a.device_model= b.device_model";
		return createQuery(sql, stbId).first();
	}
	
	/**
	 * 查询设备型号
	 * 设备类型机顶盒，对应虚拟卡的设备信息
	 * @return
	 * @throws JDBCException
	 */
	public List<RStbModel> queryAllDeviceMdoel() throws JDBCException {
		String sql = "SELECT * FROM vew_device_model";
		return createQuery(sql).list();
	}

	public List<RStbModel> queryAllDeviceMdoelBySupplier(String supplierId) throws JDBCException {
		String sql = "SELECT * FROM vew_device_model WHERE supplier_id=?";
		return createQuery(sql,supplierId).list();
	}

	public List<StbModelDto> queryAll() throws JDBCException {
		String sql = "SELECT s.*,r.model_name virtual_card_model_name,m.model_name virtual_modem_model_name,sup.supplier_name "
			+ " FROM r_stb_model s,r_card_model r,r_modem_model m,r_device_supplier sup "
			+ " WHERE s.virtual_card_model = r.device_model(+)"
			+ " and s.virtual_modem_model = m.device_model(+) AND s.supplier_id=sup.supplier_id";
		return createQuery(StbModelDto.class, sql).list();
	}
	
	public List<RStbModel> queryStbModelByCountyId(String countyId) throws Exception {
		String sql = "select distinct t.* from r_stb_model t,r_device_model_county dc"
			+ " where t.device_model=dc.device_model(+) and dc.device_type=?";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and dc.county_id='"+countyId+"'";
		}
		return this.createQuery(sql, SystemConstants.DEVICE_TYPE_STB).list();
	}
	
	public List<RStbModel> queryStbModel() throws Exception {
		String sql = "select distinct t.* from r_stb_model t";
		return this.createQuery(sql).list();
	}
}

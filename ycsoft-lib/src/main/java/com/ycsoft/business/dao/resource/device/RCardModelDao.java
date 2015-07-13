/**
 * RCardModelDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.resource.CardModelDto;


/**
 * RCardModelDao -> R_CARD_MODEL table's operator
 */
@Component
public class RCardModelDao extends BaseEntityDao<RCardModel> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6209887089876688488L;

	/**
	 * default empty constructor
	 */
	public RCardModelDao() {}

	/**
	 * 根据设备id查找card类型信息
	 * @param device_id 设备id
	 * @return
	 */
	public RCardModel findByDeviceId(String deviceId) throws JDBCException {
		String sql = "select * from r_card_model m,r_device s where m.device_model=s.device_model and s.device_id=?";
		return createQuery(sql, deviceId).first();
	}
	public List<CardModelDto> queryAll() throws JDBCException {
		String sql = "SELECT c.*,t.supplier_name ca_supplier_name,sup.supplier_name "
				+ " FROM r_card_model c ,t_server_supplier t,r_device_supplier sup "
				+ " WHERE c.ca_type = t.supplier_id AND c.supplier_id=sup.supplier_id";
		return createQuery(CardModelDto.class, sql).list();
	}
	
	public List<RCardModel> queryCardModelByCountyId(String countyId) throws Exception {
		String sql = "select distinct t.* from r_card_model t,r_device_model_county dc"
			+ " where t.device_model=dc.device_model and dc.device_type=?";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and dc.county_id='"+countyId+"'";
		}
		return this.createQuery(sql, SystemConstants.DEVICE_TYPE_CARD).list();
	}

	public RCardModel findModelByCard(String model) throws Exception {
		String sql = "select * from r_card_model m where m.device_model =? ";
		return createQuery(sql, model).first();
	}
}

/**
 * RDeviceHisDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceHis;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RDeviceHisDao -> R_DEVICE_HIS table's operator
 */
@Component
public class RDeviceHisDao extends BaseEntityDao<RDeviceHis> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5057760898234241919L;

	/**
	 * default empty constructor
	 */
	public RDeviceHisDao() {}

	public List<String> queryByStbId(String[] StbId) throws JDBCException {
		String sql = "select distinct stb_id from r_device_his  where "+getSqlGenerator().setWhereInArray("STB_ID",StbId)+" ";
		return findUniques(sql);
	}
	
}

/**
 * CFeeDeviceDao.java	2010/04/08
 */

package com.ycsoft.business.dao.core.fee;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.business.dto.device.BuyDeviceDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CFeeDeviceDao -> C_FEE_DEVICE table's operator
 */
@Component
public class CFeeDeviceDao extends BaseEntityDao<CFeeDevice> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6401178388172032238L;

	/**
	 * default empty constructor
	 */
	public CFeeDeviceDao() {
	}
	
	public BuyDeviceDto queryByStbId(String stbId) throws JDBCException {
		String sql = "  SELECT s.device_model,c.*,cd.* FROM c_fee c,c_fee_device  t,r_stb s,c_cust_device cd" +
				" WHERE s.device_id=t.device_id AND c.fee_sn=t.fee_sn AND t.device_type='STB' " +
				" AND cd.device_code=t.device_code AND t.device_code=?";
		return createQuery(BuyDeviceDto.class,sql, stbId).first();
	}

}

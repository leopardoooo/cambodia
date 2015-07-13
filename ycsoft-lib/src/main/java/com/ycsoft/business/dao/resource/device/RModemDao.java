/**
 * RModemDao.java	2010/06/25
 */

package com.ycsoft.business.dao.resource.device;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RModem;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RModemDao -> R_MODEM table's operator
 */
@Component
public class RModemDao extends BaseEntityDao<RModem> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4944079750039809343L;

	/**
	 * default empty constructor
	 */
	public RModemDao() {}
	
	/**
	 * 根据机顶盒id ，查询配对的MODEM
	 * @param stbDeviceId
	 * @return
	 */
	public RModem findPairModemByStbDeviceId(String stbDeviceId)
			throws JDBCException {
		String sql = "select m.*,'MODEM' device_type,mm.is_virtual from r_modem m,r_stb s,r_modem_model mm" +
				" where s.device_id=? and s.pair_modem_id=m.device_id and m.device_model=mm.device_model";
		return createQuery(sql, stbDeviceId).first();
	}
	
	public boolean isExistsModem(String modemMac) throws JDBCException {
		String sql = "select count(modem_mac) from r_modem t where t.modem_mac = ?";
		return this.count(sql, modemMac) > 0;
	}

}

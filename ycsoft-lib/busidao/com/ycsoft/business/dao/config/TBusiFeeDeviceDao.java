/**
 * TBusiFeeDeviceDao.java	2010/10/30
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiFeeDevice;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TBusiFeeDeviceDao -> T_BUSI_FEE_DEVICE table's operator
 */
@Component
public class TBusiFeeDeviceDao extends BaseEntityDao<TBusiFeeDevice> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3051688580851666551L;

	/**
	 * default empty constructor
	 */
	public TBusiFeeDeviceDao() {}

	/**
	 * 根据标准费用ID，购买方式，设备类型删除原有数据
	 * @param fee_std_id
	 * @param device_buy_mode
	 * @param device_type
	 * @throws JDBCException
	 */
	public void deleteById(String fee_std_id, String device_buy_mode,
			String device_type) throws JDBCException {
		String sql = "delete  from t_busi_fee_device t where t.fee_std_id=? and t.device_buy_mode=? and t.device_type=?";
		executeUpdate(sql, fee_std_id,device_buy_mode,device_type);
	}
	public void deleteFeeById(String fee_std_id) throws JDBCException {
		String sql = "delete  from t_busi_fee_device t where t.fee_std_id=? ";
		executeUpdate(sql, fee_std_id);
	}
	/**
	 * 配备升级专用
	 * @param mode
	 * @return
	 * @throws JDBCException
	 */
	public TBusiFeeDevice queryFeeByMode(String deviceType) throws JDBCException {
		String sql = " select distinct(t.fee_std_id) from t_busi_fee_device T WHERE T.DEVICE_BUY_MODE =? and t.device_type =? ";
		return createQuery(sql,SystemConstants.BUSI_BUY_MODE_UPGRADE,deviceType).first();
	}
	
	public List<TBusiFeeDevice> queryModelByStdId() throws JDBCException {
		String sql = " select t.*,r.model_name from r_device_model  r,t_busi_fee_device t where t.device_model = r.device_model(+) ";
		return createQuery(sql).list();
	}
	
}

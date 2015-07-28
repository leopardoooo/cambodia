/**
 * RDeviceFeeDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * RDeviceFeeDao -> R_DEVICE_FEE table's operator
 */
@Component
public class RDeviceFeeDao extends BaseEntityDao<RDeviceFee> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2312958657902126791L;

	/**
	 * default empty constructor
	 */
	public RDeviceFeeDao() {
	}

	/**
	 * 根据设备类型购买方式获取资费信息
	 *
	 * @param deviceModel
	 * @param buyMode
	 */
	public List<RDeviceFee> queryFee(String deviceType,String deviceModel, String buyMode,String templateId)
			throws JDBCException {
		String sql = " select t1.fee_id ,t3.fee_name, t1.default_value fee_value, t1.fee_std_id," +
				"t1.max_value max_fee_value,t1.min_value min_fee_value"
				+ " from t_busi_fee_std t1, (select a.* from t_busi_fee_device a "
				+ " where  a.device_model = ? "
				+ " and a.device_buy_mode = ?) t2,t_busi_fee t3 "
				+ " where t1.fee_std_id = t2.fee_std_id and t1.fee_id=t3.fee_id "
				+ " and t1.template_id = ? ";
		return createQuery(sql,deviceModel, buyMode,templateId).list();
	}

	
	public List<RDeviceFee> queryUpgradeuFee(String deviceType, String buyMode,String templateId)
			throws JDBCException {
		String sql = "  select t1.fee_id,t3.fee_name,t1.default_value fee_value,t1.fee_std_id,t1.max_value max_fee_value," +
				" t1.min_value min_fee_value from t_busi_fee_std t1, " +
				" (select distinct (a.fee_std_id) from t_busi_fee_device a where a.device_type = ? and a.device_buy_mode = ?) t2," +
				"t_busi_fee t3 where t1.fee_std_id = t2.fee_std_id and t1.fee_id = t3.fee_id and t1.template_id = ? ";
		return createQuery(sql,deviceType, buyMode,templateId).list();
	}
	
	/**
	 * 插入设备购买费用
	 * @param deviceFee
	 * @throws JDBCException
	 */
	public void insert(RDeviceFee deviceFee) throws JDBCException {
		String sql = "INSERT INTO r_device_fee "
				+ " (device_model, buy_mode, fee_id, fee_value) "
				+ " VALUES (?, ?, ?, ?)";
		executeUpdate(sql, deviceFee.getDevice_model(),
				deviceFee.getBuy_mode(), deviceFee.getFee_id(), deviceFee
						.getFee_value());
	}

	/**
	 * 更新设备购买费用
	 * @param deviceFee
	 * @throws JDBCException
	 */
	public void update(RDeviceFee deviceFee) throws JDBCException {
		String sql = "UPDATE r_device_fee SET " + " fee_id = ?,fee_value = ? "
				+ " WHERE device_model = ? and buy_mode = ?";
		executeUpdate(sql, deviceFee.getFee_id(), deviceFee.getFee_value(),
				deviceFee.getDevice_model(), deviceFee.getBuy_mode());
	}

	public List<RDeviceFee> queryByBuyMode(String buyMode) throws JDBCException {
		String sql = "SELECT * FROM r_device_fee t WHERE t.buy_mode=?";
		return createQuery(sql, buyMode).list();
	}

	public List<RDeviceFee> queryAll() throws JDBCException {
		String sql = "SELECT a.*,b.*,f.fee_name FROM r_device_fee a,vew_device_model b,t_busi_fee f  "
				+ " WHERE a.device_model=b.device_model AND f.fee_id=a.fee_id AND f.status=?";
		return createQuery(sql,StatusConstants.ACTIVE).list();
	}
}

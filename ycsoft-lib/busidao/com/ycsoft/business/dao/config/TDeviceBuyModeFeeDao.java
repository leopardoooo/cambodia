/**
 * TDeviceBuyModeFeeDao.java	2010/10/31
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TDeviceBuyModeFee;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TDeviceBuyModeFeeDao -> T_DEVICE_BUY_MODE_FEE table's operator
 */
@Component
public class TDeviceBuyModeFeeDao extends BaseEntityDao<TDeviceBuyModeFee> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3676310481412685451L;

	/**
	 * default empty constructor
	 */
	public TDeviceBuyModeFeeDao() {}

	/**
	 * 批量删除
	 * @param buyModeStr 值用","隔开,例如:"1,2,3"
	 */
	public void deleteBach(String[] buyModeArr) throws Exception {
		String sql = "delete from t_device_buy_mode_fee where buy_mode in ("+sqlGenerator.in(buyModeArr)+")";
		executeUpdate(sql);
	}

	/**
	 * 根据feeid删除记录
	 * @param feeId
	 * @throws Exception
	 */
	public void deleteByFeeId(String feeId) throws Exception {
		String sql = "delete from t_device_buy_mode_fee where fee_id=?";
		executeUpdate(sql, feeId);
	}

}

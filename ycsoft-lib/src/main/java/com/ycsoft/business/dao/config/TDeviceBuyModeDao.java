/**
 * TDeviceBuyModeDao.java	2010/06/24
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TDeviceBuyModeDao -> T_DEVICE_BUY_MODE table's operator
 */
@Component
public class TDeviceBuyModeDao extends BaseEntityDao<TDeviceBuyMode> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3753474940227326820L;

	/**
	 * default empty constructor
	 */
	public TDeviceBuyModeDao() {}

	/**
	 * 查询设备所有销售方式
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceBuyMode() throws Exception {
		String sql ="select * from t_device_buy_mode";
		return this.createQuery(sql).list();
	}

	/**
	 * 根据销售类型,查询销售方式
	 * @param buyType
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceBuyModeByBuyType(String buyType) throws Exception {
		String sql = "select * from t_device_buy_mode where buy_type=?";
		return this.createQuery(sql, buyType).list();
	}

	/**
	 * 根据销售类型,查询销售方式
	 * @param buyType
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceBuyModeByParams(String buyType, String module,String dataRight) throws Exception {
		String sql = "select * from t_device_buy_mode where buy_type=? and module=? and " + dataRight;
		return this.createQuery(sql, buyType, module).list();
	}
	
	/**
	 * 根据是否变更产权查询设备销售方式
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceBuyModeByOwnership(String ownship,String dataRight) throws Exception {
		String sql = "select t1.buy_mode, t1.buy_mode_name,t2.fee_id,t2.fee_name from "
				 + " t_device_buy_mode t1,(SELECT b.buy_mode t_buy_mode,b.fee_id,A.FEE_NAME FROM T_BUSI_FEE A,T_DEVICE_BUY_MODE_FEE B "
				 + " WHERE A.FEE_ID(+)=B.FEE_ID AND A.STATUS=?) t2 "
				 + " where t1.buy_mode=t2.t_buy_mode(+) "
				 + " and t1.buy_type=? and t1.change_ownship=? and " + dataRight;
		return this.createQuery(sql,StatusConstants.ACTIVE, SystemConstants.FEE_TYPE_BUSI,ownship).list();
	}

	/**
	 * 查询可以购买的设备购买方式
	 * @param dataRight
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceCanFee(String dataRight) throws Exception {
		String sql = "select t1.* from "
				 + " t_device_buy_mode t1 where t1.is_fee=? and t1.BUY_TYPE = ? and " + dataRight;
		return this.createQuery(sql,SystemConstants.BOOLEAN_TRUE, SystemConstants.FEE_TYPE_BUSI).list();
	}

}

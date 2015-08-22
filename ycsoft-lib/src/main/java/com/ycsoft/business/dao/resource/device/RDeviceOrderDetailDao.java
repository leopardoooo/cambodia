/**
 * RDeviceOrderDetailDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceOrderDetail;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * RDeviceOrderDetailDao -> R_DEVICE_ORDER_DETAIL table's operator
 */
@Component
public class RDeviceOrderDetailDao extends BaseEntityDao<RDeviceOrderDetail> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1000791955709133202L;


	/**
	 * default empty constructor
	 */
	public RDeviceOrderDetailDao() {
	}

	/**
	 * 更新订购的数量
	 * @param deviceDoneCode
	 * @throws JDBCException
	 */
	public void updateSupplyNum(Integer orderDoneDode,Integer deviceDoneCode) throws JDBCException {
		String sql = "UPDATE r_device_order_detail t SET t.supply_num=decode(t.supply_num,null,0,t.supply_num)+( "
				+ " SELECT decode(sum(d.count),null,0,sum(d.count)) FROM r_device_done_detail d WHERE " +
				" d.device_type=t.device_type AND d.device_model=t.device_model and d.device_done_code=?)"
				+ " WHERE t.device_done_code=?";
		executeUpdate(sql, deviceDoneCode,orderDoneDode);
	}

	public void updateMateralNum(Integer orderDoneDode,String deviceType,String deviceModel, Integer num) throws JDBCException {
		String sql = "UPDATE r_device_order_detail t SET t.supply_num=decode(t.supply_num,null,0,t.supply_num)+? WHERE " +
				" t.device_type =? AND t.device_model=? and t.device_done_code=?";
		executeUpdate(sql, num,deviceType,deviceModel,orderDoneDode);
	}
	
	/**
	 * 查询订购明细
	 * @param deviceDoneCode
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceOrderDetail> queryByDoneCode(int deviceDoneCode) throws JDBCException {
		String sql = "SELECT * FROM r_device_order_detail t WHERE t.device_done_code=?";
		return createQuery(sql, deviceDoneCode).list();
	}


	/**
	 * 删除订购明细
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void removeByDoneCode(Integer doneCode) throws JDBCException {
		String sql = "DELETE r_device_order_detail WHERE device_done_code=?";
		executeUpdate(sql, doneCode);
	}
	
	public RDeviceOrderDetail queryMateralDeviceDetail(Integer orderDoneDode,String deviceType,String deviceModel) throws JDBCException {
		String sql = "SELECT * FROM r_device_order_detail t WHERE " +
				" t.device_type =? AND t.device_model=? and t.device_done_code=?";
		return createQuery(sql,deviceType,deviceModel,orderDoneDode).first();
	}
}

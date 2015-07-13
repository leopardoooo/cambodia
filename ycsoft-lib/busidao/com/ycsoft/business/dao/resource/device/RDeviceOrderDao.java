/**
 * RDeviceOrderDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;
import org.springframework.stereotype.Component;
import com.ycsoft.beans.device.RDeviceOrder;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * RDeviceOrderDao -> R_DEVICE_ORDER table's operator
 */
@Component
public class RDeviceOrderDao extends BaseEntityDao<RDeviceOrder> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5153702537719710478L;


	/**
	 * default empty constructor
	 */
	public RDeviceOrderDao() {}
	
	public void editDeviceOrder(Integer deviceDoneCode, String isHistory) throws JDBCException {
		String sql = "update r_device_order set is_history=? where device_done_code=?";
		this.executeUpdate(sql, isHistory, deviceDoneCode);
	}

	/**
	 * 查询待收货设备订单
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceOrder> queryUnInputByDepot(String depotId) throws JDBCException {
		String sql ="SELECT a.*,c.supplier_name FROM r_device_order a,r_device_supplier c WHERE a.depot_id =? AND" +
				" a.supplier_id = c.supplier_id AND EXISTS (SELECT 1 FROM r_device_order_detail b " +
				" WHERE  b.order_num>b.supply_num  AND a.device_done_code=b.device_done_code)";
		return createQuery(sql, depotId).list();
	}


	/**
	 * 查询当前仓库的设备订单
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public Pager<RDeviceOrder> queryDeviceOrder(String depotId,
			Integer start, Integer limit, String query, String isHistory) throws JDBCException {
		String sql = "SELECT a.*,b.supplier_name,d.device_type,d.device_model,d.price,d.order_num,d.supply_num" +
				" FROM r_device_order a,r_device_supplier b,r_device_order_detail d" +
				" WHERE a.supplier_id=b.supplier_id(+) and a.device_done_code=d.device_done_code(+)" +
				" AND a.depot_id=?";
		if(StringHelper.isNotEmpty(query)){
			sql += " and a.order_no like '%"+query+"%'";
		}
		if(StringHelper.isNotEmpty(isHistory)){
			if(!isHistory.equals("All")){
				sql += " and a.is_history='"+isHistory+"'";
			}
		}else{
			sql += " and a.is_history='F'";//默认查询正在执行的订单
		}
		sql +=" order by a.create_time desc";
		return createQuery(sql, depotId).setStart(start).setLimit(limit).page();
	}

}

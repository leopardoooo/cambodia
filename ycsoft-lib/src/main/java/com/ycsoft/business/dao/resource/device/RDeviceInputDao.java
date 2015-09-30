/**
 * RDeviceInputDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceInput;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * RDeviceInputDao -> R_DEVICE_INPUT table's operator
 */
@Component
public class RDeviceInputDao extends BaseEntityDao<RDeviceInput> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2927122710352519033L;

	/**
	 * default empty constructor
	 */
	public RDeviceInputDao() {}
	
	/**
	 * 查询设备入库信息
	 * @param deviceId
	 * @return
	 * @throws JDBCException
	 */
	public RDeviceInput queryByDeviceId(String deviceId) throws JDBCException {
		String sql = "select i.* from r_device_input i,r_device_done_deviceid di"
			+" where di.device_done_code=i.device_done_code and di.device_id=?";
		return this.createQuery(sql, deviceId).first();
	}
	
	public void editInputNo(Integer deviceDoneCode,String transferNo, String remark) throws Exception {
		String sql = "update r_device_input set input_no=?,remark=? where device_done_code=?";
		executeUpdate(sql, transferNo,remark, deviceDoneCode);
	}

	/**
	 * 查找设备入库信息
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public Pager<RDeviceInput> queryByDepotId(String depotId, String query,
			Integer start, Integer limit) throws JDBCException {
		String sql = "SELECT s.supplier_name,t.*,d.device_type,d.device_model,d.count" +
				" FROM r_device_input t ,r_device_supplier s,r_device_done_detail d" +
				"  WHERE t.supplier_id=s.supplier_id(+)" +
				" and  t.device_done_code=d.device_done_code(+)" +
				" and t.depot_id=?";
		if(StringHelper.isNotEmpty(query)){
			sql += " and (t.input_no like '%"+query+"%' or t.batch_num like '%"+query+"%' or d.device_type like '%"+query.toUpperCase()+"%')";
		}
		sql += " order by t.create_time desc";
		return createQuery(sql, depotId).setStart(start).setLimit(limit).page();
	}

}

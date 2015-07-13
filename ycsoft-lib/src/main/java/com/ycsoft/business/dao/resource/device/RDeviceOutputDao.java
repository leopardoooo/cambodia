/**
 * RDeviceOutputDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceOutput;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;


/**
 * RDeviceOutputDao -> R_DEVICE_OUTPUT table's operator
 */
@Component
public class RDeviceOutputDao extends BaseEntityDao<RDeviceOutput> {

	/**
	 *
	 */
	private static final long serialVersionUID = 8205062459744101831L;

	/**
	 * default empty constructor
	 */
	public RDeviceOutputDao() {}
	
	public void editOutputNo(Integer deviceDoneCode, String outputNo, String remark) throws JDBCException {
		String sql = "update r_device_output t set t.output_no=?,remark = ? where t.device_done_code=?";
		this.executeUpdate(sql, outputNo, remark, deviceDoneCode);
	}

	/**
	 * 查询仓库的出库信息
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public Pager<RDeviceOutput> queryByDepot(String depotId,String query,Integer start,Integer limit) throws JDBCException {
		String sql = "SELECT t.*,s.supplier_name,d.device_type,d.device_model,d.count" +
				" FROM r_device_output t,r_device_supplier s,r_device_done_detail d "
				+ " WHERE s.supplier_id(+)=t.supplier_id and t.device_done_code=d.device_done_code(+) and t.depot_id=?";
		if(StringHelper.isNotEmpty(query)){
			sql += " and t.output_no like '%"+query+"%'";
		}
		sql += " order by t.create_time desc";
		return createQuery(sql, depotId).setStart(start).setLimit(limit).page();
	}
	
	public Pager<DeviceDto> queryDeviceDetail(Integer deviceDoneCode, String deviceType,String deviceModel,
			Integer start, Integer limit) throws JDBCException {
		String sql = "select dh.device_id,dh.device_type,dh.depot_id,d.county_id,"+
			" (case when dh.stb_id is not null then dh.stb_id"+
            " when dh.card_id is not null then dh.card_id"+
            " when dh.modem_mac is not null then dh.modem_mac end) device_code"+
            " from r_device_done_deviceid di,r_device_his dh,s_dept d"+
            " where di.device_id=dh.device_id and dh.depot_id=d.dept_id"+
            " and (d.dept_type='FGS' or d.dept_type='YYT') and d.status='ACTIVE'"+
            " and di.device_done_code=? and dh.device_type=? and dh.device_model = ? ";
		return this.createQuery(DeviceDto.class, sql, deviceDoneCode, deviceType,deviceModel)
				.setStart(start).setLimit(limit).page();
	}

}

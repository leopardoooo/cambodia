/**
 * RDeviceEditDao.java	2010/11/15
 */
 
package com.ycsoft.business.dao.resource.device; 

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceEdit;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;


/**
 * RDeviceEditDao -> R_DEVICE_EDIT table's operator
 */
@Component
public class RDeviceEditDao extends BaseEntityDao<RDeviceEdit> {
	
	/**
	 * default empty constructor
	 */
	public RDeviceEditDao() {}

	public Pager<RDeviceEdit> queryByDepotIdAndEditType(String depotId,
			String editType, String query, Integer start, Integer limit)
			throws Exception {
		String sql = "select t.*,d.device_type,d.device_model,d.count" +
				" from r_device_edit t,r_device_done_detail d" +
				" where t.device_done_code=d.device_done_code(+) and t.depot_id=? and t.edit_type=?";
		if(StringHelper.isNotEmpty(query)){
//			sql += " and t."
		}
		sql += " order by t.create_time desc";
		return this.createQuery(sql, depotId, editType).setStart(start).setLimit(limit).page();
	}
	
	/**
	 * 查询操作对应的设备明细
	 */
	public Pager<DeviceDto> queryDeviceDetail(Integer deviceDoneCode,
			String deviceType, Integer start, Integer limit) throws Exception {
		String sql = "";
		if(deviceType.equals(SystemConstants.DEVICE_TYPE_STB)){
			sql = "select d.*,b.stb_id device_code,dpt.county_id" +
			" from r_device d,r_device_done_deviceid di,r_stb b,s_dept dpt"+
			" where d.device_id=di.device_id and d.device_id=b.device_id and d.depot_id=dpt.dept_id"+
			" and (dpt.dept_type='FGS' or dpt.dept_type='YYT') and dpt.status='ACTIVE'" +
			" and di.device_done_code=?";
		}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)){
			sql = "select d.*,b.card_id device_code,dpt.county_id" +
			" from r_device d,r_device_done_deviceid di,r_card b,s_dept dpt"+
			" where d.device_id=di.device_id and d.device_id=b.device_id and d.depot_id=dpt.dept_id"+
			" and (dpt.dept_type='FGS' or dpt.dept_type='YYT') and dpt.status='ACTIVE'" +
			" and di.device_done_code=?";
		}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
			sql = "select d.*,b.modem_mac device_code,dpt.county_id" +
			" from r_device d,r_device_done_deviceid di,r_modem b,s_dept dpt"+
			" where d.device_id=di.device_id and d.device_id=b.device_id and d.depot_id=dpt.dept_id"+
			" and (dpt.dept_type='FGS' or dpt.dept_type='YYT') and dpt.status='ACTIVE'" +
			" and di.device_done_code=?";
		}
		return this.createQuery(DeviceDto.class, sql, deviceDoneCode).setStart(
				start).setLimit(limit).page();
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void saveDeviceEdit(Integer doneCode,String optrId,String deviceIds ,String remark) throws Exception{
		String sql="insert into r_device_edit "+
					" select  distinct ? ,depot_Id, 'DIFFENCE_TYPE_R_DEVICE'," +
					" ?,sysdate,?,'NODIFF','UNCHECK' from r_device where device_id in ("+deviceIds+")";
		this.executeUpdate(sql, doneCode,optrId,remark);
		 
	}
}

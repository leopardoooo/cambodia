/**
 * RDeviceProcureDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceProcure;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.DeviceProcureDto;


/**
 * RDeviceProcureDao -> R_DEVICE_PROCURE table's operator
 */
@Component
public class RDeviceProcureDao extends BaseEntityDao<RDeviceProcure> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8247429977867452300L;

	/**
	 * default empty constructor
	 */
	public RDeviceProcureDao() {}
	
	public void editProcureNo(Integer deviceDoneCode, String procureNo) throws JDBCException {
		String sql = "update r_device_procure t set t.procure_no=? where t.device_done_code=?";
		this.executeUpdate(sql, procureNo, deviceDoneCode);
	}

	public Pager<DeviceProcureDto> queryBydepot(String depotId, String query,
			Integer start, Integer limit) throws JDBCException {
		String sql = "SELECT t.*,d.device_type,d.device_model,d.count" +
				" FROM  r_device_procure t,r_device_done_detail d" +
				" WHERE t.device_done_code=d.device_done_code(+) and t.depot_id=?";
		if(StringHelper.isNotEmpty(query)){
			sql += " and t.procure_no like '%"+query+"%'";
		}
		sql += " order by t.create_time desc";
		return createQuery(DeviceProcureDto.class, sql, depotId).setStart(start).setLimit(limit).page();
	}
	
	private String returnSql(String deviceType){
		String sql = "";
		if(deviceType.equals(SystemConstants.DEVICE_TYPE_STB)){
			sql = StringHelper.append(
					"select t.*,b.device_id,b.stb_id device_code,b.device_model,'STB' device_type",
					" from r_device_done_deviceid di,r_device_procure t,r_stb b"
			);
		}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)){
			sql = StringHelper.append(
					"select t.*,b.device_id,b.card_id device_code,b.device_model,'CARD' device_type",
					" from r_device_done_deviceid di,r_device_procure t,r_card b"
			);
		}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
			sql = StringHelper.append(
					"select t.*,b.device_id,b.modem_mac device_code,b.device_model,'MODEM' device_type",
					" from r_device_done_deviceid di,r_device_procure t,r_modem b"
			);
		}
		sql =StringHelper.append(
				sql,
				" where di.device_done_code=t.device_done_code",
				" and di.device_id=b.device_id",
				" and di.device_done_code=?",
				" order by device_code"
		);
		return sql;
	}
	
	public List<DeviceProcureDto> queryProcureDeviceDetail(Integer deviceDoneCode, String deviceType) throws JDBCException {
		String sql = returnSql(deviceType);
		return this.createQuery(DeviceProcureDto.class, sql, deviceDoneCode).list();
	}
	
	public void removeByDeviceDoneCode(Integer deviceDoneCode) throws JDBCException {
		String sql = "delete from r_device_procure t where t.device_done_code=?";
		this.executeUpdate(sql, deviceDoneCode);
	}

	public DeviceProcureDto queryProcure(Integer deviceDoneCode) throws JDBCException {
		String sql = "select * from r_device_procure t where t.device_done_code = ? ";
		return this.createQuery(DeviceProcureDto.class, sql, deviceDoneCode).first();
	}
}

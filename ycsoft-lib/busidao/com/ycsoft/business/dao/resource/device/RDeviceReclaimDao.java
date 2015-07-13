/**
 * RCardDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceReclaim;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.DeviceReclaimDto;

/**
 * RCardDao -> R_CARD table's operator
 */
@Component
public class RDeviceReclaimDao extends BaseEntityDao<RDeviceReclaim> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4303009976635007765L;

	/**
	 * default empty constructor
	 */
	public RDeviceReclaimDao() {
	}
	
	public void updateReclaimDevice(Integer doneCode, String deviceId, String status,
			String confirm_optr, String confirm_time) throws JDBCException {
		String sql = "update r_device_reclaim set status=?,confirm_optr=?,confirm_time=?" +
				" where done_code=? and device_id=?";
		this.executeUpdate(sql, status, confirm_optr, confirm_time, doneCode, deviceId);
	}
	
	
	public void updateStatus(Integer doneCode, String deviceId,String status) throws JDBCException {
		String sql = "update r_device_reclaim set status=? where done_code=? and device_id=?";
		this.executeUpdate(sql, status, doneCode, deviceId);
	}
	
	/**
	 * 查找未确认的待回收设备
	 * @param deviceId
	 * @return
	 * @throws JDBCException
	 */
	public RDeviceReclaim queryDeviceReclaim(String deviceId) throws JDBCException {
		String sql = "select * from r_device_reclaim r where r.device_id=? and r.status=?";
		return createQuery(sql, deviceId,StatusConstants.UNCONFIRM).first();
	}

	public Pager<DeviceReclaimDto> queryDeviceReclaim(String depotId, String query,
			String startDate, String endDate, String deviceType,String confirmType,String isHistory,Integer start, Integer limit)
			throws JDBCException {
		String cond = "";
		if(StringHelper.isNotEmpty(query)){
			cond = " and t.device_code like '%"+query+"%'";
		}
		if(StringHelper.isNotEmpty(startDate)){
			cond += " and to_char(t.create_time,'yyyy-MM-dd')>='"+startDate+"'";
		}
		if(StringHelper.isNotEmpty(endDate)){
			cond += " and to_char(t.create_time,'yyyy-MM-dd')<='"+endDate+"'";
		}
		if(StringHelper.isNotEmpty(deviceType)){
			cond += " and t.device_type='"+deviceType+"'";
		}
		if(StringHelper.isNotEmpty(isHistory)){
			if(!isHistory.equals("All")){
				cond += " and t.is_history='"+isHistory+"'";
			}
		}else{
			cond += " and t.is_history='F'";//默认查询正在执行的订单
		}
		if(StringHelper.isNotEmpty(confirmType)){
			//如果不为CONFIRM
			if(!"CONFIRM".equals(confirmType)){
				cond += " and t.status<>'CONFIRM'";
			}else{
				cond += " and t.status='"+confirmType+"'";
			}
		}
		String sql = "select * from (select r.*,d.device_type,d.depot_status,b.stb_id device_code,c.card_id pair_device_code,m.modem_mac pair_device_modem_code" +
				" from r_device_reclaim r,r_device d,r_stb b,r_card c,r_modem m " +
				" where r.device_id=d.device_id and d.device_id=b.device_id and r.depot_id=? " +
				" and b.pair_card_id=c.device_id(+) and b.pair_modem_id=m.device_id(+)" +
				" union all "+
				"select r.*,d.device_type,d.depot_status,c.card_id device_code ,'','' from" +
				" r_device_reclaim r,r_device d,r_card c " +
				" where r.device_id=d.device_id and d.device_id=c.device_id and r.depot_id=? " +
				" and r.device_id not in(select t2.device_id from r_stb t1,r_device_reclaim t2 " +
				" where t1.pair_card_id= t2.device_id and t2.depot_id=?)" +
				" union all "+
				"select r.*,d.device_type,d.depot_status,m.modem_mac device_code ,'','' from" +
				" r_device_reclaim r,r_device d,r_modem m " +
				" where r.device_id=d.device_id and d.device_id=m.device_id and r.depot_id=?" +
				" and r.device_id not in(select t2.device_id from r_stb t1,r_device_reclaim t2 " +
				" where t1.pair_modem_id= t2.device_id and t2.depot_id=?)" +
				") t" +
				" where 1=1"+cond+" order by t.create_time desc";
		return this.createQuery(DeviceReclaimDto.class, sql, depotId, depotId,depotId,depotId,
				depotId).setStart(start).setLimit(limit).page();
	}
	
	public void updateReclaimConfirm(Integer doneCode, String deviceId,
			String optrId) throws JDBCException {
		String sql = "update r_device_reclaim set CONFIRM_OPTR=?,CONFIRM_TIME=?,status=?" +
				" where device_id=? and done_code=?";
		this.executeUpdate(sql, optrId, DateHelper.now(),
				StatusConstants.CONFIRM, deviceId, doneCode);
	}
	
	public void updateReclaimHistory(String deviceId,String deptId) throws JDBCException {
		String sql = "update r_device_reclaim set is_history='T'" +
				" where device_id=? and dept_id=?";
		this.executeUpdate(sql, deviceId, deptId);
	}
	
	public RDeviceReclaim queryReclaimDevice(Integer doneCode, String deviceId) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(doneCode);
		String sql = "select * from r_device_reclaim where done_code=? ";
		if(StringHelper.isNotEmpty(deviceId)){
			sql += "and device_id=?";
			params.add(deviceId);
		}
		return this.createQuery(sql, params.toArray()).first();
	}

}

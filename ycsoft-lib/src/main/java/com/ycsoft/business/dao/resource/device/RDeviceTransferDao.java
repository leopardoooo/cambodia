/**
 * RDeviceTransferDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceTransfer;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.depot.RDeviceTransferDto;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;

/**
 * RDeviceTransferDao -> R_DEVICE_TRANSFER table's operator
 */
@Component
public class RDeviceTransferDao extends BaseEntityDao<RDeviceTransfer> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8299100235500473992L;

	/**
	 * default empty constructor
	 */
	public RDeviceTransferDao() {
	}
	
	/**
	 * 查询设备调拨信息
	 * @param deviceId
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceTransfer> queryByDeviceId(String deviceId) throws JDBCException {
		String sql = "select t.* from r_device_transfer t,r_device_done_deviceid di"
			+" where di.device_done_code=t.device_done_code and di.device_id=?";
		return this.createQuery(sql, deviceId).list();
	}
	
	public void editDeviceTransferHistory(Integer deviceDoneCode, String isHistory) throws JDBCException {
		String sql = "update r_device_transfer set is_history=? where device_done_code=?";
		this.executeUpdate(sql, isHistory, deviceDoneCode);
	}
	
	public void editTransferNo(Integer deviceDoneCode,String transferNo, String remark) throws Exception {
		String sql = "update r_device_transfer set transfer_no=?,remark=? where device_done_code=?";
		executeUpdate(sql, transferNo,remark, deviceDoneCode);
	}

	/**
	 * 查询入库的记录
	 *
	 * @param depotId
	 * @param status
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceTransfer> queryInput(String depotId, String status,
			boolean isToday) throws JDBCException {
		String sql = "SELECT * FROM r_device_transfer t WHERE t.depot_order =? AND t.status=?";
		if (isToday)
			sql += " and t.create_time BETWEEN to_date(to_char(SYSDATE,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') and  to_date(to_char(SYSDATE,'yyyymmdd')||'23:59:59','yyyymmdd hh24:mi:ss')";
		return createQuery(sql, depotId, status).list();

	}

	/**
	 * 查询出库的记录
	 *
	 * @param depotId
	 * @param status
	 * @param isToday
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceTransfer> queryOutput(String depotId, String status,
			boolean isToday) throws JDBCException {
		String sql = "SELECT * FROM r_device_transfer t WHERE t.depot_source =? AND t.status=?";
		if (isToday)
			sql += " and t.create_time BETWEEN to_date(to_char(SYSDATE,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') and  to_date(to_char(SYSDATE,'yyyymmdd')||'23:59:59','yyyymmdd hh24:mi:ss')";
		return createQuery(sql, depotId, status).list();

	}

	/**
	 * 查询仓库的流转记录
	 *
	 * @param depotId
	 * @throws JDBCException
	 */
	public Pager<RDeviceTransferDto> queryByDepot(String depotId, String query,
			String startDate, String endDate, Integer start, Integer limit, String isHistory,String deviceModel)
			throws JDBCException {
		String cond = "";
		if(StringHelper.isNotEmpty(query)){
			cond = " and t.transfer_no like '%"+query+"%'";
		}
		if(StringHelper.isNotEmpty(startDate)){
			cond += " and to_char(t.create_time,'yyyy-mm-dd') between '"+startDate+"' and '"+endDate+"'";
		}
		if(StringHelper.isNotEmpty(isHistory)){
			if(!isHistory.equals("All")){
				cond += " and t.is_history='"+isHistory+"'";
			}
		}else{
			cond += " and t.is_history='F'";//默认查询正在执行的订单
		}
		if(StringHelper.isNotEmpty(deviceModel)){
			cond += " and d.device_model='"+deviceModel+"' ";
		}
		
		String sql = "select * from ("+
				" SELECT 'TRANIN' TRAN_TYPE,t.*,d.device_type,d.device_model,d.count" +
				" FROM r_device_transfer t,r_device_done_detail d" +
				" WHERE t.device_done_code=d.device_done_code(+) and t.depot_order=? "+
				cond +
				" union all" +
				" SELECT 'TRANOUT' TRAN_TYPE,t.*,d.device_type,d.device_model,d.count" +
				" FROM r_device_transfer t,r_device_done_detail d" +
				" WHERE t.device_done_code=d.device_done_code(+) and t.depot_source=? "+
				cond +
				" ) order by create_time desc";
		return createQuery(RDeviceTransferDto.class, sql, depotId, depotId)
				.setStart(start).setLimit(limit).page();
	}

	public RDeviceTransfer queryByTransferNo(String transferNo) throws JDBCException {
		RDeviceTransfer t = new  RDeviceTransfer();
		t.setTransfer_no(transferNo);
		List<RDeviceTransfer> l = findByEntity(t);
		return l.size()>0?l.get(0):null;
	}
	
	public List<RDeviceTransferDto> queryAllTransferDeviceDtail(
			int deviceDoneCode, String deviceType, String deviceModel)
			throws JDBCException {
		String sql = returnSql(deviceType);
		return createQuery(RDeviceTransferDto.class, sql, deviceDoneCode, deviceModel).list();
	}
	
	private String returnSql(String deviceType){
		String sql = "";
		if(deviceType.equals(SystemConstants.DEVICE_TYPE_STB)){
			sql = StringHelper.append(
					"select t.*,' ' tran_type,b.stb_id device_code,b.device_model,'STB' device_type",
					" from r_device_done_deviceid di,r_device_transfer t,r_stb b"
			);
		}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)){
			sql = StringHelper.append(
					"select t.*,' ' tran_type,b.card_id device_code,b.device_model,'CARD' device_type",
					" from r_device_done_deviceid di,r_device_transfer t,r_card b"
			);
		}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
			sql = StringHelper.append(
					"select t.*,' ' tran_type,b.modem_mac device_code,b.device_model,'MODEM' device_type",
					" from r_device_done_deviceid di,r_device_transfer t,r_modem b"
			);
		}
		sql =StringHelper.append(
				sql,
				" where di.device_done_code=t.device_done_code",
				" and di.device_id=b.device_id",
				" and di.device_done_code=? and b.device_model=?",
				" order by device_code"
		);
		return sql;
	}
	
	/**
	 * 转发之前查询信息,以前调拨了各个类型的设备多多少，现在可以转发的各个类型多少.
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map> queryTransInfoByDoneCode(Integer doneCode) throws Exception{
		Map<String, Map> map = new HashMap<String, Map>();
		Map<String, String> old = new HashMap<String, String>();
		Map<String, String> now = new HashMap<String, String>();
		String oldCount = "select res.device_type,count(1) device_code from "+getTransInfoSql()+ 
				" group by res.device_type ";
		List<DeviceDto> list = createQuery(DeviceDto.class, oldCount, doneCode, doneCode, doneCode).list();
		for(DeviceDto m:list){
			old.put(m.getDevice_type(),m.getDevice_code());
		}
		
		String newCount = "select res.device_type,count(1) device_code from "+getTransInfoSql()+
				" where res.tran_status = 'IDLE'  and res.freezed = 'F' " +
				" and res.depot_status = 'IDLE' " + " and res.device_status = 'ACTIVE' " +
				" group by res.device_type ";
		list.clear();
		list = createQuery(DeviceDto.class, newCount, doneCode, doneCode, doneCode).list();
		for(DeviceDto m:list){
			now.put(m.getDevice_type(),m.getDevice_code());
		}
		map.put("old", old);
		map.put("now", now);
		return map;
	}
	
	private String getTransInfoSql(){
		return " (" +
				" select s.stb_id device_code,s.pair_card_id,s.pair_modem_id,t.* from r_device t ,r_stb s " +
				" where s.device_id = t.device_id " +
				" and t.device_id in ( select rd.device_id from r_device_done_deviceid rd where rd.device_done_code = ? ) " +
				" union all " +
				" select s.card_id device_code,'' pair_card_id,'' pair_modem_id,t.* from r_device t ,r_card s " +
				" where s.device_id = t.device_id " +
				" and t.device_id in ( select rd.device_id from r_device_done_deviceid rd where rd.device_done_code = ? ) " +
				" union all " +
				" select s.modem_mac device_code,'' pair_card_id,'' pair_modem_id,t.* from r_device t ,r_modem s " +
				" where s.device_id = t.device_id " +
				" and t.device_id in ( select rd.device_id from r_device_done_deviceid rd where rd.device_done_code = ? ) " +
				" ) res "  ;
	}
	
	public List<DeviceDto> queryReTransDevices(Integer device_done_code) throws Exception{
		String sql = "select res.* from "+getTransInfoSql()+ 
				" where res.tran_status = 'IDLE'  and res.freezed = 'F' " +
				" and res.depot_status = 'IDLE' " + " and res.device_status = 'ACTIVE' ";
		return createQuery(DeviceDto.class,sql, device_done_code, device_done_code, device_done_code).list();
	}
}

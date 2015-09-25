/**
 * RDeviceDao.java	2010/05/07
 */

package com.ycsoft.business.dao.resource.device;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RStb;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.device.DeviceSmallDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;


/**
 * RDeviceDao -> R_DEVICE table's operator
 */
@Component
public class RDeviceDao extends BaseEntityDao<RDevice> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7588840323995923091L;

	/**
	 * default empty constructor
	 */
	public RDeviceDao() {}
	
	public void updateDeviceLoss(String deviceId,String isLossed) throws Exception {
		String sql = "update r_device set is_loss=? where device_id=?";
		this.executeUpdate(sql, isLossed,deviceId);
		
		//虚拟设备
		sql = "UPDATE r_device SET is_loss=? WHERE device_id in ( "
			+ " select t.pair_card_id from r_stb t "
			+ " where t.pair_card_id is not null and t.device_id=?"
			+ " union all "
			+ " select t.pair_modem_id from r_stb t "
			+ " where t.pair_modem_id is not null and t.device_id=?)";
		executeUpdate(sql, isLossed, deviceId, deviceId);
	}
	
	public Pager<DeviceDto> queryIdleDevice(String deviceCode, String depotId,
			Integer start, Integer limit) throws Exception {
		String sql="",stbCond="",cardCond="",modemCond="";
		if(StringHelper.isNotEmpty(deviceCode)){
			stbCond = " and s.stb_id='"+deviceCode+"'";
			cardCond = " and c.card_id='"+deviceCode+"'";
			modemCond = " and m.modem_mac='"+deviceCode+"'";
		}
		sql = "select d.*,s.stb_id device_code,c.card_id pair_device_code,c.device_model pair_device_model," +
			"m.modem_mac pair_device_modem_code,m.device_model pair_device_modem_model" +
			" from r_stb s,r_device d,r_card c,r_modem m"+
			" where d.device_id = s.device_id" +
			" and s.pair_card_id=c.device_id(+) and s.pair_modem_id=m.device_id(+)"+
			" and d.depot_status=? and d.depot_id=? and d.is_loss=?"+ stbCond+
        	" union "+
        	"select d.*,c.card_id device_code,'','','','' from r_card c,r_device d" +
        	" where d.device_id = c.device_id and d.is_virtual='F'"+
        	" and d.depot_status=? and d.depot_id=? and d.is_loss=?"+cardCond+
			" union "+
			"select d.*,m.modem_mac device_code,'','','','' from r_modem m,r_device d" +
			" where d.device_id = m.device_id and d.is_virtual='F'"+
			" and d.depot_status=? and d.depot_id=? and d.is_loss=?"+modemCond;
		
		return this.createQuery(DeviceDto.class, sql, 
				StatusConstants.IDLE, depotId, SystemConstants.BOOLEAN_TRUE,
				StatusConstants.IDLE, depotId, SystemConstants.BOOLEAN_TRUE,
				StatusConstants.IDLE, depotId, SystemConstants.BOOLEAN_TRUE)
				.setStart(start).setLimit(limit).page();
	}
	
	
	public CCust queryCustByDeviceId(String deviceId) throws JDBCException {
		String sql = "select c.cust_id from c_cust c,c_cust_device cd where c.cust_id=cd.cust_id and cd.device_id=?" +
				" union select c.cust_id from c_cust_his c,c_cust_device_his cd where c.cust_id=cd.cust_id and cd.device_id=?";
		return this.createQuery(CCust.class, sql, deviceId, deviceId).first();
	}
	
	public CCustDevice findCustDeviceByDeviceId(String deviceId) throws JDBCException {
		String sql = "select * from c_cust_device_his where device_id=?";
		return this.createQuery(CCustDevice.class, sql, deviceId).first();
	}

	/**
	 * 根据设备编号查询设备的公共信息
	 * @param deviceCode
	 * @return
	 */
	public RDevice findByDeviceCode(String deviceCode) throws JDBCException {
		String sql  ="select d.* from r_stb s,r_device d where d.device_id = s.device_id and s.stb_id=:deviceCode " +
				" union select d.* from r_card c,r_device d where d.device_id = c.device_id and c.card_id=:deviceCode" +
				" union select d.* from r_modem m,r_device d where d.device_id = m.device_id and m.modem_mac=:deviceCode";
		Map<String, Serializable> paramers = new HashMap<String, Serializable>();
		paramers.put("deviceCode", deviceCode);
		return createNameQuery(sql, paramers).first();
	}

	/**
	 *  根据设备id查询设备的公共信息
	 * @param deviceId
	 * @return
	 */
	public RDevice findByDeviceId(String deviceId) throws JDBCException {
		String sql = "select * from r_device where device_id=?";
		return createQuery(sql, deviceId).first();
	}

	/**
	 * 查找客户下 购买方式的设备
	 * @param custId
	 * @param buyMode
	 * @return
	 */
	public List<RDevice> queryDeviceByBuyModel(String custId, String buyMode) throws JDBCException {
		String sql = "select * from r_device r where r.device_id in (select device_id from c_cust_device d where d.cust_id=? and d.buy_mode=?)";
		return createQuery(sql, custId, buyMode).list();
	}

	/**
	 * 更新设备流转为转出
	 *
	 * @param doneCode 调拨流水
	 * @throws JDBCException
	 */
	public void updateTranOut(Integer doneCode) throws JDBCException {
		String sql = "UPDATE r_device SET tran_status=? WHERE device_id IN "
				+ " (SELECT device_id FROM r_device_done_deviceid t WHERE t.device_done_code=?)";

		executeUpdate(sql, StatusConstants.UNCONFIRM,
				doneCode);
		
		//虚拟设备
		sql = "UPDATE r_device SET tran_status=? WHERE device_id in ( "
			+ " select t.pair_card_id  "
			+ " from r_device_done_deviceid di,r_stb t "
			+ " where di.device_id=t.device_id and t.pair_card_id is not null "
			+ " and di.device_done_code=? "
			+ " union all "
			+ " select t.pair_modem_id  "
			+ " from r_device_done_deviceid di,r_stb t "
			+ " where di.device_id=t.device_id and t.pair_modem_id is not null "
			+ " and di.device_done_code=?)";
		executeUpdate(sql, StatusConstants.UNCONFIRM,doneCode, doneCode);
	}

	
	/**
	 * 更新设备流转为空闲，并改变库位
	 *
	 * @param doneCode 调拨流水
	 * @param depotOrder 目标仓库
	 * @throws JDBCException
	 */
	public void updateTranIdelDepot(Integer doneCode, String depotOrder)
			throws JDBCException {
		String sql = "UPDATE r_device SET tran_status=?,depot_id=? WHERE device_id IN "
				+ " (SELECT device_id FROM r_device_done_deviceid t WHERE t.device_done_code=?)";

		executeUpdate(sql, StatusConstants.IDLE,
				depotOrder, doneCode);
		
		//虚拟设备
		sql = "UPDATE r_device SET tran_status=?,depot_id=? WHERE device_id in ( "
			+ " select t.pair_card_id  "
			+ " from r_device_done_deviceid di,r_stb t "
			+ " where di.device_id=t.device_id and t.pair_card_id is not null "
			+ " and di.device_done_code=? "
			+ " union all "
			+ " select t.pair_modem_id  "
			+ " from r_device_done_deviceid di,r_stb t "
			+ " where di.device_id=t.device_id and t.pair_modem_id is not null "
			+ " and di.device_done_code=?)";
		executeUpdate(sql, StatusConstants.IDLE,
				depotOrder, doneCode, doneCode);
	}

	public void updateTranIdel(Integer doneCode)throws JDBCException {
		String sql = "UPDATE r_device SET tran_status=? WHERE device_id IN "
				+ " (SELECT device_id FROM r_device_done_deviceid t WHERE t.device_done_code=?)";

		executeUpdate(sql, StatusConstants.IDLE,
				doneCode);

			//虚拟设备
			sql = "UPDATE r_device SET tran_status=? WHERE device_id in ( "
				+ " select t.pair_card_id  "
				+ " from r_device_done_deviceid di,r_stb t "
				+ " where di.device_id=t.device_id and t.pair_card_id is not null "
				+ " and di.device_done_code=? "
				+ " union all "
				+ " select t.pair_modem_id  "
				+ " from r_device_done_deviceid di,r_stb t "
				+ " where di.device_id=t.device_id and t.pair_modem_id is not null "
				+ " and di.device_done_code=?)";
			executeUpdate(sql, StatusConstants.IDLE,doneCode, doneCode);
		
		}
	

	/**
	 * 查询设备信息
	 * @param deviceCode
	 * @return
	 * @throws JDBCException
	 */
	public DeviceDto queryByDeviceCode(String deviceCode) throws JDBCException {
		String sql = "SELECT d.*,s.device_model,s.stb_id device_code,"
				+ "r.card_id pair_device_code,r.device_model pair_device_model,"
				+ " m.MODEM_MAC pair_device_modem_code,m.DEVICE_MODEL pair_device_modem_model,'' modem_mac,s.pair_card_id,s.pair_modem_id"
				+ " FROM R_STB S, R_CARD r,r_device d,r_modem m"
				+ " WHERE s.device_id=d.device_id AND s.pair_card_id= r.DEVICE_ID(+)" 
				+ " AND s.pair_modem_id=m.device_id(+) AND S.STB_ID = :deviceCode"
				+ " UNION SELECT d.*,c.device_model,c.card_id device_code,'','','','','','',''"
				+ " FROM R_CARD C,r_device d"
				+ " WHERE c.device_id=d.device_id AND C.CARD_ID = :deviceCode "
				+ " UNION SELECT d.*,m.device_model,m.modem_mac device_code,'','','','',m.modem_mac,'',''"
				+ " FROM R_MODEM M,r_device d WHERE m.device_id=d.device_id AND M.Modem_mac = :deviceCode";

		Map<String, Serializable> paramers = new HashMap<String, Serializable>();
		paramers.put("deviceCode", deviceCode);
		return createNameQuery(DeviceDto.class,sql, paramers).first();

	}
	
	public DeviceDto getStbCardById(String deviceCode,String deviceType) throws JDBCException {
		String str = "";
		if(deviceType.equals("STB")){
			str +=" T.STB_ID = ? ";
		}else if(deviceType.equals("CARD")){
			str +=" T1.CARD_ID = ? ";
		}else{
		}
		String sql = "select t.STB_ID device_code,T1.CARD_ID pair_device_code,t1.device_id pair_card_id,r.* from R_STB T,R_CARD T1,r_device r " +
				" WHERE T.PAIR_CARD_ID = T1.DEVICE_ID(+)  and  r.device_id = t.device_id and " + str ;
		return  createQuery(DeviceDto.class,sql, deviceCode).first();

	}
	
	public List<DeviceDto> queryByBatchNum(String batchNum,String depotId) throws JDBCException {
		String sql = "SELECT d.*,s.device_model,s.stb_id device_code,"
				+ "r.card_id pair_device_code,r.device_model pair_device_model,"
				+ " m.MODEM_MAC pair_device_modem_code,m.DEVICE_MODEL pair_device_modem_model,'' modem_mac,s.pair_card_id,s.pair_modem_id"
				+ " FROM R_STB S, R_CARD r,r_device d,r_modem m"
				+ " WHERE s.device_id=d.device_id AND s.pair_card_id= r.DEVICE_ID(+)" 
				+ " AND s.pair_modem_id=m.device_id(+) AND d.Batch_Num = :batchNum AND d.depot_id=:depotId AND d.tran_status='IDLE'"
				+ " UNION SELECT d.*,c.device_model,c.card_id device_code,'','','','','','',''"
				+ " FROM R_CARD C,r_device d"
				+ " WHERE c.device_id=d.device_id AND d.Batch_Num = :batchNum AND d.depot_id=:depotId AND d.tran_status='IDLE'"
				+ " UNION SELECT d.*,m.device_model,m.modem_mac device_code,'','','','',m.modem_mac,'',''"
				+ " FROM R_MODEM M,r_device d WHERE m.device_id=d.device_id AND d.Batch_Num = :batchNum AND d.depot_id=:depotId AND d.tran_status='IDLE'";

		Map<String, Serializable> paramers = new HashMap<String, Serializable>();
		paramers.put("batchNum", batchNum);
		paramers.put("depotId", depotId);
		return createNameQuery(DeviceDto.class,sql, paramers).list();

	}
	public DeviceDto queryDeviceByCard(String deviceCode) throws JDBCException {
		String sql = StringHelper.append(
						"select rd.*,r.card_id device_code,rm.ca_type,s.county_id,s.area_id  " +
						" from r_card r,r_card_model rm,r_device rd ,s_dept s " +
						" where  s.dept_id=rd.depot_id and r.card_id = ? and r.device_model=rm.device_model and rd.device_id=r.device_id");
		return createQuery(DeviceDto.class,sql, deviceCode).first();
	}
	
	public DeviceDto queryDeviceByStb(String deviceCode) throws JDBCException {
		String sql = StringHelper.append("select r.* from r_stb r where  r.stb_id = ? ");
		return createQuery(DeviceDto.class,sql, deviceCode).first();
	}
	
	/**
	 * 根据设备类型查询型号
	 * @param deviceType
	 * @param modemType 
	 * @return
	 * @throws Exception
	 */
	public List<RDeviceModel> queryDeviceModelByType(String deviceType, String modemType) throws Exception {
		String sql = "select * from vew_device_model where device_type=?";
		if(StringHelper.isNotEmpty(modemType)){
			sql = StringHelper.append(sql," and modem_type='",modemType,"'");
		}
		return this.createQuery(RDeviceModel.class, sql ,deviceType).list();
	}
	
	/**
	 * 根据设备编号查询设备及客户信息
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryDeviceInfoByCode(String deviceCode) throws Exception {
		String sql = "select a.*,case when (r.device_id is  null) then c.cust_no end cust_no," +
				" case when (r.device_id is  null) then c.cust_id end cust_id," +
				"case when (r.device_id is  null) then c.cust_name end cust_name,dp.county_id from ("+
				"SELECT d.*,s.stb_id device_code,"+
				"r.card_id pair_device_code,r.device_model pair_device_model,m.modem_mac pair_device_modem_code,m.device_model pair_device_modem_model," +
				" '' modem_mac,'' modem_type"+
				" FROM R_STB S, R_CARD r,r_modem m,r_device d"+
				" WHERE s.device_id=d.device_id AND s.pair_card_id= r.DEVICE_ID(+)" +
				" AND s.pair_modem_id= m.DEVICE_ID(+) AND S.STB_ID=:deviceCode"+
				" UNION SELECT d.*,c.card_id device_code,'','','','','',''"+
				" FROM R_CARD C,r_device d"+
				" WHERE c.device_id=d.device_id AND C.CARD_ID=:deviceCode"+
				" UNION SELECT d.*,m.modem_mac device_code,'','','','',m.modem_mac,m.modem_type"+
				" FROM R_MODEM M,r_device d WHERE m.device_id=d.device_id AND M.Modem_mac=:deviceCode"+
				" ) a,c_cust c,c_cust_device cd,s_dept dp,r_device_reclaim r" +
				" where r.device_id(+) = cd.device_id and r.status(+)='UNCONFIRM' and cd.device_id(+)=a.device_id and c.cust_id(+)=cd.cust_id and dp.dept_id=a.depot_id" +
				" and dp.status='ACTIVE'";
		Map<String,String> map = new HashMap<String,String>();
		map.put("deviceCode",deviceCode);
		return createNameQuery(DeviceDto.class, sql,map).first();
	}
	
	private String getSql(String deviceModel,String depotId,String status,
			String mode,String depotStatus, String backup, String batch_num){
		String sql = "";
		if(StringHelper.isNotEmpty(mode)){
			if(mode.equals(SystemConstants.DEVICE_TYPE_STB)){
				sql += "select r.*,t.stb_id device_code,"+
						" d.card_id pair_device_code,d.device_model pair_device_model,t.mac modem_mac"+
						" from r_device r,r_stb t,r_card d"+
						" where r.device_id=t.device_id and t.pair_card_id=d.device_id(+) "+
						" and r.depot_id=?";
//			}else if(mode.equals(SystemConstants.DEVICE_TYPE_CARD)){
//				sql += " select r.*,t.card_id device_code,"+
//						"'' pair_device_code,'' pair_device_model,'' modem_mac"+
//						" from r_device r,r_card t"+
//						" where r.device_id=t.device_id " +
//						" and not exists (select s.device_id from r_stb s where s.pair_card_id = t.device_id )"+
//						" and r.depot_id=?";
			}else if(mode.equals(SystemConstants.DEVICE_TYPE_MODEM)){
				sql += " select r.*,t.modem_mac device_code,"+
						"'' pair_device_code,'' pair_device_model,t.modem_mac"+
						" from r_device r,r_modem t"+
						" where r.device_id=t.device_id"+
//						" and not exists (select s.device_id from r_stb s where s.pair_modem_id = t.device_id ) "+
						" and r.depot_id=?";
			}else if(mode.equals("STBCARD")){
				sql += "select r.*,t.stb_id device_code,"+
						"d.card_id pair_device_code,d.device_model pair_device_model,'' modem_mac"+
						" from r_device r,r_stb t,r_card d"+
						" where r.device_id=t.device_id and t.pair_card_id=d.device_id and t.mac is null "+
						" and r.depot_id=?";
			}else if(mode.equals("STBCARDMODEM")){
				sql += "select r.*,t.stb_id device_code,"+
						" d.card_id pair_device_code,d.device_model pair_device_model,t.mac modem_mac"+
						" from r_device r,r_stb t,r_card d"+
						" where r.device_id=t.device_id and t.pair_card_id=d.device_id "
						+ " and t.pair_card_id is not null and t.mac is not null "+
						" and r.depot_id=?";
			}else{
				sql += "select r.*,t.stb_id device_code,"+
					" t.mac modem_mac"+
					" from r_device r,r_stb t"+
					" where r.device_id=t.device_id and t.mac is not null "+
					" and r.depot_id=?";
			}
		}
		
		if(StringHelper.isNotEmpty(deviceModel)){
			sql = StringHelper.append(sql," and t.device_model in (",sqlGenerator.in(deviceModel.split(",")),")");
		}
		if(StringHelper.isNotEmpty(status)){
			sql = StringHelper.append(sql," and r.device_status='",status,"'");
		}
		if(StringHelper.isNotEmpty(depotStatus)){
			sql = StringHelper.append(sql," and r.depot_status='",depotStatus,"'");
		}
		if(StringHelper.isNotEmpty(backup)){
			sql = StringHelper.append(sql, " and r.backup='", backup, "'");
		}
		if(StringHelper.isNotEmpty(batch_num)){
			sql = StringHelper.append(sql, " and r.batch_num='", batch_num, "'");
		}
		
		return sql;
	}
	
	/**
	 * 多条件查询
	 * @param stbModel
	 * @param cardModel
	 * @param modemModel
	 * @param depotId
	 * @param status
	 * @param mode 方式(单机、机卡配对)
	 * @param modemType 
	 * @return
	 * @throws Exception
	 */
	public Pager<DeviceDto> queryDeviceByMultiCriteria(String deviceModel,String depotId,String status,
			String mode,String depotStatus,String modemType, String backup, String batch_num,String start_input_time,String end_input_time, Integer start,Integer limit) 
		throws Exception {
		String sql = getSql(deviceModel, depotId, status, mode, depotStatus, backup, batch_num);
		//如果是猫
		if(SystemConstants.DEVICE_TYPE_MODEM.equals(mode)){
			//猫类型不为空，猫型号为空时
			if(StringHelper.isNotEmpty(modemType) && StringHelper.isEmpty(deviceModel)){
				sql = StringHelper.append(sql," and t.modem_type='",modemType,"'");
			}
		}
		if(StringHelper.isNotEmpty(start_input_time) || StringHelper.isNotEmpty(end_input_time)){
			sql = StringHelper.append(sql, " and r.device_id in ( select tdd.device_id from r_device_done_deviceid tdd,r_device_input rdi " +
					" where tdd.device_done_code = rdi.device_done_code  ");
			if (StringHelper.isNotEmpty(start_input_time)) {
				sql = StringHelper.append(sql, " and rdi.create_time>=to_date('",
						start_input_time, " 00:00:00','yyyy-mm-dd hh24:mi:ss')");
			}
			if (StringHelper.isNotEmpty(end_input_time)) {
				sql = StringHelper.append(sql, " and rdi.create_time<=to_date('",
						end_input_time, " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
			}
			sql = StringHelper.append(sql,")");
		}
	
		sql =StringHelper.append("select a.*,cc.cust_no cust_id,cc.cust_name,cc.str9 from ( ",
				sql," ) a,c_cust_device cd,c_cust cc ",
				" where cd.device_id(+)=a.device_id and cc.cust_id(+)=cd.cust_id order by a.device_type");
		return this.createQuery(DeviceDto.class, sql,depotId).setStart(start).setLimit(limit).page();
	}
	
	
	public List<DeviceDto> queryDeviceByMultiCriteria(String deviceModel,String depotId,String status,
			String mode,String depotStatus,String modemType, String backup, String batch_num,String start_input_time,String end_input_time) 
		throws Exception {
		String sql = getSql(deviceModel, depotId, status, mode, depotStatus, backup, batch_num);
		//如果是猫
		if(SystemConstants.DEVICE_TYPE_MODEM.equals(mode)){
			//猫类型不为空，猫型号为空时
			if(StringHelper.isNotEmpty(modemType) && StringHelper.isEmpty(deviceModel)){
				sql = StringHelper.append(sql," and t.modem_type='",modemType,"'");
			}
		}
		if(StringHelper.isNotEmpty(start_input_time) || StringHelper.isNotEmpty(end_input_time)){
			sql = StringHelper.append(sql, " and r.device_id in ( select tdd.device_id from r_device_done_deviceid tdd,r_device_input rdi " +
					" where tdd.device_done_code = rdi.device_done_code  ");
			if (StringHelper.isNotEmpty(start_input_time)) {
				sql = StringHelper.append(sql, " and rdi.create_time>=to_date('",
						start_input_time, " 00:00:00','yyyy-mm-dd hh24:mi:ss')");
			}
			if (StringHelper.isNotEmpty(end_input_time)) {
				sql = StringHelper.append(sql, " and rdi.create_time<=to_date('",
						end_input_time, " 23:59:59','yyyy-mm-dd hh24:mi:ss')");
			}
			sql = StringHelper.append(sql,")");
		}
	
		sql =StringHelper.append("select a.*,cc.cust_no cust_id,cc.cust_name from ( ",
				sql," ) a,c_cust_device cd,c_cust cc ",
				" where cd.device_id(+)=a.device_id and cc.cust_id(+)=cd.cust_id order by a.device_type");
		return this.createQuery(DeviceDto.class, sql,depotId).list();
	}
	
	public List<DeviceDto> queryIDLEDeviceByMultiCriteria(String deviceModel,String depotId,String status,
			String mode,String depotStatus,String backup) 
		throws Exception {
		String sql = getSql(deviceModel, depotId, status, mode, depotStatus,backup, "");
		sql += " order by device_type";
		
		return this.createQuery(DeviceDto.class, sql,depotId).list();
	}

	public List<DeviceDto> queryDeviceByBatch(String depotId, String batchNum)throws JDBCException{
		final String sql = "SELECT t.* FROM r_device t WHERE t.batch_num = ? AND t.depot_id = ?";
		return this.createQuery(DeviceDto.class, sql, batchNum, depotId).list();
	}
	
	/**
	 * 查询设备信息
	 * @param deviceCodes 设备号
	 * @return
	 * @throws JDBCException
	 */
	public List<DeviceDto> queryByDeviceCodes(String[] deviceCodes) throws JDBCException {
		
		String codes = getSqlGenerator().in(deviceCodes);
		String sql = StringHelper
				.append(
						"SELECT d.*,s.device_model,s.stb_id device_code,",
						"r.card_id pair_device_code,r.device_model pair_device_model,'' modem_mac",
						" FROM R_STB S, R_CARD r,r_device d",
						" WHERE s.device_id=d.device_id AND s.pair_card_id= r.DEVICE_ID(+) AND S.STB_ID in ("+codes+")",
						" UNION SELECT d.*,c.device_model,c.card_id device_code,'','',''",
						" FROM R_CARD C,r_device d",
						" WHERE c.device_id=d.device_id AND C.CARD_ID in ("+codes+")",
						" UNION SELECT d.*,m.device_model,m.modem_mac device_code,'','',m.modem_mac",
						" FROM R_MODEM M,r_device d WHERE m.device_id=d.device_id AND M.Modem_mac in ("+codes+")");
		return createQuery(DeviceDto.class, sql).list();
	}

	
	public List<DeviceDto> queryByDeviceCodescount(String[] deviceCodes,
			String deviceType) throws JDBCException {
		String type = "";
		String sql = "";
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB)) {
			type = "S.STB_ID";
		}
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)) {
			type = "C.CARD_ID";
		}
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)) {
			type = "M.Modem_mac";
		}
		if (type.equals("S.STB_ID")) {
			sql = StringHelper
					.append(
							"SELECT d.*,s.device_model,s.stb_id device_code,",
							"r.card_id pair_device_code,r.device_model pair_device_model,'' modem_mac",
							" FROM R_STB S, R_CARD r,r_device d",
							" WHERE s.device_id=d.device_id AND s.pair_card_id= r.DEVICE_ID(+) and("+getSqlGenerator().setWhereInArray("S.STB_ID",deviceCodes)+") ");
		}
		if (type.equals("C.CARD_ID")) {
			sql = " SELECT d.*,c.device_model,c.card_id device_code,'','','' FROM R_CARD C,r_device d  WHERE c.device_id=d.device_id and("+getSqlGenerator().setWhereInArray("C.CARD_ID",deviceCodes)+")  ";
		}
		if (type.equals("M.Modem_mac")) {
			sql = " SELECT d.*,m.device_model,m.modem_mac device_code,'','',m.modem_mac FROM R_MODEM M,r_device d WHERE m.device_id=d.device_id and("+getSqlGenerator().setWhereInArray("M.Modem_mac",deviceCodes)+") ";					
		}

		return createQuery(DeviceDto.class, sql).list();
	}
	/**
	 * 部分modem只录入了r_modem没有录入到r_device,查询的时候查不到，但是在录入设备的时候会报主键冲突错误.这个方法的目的是这些数据在录入之前也给找出来.
	 * @param deviceCodes
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List<DeviceDto> queryByDeviceCodescountIncludeWrongData(
			String[] deviceCodes, String deviceType) throws Exception{
		String type = "";
		String sql = "";
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB)) {
			type = "S.STB_ID";
		}
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)) {
			type = "C.CARD_ID";
		}
		if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)) {
			type = "M.Modem_mac";
		}
		if (type.equals("S.STB_ID")) {
			sql = StringHelper
					.append(
							"SELECT d.*,s.device_model,s.stb_id device_code,",
							"r.card_id pair_device_code,r.device_model pair_device_model,'' modem_mac",
							" FROM R_STB S, R_CARD r,r_device d",
							" WHERE s.device_id=d.device_id(+) AND s.pair_card_id= r.DEVICE_ID(+) and("+getSqlGenerator().setWhereInArray("S.STB_ID",deviceCodes)+") ");
		}
		if (type.equals("C.CARD_ID")) {
			sql = " SELECT d.*,c.device_model,c.card_id device_code,'','','' FROM R_CARD C,r_device d  WHERE c.device_id=d.device_id(+) and("+getSqlGenerator().setWhereInArray("C.CARD_ID",deviceCodes)+")  ";
		}
		if (type.equals("M.Modem_mac")) {
			sql = " SELECT d.*,m.device_model,m.modem_mac device_code,'','',m.modem_mac FROM R_MODEM M,r_device d WHERE m.device_id=d.device_id(+) and("+getSqlGenerator().setWhereInArray("M.Modem_mac",deviceCodes)+") ";					
		}

		return createQuery(DeviceDto.class, sql).list();
	}
	
	/**
	 * 查询仓库下差异的设备，未确认和确认
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public Pager<DeviceDto> queryDiffence(String deviceCode,String depotId,Integer start,Integer limit)
			throws JDBCException {
		String stbSql = "",cardSql = "",modemSql = "";
		if(StringHelper.isNotEmpty(deviceCode)){
			stbSql = " and stb_id='"+deviceCode+"'";
			cardSql = " and card_id='"+deviceCode+"'";
			modemSql = " and modem_mac='"+deviceCode+"'";
		}
		String depotSql = "",diffDepotSql = "";
		if(!depotId.equals(SystemConstants.COUNTY_ALL)){
			depotSql =" AND d.depot_id='"+depotId+"'";
			diffDepotSql =" AND rdd.depot_id(+)='"+depotId+"'";
		}
		String sql = "SELECT d.*,s.stb_id device_code,"
				+ "r.card_id pair_device_code,r.device_model pair_device_model,"
				+ " m.modem_mac pair_device_modem_code,m.device_model pair_device_modem_model,'' modem_mac "
				+ " FROM R_STB S, R_CARD r ,R_modem m ,r_device d"
				+ " WHERE s.pair_card_id= r.DEVICE_ID(+) AND s.pair_modem_id= m.DEVICE_ID(+)"
				+ " AND s.device_id=d.device_id  AND d.diffence_type in (:uncheck,:diff) " + depotSql + stbSql
				+ " UNION SELECT d.*,c.card_id device_code,'','','','','' "
				+ " FROM R_CARD C,r_device d"
				+ " WHERE d.device_id=c.device_id and d.is_virtual='F' AND d.diffence_type in (:uncheck,:diff)" + depotSql + cardSql
				+ " UNION SELECT d.*,m.modem_mac device_code,'','','','',m.modem_mac "
				+ " FROM R_MODEM m,r_device d "
				+ " WHERE d.device_id=m.device_id and d.is_virtual='F' AND  d.diffence_type in (:uncheck,:diff)" + depotSql + modemSql;
		Map<String, Serializable> paramers = new HashMap<String, Serializable>();
		paramers.put("uncheck", SystemConstants.DEVICE_DIFFENCN_TYPE_UNCHECK);
		paramers.put("diff", SystemConstants.DEVICE_DIFFENCN_TYPE_DIFF);
		sql = "select d.*, max(rdd.create_time) create_time from ( " + sql + " ) d, R_DEVICE_DIFEENCE rdd"
			+" where d.device_id = rdd.device_id(+)" + diffDepotSql
			+" group by d.device_id,d.device_type,"
			+" d.device_model,d.device_status,d.depot_status,d.tran_status,d.used,d.backup,d.freezed,"
			+" d.diffence_type,d.depot_id,d.ownership,d.ownership_depot,d.warranty_date,d.is_virtual,"
			+" d.is_local,d.is_loss,d.is_new_stb,device_code,pair_device_code,pair_device_model,"
			+" pair_device_modem_code,pair_device_modem_model,modem_mac,batch_num,total_num order by create_time desc";
		return createNameQuery(DeviceDto.class, sql, paramers).setStart(start).setLimit(limit).page();

	}

	/**
	 * 更新差异状态
	 * @param deviceIds
	 * @param diffenceType
	 * @throws JDBCException
	 */
	public void updateDiffenceType(String[] deviceIds, String diffenceType)
			throws JDBCException {
		String sql = "UPDATE r_device SET diffence_type='" + diffenceType
				+ "' WHERE device_id=?";
		executeBatch(sql, deviceIds);
		
		//虚拟设备
		sql = "UPDATE r_device SET diffence_type=? WHERE device_id in ( "
			+ " select t.pair_card_id  "
			+ " from r_stb t "
			+ " where t.pair_card_id is not null and t.device_id in ("+sqlGenerator.in(deviceIds)+")"
			+ " union all "
			+ " select t.pair_modem_id  "
			+ " from r_stb t "
			+ " where t.pair_modem_id is not null and t.device_id in ("+sqlGenerator.in(deviceIds)+"))";
		executeUpdate(sql, diffenceType);
	}

	/**
	 * 更新仓库状态为使用
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void updateDepotStatus(Integer doneCode,String deviceId,String busiCode,String buyMode,String depotStatus,SOptr optr) throws JDBCException {
		String str = "";
		if(StringHelper.isNotEmpty(deviceId)){
			str = " and t.device_id ='"+deviceId+"' ";
		}
		
		String sql = "UPDATE r_device SET depot_status=?, ownership = ( case when  ? = 'T' and ownership= 'GD' then 'CUST' " +
				" WHEN ? = 'T' and ownership= 'CUST' THEN 'GD' ELSE ownership  end " +
				" ) WHERE device_id IN ("
			+ " SELECT device_id FROM r_device_done_deviceid t WHERE t.device_done_code=? "+str+")";
		executeUpdate(sql, depotStatus,buyMode, buyMode,doneCode);
	

		//虚拟设备
		sql = "UPDATE r_device SET depot_status=?,ownership = ( case when  ? = 'T' and ownership= 'GD' then 'CUST' " +
				" WHEN ? = 'T' and ownership= 'CUST' THEN 'GD' ELSE ownership  end " +
				" ) WHERE device_id in ( "
			+ " select t.pair_card_id  "
			+ " from r_device_done_deviceid di,r_stb t "
			+ " where di.device_id=t.device_id and t.pair_card_id is not null "
			+ " and di.device_done_code=? "+str+""
			+ " union all "
			+ " select t.pair_modem_id  "
			+ " from r_device_done_deviceid di,r_stb t "
			+ " where di.device_id=t.device_id and t.pair_modem_id is not null "
			+ " and di.device_done_code=? "+str+")";
		executeUpdate(sql, depotStatus,buyMode,buyMode, doneCode, doneCode);
	}

	
	/**
	 * 将设备移到r_device_his表
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void removeToHis(Integer doneCode) throws JDBCException {
		String sql = "insert into r_device_his"+
				"    (device_id, device_type, device_status, depot_status, used, backup, freezed, diffence_type, depot_id, ownership, warranty_date, is_virtual, is_local, "+
				"      stb_id, pair_card_id, card_id, modem_id, modem_mac, is_new_stb, device_model, pair_modem_id, batch_num,total_num)"+
				" select r.device_id, r.device_type, r.device_status, r.depot_status, r.used, r.backup, r.freezed, r.diffence_type, r.depot_id, r.ownership, r.warranty_date, r.is_virtual, r.is_local,"+
				" rs.stb_id,rs.pair_card_id,rc.card_id,rm.modem_id,rm.modem_mac,r.is_new_stb,r.device_model,rs.pair_modem_id,r.batch_num,r.total_num"+
				" from r_device_output o,r_device_done_deviceid d,r_device r,r_stb rs,r_card rc,r_modem rm"+
				" where o.device_done_code=d.device_done_code "+
				" and r.device_id=d.device_id and rs.device_id=r.device_id"+
				" and rc.device_id(+)=rs.pair_card_id and rm.device_id(+)=rs.pair_modem_id"+
				" and o.device_done_code=?"+
				" union all"+
				" select r.device_id, r.device_type, r.device_status, r.depot_status, r.used, r.backup, r.freezed, r.diffence_type, r.depot_id, r.ownership, r.warranty_date, r.is_virtual, r.is_local,"+
				" null stb_id,null pair_card_id,rc.card_id,null modem_id,null modem_mac,r.is_new_stb,r.device_model,null pair_modem_id,r.batch_num,r.total_num"+
				" from r_device_output o,r_device_done_deviceid d,r_device r,r_stb rs,r_card rc"+
				" where o.device_done_code=d.device_done_code "+
				" and rs.device_id=d.device_id and rs.pair_card_id=r.device_id"+
				" and rs.pair_card_id=rc.device_id"+
				" and o.device_done_code=? "+
				" union all"+
				" select r.device_id, r.device_type, r.device_status, r.depot_status, r.used, r.backup, r.freezed, r.diffence_type, r.depot_id, r.ownership, r.warranty_date, r.is_virtual, r.is_local,"+
				" null stb_id,null pair_card_id, null card_id,rm.modem_id,rm.modem_mac,r.is_new_stb,r.device_model,null pair_modem_id,r.batch_num,r.total_num"+
				" from r_device_output o,r_device_done_deviceid d,r_device r,r_stb rs,r_modem rm"+
				" where o.device_done_code=d.device_done_code "+
				" and d.device_id=rs.device_id "+
				" and rs.pair_modem_id=r.device_id and rs.pair_modem_id=rm.device_id"+
				" and o.device_done_code=?"+
				" union all"+
				" select r.device_id, r.device_type, r.device_status, r.depot_status, r.used, r.backup, r.freezed, r.diffence_type, r.depot_id, r.ownership, r.warranty_date, r.is_virtual, r.is_local,"+
				" null stb_id,null pair_card_id,rc.card_id,null modem_id,null modem_mac,r.is_new_stb,r.device_model,null pair_modem_id,r.batch_num,r.total_num"+
				" from r_device_output o,r_device_done_deviceid d,r_device r,r_card rc"+
				" where o.device_done_code=d.device_done_code "+
				" and r.device_id=d.device_id and  d.device_id=rc.device_id"+
				" and o.device_done_code=?"+
				" union all"+
				" select r.device_id, r.device_type, r.device_status, r.depot_status, r.used, r.backup, r.freezed, r.diffence_type, r.depot_id, r.ownership, r.warranty_date, r.is_virtual, r.is_local,"+
				" null stb_id,null pair_card_id, null card_id,rm.modem_id,rm.modem_mac,r.is_new_stb,r.device_model,null pair_modem_id,r.batch_num,r.total_num"+
				" from r_device_output o,r_device_done_deviceid d,r_device r,r_modem rm"+
				" where o.device_done_code=d.device_done_code "+
				" and d.device_id=r.device_id"+
				" and d.device_id=rm.device_id"+
				" and o.device_done_code=? ";
		
		executeUpdate(sql, doneCode, doneCode, doneCode,doneCode,doneCode);
		
		sql = "select * from r_device r where r.device_id IN ( SELECT device_id FROM r_device_done_deviceid t WHERE t.device_done_code=?)";
		List<RDevice> deviceList = createQuery(sql, doneCode).list();
		
		for(RDevice device : deviceList){
			String deviceId = device.getDevice_id();
			if(SystemConstants.DEVICE_TYPE_CARD.equals(device.getDevice_type())){
				sql = "delete r_card where device_id=?";
				executeUpdate(sql, deviceId);
			}else if(SystemConstants.DEVICE_TYPE_STB.equals(device.getDevice_type())){
				RStb stb = createQuery(RStb.class,"select * from r_stb where device_id=?", device.getDevice_id()).first();
				if(StringHelper.isNotEmpty(stb.getPair_card_id())){
					sql = "delete r_card where device_id = (select device_id from r_device where device_id=? )";
					executeUpdate(sql, stb.getPair_card_id());
					
					sql = "delete r_device where device_id=? ";
					executeUpdate(sql, stb.getPair_card_id());
				}
				if(StringHelper.isNotEmpty(stb.getPair_modem_id())){
					sql = "delete r_modem where device_id = (select device_id from r_device where device_id=? )";
					executeUpdate(sql, stb.getPair_modem_id());
					
					sql = "delete r_device where device_id=? ";
					executeUpdate(sql, stb.getPair_modem_id());
				}
				sql = "delete r_stb where device_id=?";
				executeUpdate(sql, deviceId);
			}else if(SystemConstants.DEVICE_TYPE_MODEM.equals(device.getDevice_type())){
				sql = "delete r_modem where device_id=?";
				executeUpdate(sql, deviceId);
			}
			
			sql = "delete r_device where device_id=?";
			executeUpdate(sql, deviceId);
		}
	}
	/**
	 * 仓库状态空闲的设备修改设备状态
	 * @param depotId
	 * @param deviceIds
	 * @param deviceStatus
	 * @param isNewStb 
	 * @throws JDBCException
	 */
	public void updateDeviceStatus(String depotId, String[] deviceIds,
			String deviceStatus, String isNewStb) throws JDBCException {
		String sql = "UPDATE r_device SET device_status=? ,is_new_stb = ? " + 
				" WHERE  depot_id=? AND depot_status=?  and ("+getSqlGenerator().setWhereInArray("device_id",deviceIds)+")  ";
		executeUpdate(sql, deviceStatus,isNewStb, depotId, StatusConstants.IDLE);
		
		//虚拟设备
		sql = "UPDATE r_device SET device_status=?,is_new_stb = ? WHERE device_id in ( "
			+ " select t.pair_card_id  "
			+ " from r_stb t "
			+ " where t.pair_card_id is not null and ("+getSqlGenerator().setWhereInArray("device_id", deviceIds)+")"
			+ " union all "
			+ " select t.pair_modem_id  "
			+ " from r_stb t "
			+ " where t.pair_modem_id is not null and ("+getSqlGenerator().setWhereInArray("device_id", deviceIds)+"))";
		executeUpdate(sql, deviceStatus,isNewStb);
	}
	
	public boolean isExistsDeviceByDepotId(String depotId) throws JDBCException {
		String sql = "select count(1) from r_device where depot_id=?";
		return this.count(sql, depotId)>0;
	}
	
	/**
	 * 根据卡号查机顶盒号
	 * @param cardId
	 * @return
	 * @throws JDBCException
	 */
	public DeviceDto queryDeviceByCardId(String cardId) throws JDBCException {
		String sql = "select rs.stb_id device_code from r_stb rs ,r_card rc " +
				" where rs.pair_card_id = rc.device_id and rc.card_id = ? ";
		return createQuery(DeviceDto.class, sql,cardId).first();
	}
	
	
	public List<DeviceDto> queryCardModemByDeviceCodes(String[] deviceCodes) throws JDBCException {
		String sql = StringHelper
				.append( "select s.stb_id  device_code,c.card_id pair_device_code from r_stb s,r_card c where s.pair_card_id = c.device_id  and s.stb_id <> c.card_id " +
						" and ("+getSqlGenerator().setWhereInArray("c.card_id", deviceCodes)+")" +
						" union select  s.stb_id device_code,m.modem_mac pair_device_code from r_stb s,r_modem m " +
						" where s.pair_modem_id = m.device_id and ("+getSqlGenerator().setWhereInArray("m.modem_mac", deviceCodes)+")");
		return createQuery(DeviceDto.class, sql).list();
	}

	public DeviceDto queryByDeviceId(String deviceId) throws JDBCException {
		String sql = "select t.device_id,t.pair_card_id,t.pair_modem_id from r_stb t where t.pair_card_id = ? or t.pair_modem_id = ? " +
				" union select r.device_id ,r.pair_card_id pair_device_code,r.pair_modem_id  pair_device_modem_code " +
				" from r_stb r where r.device_id =? ";
		return createQuery(DeviceDto.class,sql, deviceId,deviceId,deviceId).first();

	}
	
	public void updateIsNewStb(String[] deviceIds,String isNewStb) throws JDBCException {
		String sql = "UPDATE r_device SET is_new_stb =?" +
				" WHERE  IS_NEW_STB<>? and " + getSqlGenerator().setWhereInArray("device_id",deviceIds);
		executeUpdate(sql, isNewStb,isNewStb);
	}
	
	public boolean isDeviceProcure(String deviceId) throws Exception {
		String sql  ="select count(1) from r_device_procure t,r_device_done_deviceid r " +
		"where t.device_done_code=r.device_done_code and t.procure_type='YTJGM' and r.device_id =? ";
		return count(sql, deviceId) > 0;
	}
	
	public Pager<DeviceDto> queryTransferDeviceDetail(
			int deviceDoneCode,String deviceType,Integer start, Integer limit) throws JDBCException {
		String sql = "";
		
		if(deviceType.equals(SystemConstants.DEVICE_TYPE_FITTING)){
			sql = "SELECT r.*,'' device_code FROM r_device r,r_device_done_deviceid di"
					+ " where r.device_id=di.device_id and di.device_done_code=? order by r.device_id";
		}else{
			if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB)) {
				sql += "SELECT r.*,b.stb_id device_code FROM r_device r ,r_stb b,r_device_done_deviceid di ";
			} else if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)) {
				sql += "SELECT r.*,b.card_id device_code FROM r_device r ,r_card b,r_device_done_deviceid di ";
			} else if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)) {
				sql += "SELECT r.*,b.modem_id device_code FROM r_device r ,r_modem b,r_device_done_deviceid di ";
			}
			sql += "  where  di.device_id = b.device_id and b.device_id=r.device_id and  di.device_done_code=?  order by device_code";
		}
		return createQuery(DeviceDto.class,sql, deviceDoneCode)
				.setStart(start).setLimit(limit).page();
	}

	public RDevice queryIdleMateralDevice(String deviceModel,String depotId) throws Exception {
		String sql = "select * from r_device where depot_id=? and device_type=? and device_model=? and device_status=? "
				+ "and depot_status=? and tran_status=?";
		return createQuery(RDevice.class, sql,depotId,SystemConstants.DEVICE_TYPE_FITTING,deviceModel,
				StatusConstants.ACTIVE,StatusConstants.IDLE,StatusConstants.IDLE).first();
	}
	
	public List<RDevice> queryMateralDeviceByDepotId(String depotId) throws Exception {
		String sql = "select * from r_device where depot_id=? and device_type =? and device_status=? "
				+ "and depot_status=? and tran_status=?";
		return createQuery(RDevice.class, sql,depotId,SystemConstants.DEVICE_TYPE_FITTING
				,StatusConstants.ACTIVE,StatusConstants.IDLE,StatusConstants.IDLE).list();
	}
	
	public List<RDevice> queryDeviceByIds(String[] deviceIds) throws Exception {
		String sql = "select * from r_device where("+getSqlGenerator().setWhereInArray("device_id", deviceIds)+") ";
		return createQuery(RDevice.class, sql).list();
	}
	
	public List<RDevice> queryDeviceByDoneCode(Integer doneCode)throws JDBCException {
		String sql = "select * from r_device WHERE device_id IN "
				+ " (SELECT device_id FROM r_device_done_deviceid t WHERE t.device_done_code=?)";
		return createQuery(RDevice.class, sql,doneCode).list();
	}
	
	public void removeDeviceToHis(String deviceId) throws JDBCException {
		String sql = "insert into r_device_his"+
				"    (device_id, device_type, device_status, depot_status, used, backup, freezed, diffence_type, depot_id, ownership, warranty_date, is_virtual, is_local, "+
				"       is_new_stb, device_model, batch_num,total_num)"+
				" select r.device_id, r.device_type, r.device_status, r.depot_status, r.used, r.backup, r.freezed, r.diffence_type, r.depot_id, r.ownership, r.warranty_date, r.is_virtual, r.is_local,"+
				" r.is_new_stb,r.device_model,r.batch_num,r.total_num"+
				" from r_device_done_deviceid d,r_device r"+
				" where r.device_id=? ";
		executeUpdate(sql, deviceId);	
		
		sql = "delete r_device where device_id=?";
		executeUpdate(sql, deviceId);
	}
	
	public void updateMateralTransferDepot(String deviceId, String depotOrder)
			throws JDBCException {
		String sql = "UPDATE r_device SET tran_status=?,depot_id=? WHERE device_id =? ";

		executeUpdate(sql, StatusConstants.IDLE,depotOrder, deviceId);
	}

	public void removeMateralDevice(String device_id, Integer total_num) throws JDBCException{
		String sql = "UPDATE r_device SET total_num= total_num-?  WHERE device_id=?";
		executeUpdate(sql,total_num, device_id);
	}
	
	public void addMateralDevice(String device_id, Integer total_num) throws JDBCException{
		String sql = "UPDATE r_device SET total_num= total_num + ?  WHERE device_id=?";
		executeUpdate(sql,total_num, device_id);
	}
	
	public List<DeviceSmallDto> getDeviceCodeByDeviceId(String[] deviceIds) throws JDBCException{
		String sql = "select T.CARD_ID device_code,t.device_id from r_card T WHERE " + getSqlGenerator().setWhereInArray("t.device_id",deviceIds)
				+ "UNION select t.stb_id device_code,t.device_id from r_Stb T WHERE  " + getSqlGenerator().setWhereInArray("t.device_id",deviceIds)
				+ "UNION select t.modem_mac device_code,t.device_id from r_modem T WHERE "  + getSqlGenerator().setWhereInArray("t.device_id",deviceIds);
		return createQuery(DeviceSmallDto.class, sql).list();
	}

	public com.ycsoft.business.dto.device.DeviceDto queryDeviceInfoByCodeAndModel(String deviceCode) throws JDBCException{
		String sql = "select T.CARD_ID device_code,t.device_model,r.* from r_card T,r_device r WHERE r.device_id=t.device_id and t.card_id = ?  "
				+ "UNION select t.stb_id device_code,t.device_model,r.* from r_Stb T,r_device r WHERE r.device_id=t.device_id and t.stb_id = ?  "
				+ "UNION select t.modem_mac device_code,t.device_model,r.* from r_modem T,r_device r WHERE r.device_id=t.device_id and t.modem_mac = ?  ";
		return createQuery(com.ycsoft.business.dto.device.DeviceDto.class, sql,deviceCode,deviceCode,deviceCode).first();
	}
	
}

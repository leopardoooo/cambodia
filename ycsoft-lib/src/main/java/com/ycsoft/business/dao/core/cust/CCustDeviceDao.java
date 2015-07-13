/**
 * CCustDeviceDao.java	2010/05/05
 */

package com.ycsoft.business.dao.core.cust;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.business.dto.core.cust.CustDeviceDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CCustDeviceDao -> C_CUST_DEVICE table's operator
 */
@Component
public class CCustDeviceDao extends BaseEntityDao<CCustDevice> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7328893146262653280L;

	/**
	 * default empty constructor
	 */
	public CCustDeviceDao() {
	}
	
	public CCustDevice queryCustDeviceByDeviceCode(String custId, String deviceCode) throws JDBCException {
		String sql = "select * from c_cust_device where device_code=? and cust_id=?";
		return this.createQuery(sql, deviceCode, custId).first();
	}
	
	public CCustDevice queryCustDeviceByDeviceId(String custId, String deviceId) throws JDBCException {
		String sql = "select * from c_cust_device where device_id=? and cust_id=?";
		return this.createQuery(sql, deviceId, custId).first();
	}
	
	public List<CCustDevice> queryDevices(String custId) throws JDBCException {
		String sql = "select * from c_cust_device where cust_id=?";
		return this.createQuery(sql, custId).list();
	}

	/**
	 * 删除客户单个设备
	 * @param custId
	 * @param deviceId
	 * @throws Exception
	 */
	public void removeDevice(String custId,String deviceId,Integer doneCode,String isReclaim) throws Exception{
		String sql = "insert into C_CUST_DEVICE_HIS(DONE_CODE,CUST_ID,DEVICE_TYPE," +
				" DEVICE_ID,DEVICE_CODE,PAIR_CARD_ID,BUY_MODE,BUY_TIME,STATUS,STATUS_DATE," +
				" COUNTY_ID,AREA_ID,PAIR_CARD_CODE,LOSS_REG,BUY_DONE_CODE,PAIR_MODEM_ID,PAIR_MODEM_CODE,IS_RECLAIM,replacover_date,change_reason)" +
				" select ?,CUST_ID,DEVICE_TYPE,DEVICE_ID,DEVICE_CODE,PAIR_CARD_ID,BUY_MODE," +
				" BUY_TIME,STATUS,sysdate,COUNTY_ID,AREA_ID,PAIR_CARD_CODE,LOSS_REG," +
				" DONE_CODE,PAIR_MODEM_ID,PAIR_MODEM_CODE,?,replacover_date,change_reason from C_CUST_DEVICE where cust_id=? and device_id=?";
		executeUpdate(sql, doneCode,isReclaim, custId, deviceId);
		
		sql = "delete c_cust_device where cust_id=? and device_id=?";
		executeUpdate(sql, custId,deviceId);
	}

	/**
	 * 删除客户单个设备
	 * @param custId
	 * @param deviceId
	 * @throws Exception
	 */
	public void removeAllDevice(String custId) throws Exception{
		String sql = "delete c_cust_device where cust_id=? ";
		executeUpdate(sql, custId);
	}
	/**
	 * 修改客户设备状态
	 * @param custId
	 * @param deviceId
	 * @param status
	 * @throws Exception
	 */
	public void updateDeviceStatus(String custId,String deviceId,String status) throws Exception{
		String sql = "update c_cust_device set status=? where cust_id=? and device_id=?";
		executeUpdate(sql, status,custId,deviceId);
	}

	/**
	 * 根据设备真实号修改客户设备状态
	 * @param custId
	 * @param deviceId
	 * @param status
	 * @throws Exception
	 */
	public void updateDeviceStatusByDeviceCode(String custId,String deviceCode,String status) throws Exception{
		String sql = "update c_cust_device set status=? where cust_id=? and device_code=? ";
		executeUpdate(sql, status,custId,deviceCode);
	}

	/**
	 * 根据设备id查询客户设备
	 * @param deviceId
	 * @return
	 */
	public CCustDevice findByDeviceId(String deviceId, String countyId)
			throws JDBCException {
		String sql = "select * from c_cust_device where device_id=? and county_id=?";
		return createQuery(sql, deviceId, countyId).first();
	}
	
	/**
	 * 根据设备编号查询客户设备
	 * @param deviceId
	 * @return
	 */
	public CCustDevice findByDeviceCode(String deviceCode, String countyId)
			throws JDBCException {
		String sql = "select * from c_cust_device where device_code=? and county_id=?";
		return createQuery(sql, deviceCode, countyId).first();
	}

	public List<CCustDevice> findBuyModeById(String custId,String[] deviceCode, String countyId)
			throws JDBCException {
		List<String> params = new ArrayList<String>();
		params.add(countyId);
		String sql = "select * from c_cust_device where  county_id=? and "+getSqlGenerator().setWhereInArray("device_code",deviceCode)+" ";
		if(StringHelper.isNotEmpty(custId)){
			sql += " and cust_id =? ";
			params.add(custId);
		}
		return createQuery(sql, params.toArray()).list();
	}

	
	/**
	 * 查询客户下的所有设备
	 * @param custId
	 * @param countyId
	 * @return
	 */
	public List<CustDeviceDto> queryCustDevices(String custId, String custStatus, String countyId)
			throws JDBCException {
		if (!custStatus.equals(StatusConstants.INVALID)) {
			String sql = "select cd.done_code,cd.replacover_date, cd.cust_id, cd.device_type, cd.device_id, cd.device_code,"
					+ " cd.pair_card_id, cd.buy_mode, cd.buy_time, cd.status, cd.status_date, cd.county_id,"
					+ " cd.area_id, cd.pair_card_code, cd.loss_reg,cd.pair_modem_id,cd.pair_modem_code, "
					+ " r.ownership,r.device_model,"
					+ "(select s.device_id from r_stb s,c_cust_device c"
						+ " where (s.pair_card_id = r.device_id or s.pair_modem_id = r.device_id)"
						+ " and s.device_id=c.device_id and c.cust_id=? and c.county_id=?"
						+ ") pair_stb_device_id,"
					+ "(select d.is_virtual from r_device d where d.device_id=cd.pair_card_id) is_virtual_card,"
					+ "(select d.is_virtual from r_device d where d.device_id=cd.pair_modem_id) is_virtual_modem,sm.definition_type,sm.interactive_type, r.depot_id,cd.change_reason"
					+ " from c_cust_device cd,r_device r,r_stb s,r_stb_model sm "
					+ " where cd.device_id = r.device_id(+) AND s.pair_card_id(+)=r.device_id and r.device_model=sm.device_model(+)"
					+ " and cd.cust_id=? and cd.county_id=?"
					+ " and not exists (select 1 from r_device_reclaim dr where dr.device_id=cd.device_id and dr.status=?)";
			return createQuery(CustDeviceDto.class, sql, custId, countyId,
					custId, countyId, StatusConstants.UNCONFIRM).list();
		}else{
			String sql = " select cd.done_code, cd.replacover_date,cd.cust_id, cd.device_type, cd.device_id, cd.device_code,"
					+ " cd.pair_card_id, cd.buy_mode, cd.buy_time, cd.status, cd.status_date, cd.county_id,"
					+ " cd.area_id, cd.pair_card_code, cd.loss_reg,cd.pair_modem_id,cd.pair_modem_code, "
					+ " r.ownership,r.device_model,"
					+ "(select s.device_id from r_stb s,c_cust_device c"
						+ " where (s.pair_card_id = r.device_id or s.pair_modem_id = r.device_id) "
						+ " and s.device_id=c.device_id and c.cust_id=? and c.county_id=?"
						+ ") pair_stb_device_id, "
					+ "(select d.is_virtual from r_device d where d.device_id=cd.pair_card_id) is_virtual_card,"
					+ "(select d.is_virtual from r_device d where d.device_id=cd.pair_modem_id) is_virtual_modem,sm.definition_type, r.depot_id,cd.change_reason"
					+ " from c_cust_device_his cd,r_device r,r_stb s,r_stb_model sm "
					+ " where cd.device_id = r.device_id(+) AND s.pair_card_id(+)=r.device_id and r.device_model=sm.device_model(+)"
					+ " and cd.cust_id=? and cd.county_id=?"
					+ " and (cd.done_code,cd.device_code) in ("
					+ " select distinct max(t.done_code),t.device_code from c_cust_device_his t"
					+ " where t.cust_id = ? and t.county_id = ? group by t.device_code)"
					+ " and not exists (select 1 from r_device_reclaim dr where dr.device_id=cd.device_id and dr.status=?)"
					+ " and not exists (select 1 from c_cust_device dr"
					+ " where dr.device_id = cd.device_id and dr.cust_id <> cd.cust_id)"
					+ " and r.depot_status=?";
			
			return createQuery(CustDeviceDto.class, sql, custId, countyId,
					custId, countyId, custId, countyId,
					StatusConstants.UNCONFIRM, StatusConstants.USE).list();
		}
	}
	
	public CustDeviceDto queryDevice(String deviceCode) throws JDBCException {
		String sql = "SELECT * FROM c_cust_device cd,r_stb s where s.device_id=cd.device_id and s.stb_id=?";
		return createQuery(CustDeviceDto.class, sql, deviceCode).first();
	}

	/**
	 * @param custId
	 * @param deviceId
	 * @param buyMode
	 */
	public void updateDeviceBuyMode(String custId, String deviceId,
			String buyMode)  throws JDBCException {
		String sql ="update c_cust_device set buy_mode=? where cust_id=? and device_id=?";
		executeUpdate(sql,buyMode, custId,deviceId);

	}

	/**
	 * 修改挂失信息
	 * @param custId
	 * @param deviceId
	 */
	public void updateLossReg(String custId, String deviceId,String lossFlag) throws JDBCException  {
		String sql ="update c_cust_device set loss_reg=? where cust_id=? and device_id=?";
		executeUpdate(sql,lossFlag, custId,deviceId);
	}

	public void updatePairCard(String stbDeviceId, String newCardId,String newCardCode) throws JDBCException {
		String sql ="update c_cust_device set pair_card_id=?,pair_card_code=? where device_id=?";
		executeUpdate(sql,newCardId,newCardCode,stbDeviceId);
	}
	
	public void updatePairModem(String stbDeviceId, String newModemId,String newModemCode) throws JDBCException {
		String sql ="update c_cust_device set pair_modem_id=?,pair_modem_code=? where device_id=?";
		executeUpdate(sql,newModemId,newModemCode,stbDeviceId);
	}

}

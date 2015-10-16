/**
 * RDeviceChangeDao.java	2010/05/07
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceChange;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RDeviceChangeDao -> R_DEVICE_CHANGE table's operator
 */
@Component
public class RDeviceChangeDao extends BaseEntityDao<RDeviceChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2696323709514185586L;

	/**
	 * default empty constructor
	 */
	public RDeviceChangeDao() {}

	/**
	 * @param doneCode
	 */
	public void removeByDoneCode(Integer doneCode) throws Exception{
		String sql ="delete R_DEVICE_CHANGE where done_code=?";
		executeUpdate(sql, doneCode);

	}

	public List<RDeviceChange> queryByDoneCode(Integer doneCode) throws Exception{
		return findList("select * from R_DEVICE_CHANGE where done_code=?", doneCode);
	}

	/**
	 * 根据流水号恢复修改过的属性
	 * @param doneCode
	 * @throws Exception
	 */
	public void recover(Integer doneCode)throws Exception {
		List<RDeviceChange> changeList = queryByDoneCode(doneCode);
		for (RDeviceChange change :changeList){
			String sql = "update r_device set "+ change.getColumn_name() + "=? where device_id=?";
			executeUpdate(sql,change.getOld_value(),change.getDevice_id());
		}
	}
	
	public List<RDeviceChange> queryNearDeviceChange(String deviceId,
			String busiCode) throws JDBCException {
		String sql ="select * from r_device_change where done_code=("
			+" select max(t.done_code) from r_device_change t"
			+" where t.device_id=? and t.busi_code=?)";
		return this.createQuery(sql, deviceId, busiCode).list();
	}
	
	/**
	 * 入库异动
	 * @param doneCode
	 * @param busiCode
	 * @param county_id
	 * @param area_id
	 * @throws JDBCException
	 */
	public void saveInputChange(Integer doneCode,String busiCode,String county_id,String area_id) throws JDBCException {
		String sql = "insert into r_device_change (done_code, busi_code, device_id, pair_card_id,pair_modem_id,column_name, " +
				" old_value, new_value, change_date, optr_id, dept_id, county_id, area_id) select ?,?," +
				" rd.device_id,s.pair_card_id,s.pair_modem_id, 'depot_id',null,rd.depot_id,sysdate,i.optr_id,i.depot_id,?,? " +
				" from r_device_input i,r_device_done_deviceid d,r_device rd,r_stb s " +
				" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id and s.device_id(+)=rd.device_id " +
				" and i.device_done_code= ? ";
		executeUpdate(sql, doneCode, busiCode,county_id,area_id,doneCode);
	}
	
	/**
	 * 调拨异动
	 * @param doneCode
	 * @param busiCode
	 * @param county_id
	 * @param area_id
	 * @throws JDBCException
	 */
	public void saveTransChange(Integer doneCode,String busiCode,String county_id,String area_id) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id,pair_card_id,pair_modem_id,column_name," +
				" old_value, new_value, change_date, optr_id, dept_id, county_id, area_id) select ?,?,rd.device_id," +
				" s.pair_card_id,s.pair_modem_id,'trans_status','IDLE','UNCONFIRM',sysdate,i.optr_id,i.depot_source,?,? " +
				" from r_device_transfer i,r_device_done_deviceid d,r_device rd,r_stb s " +
				" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id and s.device_id(+)=rd.device_id and i.device_done_code= ? ";
		executeUpdate(sql, doneCode, busiCode,county_id,area_id,doneCode);
	}
	
	
	/**
	 * 调拨确认
	 * @param doneCode
	 * @param busiCode
	 * @param deviceDoneCode
	 * @param county_id
	 * @param area_id
	 * @throws JDBCException
	 */
	public void saveTransArrirmChange(Integer doneCode,String busiCode,Integer deviceDoneCode,String county_id,String area_id) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id,pair_card_id,pair_modem_id,column_name, old_value," +
				" new_value, change_date, optr_id, dept_id, county_id, area_id) select ?,?,rd.device_id,s.pair_card_id,s.pair_modem_id, " +
				" 'depot_id',i.depot_source,i.depot_order,sysdate,i.confirm_optr_id,i.depot_order,?,?  " +
				" from r_device_transfer i,r_device_done_deviceid d,r_device rd,r_stb s " +
				" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id and s.device_id(+)=rd.device_id " +
				" and i.device_done_code= ?  ";
		executeUpdate(sql, doneCode, busiCode,county_id,area_id,deviceDoneCode);
	}
	
	/**
	 * 器材确认调拨异动
	 * @param doneCode
	 * @param busiCode
	 * @param deviceDoneCode
	 * @param county_id
	 * @param area_id
	 * @throws JDBCException
	 */
	public void saveMateralTransArrirmChange(Integer doneCode,String busiCode,Integer deviceDoneCode,String county_id,String area_id) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id,column_name, old_value," +
				" new_value, change_date, optr_id, dept_id, county_id, area_id) select ?,?,rd.device_id, " +
				" 'depot_id',i.depot_source,i.depot_order,sysdate,i.confirm_optr_id,i.depot_order,?,?  " +
				" from r_device_transfer i,r_device_done_deviceid d,r_device rd " +
				" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id " +
				" and i.device_done_code= ?  ";
		executeUpdate(sql, doneCode, busiCode,county_id,area_id,deviceDoneCode);
	}
	
	/**
	 * 调拨取消
	 * @param doneCode
	 * @param busiCode
	 * @param deviceDoneCode
	 * @param county_id
	 * @param area_id
	 * @throws JDBCException
	 */
	public void saveTransCancelChange(Integer doneCode,String busiCode,Integer deviceDoneCode,String county_id,String area_id) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id,pair_card_id,pair_modem_id,column_name, old_value, " +
				" new_value, change_date, optr_id, dept_id, county_id, area_id)  select ?,?,rd.device_id,s.pair_card_id,s.pair_modem_id, " +
				" 'trans_status','UNCONFIRM','IDLE',sysdate,i.confirm_optr_id,i.depot_source,?,? " +
				" from r_device_transfer i,r_device_done_deviceid d,r_device rd,r_stb s " +
				" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id and s.device_id(+)=rd.device_id " +
				" and i.device_done_code=?  ";
		executeUpdate(sql, doneCode, busiCode,county_id,area_id,deviceDoneCode);
	}
	
	
	/**
	 * 出库异动
	 * @param doneCode
	 * @param busiCode
	 * @param county_id
	 * @param area_id
	 * @throws JDBCException
	 */
	public void saveOutChange(Integer doneCode,String busiCode,String county_id,String area_id) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id,pair_card_id,pair_modem_id,column_name, old_value," +
				" new_value, change_date, optr_id, dept_id, county_id, area_id) select ?,?,rd.device_id,s.pair_card_id,s.pair_modem_id," +
				" 'depot_id',rd.depot_id,NULL,sysdate,i.optr_id,i.depot_id,?,? " +
				" from r_device_output i,r_device_done_deviceid d,r_device rd,r_stb s " +
				" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id and s.device_id(+)=rd.device_id " +
				" and i.device_done_code= ?  ";
		executeUpdate(sql, doneCode, busiCode,county_id,area_id,doneCode);
	}
	
	public void saveDeviceChange(Integer doneCode,String busiCode,String deviceId,String columnName,String oldValue,String newValue
			,String optrId,String deptId,String countyId,String areaId) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id, column_name, old_value, new_value, change_date, optr_id, " +
				" dept_id, county_id, area_id, pair_card_id, pair_modem_id) select ?,?,d.device_id,?,?,?,sysdate,?,?,?,?, s.pair_card_id,s.pair_modem_id  " +
				" from r_device d,r_stb s where s.device_id(+)=d.device_id and d.device_id=? ";
		executeUpdate(sql, doneCode, busiCode,columnName,oldValue,newValue,optrId,deptId,countyId,areaId,deviceId);
	}
	
	public void saveDeviceChangeAndBuyMode(Integer doneCode,String busiCode,String deviceId,String columnName,String oldValue,String newValue,String buyMode
			,String optrId,String deptId,String countyId,String areaId) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id, column_name, old_value, new_value, change_date, optr_id, " +
				" dept_id, county_id, area_id, pair_card_id, pair_modem_id,buy_mode) select ?,?,d.device_id,?,?,?,sysdate,?,?,?,?, s.pair_card_id,s.pair_modem_id,?  " +
				" from r_device d,r_stb s where s.device_id(+)=d.device_id and d.device_id=? ";
		executeUpdate(sql, doneCode, busiCode,columnName,oldValue,newValue,optrId,deptId,countyId,areaId,buyMode,deviceId);
	}
	
	public void saveDeviceChangeIsNewStb(Integer doneCode,String busiCode,String[] deviceId,String newValue
			,String optrId,String deptId,String countyId,String areaId) throws JDBCException {
		String sql = " insert into r_device_change (done_code, busi_code, device_id, column_name, old_value, new_value, change_date, optr_id, " +
				" dept_id, county_id, area_id, pair_card_id, pair_modem_id) select ?,?,d.device_id,'is_new_stb',d.is_new_stb,?,sysdate,?,?,?,?, s.pair_card_id,s.pair_modem_id  " +
				" from r_device d,r_stb s where s.device_id(+)=d.device_id and (d.is_new_stb<>? or d.is_new_stb is null) and "+getSqlGenerator().setWhereInArray("d.device_id",deviceId);
		executeUpdate(sql, doneCode, busiCode,newValue,optrId,deptId,countyId,areaId,newValue);
	}
	
	
	public void saveProcureChange(Integer doneCode,String busiCode,String deviceId,String buyMode,String status,String county_id,String area_id) throws JDBCException {
		String str = "";
		if(StringHelper.isNotEmpty(deviceId)){
			str = " and d.device_id ='"+deviceId+"' ";
		}
		String sql = " insert into r_device_change (done_code, busi_code, device_id,pair_card_id,pair_modem_id,column_name, old_value," +
				" new_value, change_date, optr_id, dept_id, county_id, area_id) select ?,?,rd.device_id,s.pair_card_id,s.pair_modem_id, " +
				" 'depot_status',rd.depot_status,?,sysdate,i.optr_id,i.depot_id,?,?  " +
				" from r_device_procure i,r_device_done_deviceid d,r_device rd,r_stb s " +
				" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id and s.device_id(+)=rd.device_id  " +
				" and i.device_done_code= ?  "+str;
		executeUpdate(sql, doneCode, busiCode,status,county_id,area_id,doneCode);
		if(buyMode.equals("T")){
			sql = " insert into r_device_change (done_code, busi_code, device_id,pair_card_id,pair_modem_id,column_name, old_value," +
					" new_value, change_date, optr_id, dept_id, county_id, area_id) select ?,?,rd.device_id,s.pair_card_id,s.pair_modem_id, " +
					" 'ownership',rd.ownership,case when  ? = 'T' and rd.ownership= 'GD' then 'CUST' " +
					" WHEN ? = 'T' and rd.ownership= 'CUST' THEN 'GD' ELSE rd.ownership  end,sysdate,i.optr_id,i.depot_id,?,?  " +
					" from r_device_procure i,r_device_done_deviceid d,r_device rd,r_stb s " +
					" where i.device_done_code=d.device_done_code and rd.device_id=d.device_id and s.device_id(+)=rd.device_id  " +
					" and i.device_done_code= ?  "+str;
			executeUpdate(sql, doneCode, busiCode,buyMode,buyMode,county_id,area_id,doneCode);
		}
	}

	public void saveMateralTransChange(Integer doneCode,
			String busiCode, String device_id, String columnName,
			Integer oldValue, Integer newValue, String confirm_optr_id,
			String depot_source, String county_id, String area_id, String buyMode) throws JDBCException {
			String sql = " insert into r_device_change (done_code, busi_code, device_id,column_name, old_value, " +
					" new_value, change_date, optr_id, dept_id, county_id, area_id,buy_mode)  VALUES(?,?,?,?,?,?,sysdate,?,?,?,?,?)";
			executeUpdate(sql, doneCode, busiCode,device_id,columnName,oldValue,newValue,confirm_optr_id,depot_source,county_id,area_id,buyMode);
	}
	
	
}

/**
 * CCustUnitToResidentDao.java	2010/03/23
 */

package com.ycsoft.business.dao.core.cust;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustUnitToResident;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CCustUnitToResidentDao -> C_CUST_UNIT_TO_RESIDENT table's operator
 */
@Component
public class CCustUnitToResidentDao extends BaseEntityDao<CCustUnitToResident> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6623227349139928159L;

	/**
	 * default empty constructor
	 */
	public CCustUnitToResidentDao() {}
	
	public int getMnCustCountByCustId(String mnCustId) throws JDBCException {
		String sql = "select count(*) from c_cust_unit_to_resident where unit_cust_id=?";
		return this.count(sql, mnCustId);
	}

	/**
	 * 删除客户对应的一个或者多个单位
	 * @param unitId
	 * @param custId
	 * @throws JDBCException
	 */
	public void deleteCustUnit(String[] unitId ,String custId) throws JDBCException{
		String sql="delete  c_cust_unit_to_resident where  " +
				" unit_cust_id= ? and resident_cust_id ='"+custId+"'";
		this.executeBatch(sql, unitId);
	}

	/**
	 * 复制客户对应的一个或者多个单位 到his表
	 * @param unitId
	 * @param custId
	 * @throws JDBCException
	 */
	public void  insertToHis(String[] unitId ,String custId,Integer doneCode) throws JDBCException{
		String sql=" INSERT INTO c_cust_unit_to_resident_his " +
				"(unit_cust_id, resident_cust_id, create_time, done_code)" +
				" SELECT unit_cust_id, resident_cust_id, create_time, "+doneCode +
				"  FROM c_cust_unit_to_resident  " +
				"  where unit_cust_id= ? and resident_cust_id ='"+custId+"'";
	this.executeBatch(sql, unitId);
	}

}

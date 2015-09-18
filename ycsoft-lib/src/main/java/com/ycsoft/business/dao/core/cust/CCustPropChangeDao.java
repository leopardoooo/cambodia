/**
 * CCustPropChangeDao.java	2010/03/24
 */

package com.ycsoft.business.dao.core.cust;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CCustPropChangeDao -> C_CUST_PROP_CHANGE table's operator
 */
@Component
public class CCustPropChangeDao extends BaseEntityDao<CCustPropChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1769584689995733128L;

	/**
	 * default empty constructor
	 */
	public CCustPropChangeDao() {}
	
	public CCustPropChange queryPropByCustIdAndColumn(String custId,String countyId,
			String columnName) throws Exception {
		String sql = "select * from c_cust_prop_change t" +
				" where t.cust_id=? and t.county_id=? and t.column_name=? order by t.change_time desc";
		return this.createQuery(sql, custId, countyId, columnName).first();
	}

	/**
	 * 根据客户编号获取客户的异动信息
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCustPropChange>  queryPropChangeByCustID(String custId,String custType,String countyId)throws Exception{
		String custTypeSql = "";
		if(custType.equals(SystemConstants.CUST_TYPE_RESIDENT)){
			custTypeSql = " and t.group_id='1'";
		}else if(custType.equals(SystemConstants.CUST_TYPE_NONRESIDENT)){
			custTypeSql = " and t.group_id='2'";
		}
		String sql = "select * from ( " +
				"select a.column_name,case when a.column_name='addr_id' then (select adr.addr_name from  t_address adr where adr.addr_id=a.old_value) else a.old_value end old_value" +
				",case when a.column_name='addr_id' then (select adr.addr_name from  t_address adr where adr.addr_id=a.new_value) else a.new_value end new_value,a.change_time,b.comments column_name_text,b.param_name " +
				" from c_cust_prop_change a,t_tab_define b " +
				" where a.column_name=b.column_name " +
				" and b.table_name='CCUST' and a.column_name<>'password' and b.status =? " +
				" and a.cust_id=?" +
				" and a.county_id=?" +
				" union " +
				"select a.column_name,a.old_value,a.new_value,a.change_time,t.attribute_name column_name_text,t.param_name"+
				" from c_cust_prop_change a,  t_extend_attribute t,t_extend e"+
				" where a.column_name = t.col_name and t.extend_id=e.extend_id" + custTypeSql +
				" and e.extend_table='C_CUST' and a.column_name<>'password' and a.cust_id=? and a.county_id=?"+
				" ) order by change_time desc";
		return this.createQuery(CCustPropChange.class , sql, StatusConstants.ACTIVE,custId, countyId, custId, countyId).list();

	}

	/**
	 *
	 * @param custId
	 * @param doneCode
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCustPropChange>  queryPropChangeByDoneCode(String custId,Integer doneCode,String countyId)throws Exception{
		List<CCustPropChange> propChangeList = null;
		String sql = "select a.column_name,a.old_value,a.new_value,a.change_time,b.comments column_name_text,b.param_name " +
				" from c_cust_prop_change a,t_tab_define b " +
				" where a.column_name=b.column_name " +
				" and b.table_name='CCUST' and b.status=? " +
				" and a.cust_id=? " +
				" and a.done_code=? " +
				" and a.county_id=? ";
		propChangeList =  this.createQuery(CCustPropChange.class , sql,StatusConstants.ACTIVE,custId,doneCode,countyId).list();
		return propChangeList;

	}

	public void removeByDoneCode(String custId,Integer doneCode,String countyId) throws Exception{
		String sql = "delete c_cust_prop_change " +
				"where cust_id =? " +
				" and done_code=? " +
				" and county_id=?";
		executeUpdate(sql, custId,doneCode,countyId);
	}

}

/**
 * BCreditAddressStopDao.java	2010/03/07
 */

package com.ycsoft.business.dao.bill;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.bill.BCreditAddressStop;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * SAreaDao -> BThersholdCfg table's operator
 */
@Component
public class BCreditAddressStopDao extends BaseEntityDao<BCreditAddressStop> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1527191533766704635L;

	/**
	 * default empty constructor
	 */
	public BCreditAddressStopDao() {}
	
	
	public List<BCreditAddressStop> queryAll()throws Exception{
		final String sql = "SELECT a.addr_id,a.addr_name,a.county_id, a.area_id,b.base_prod_count FROM t_address a , b_credit_address_stop b"
						+" where a.addr_id=b.addr_id(+) and a.tree_level='2'";
		return createQuery(sql).list();
	}
	
	
	public int saveBCreditAddressStop(BCreditAddressStop bas)throws Exception{
		final String sql = "INSERT INTO B_CREDIT_ADDRESS_STOP(ADDR_ID,BASE_PROD_COUNT,AREA_ID,COUNTY_ID) VALUES(?,?,?,?)";
		return this.executeUpdate(sql, bas.getAddr_id(), bas.getBase_prod_count(), bas.getArea_id(),bas.getCounty_id());
	}
	
	public int updateBCreditAddressStop(BCreditAddressStop bas)throws Exception{
		final String sql = "UPDATE B_CREDIT_ADDRESS_STOP t SET t.base_prod_count=? WHERE t.addr_id =?";
		return this.executeUpdate(sql, bas.getBase_prod_count(), bas.getAddr_id());
	}

}

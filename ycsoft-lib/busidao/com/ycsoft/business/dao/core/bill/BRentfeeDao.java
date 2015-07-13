/**
 * BRentfeeDao.java	2012/12/19
 */
 
package com.ycsoft.business.dao.core.bill; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BRentfee;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * BRentfeeDao -> B_RENTFEE table's operator
 */
@Component
public class BRentfeeDao extends BaseEntityDao<BRentfee> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6021979366476522598L;
	/**
	 * default empty constructor
	 */
	public BRentfeeDao() {}
	public void deleteRentfee(String prod_sn,String acctId, String acctItemId) throws Exception{
		String sql = "delete B_RENTFEE where prod_sn=? and acct_id = ? and acctitem_id = ? ";
		this.executeUpdate(sql, prod_sn, acctId, acctItemId);
	}
	
	public void deleteRentfee(String prod_sn,String county_id) throws Exception{
		String sql = "delete B_RENTFEE where prod_sn=? and county_id = ?  ";
		this.executeUpdate(sql, prod_sn, county_id);
	}
}

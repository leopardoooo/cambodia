/**
 * PProdCountyPriceDao.java	2012/10/24
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdCountyPrice;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * PProdCountyPriceDao -> P_PROD_COUNTY_PRICE table's operator
 */
@Component
public class PProdCountyPriceDao extends BaseEntityDao<PProdCountyPrice> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3447949561739600156L;

	/**
	 * default empty constructor
	 */
	public PProdCountyPriceDao() {}
	
	public List<PProdCountyPrice> getLowestCounty(String prodId,String countyId,String dataRight) throws Exception{
		String sql = StringHelper.append("select prod_id,county_id,price/100 price,area_price/100 area_price,county_price/100  county_price from p_prod_county_price  where prod_id = ? ");
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(sql ," and ",dataRight.trim());
		}
	    return createQuery(PProdCountyPrice.class,sql,prodId).list();
	}
	
	public List<PProdCountyPrice> getLowestCountyById(String prodId,String countyId,String dataRight) throws Exception{
		String sql = StringHelper.append("select t.*,t1.prod_name from p_prod_county_price t,p_prod t1 " +
				"where t1.prod_id = t.prod_id and t.prod_id =? " );
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(sql ," and t.",dataRight.trim());
		}
	    return createQuery(PProdCountyPrice.class,sql,prodId).list();
	}
	
	public List<PProdCountyPrice> getLowestCountyByPkId(String prodId,String countyId,String dataRight) throws Exception{
		String sql = StringHelper.append("select t.*,t1.prod_name from p_prod_county_price t,p_prod t1 " +
				"where t1.prod_id = t.prod_id and t.prod_id in " +
				" (select distinct t2.prod_id from p_package_prod t2 where t2.package_id=? and t2.type=? )");
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(sql ," and t.",dataRight.trim());
		}
	    return createQuery(PProdCountyPrice.class,sql,prodId,SystemConstants.PACKAGE_MARKET_TYPE).list();
	}
	
	public List<PProdCountyPrice> queryCountyByDataRight(String countyId,String dataRight) throws Exception{
		String sql = "select * from s_county  where county_id != '"+SystemConstants.COUNTY_ALL+"' ";
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(sql ,"  and ",dataRight.trim());
		}
		sql = StringHelper.append(sql ," order by county_id ");
		return createQuery(sql).list();
	}
	
	public void deleteLowestCounty (String prodId,String [] countyId) throws Exception {
		String	sql = "delete p_prod_county_price where prod_id = '"+prodId+"' and county_id = ? ";
		 executeBatch(sql, countyId);
	}
}

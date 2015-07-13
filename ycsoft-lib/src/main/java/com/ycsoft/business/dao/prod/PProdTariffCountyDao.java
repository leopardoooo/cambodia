/**
 * PProdTariffCountyDao.java	2010/10/21
 */

package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdTariffCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;



/**
 * PProdTariffCountyDao -> P_PROD_TARIFF_COUNTY table's operator
 */
@Component
public class PProdTariffCountyDao extends BaseEntityDao<PProdTariffCounty> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5007788511438751275L;
	/**
	 * default empty constructor
	 */
	public PProdTariffCountyDao() {}
	public void removeByTariffId (String tariffId) throws Exception {
		String	sql = "delete P_PROD_TARIFF_COUNTY where tariff_id =  ? ";
		executeUpdate(sql, tariffId);
	}
	
	public void addTariffCounty (String [] countyId, String tariffId) throws Exception {
		String	sql = "insert into P_PROD_TARIFF_COUNTY(county_id, tariff_id) values (?, '"+tariffId+"')";
		 executeBatch(sql, countyId);
	}
	public void deleteTariffCounty (String tariffId) throws Exception {
		String	sql = "delete P_PROD_TARIFF_COUNTY where tariff_id = ? ";
		executeUpdate(sql, tariffId);
	}
	public List<TreeDto> getTariffCountyBytariffId(String tariffId) throws Exception{
		String sql = " select county_id id from P_PROD_TARIFF_COUNTY where tariff_id = ? ";
		return createQuery(TreeDto.class,sql,tariffId).list();
	}
	
	public List<String> queryTariffCountyById(String tariffId) throws Exception{
		String sql = " select county_id from P_PROD_TARIFF_COUNTY where tariff_id = ? ";
		return findUniques(sql, tariffId);
	}
	
	public List<PProdTariffCounty> queryTariffCountyById(String[] tariffIds) throws Exception{
		String sql = " select county_id,tariff_id from P_PROD_TARIFF_COUNTY where 1=1 and ("+getSqlGenerator().setWhereInArray("tariff_id",tariffIds)+")  ";
		return this.createQuery(sql).list();
	}
	

}

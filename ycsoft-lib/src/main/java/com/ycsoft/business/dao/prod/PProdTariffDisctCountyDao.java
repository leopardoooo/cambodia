/**
 * PProdTariffCountyDao.java	2012/08/09
 */

package com.ycsoft.business.dao.prod;

import java.util.List;
import org.springframework.stereotype.Component;
import com.ycsoft.beans.prod.PProdTariffCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.tree.TreeDto;



/**
 * PProdTariffDisctCountyDao -> P_PROD_TARIFF_DISCT_COUNTY table's operator
 */
@Component
public class PProdTariffDisctCountyDao extends BaseEntityDao<PProdTariffCounty> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4362622763057134547L;

	/**
	 * default empty constructor
	 */
	public PProdTariffDisctCountyDao() {}
	
	public List<TreeDto> getTariffDisctCountyByDisctId(String disctId) throws Exception{
		String sql = " select county_id id from p_prod_tariff_disct_county where disct_id = ? ";
		return createQuery(TreeDto.class,sql,disctId).list();
	}
	
	public void addDisctCounty (String [] countyIds, String disctId) throws Exception {
		String	sql = "insert into P_PROD_TARIFF_DISCT_COUNTY(county_id, disct_id) values (?, '"+disctId+"')";
		 executeBatch(sql, countyIds);
	}
	
	public void deleteDisctCounty (String disctId) throws Exception {
		String	sql = "delete P_PROD_TARIFF_DISCT_COUNTY where disct_id = ? ";
		executeUpdate(sql, disctId);
	}

}

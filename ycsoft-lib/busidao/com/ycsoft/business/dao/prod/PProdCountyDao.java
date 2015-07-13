package com.ycsoft.business.dao.prod;

/**
 * PProdCountyDao.java	2011/09/15
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * PProdCountyDao -> P_PROD_COUNTY table's operator
 */
@Component
public class PProdCountyDao extends BaseEntityDao<PProdCounty> {
	
	/**
	 * default empty constructor
	 */
	public PProdCountyDao() {}

	public List<TreeDto> getProdCountyById(String prodId) throws JDBCException {
		String sql = " select county_id id from P_Prod_COUNTY where prod_id = ? ";
		return createQuery(TreeDto.class,sql,prodId).list();
	}

	public void deletebyProdId(String prodId) throws JDBCException {
		String	sql = "delete P_PROD_COUNTY where prod_id = ? ";
		executeUpdate(sql, prodId);
	}
	
	public void saveProdCountyId (String prodId,String [] countyId) throws Exception {
		String	sql = "insert into P_PROD_COUNTY(county_id,prod_id) values (?, '"+prodId+"')";
		 executeBatch(sql, countyId);
	}

	public List<String> queryByProdId(String prodId) throws JDBCException {
		String sql = " select distinct county_id from P_Prod_COUNTY where prod_id = ? ";
		return findUniques(sql, prodId);
	}

}

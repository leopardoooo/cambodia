/**
 * PProdDictCountyDao.java	2012/03/06
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdDictCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * PProdDictCountyDao -> P_PROD_DICT_COUNTY table's operator
 */
@Component
public class PProdDictCountyDao extends BaseEntityDao<PProdDictCounty> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5178280680284322059L;

	/**
	 * default empty constructor
	 */
	public PProdDictCountyDao() {}
	public List<TreeDto> getProdDictCountyById(String nodeId) throws JDBCException {
		String sql = " select county_id id from P_PROD_DICT_COUNTY where node_id = ? ";
		return createQuery(TreeDto.class,sql,nodeId).list();
	}
	
	public void deleteById(String nodeId) throws JDBCException {
		String sql = "delete from P_PROD_DICT_COUNTY p where p.node_id=?";
		executeUpdate(sql, nodeId);
	}
	
	
	public List<PProdDictCounty> queryCountyById(String nodeId) throws Exception{
		String sql = "select * from P_PROD_DICT_COUNTY where node_id = ?";
		return createQuery(PProdDictCounty.class,sql,nodeId).list();
	}
}

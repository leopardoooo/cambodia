package com.ycsoft.business.dao.system;

/**
 * SRoleCountyDao.java	2011/11/24
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SRoleCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;



/**
 * SRoleCountyDao -> S_ROLE_COUNTY table's operator
 */
@Component
public class SRoleCountyDao extends BaseEntityDao<SRoleCounty> {
	
	/**
	 * default empty constructor
	 */
	public SRoleCountyDao() {}

	public void deletebyRoleId(String roleId) throws JDBCException {
		String	sql = "delete s_role_county where role_id = ? ";
		executeUpdate(sql, roleId);
	}
	
	public void saveRoleCountyId (String roleId,String [] countyId) throws Exception {
		String	sql = "insert into s_role_county(county_id,role_id) values (?, '"+roleId+"')";
		 executeBatch(sql, countyId);
	}
	
	public List<TreeDto> getRoleCountyById(String roleId) throws JDBCException {
		String sql = " select county_id id from s_role_county where role_id = ? ";
		return createQuery(TreeDto.class,sql,roleId).list();
	}
	
	public List<SRoleCounty> getRoleById(String countyId) throws JDBCException {
		String sql = " select role_id from s_role_county where county_id = ? ";
		return createQuery(SRoleCounty.class,sql,countyId).list();
	}
}

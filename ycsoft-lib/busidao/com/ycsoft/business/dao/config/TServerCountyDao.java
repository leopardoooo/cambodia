/**
 * TServerCountyDao.java	2013/01/04
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TServerCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * TServerCountyDao -> T_SERVER_COUNTY table's operator
 */
@Component
public class TServerCountyDao extends BaseEntityDao<TServerCounty> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4264171937082801637L;

	/**
	 * default empty constructor
	 */
	public TServerCountyDao() {}
	public List<TreeDto> getServerCountyById(String serverId) throws Exception{
		String sql = " select county_id id from t_server_county where server_id = ? ";
		return createQuery(TreeDto.class,sql,serverId).list();
	}
	
	public void delCountyById(String serverId) throws Exception{
		String sql = " delete from t_server_county where server_id = ? ";
		executeUpdate(sql, serverId);
 	}
}

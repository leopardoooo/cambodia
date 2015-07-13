/**
 * RProjectAddrDao.java	2012/05/07
 */
 
package com.ycsoft.business.dao.project; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.project.RProjectAddr;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * RProjectAddrDao -> R_PROJECT_ADDR table's operator
 */
@Component
public class RProjectAddrDao extends BaseEntityDao<RProjectAddr> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7732183323037398733L;

	/**
	 * default empty constructor
	 */
	public RProjectAddrDao() {}
	
	public void deleteByProjectId(String projectId) throws Exception {
		String sql = "delete from r_project_addr t where t.project_id=?";
		this.executeUpdate(sql, projectId);
	}
	
	public List<RProjectAddr> queryAddrByProjectId(String projectId) throws Exception {
		String sql = "select pa.*,a.addr_name addr_name from r_project_addr pa,t_address a" +
				" where pa.addr_id=a.addr_id and pa.project_id=?";
		return this.createQuery(sql, projectId).list();
	}

}

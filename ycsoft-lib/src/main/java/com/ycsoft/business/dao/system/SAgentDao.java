/**
 * SAgentDao.java	2015/09/02
 */
 
package com.ycsoft.business.dao.system; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SAgent;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * SAgentDao -> S_AGENT table's operator
 */
@Component
public class SAgentDao extends BaseEntityDao<SAgent> {
	
	/**
	 * default empty constructor
	 */
	public SAgentDao() {}
	
	public SAgent queryAgentByDeptId(String deptId) throws Exception {
		String sql = "select a.* from s_dept d, s_agent a where d.agent_id=a.id and d.dept_id=?";
		return this.createQuery(sql, deptId).first();
	}

}

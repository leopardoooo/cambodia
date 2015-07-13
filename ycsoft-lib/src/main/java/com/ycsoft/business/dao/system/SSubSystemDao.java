/**
 * SSubSystemDao.java	2010/09/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSubSystem;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * SSubSystemDao -> S_SUB_SYSTEM table's operator
 */
@Component
public class SSubSystemDao extends BaseEntityDao<SSubSystem> {

	/**
	 *
	 */
	private static final long serialVersionUID = -9027761666839400616L;

	/**
	 * default empty constructor
	 */
	public SSubSystemDao() {}

	public List<SSubSystem> queryAllSubSystem(SOptr optr) throws Exception {
		String sql = "select * from s_sub_system s where s.sub_system_id in " +
				"(select b.sub_system_id from s_optr_role a,s_role b where a.role_id=b.role_id and b.role_type='"+SystemConstants.ROLE_TYPE_MENU+"' and b.sub_system_id is not null and  a.optr_id=?" +
				" union all select distinct sr.Sub_System_Id from s_resource sr,s_optr_resource sor where sr.res_id=sor.res_id and sor.optr_id = ? and sor.more_or_less ='1')" +
				" order by sub_system_id asc";
		return this.createQuery(sql,optr.getOptr_id(),optr.getOptr_id()).list();
	}

}

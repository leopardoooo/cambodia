/**
 * WRevisitInfoDao.java	2013/08/23
 */
 
package com.ycsoft.business.dao.task; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WRevisitInfo;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * WRevisitInfoDao -> W_REVISIT_INFO table's operator
 */
@Component
public class WRevisitInfoDao extends BaseEntityDao<WRevisitInfo> {
	
	/**
	 * default empty constructor
	 */
	public WRevisitInfoDao() {}
	
	public void modifyPlanOptr(String work_id, String newRevistiInstallerIds)throws Exception {
		final String sql = "UPDATE w_revisit_info t set t.installer_optr=? WHERE t.work_id=?";
		executeUpdate(sql, newRevistiInstallerIds, work_id);
	}

}

package com.ycsoft.business.dao.core.job;

/**
 * JDataSyncDao.java	2011/02/23
 */
 

import java.util.List;

import org.springframework.stereotype.Component;
import com.ycsoft.beans.core.job.JDataSync;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * JDataSyncDao -> J_DATA_SYNC table's operator
 */
@Component
public class JDataSyncDao extends BaseEntityDao<JDataSync> {
	
	/**
	 * default empty constructor
	 */
	public JDataSyncDao() {}

	public List<JDataSync> getDataSyncJob(String cmdType, String detailParams,String tableName) throws Exception{
		String sql = " select * from j_data_sync t where t.cmd_type =? and t.detail_params =? and t.table_name =?  and t.is_success ='Y' ";
		return createQuery(JDataSync.class,sql,cmdType,detailParams,tableName).list();
	}

	
	
	
}

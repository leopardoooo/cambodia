/**
 * TServerDao.java	2013/01/04
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TServer;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TServerDao -> T_SERVER table's operator
 */
@Component
public class TServerDao extends BaseEntityDao<TServer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 488656420858769025L;

	/**
	 * default empty constructor
	 */
	public TServerDao() {}
	public List<TServer> query(String countyId )throws Exception{
		String sql = " select a.* from t_server a where 1=1 ";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql = StringHelper.append(sql," and a.server_id in (select server_id from t_server_county where county_id = "+countyId+")");
		}
		return createQuery(TServer.class,sql ).list();
	}
	public TServer queryById(String serverId) throws Exception{
		String sql = " select * from t_server  where server_id = ? ";
		return createQuery(TServer.class,sql,serverId ).first();
	}
	
}

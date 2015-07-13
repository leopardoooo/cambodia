/**
 * SBulletinCountyDao.java	2010/11/26
 */
 
package com.ycsoft.business.dao.system; 

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SBulletin;
import com.ycsoft.beans.system.SBulletinCounty;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.SBulletinDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * SBulletinCountyDao -> S_BULLETIN_COUNTY table's operator
 */
@Component
public class SBulletinCountyDao extends BaseEntityDao<SBulletinCounty> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8766537619543807446L;

	/**
	 * default empty constructor
	 */
	public SBulletinCountyDao() {}
	public void delBullCountyById(String bulletinId) throws Exception{
		String sql = " delete from S_BULLETIN_COUNTY where bulletin_id = ? ";
		executeUpdate(sql, bulletinId);
 	}
	
	public List<TreeDto> getBullCountyById(String bulletinId) throws Exception{
		String sql = " select dept_id id from S_BULLETIN_COUNTY where bulletin_id = ? ";
		return createQuery(TreeDto.class,sql,bulletinId).list();
 	}
}

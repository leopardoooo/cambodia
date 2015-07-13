/**
 * RProjectCountyDao.java	2012/05/07
 */
 
package com.ycsoft.business.dao.project; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.project.RProjectCounty;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * RProjectCountyDao -> R_PROJECT_COUNTY table's operator
 */
@Component
public class RProjectCountyDao extends BaseEntityDao<RProjectCounty> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3277459855846198677L;

	/**
	 * default empty constructor
	 */
	public RProjectCountyDao() {}
	
	public List<RProjectCounty> queryByCountyId(String countyId) throws Exception {
		String sql = "select pc.*,c.county_name from r_project_county pc,s_county c" +
				" where pc.county_id=c.county_id";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and c.county_id='"+countyId+"'";
		}
		return this.createQuery(sql).list();
	}

}

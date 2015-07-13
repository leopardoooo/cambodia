/**
 * PProdUserResDao.java	2010/11/22
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdUserRes;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * PProdUserResDao -> P_PROD_USER_RES table's operator
 */
@Component
public class PProdUserResDao extends BaseEntityDao<PProdUserRes> {
	
	/**
	 * default empty constructor
	 */
	public PProdUserResDao() {}

	public List<PProdUserRes> queryByCountyId(String countyId) throws Exception{
		String sql="select a.*,b.rule_str rule_id_text from p_prod_user_res a, t_rule_define b " +
				" where a.county_id=? and a.rule_id=b.rule_id(+)";
		return this.findList(sql, countyId);
		
	}

}

/**
 * TRuleDefineCountyDao.java	2011/07/14
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;
import com.ycsoft.beans.config.TRuleDefineCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;

/**
 * TRuleDefineCountyDao -> T_RULE_DEFINE_COUNTY table's operator
 */
@Component
public class TRuleDefineCountyDao extends BaseEntityDao<TRuleDefineCounty> {

	/**
	 * default empty constructor
	 */
	public TRuleDefineCountyDao() {
	}

	public void delete(String ruleId) throws Exception {
		String sql = "delete from t_rule_define_county where rule_id=?";
		this.executeUpdate(sql, ruleId);
	}

}

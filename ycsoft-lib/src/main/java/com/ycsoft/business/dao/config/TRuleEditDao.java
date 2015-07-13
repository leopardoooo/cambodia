/**
 * TRuleEditDao.java	2010/08/30
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TRuleEdit;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.config.VewRulePropDto;


/**
 * TRuleEditDao -> T_RULE_EDIT table's operator
 */
@Component
public class TRuleEditDao extends BaseEntityDao<TRuleEdit> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1803843995257098205L;

	/**
	 * default empty constructor
	 */
	public TRuleEditDao() {}

	public void deleteByRuleId(String ruleId) throws Exception {
		String sql = "delete from t_rule_edit where rule_id=?";
		executeUpdate(sql, ruleId);
	}

	/**
	 * 返回rule的所有参数模块
	 * @param ruleId
	 * @return
	 */
	public List<String> queryModelByRule(String ruleId) throws Exception {
		String sql = "SELECT b.model_name FROM t_rule_edit a,vew_rule_prop b " +
				" WHERE  a.prop_id=b.prop_id AND a.rule_id=? GROUP BY b.model_name";
		List<VewRulePropDto> list = createQuery(VewRulePropDto.class, sql, ruleId).list();
		return CollectionHelper.converValueToList(list, "model_name");
	}
	public void updateTRuleEdit(TRuleEdit re) throws Exception {
		String sql = "update t_rule_edit set left_bracket=?,prop_id=?,operator=?,prop_value=?," +
			"logic=?,right_barcket=?,data_type=?,param_name=?,prop_name=?,prop_value_text=? " +
			"where rule_id=? and row_idx=?";
		executeUpdate(sql, re.getLeft_bracket(),re.getProp_id(),re.getOperator(),
				re.getProp_value(),re.getLogic(),re.getRight_barcket(),re.getData_type(),
					re.getParam_name(),re.getProp_name(),re.getProp_value_text(),re.getRule_id(),re.getRow_idx());

	}

	public List<TRuleEdit> queryRuleEditByRuleId(String ruleId) throws Exception {
		String sql="select * from t_rule_edit where rule_id=? order by row_idx";
		return this.createQuery(TRuleEdit.class, sql, ruleId).list();
	}
}

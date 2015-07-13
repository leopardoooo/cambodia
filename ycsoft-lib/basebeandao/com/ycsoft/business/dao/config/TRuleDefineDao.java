/**
 * TRuleDefineDao.java	2010/07/21
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.config.RuleDefineDto;
import com.ycsoft.sysmanager.dto.config.VewRuleDto;
import com.ycsoft.sysmanager.dto.config.VewRuleObjDto;
import com.ycsoft.sysmanager.dto.config.VewRulePropDto;

/**
 * TRuleDefineDao -> T_RULE_DEFINE table's operator
 */
@Component
public class TRuleDefineDao extends BaseEntityDao<TRuleDefine> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1382248453866370301L;

	/**
	 * default empty constructor
	 */
	public TRuleDefineDao() {
	}
	
	public List<VewRuleDto> queryPromFeeUserRule()throws Exception{
		String sql="select d.* from t_rule_define d,s_itemvalue s " +
				" where d.rule_id=s.item_value and s.item_key='PROMFEE_USERRULE' and d.rule_type='BUSI' ";
//		String sql="select d.* from t_rule_define d " +
//				" where  d.rule_type='BUSI' ";
		return this.createQuery(VewRuleDto.class, sql).list();
	}

	/**
	 * 根据数据类型查询对应规则属性
	 * 
	 * @param dataType
	 * @return
	 * @throws Exception
	 */
	public List<VewRulePropDto> queryRulePropByDataRightType(String dataType)
			throws Exception {
		String sql = "select * from vew_rule_prop where data_right_type=?";
		return this.createQuery(VewRulePropDto.class, sql, dataType).list();
	}

	/**
	 * 规则属性
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VewRulePropDto> queryRuleProp(String prop_name)
			throws Exception {
		String sql = "select r.model_name prop_id,'-1' model_name,r.model_desc prop_name,model_name param_name,r.data_type "
				+ "from vew_rule_prop r where r.prop_name like '%"
				+ prop_name
				+ "%' group by model_name,'-1',model_desc,model_name,r.data_type "
				+ "union all "
				+ "select r.prop_id prop_id,r.model_name model_name,r.prop_name prop_name,r.param_name,r.data_type "
				+ "from vew_rule_prop r where r.prop_name like '%"
				+ prop_name
				+ "%'";
		return this.createQuery(VewRulePropDto.class, sql).list();
	}

	/**
	 * 规则对应的 引用对象
	 * 
	 * @param rule_id
	 *            规则id
	 * @return
	 * @throws Exception
	 */
	public List<VewRuleObjDto> queryRuleObjByRuleId(String rule_id)
			throws Exception {
		String sql = "select * from  vew_rule_obj where rule_id=? ";
		return createQuery(VewRuleObjDto.class, sql, rule_id).list();
	}

	public TRuleDefine getTruleByRuleId(String ruleId) throws Exception {
		String sql = "select * from  T_RULE_DEFINE  where rule_id = ?  ";
		return createQuery(TRuleDefine.class, sql, ruleId).first();
	}

	public List<VewRuleDto> getTruleByDataType(String dataType,String countyId)throws Exception {
		String sql = "select * from t_rule_define t where t.data_type=? ";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and t.rule_id in (select t1.rule_id from t_rule_define_county t1 where t1.county_id in ('"+countyId+"','"+SystemConstants.COUNTY_ALL+"'))";
		}
		return createQuery(VewRuleDto.class, sql, dataType).list();
	}

	public List<VewRuleDto> findRuleViewDictByType(String ruleType, String countyId)
			throws JDBCException {
		if(ruleType.equals("RENT_RULE")){
			String sql = "select rule_id, rule_name from vew_rule where rule_type=?";
			return createQuery(VewRuleDto.class, sql, ruleType).list();
		}else if(countyId.equals(SystemConstants.COUNTY_ALL)){
			String sql = "select r.rule_id,r.rule_name from vew_rule r where r.rule_type=?" +
					" and r.eff_date<sysdate and (r.exp_date is null or r.exp_date>sysdate)";
			return createQuery(VewRuleDto.class, sql, ruleType).list();
		}else {
			String sql = "select r.rule_id, r.rule_name from vew_rule r" +
				" where r.rule_id IN (select distinct c.rule_id from t_rule_define_county c where c.county_id in (?, ?) ) and r.rule_type=?" +
				" and r.eff_date<sysdate and (r.exp_date is null or r.exp_date>sysdate)";
			return createQuery(VewRuleDto.class, sql, SystemConstants.COUNTY_ALL, countyId,ruleType).list();
		}
	}

	public List<VewRuleDto> findRuleALL() throws JDBCException {
		String sql = "select * from vew_rule ";
		return createQuery(VewRuleDto.class, sql).list();
	}

	public Pager<RuleDefineDto> queryAllRule(String query,String dataType,String countyId,Integer start,Integer limit) throws Exception {
		String sql = "select r.*,d.*,wmsys.wm_concat(c.county_id) county_id "
				+ " from t_rule_define r,s_data_right_type d,"
				+ " t_rule_define_county c"
				+ " where r.data_type=d.data_right_type(+) and r.rule_id=c.rule_id(+)";
				
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and c.county_id in ('"+countyId+"','"+SystemConstants.COUNTY_ALL+"')";
		}
		if(StringHelper.isNotEmpty(dataType)){
			sql += " and r.rule_type= '" + dataType+"' ";
		}
		if(StringHelper.isNotEmpty(query)){
			sql += " and r.rule_name like '%"+query+"%'";
		}
		sql += " group by r.rule_id,r.rule_name,r.rule_str,r.remark,r.rule_str_cn,r.rule_type,r.data_type,r.cfg_type,r.pre_billing_rule,"
			+ " r.optr_id,r.eff_date,r.exp_date,d.data_right_type,d.type_name,d.table_name,d.result_column,d.select_column,d.null_is_all,d.is_level"
			+ " order by to_number(r.rule_id) desc";
		return createQuery(RuleDefineDto.class, sql).setStart(start).setLimit(limit).page();
	}
	
	public List<TRuleDefine> queryRuleByCountyId(String countyId) throws Exception {
		
		if(countyId.equals(SystemConstants.COUNTY_ALL)){
			String sql = "select r.rule_id,r.rule_name from t_rule_define r order by to_number(r.rule_id) desc";
			return this.createQuery(sql).list();
		}else{
			String sql = "select r.rule_id,r.rule_name from t_rule_define r,t_rule_define_county c"
				+ " where r.rule_id=c.rule_id and c.county_id in (?,?)";
			sql += " order by to_number(r.rule_id) desc";
			return createQuery(sql, SystemConstants.COUNTY_ALL, countyId).list();
		}
	}

	public Pager<RuleDefineDto> queryAllRule(Integer start, Integer limit)
			throws Exception {
		String sql = "select r.*,d.* from t_rule_define r,s_data_right_type d"
				+ " where r.data_type=d.data_right_type(+) order by to_number(rule_id) desc";
		return createQuery(RuleDefineDto.class, sql).setStart(start).setLimit(
				limit).page();
	}
}

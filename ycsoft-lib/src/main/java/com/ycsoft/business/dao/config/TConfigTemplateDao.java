/**
 * TConfigTemplateDao.java	2010/09/05
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TConfigTemplateDao -> T_CONFIG_TEMPLATE table's operator
 */
@Component
public class TConfigTemplateDao extends BaseEntityDao<TConfigTemplate> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6254456251616103939L;

	/**
	 * default empty constructor
	 */
	public TConfigTemplateDao() {}

	/**
	 * 根据模板ID删除记录
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException{
		String sql = "delete from t_config_template t where t.template_id=?";
		executeUpdate(sql, templateId);
	}

	/**
	 * 查询配置模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TConfigTemplate> queryConfigTpls(String templateId) throws JDBCException{
		String sql = StringHelper.append("select t1.*,t2.param_name,t2.remark,t2.form_type from t_config_template ",
			" t1,t_config t2 where t1.template_id=? and t1.config_name = t2.config_name",
			" union select ? template_id,t3.config_name, '' config_value ,t3.param_name,t3.remark,t3.form_type from t_config t3",
			" where t3.config_name not in (select t4.config_name from t_config_template t4 where t4.template_id=?)");
		return createQuery(TConfigTemplate.class, sql, templateId,templateId,templateId).list();
	}
	
	/**
	 * 根据配置名称和地区查配置
	 * @param configName
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public TConfigTemplate queryConfigByConfigName(String configName,String countyId) throws JDBCException{
		String sql = StringHelper.append("select tc.* from t_config_template tc,t_template_county tt ",
				" where tc.config_name=? and tc.template_id=tt.template_id and tt.county_id=?");
		return createQuery(TConfigTemplate.class, sql, configName,countyId).first();
	}

}

/**
 * TCustUpdateCfgDao.java	2010/03/24
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TUpdateCfgDao -> T_UPDATE_CFG table's operator
 */
@Component
public class TUpdateCfgDao extends BaseEntityDao<TUpdateCfg> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7157392671708686330L;

	/**
	 * default empty constructor
	 */
	public TUpdateCfgDao() {}
	/**
	 * 根据模板ID删除记录
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException{
		String sql = "delete from t_update_cfg t where t.template_id=?";
		executeUpdate(sql, templateId);
	}

	/**
	 * 查询信息修改字段名
	 * @return
	 * @throws JDBCException
	 */
	public List<TUpdateCfg> queryFields() throws JDBCException{
		String sql = "SELECT t.comments remark, t.field_name,t.table_name FROM t_tab_define t " +
				" WHERE t.field_name IS NOT NULL and t.status = ? ";
		return createQuery(TUpdateCfg.class, sql,StatusConstants.ACTIVE).list();
	}

	/**
	 * 查询信息修改模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TUpdateCfg> queryUpdCfgTpls(String templateId) throws JDBCException{
		String sql = "select t1.*,t2.busi_name,t3.remark from t_update_cfg t1,t_busi_code t2,"
			+ " (SELECT t.comments remark, t.field_name  FROM t_tab_define t "
			+ " WHERE t.field_name IS NOT NULL and t.status = ? ) t3 "
			+ " where t1.template_id=? and t1.busi_code=t2.busi_code and t1.field_name = t3.field_name";
		return createQuery(TUpdateCfg.class, sql, StatusConstants.ACTIVE,templateId).list();
	}


}

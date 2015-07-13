/**
 * TStbFilledDao.java	2010/10/11
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TStbFilled;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TStbFilledDao -> T_STB_FILLED table's operator
 */
@Component
public class TStbFilledDao extends BaseEntityDao<TStbFilled> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5188649882707967834L;

	/**
	 * default empty constructor
	 */
	public TStbFilledDao() {}

	/**
	 * 根据模板ID查询机顶盒灌装数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TStbFilled> queryStbFilleds(String templateId) throws JDBCException {
		String sql = "select t.*,p.res_name from t_stb_filled t,p_res p where t.res_id=p.res_id and t.template_id=?";
		return createQuery(TStbFilled.class, sql, templateId).list();
	}

	public void deleteByTplId(String templateId) throws JDBCException {
		String sql = "delete from t_stb_filled t where t.template_id=?";
		executeUpdate(sql, templateId);
	}

}

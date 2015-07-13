/**
 * TProdStatusRentDao.java	2010/03/08
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TProdStatusRent;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TProdStatusRentDao -> T_PROD_STATUS_RENT table's operator
 */
@Component
public class TProdStatusRentDao extends BaseEntityDao<TProdStatusRent> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5355715049786525518L;

	/**
	 * default empty constructor
	 */
	public TProdStatusRentDao() {}
	/**
	 * 根据模板ID删除记录
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException{
		String sql = "delete from t_prod_status_rent t where t.template_id=?";
		executeUpdate(sql, templateId);
	}

	/**
	 * 查询产品状态
	 * @return
	 * @throws JDBCException
	 */
	public List<TProdStatusRent> queryProdStatus() throws JDBCException{
		String sql = "SELECT item_value status_id,item_name status_desc "

			+ " FROM VEW_DICT WHERE ITEM_KEY='STATUS_C_PROD' ";
		return createQuery(TProdStatusRent.class, sql).list();
	}

}

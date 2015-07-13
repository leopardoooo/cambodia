/**
 * TExtendGroupDao.java	2010/03/08
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExtendGroup;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TExtendGroupDao -> T_EXTEND_GROUP table's operator
 */
@Component
public class TExtendGroupDao extends BaseEntityDao<TExtendGroup> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1966335925229174927L;

	/**
	 * default empty constructor
	 */
	public TExtendGroupDao() {}

	/**
	 * 根据设备类型及对应的值，查询对应扩展表或扩展业务的分组信息
	 * @param extendType 分组类型
	 * @param extendValue 扩展表名或业务code
	 * @return
	 * @throws JDBCException
	 */
	public List<TExtendGroup> queryGroups(String extendsionId) throws JDBCException{
		String sql = "select teg.* from t_extend_group teg where teg.extend_id=?";
		return createQuery(TExtendGroup.class, sql,extendsionId).list();
	}

	/**
	 * 根据扩展表extend_id删除该扩展表的分组信息
	 * @param extend_id
	 * @throws JDBCException
	 */
	public void deleteAll(String extend_id) throws JDBCException{
		String sql = "delete from t_extend_group where extend_id = ?";
		executeUpdate(sql, extend_id);
	}
}

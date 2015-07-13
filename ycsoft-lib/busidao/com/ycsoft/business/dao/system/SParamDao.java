/**
 * SParamDao.java	2010/11/02
 */

package com.ycsoft.business.dao.system;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SParam;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * SParamDao -> S_PARAM table's operator
 */
@Component
public class SParamDao extends BaseEntityDao<SParam> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8653206392320991578L;

	/**
	 * default empty constructor
	 */
	public SParamDao() {}

	public SParam queryValue(String name) throws JDBCException{
		String sql = "select * from S_PARAM where param_name=?";
		return createQuery(sql, name).first();
	}

	public void saveParamValue(String name, Integer value) throws JDBCException {
		String sql = "update S_PARAM set param_value=? where param_name=?";
		executeUpdate(sql, value, name);
	}
}

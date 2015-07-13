/**
 * SDataTypeDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDataRightType;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * SDataTypeDao -> S_DATA_TYPE table's operator
 */
@Component
public class SDataRightTypeDao extends BaseEntityDao<SDataRightType> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5086597786887333140L;

	/**
	 * default empty constructor
	 */
	public SDataRightTypeDao() {}

	@SuppressWarnings("unchecked")
	public List queryDynamicData(String tableName,String resultColumn,String selectColumn) throws Exception {
		if(StringHelper.isEmpty(tableName))
			return null;
		String sql = "select "+resultColumn+","+selectColumn+" from "+tableName;
		return createSQLQuery(sql).list();
	}
	
	public List<SDataRightType> queryDataRight() throws JDBCException {
		String sql = "select * from s_data_right_type t order by t.is_level";
		return this.createQuery(sql).list();
	}

}

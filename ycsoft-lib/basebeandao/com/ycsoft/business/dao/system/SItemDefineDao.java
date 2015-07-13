/**
 * SItemDefineDao.java	2010/09/17
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SItemDefine;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * SItemDefineDao -> S_ITEM_DEFINE table's operator
 */
@Component
public class SItemDefineDao extends BaseEntityDao<SItemDefine> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3830496962376492677L;

	/**
	 * default empty constructor
	 */
	public SItemDefineDao() {}

	public List<SItemDefine> findAllDefines(String query) throws JDBCException {
		String sql = "select * from s_item_define s";
		if(StringHelper.isNotEmpty(query)){
			sql = sql + " where s.item_key like '%"+query+"%' or item_desc like '%"+query+"%'";
		}
		return createQuery(SItemDefine.class, sql).list();
	}

}

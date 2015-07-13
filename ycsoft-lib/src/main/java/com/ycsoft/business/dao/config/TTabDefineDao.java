/**
 * TTabDefineDao.java	2012/10/11
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TTabDefine;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * TTabDefineDao -> T_TAB_DEFINE table's operator
 */
@Component
public class TTabDefineDao extends BaseEntityDao<TTabDefine> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5063001393994351663L;

	/**
	 * default empty constructor
	 */
	public TTabDefineDao() {}

	public Pager<TTabDefine> queryTabDefine(Integer start,Integer limit, String keyword )throws Exception{
		String sql = " SELECT t.*,s.item_desc param_name_text FROM t_tab_define t ,s_item_define s " +
				" WHERE t.field_name IS NOT NULL and t.param_name = s.item_key(+) and t.status = ? ";
			if(StringUtils.isNotEmpty(keyword)){
				sql += " and (t.table_name like '%"+keyword+"%' or t.comments like '%"+keyword+"%' " +
						" or t.column_name like '%"+keyword+"%' or t.param_name like '%"+keyword+"%') ";
		    }
		return createQuery(TTabDefine.class,sql,StatusConstants.ACTIVE ).setStart(start).setLimit(limit).page();
	}
	
	/**
	 * 根据表名查询所有的columns.
	 * @param tableNameLike
	 * @return
	 * @throws Exception
	 */
	public List<TTabDefine> findTableAllColumns(String tableNameLike) throws Exception{
		if(StringHelper.isEmpty(tableNameLike)){
			throw new Exception("查询的时候表明不能为空!");
		}
		String sql = " SELECT t.* FROM t_tab_define t WHERE t.table_name like '%"+tableNameLike+"%' ";
		return createQuery(sql).list();
	}
	
	public void updateInvalid(String oldTableName,String oldColumnName) throws Exception {
		String sql ="update t_tab_define set status=? where table_name=? and column_name = ?";
		executeUpdate(sql, StatusConstants.INVALID,oldTableName,oldColumnName);
	}
	
	public TTabDefine queryTab(String tabeName,String columnName) throws Exception{
		String sql = "select * from t_tab_define where table_name = ? and column_name = ?  and status = ? ";
		return createQuery(TTabDefine.class,sql,tabeName,columnName,StatusConstants.ACTIVE ).first();
		
	}
	
}

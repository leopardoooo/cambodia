/**
 * TTemplateColumnOptrDao.java	2013/01/14
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TTemplateColumnOptr;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * TTemplateColumnOptrDao -> T_TEMPLATE_COLUMN_OPTR table's operator
 */
@Component
public class TTemplateColumnOptrDao extends BaseEntityDao<TTemplateColumnOptr> {
	
	/**
	 * default empty constructor
	 */
	public TTemplateColumnOptrDao() {}
	
	public void deleteByOptrIds(String[] optrIds, String[] columnIds) throws Exception {
		String sql = "delete from T_TEMPLATE_COLUMN_OPTR" +
				" where column_id  in ("+sqlGenerator.in(columnIds)+")";
		this.executeUpdate(sql);
	}
	
	public List<TreeDto> queryByColumnIds(String[] columnIds) throws Exception {
		String sql = "select optr_id id from T_TEMPLATE_COLUMN_OPTR where column_id in ("+sqlGenerator.in(columnIds)+")";
		return this.createQuery(TreeDto.class, sql).list();
	}

}

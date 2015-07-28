/**
 * SAreaDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


@Component
public class SItemvalueDao extends BaseEntityDao<SItemvalue> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4259491171455176153L;

	/**
	 * default empty constructor
	 */
	public SItemvalueDao() {}

	public List<SItemvalue> findViewDict(String key) throws JDBCException{
		String sql = "select * from vew_dict where item_key=? order by item_idx";
		return findList(sql,key);
	}

	public List<SItemvalue> findAllViewDict() throws JDBCException{
		String sql = "select item_key, item_name, item_value,show_county_id,item_idx from vew_dict order by item_key,item_idx";
		return findList(sql);
	}

	public List<SItemvalue> findAllKey() throws JDBCException {
		String sql = "select distinct t1.item_key,t2.item_desc from s_itemvalue t1,s_item_define t2 where t1.item_key=t2.item_key  and t2.item_desc is not null";
		return findList(sql);
	}

	/**
	 * 根据键值查询
	 * @param itemKey
	 * @return
	 * @throws JDBCException
	 */
	public List<SItemvalue> queryByKey(String itemKey) throws JDBCException{
		String sql = "select * from s_itemvalue s where s.item_key = ?  order by item_idx";
		return createQuery(sql, itemKey).list();
	}
	public List<SItemvalue> findByName(String itemName) throws JDBCException{
		String sql = "select * from s_itemvalue where item_name like '%"+itemName+"%'";
		return createQuery(sql).list();
	}
	public List<SItemvalue> findValueByName(String itemName) throws JDBCException{
		String sql = "select * from vew_dict where item_name like '%"+itemName+"%'";
		return createQuery(sql).list();
	}
	
	/**
	 * 根据键值删除
	 * @param itemKey
	 * @throws JDBCException
	 */
	public void deleteByKey(String itemKey) throws JDBCException{
		String sql = "delete from s_itemvalue s where s.item_key = ?";
		executeUpdate(sql, itemKey);
	}
	
	public List<SItemvalue> queryItemValues(String itemKey,String dataRight) throws JDBCException{
		String sql = "select * from vew_dict where item_key = ?  and "+ dataRight;
		return createQuery(sql,itemKey).list();
	}
}

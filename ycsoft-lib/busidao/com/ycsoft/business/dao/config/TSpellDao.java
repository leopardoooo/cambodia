package com.ycsoft.business.dao.config;

/**
 * TSpellDao.java	2010/10/18
 */


import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TSpell;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TSpellDao -> T_SPELL table's operator
 */
@Component
public class TSpellDao extends BaseEntityDao<TSpell> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4119466209929571855L;

	/**
	 * default empty constructor
	 */
	public TSpellDao() {}
	
	/**
	 * 更新地址拼音
	 * @param addrId
	 * @param fullSpell
	 * @param sqlSpell
	 * @throws JDBCException
	 */
	public void updateAddrName(String addrId,String fullSpell,String sqlSpell) throws JDBCException{
		String sql = "update t_spell t set t.full_sepll=?,t.seq_sepll=? where t.data_id=?";
		executeUpdate(sql, fullSpell,sqlSpell,addrId);
	}

}

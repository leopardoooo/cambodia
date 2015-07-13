package com.ycsoft.business.dao.prod;

/**
 * PPromFeeUserDao.java	2012/06/28
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromFeeUser;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromFeeUserDao -> P_PROM_FEE_USER table's operator
 */
@Component
public class PPromFeeUserDao extends BaseEntityDao<PPromFeeUser> {
	
	/**
	 * default empty constructor
	 */
	public PPromFeeUserDao() {}

	public List<PPromFeeUser> queryPromFeeUser(String promFeeId) throws JDBCException {
		String sql = "select p.*,t.rule_name,t.rule_str rule_id_text from p_prom_fee_user p,t_rule_define t where t.rule_id=p.rule_id and p.prom_fee_id=? order by p.user_no";
		return createQuery(sql, promFeeId).list();
	}

	public List<PPromFeeUser> queryPromFeeUser(String[] promFeeIds) throws JDBCException {
		String sql = "select p.*,t.rule_name,t.rule_str rule_id_text from p_prom_fee_user p,t_rule_define t where t.rule_id=p.rule_id and p.prom_fee_id in("
				+ getSqlGenerator().in(promFeeIds) + ") order by p.user_no";
		return createQuery(sql).list();
	}
	
	public void deleteByFeeId(String promFeeId) throws JDBCException {
		String sql = " delete from p_prom_fee_user p where p.prom_fee_id=?";
		this.executeUpdate(sql, promFeeId);
	}

}

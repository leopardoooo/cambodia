package com.ycsoft.business.dao.config;

/**
 * TCountyAcctChangeDao.java	2012/04/24
 */
 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TCountyAcctChange;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * TCountyAcctChangeDao -> T_COUNTY_ACCT_CHANGE table's operator
 */
@Component
public class TCountyAcctChangeDao extends BaseEntityDao<TCountyAcctChange> {
	
	/**
	 * default empty constructor
	 */
	public TCountyAcctChangeDao() {}

	public Pager<TCountyAcctChange> queryAcctDetail(String acctId,
			Integer start, Integer limit) throws JDBCException {
		String sql = "select * from t_county_acct_change t where t.t_acct_id=? order by t.done_date desc";
		return createQuery(sql, acctId).setStart(start).setLimit(limit).page();
	}

	public TCountyAcctChange queryChangeByDoneCode(Integer doneCode) throws JDBCException {
		String sql = "select * from t_county_acct_change t where t.done_code=?";
		return createQuery(sql, doneCode).first();
	}

	public void deleteCountyAcctChange(Integer doneCode) throws JDBCException {
		String sql = "delete from t_county_acct_change where done_code = ?";
		executeUpdate(sql, doneCode);
	}

}

/**
 * TPublicAcctitemDao.java	2010/06/18
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TPublicAcctitemDao -> T_PUBLIC_ACCTITEM table's operator
 */
@Component
public class TPublicAcctitemDao extends BaseEntityDao<TPublicAcctitem> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7798339932592838682L;

	/**
	 * default empty constructor
	 */
	public TPublicAcctitemDao() {}
	/**
	 * 得到账目ID
	 * @return
	 * @throws JDBCException
	 */
	public String getAcctItemdId() throws JDBCException{
		return this.findSequence(SequenceConstants.SEQ_PUBLIC_ACCTITEM_ID).toString();
	}

	/**
	 * 查询类型为SPEC的账目
	 * @return
	 * @throws JDBCException
	 */
	public List<TPublicAcctitem> queyrAll() throws JDBCException{
		String sql = "select * from t_public_acctitem t where t.acctitem_type=?";
		return createQuery(TPublicAcctitem.class, sql, SystemConstants.ACCT_TYPE_SPEC).list();
	}
}

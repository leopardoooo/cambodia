/**
 * PPromotionAcctDao.java	2010/09/14
 */

package com.ycsoft.business.dao.prod;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromotionAcctDao -> P_PROMOTION_ACCT table's operator
 */
@Component
public class PPromotionAcctDao extends BaseEntityDao<PPromotionAcct> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2451018368902306768L;

	/**
	 * default empty constructor
	 */
	public PPromotionAcctDao() {}

	/**
	 * 根据促销Id删除记录
	 * @throws JDBCException
	 */
	public void deleteByPromId(String promotionId) throws JDBCException {
		String sql = "delete from p_promotion_acct p where p.promotion_id=?";
		executeUpdate(sql,promotionId);
	}

}

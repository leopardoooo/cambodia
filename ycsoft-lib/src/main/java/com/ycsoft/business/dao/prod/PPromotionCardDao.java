/**
 * PPromotionCardDao.java	2010/09/14
 */

package com.ycsoft.business.dao.prod;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionCard;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromotionCardDao -> P_PROMOTION_CARD table's operator
 */
@Component
public class PPromotionCardDao extends BaseEntityDao<PPromotionCard> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5558650844352345249L;

	/**
	 * default empty constructor
	 */
	public PPromotionCardDao() {}

	/**
	 * 根据促销Id删除记录
	 * @param promotionId
	 * @throws JDBCException
	 */
	public void deleteByPromId(String promotionId) throws JDBCException {
		String sql = "delete from p_promotion_card p where p.promotion_id=?";
		executeUpdate(sql, promotionId);
	}

}

/**
 * PPromotionGiftDao.java	2010/09/14
 */

package com.ycsoft.business.dao.prod;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionGift;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromotionGiftDao -> P_PROMOTION_GIFT table's operator
 */
@Component
public class PPromotionGiftDao extends BaseEntityDao<PPromotionGift> {

	/**
	 *
	 */
	private static final long serialVersionUID = -9057023360320538063L;

	/**
	 * default empty constructor
	 */
	public PPromotionGiftDao() {}

	/**
	 * 根据促销Id删除记录
	 * @param promotionId
	 * @throws JDBCException
	 */
	public void deleteByPromId(String promotionId) throws JDBCException {
		String sql = "delete from p_promotion_gift p where p.promotion_id=?";
		executeUpdate(sql, promotionId);
	}

}

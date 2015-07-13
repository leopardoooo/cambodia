/**
 * CPromotionGiftDao.java	2010/07/26
 */

package com.ycsoft.business.dao.core.promotion;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.promotion.CPromotionGift;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CPromotionGiftDao -> C_PROMOTION_GIFT table's operator
 */
@Component
public class CPromotionGiftDao extends BaseEntityDao<CPromotionGift> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2532253713455204551L;

	/**
	 * default empty constructor
	 */
	public CPromotionGiftDao() {}

	/**
	 * @param promotionSn
	 */
	public void removeBySn(String promotionSn) throws Exception {
		String sql = "delete C_PROMOTION_GIFT where promotion_sn=?";
		executeUpdate(sql, promotionSn);

	}

}

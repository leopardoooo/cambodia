/**
 * CPromotionCardDao.java	2010/07/26
 */

package com.ycsoft.business.dao.core.promotion;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.promotion.CPromotionCard;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CPromotionCardDao -> C_PROMOTION_CARD table's operator
 */
@Component
public class CPromotionCardDao extends BaseEntityDao<CPromotionCard> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5735061869066135886L;

	/**
	 * default empty constructor
	 */
	public CPromotionCardDao() {}

	/**
	 * @param promotionSn
	 */
	public void removeBySn(String promotionSn) throws Exception {
		String sql = "delete C_PROMOTION_CARD where promotion_sn=?";
		executeUpdate(sql, promotionSn);

	}

}

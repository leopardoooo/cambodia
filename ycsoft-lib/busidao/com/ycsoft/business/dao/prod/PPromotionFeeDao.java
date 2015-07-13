/**
 * PPromotionFeeDao.java	2010/09/14
 */

package com.ycsoft.business.dao.prod;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionFee;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromotionFeeDao -> P_PROMOTION_FEE table's operator
 */
@Component
public class PPromotionFeeDao extends BaseEntityDao<PPromotionFee> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8169403598854104825L;

	/**
	 * default empty constructor
	 */
	public PPromotionFeeDao() {}

	/**
	 * 根据促销Id删除记录
	 * @param promotionId
	 * @throws JDBCException
	 */
	public void deleteByPromId(String promotionId) throws JDBCException {
		String sql = "delete from p_promotion_fee p where p.promotion_id=?";
		executeUpdate(sql, promotionId);
	}

}

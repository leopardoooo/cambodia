/**
 * CPromotionDao.java	2010/07/26
 */

package com.ycsoft.business.dao.core.promotion;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.promotion.CPromotionHis;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CPromotionHisDao -> C_PROMOTION_HIS table's operator
 */
@Component
public class CPromotionHisDao extends BaseEntityDao<CPromotionHis> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7391025584102704218L;

	/**
	 * default empty constructor
	 */
	public CPromotionHisDao() {}

	public CPromotionHis queryBySn(String promotionSn) throws JDBCException{
		String sql="select * from c_promotion_his where promotion_sn=? ";
		return this.createQuery(sql, promotionSn).first();
	}
}

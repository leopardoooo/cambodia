package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * PPromotionCountyDao -> P_Promotion_County table's operator
 */
@Component
public class PPromotionCountyDao extends BaseEntityDao<PPromotionCounty> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2938924224154051123L;

	/**
	 * default empty constructor
	 */
	public PPromotionCountyDao() {}

	public void deleteById(String promId) throws JDBCException {
		String sql = "delete from p_promotion_county p where p.promotion_id=?";
		executeUpdate(sql, promId);
	}
	
	public void deleteByAll (List<Object[]> list) throws Exception {
		String	sql = "delete p_promotion_county where promotion_id = ? and county_id = ?  ";
		executeBatch(sql, list);
	}
	
}

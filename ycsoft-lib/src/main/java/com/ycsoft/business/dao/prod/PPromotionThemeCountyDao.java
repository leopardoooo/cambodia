package com.ycsoft.business.dao.prod;

/**
 * PPromotionThemeCountyDao.java	2010/10/09
 */



import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionCounty;
import com.ycsoft.beans.prod.PPromotionThemeCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromotionThemeCountyDao -> P_PROMOTION_THEME_COUNTY table's operator
 */
@Component
public class PPromotionThemeCountyDao extends BaseEntityDao<PPromotionThemeCounty> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5367975545754871429L;

	/**
	 * default empty constructor
	 */
	public PPromotionThemeCountyDao() {}


	public void deleteById(String themeId) throws JDBCException {
		String sql = "delete from p_promotion_theme_county p where p.theme_id=?";
		executeUpdate(sql, themeId);
	}
	
	public List<PPromotionThemeCounty> queryCountyById(String themeId) throws Exception{
		String sql = "select * from p_promotion_theme_county where theme_id = ?";
		return createQuery(PPromotionThemeCounty.class,sql,themeId).list();
	}
	public List<PPromotionCounty> findByThemeId(String themeId) throws Exception{
		String sql = "select t.* from p_promotion_county t,p_promotion t1 where t1.theme_id=? and t1.promotion_id = t.promotion_id ";
		return createQuery(PPromotionCounty.class, sql, themeId).list();
	}
}

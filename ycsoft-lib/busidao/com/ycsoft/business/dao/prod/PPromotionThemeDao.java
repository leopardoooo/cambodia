package com.ycsoft.business.dao.prod;

/**
 * PPromotionThemeDao.java	2010/10/09
 */


import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionTheme;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromotionThemeDao -> P_PROMOTION_THEME table's operator
 */
@Component
public class PPromotionThemeDao extends BaseEntityDao<PPromotionTheme> {

	/**
	 *
	 */
	private static final long serialVersionUID = 497889215092502412L;

	/**
	 * default empty constructor
	 */
	public PPromotionThemeDao() {}


	/**
	 * 得到序列号
	 * @return
	 * @throws JDBCException
	 */
	public String getPromThemeId() throws JDBCException{
		return this.findSequence(SequenceConstants.SEQ_PROM_THEME_ID).toString();
	}

	public List<PPromotionTheme> queryAll(String query,String dataRight) throws JDBCException{
		String sql = "select * from p_promotion_theme p where 1=1 ";
		if(!dataRight.equals(SystemConstants.COUNTY_ALL)){
			sql = sql + " and p.theme_id in (select pp.theme_id from p_promotion_theme_county pp where 1=1 and "+dataRight+") ";
		}
		if(StringHelper.isNotEmpty(query)){
			sql = sql + " and  p.theme_name like '%"+query+"%'";
		}
		return createQuery(PPromotionTheme.class, sql).list();
	}

}

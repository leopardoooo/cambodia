package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionEasyProd;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class PPromotionEasyProdDao  extends BaseEntityDao<PPromotionEasyProd>{

	public List<PPromotionEasyProd> queryPromotionByType(String promotionType) throws JDBCException{
		String sql="select * from P_PROMOTION_EASY_PROD "
				+ " where eff_date<=trunc(sysdate) and (exp_date is null or exp_date >=trunc(sysdate)) "
				+" and promotion_type =? and prod_id is not null and tariff_id is not null and order_cycles>0 ";
		return  this.createQuery(sql, promotionType).list();
	}
}

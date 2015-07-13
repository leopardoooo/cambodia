/**
 * TPayTypeDao.java	2010/11/11
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TPayType;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TPayTypeDao -> T_PAY_TYPE table's operator
 */
@Component
public class TPayTypeDao extends BaseEntityDao<TPayType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5743437594882405132L;

	/**
	 * default empty constructor
	 */
	public TPayTypeDao() {}
	
	public List<TPayType> queryPayType(String dataRight) throws Exception{
		String sql ="select * from t_pay_type where "+dataRight;
		return this.createQuery(sql).list();
	}

}

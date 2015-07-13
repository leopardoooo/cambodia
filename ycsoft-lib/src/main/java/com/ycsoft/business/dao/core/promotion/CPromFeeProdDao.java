package com.ycsoft.business.dao.core.promotion;

/**
 * CPromFeeProdDao.java	2012/07/11
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.promotion.CPromFeeProd;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CPromFeeProdDao -> C_PROM_FEE_PROD table's operator
 */
@Component
public class CPromFeeProdDao extends BaseEntityDao<CPromFeeProd> {
	
	/**
	 * default empty constructor
	 */
	public CPromFeeProdDao() {}

	public List<CPromFeeProd> queryFeeProdByDoneCode(Integer doneCode) throws JDBCException {
		String sql = " select c.* from c_prom_fee_prod c,c_prom_fee p where c.prom_fee_sn=p.prom_fee_sn and p.done_code=?";
		return createQuery(sql, doneCode).list();
	}

}

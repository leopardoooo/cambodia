/**
 * CPromProdRefundDao.java	2013/03/28
 */
 
package com.ycsoft.business.dao.core.promotion; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.promotion.CPromProdRefund;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CPromProdRefundDao -> C_PROM_PROD_REFUND table's operator
 */
@Component
public class CPromProdRefundDao extends BaseEntityDao<CPromProdRefund> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7974710102018301793L;

	/**
	 * default empty constructor
	 */
	public CPromProdRefundDao() {}
	public List<CPromProdRefund> querySelectPromFee(String userId,String prodSn,String countyId) throws JDBCException {
		String sql = StringHelper.append("select  pf.prom_fee_id,pf.prom_fee_name ,cf.create_time ,cf.prom_fee_sn,cp.prod_sn " +
				"from c_prom_fee_prod cp, c_prom_fee cf,p_prom_fee pf where cp.user_id = ? and cp.prod_sn = ? " +
				"and cf.county_id= ? and cp.prom_fee_sn = cf.prom_fee_sn and cf.prom_fee_id= pf.prom_fee_id ");
		return createQuery(sql,userId,prodSn,countyId).list();
	}

	public void savePromProdRefund (String prom_fee_sn,String[] prodSns,Integer done_code,Integer refund) throws Exception {
		String	sql = "insert into C_PROM_PROD_REFUND(PROD_SN,PROM_FEE_SN,done_code,refund_pay) values (?, '"+prom_fee_sn+"','"+done_code+"','"+refund+"')";
		 executeBatch(sql, prodSns);
	}
}

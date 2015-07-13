package com.ycsoft.business.dao.core.acct;

/**
 * CGeneralContractPayDao.java	2012/05/23
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CGeneralContractPay;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CGeneralContractPayDao -> C_GENERAL_CONTRACT_PAY table's operator
 */
@Component
public class CGeneralContractPayDao extends BaseEntityDao<CGeneralContractPay> {
	
	/**
	 * default empty constructor
	 */
	public CGeneralContractPayDao() {}

	public Pager<CGeneralContractPay> queryPayInfo(String contractId,
			Integer start, Integer limit) throws JDBCException {
		Pager<CGeneralContractPay> pager = new Pager<CGeneralContractPay>();
		//加入fee_id 方便在修改账目日起的时候
		String sql = "select f.acct_date,f.fee_sn, t.*  from c_general_contract_pay t, c_fee f " +
				" where   t.done_code = f.create_done_code and t.contract_id=? ";
		if( limit ==null || limit ==0 ){
			List<CGeneralContractPay> list = createQuery(CGeneralContractPay.class, sql, contractId).list();
			pager.setRecords(list);
			return pager;
		}else{
			pager = createQuery(CGeneralContractPay.class, sql, contractId).setStart(start).setLimit(limit).page();
		}
		return pager;
	}

}

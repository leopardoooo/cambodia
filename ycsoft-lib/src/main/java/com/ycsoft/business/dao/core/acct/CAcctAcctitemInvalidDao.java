package com.ycsoft.business.dao.core.acct;

/**
 * CAcctAcctitemInvalidDao.java	2011/04/16
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemInvalid;
import com.ycsoft.business.dto.core.acct.AcctAcctitemInvalidDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CAcctAcctitemInvalidDao -> C_ACCT_ACCTITEM_INVALID table's operator
 */
@Component
public class CAcctAcctitemInvalidDao extends BaseEntityDao<CAcctAcctitemInvalid> {
	
	/**
	 * default empty constructor
	 */
	public CAcctAcctitemInvalidDao() {}
	
	public int updateInvalidFee(String acctId,String acctItemId,String feeType,int fee) throws Exception{
		String sql ="update c_acct_acctitem_invalid set invalid_fee=invalid_fee+? where acct_id=? and acctitem_id=? " +
				" and fee_type=?";
		return this.executeUpdate(sql, fee,acctId,acctItemId,feeType);
	}
	public List<AcctAcctitemInvalidDto> queryAcctitemInvalidByCustId(String custId) throws Exception{
		String sql ="SELECT c.acct_id,c.acctitem_id,c.fee_type, sum(c.invalid_fee) invalid_fee, t.acctitem_name ,a.can_refund" +
				" FROM C_ACCT_ACCTITEM_INVALID c,vew_acctitem t ,t_acct_fee_type a where c.cust_id = ?   and t.acctitem_id =c.acctitem_id  " +
				" and c.fee_type = a.fee_type  group by c.acct_id,c.acctitem_id,c.fee_type, t.acctitem_name,a.can_refund ";
		return this.createQuery(AcctAcctitemInvalidDto.class, sql, custId).list();
	}
}

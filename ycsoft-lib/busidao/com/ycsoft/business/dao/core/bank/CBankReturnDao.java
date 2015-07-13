/**
 * CBankReturnDao.java	2013/09/05
 */
 
package com.ycsoft.business.dao.core.bank; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.business.dto.core.acct.BankReturnDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.DataHandler;
import com.ycsoft.daos.core.JDBCException;


/**
 * CBankReturnDao -> C_BANK_RETURN table's operator
 */
@Component
public class CBankReturnDao extends BaseEntityDao<CBankReturn> {
	
	/**
	 * default empty constructor
	 */
	public CBankReturnDao() {}

	public void queryBankList(DataHandler<CBankReturn> handler) throws Exception{
		final String sql="SELECT * "
						 +" FROM C_BANK_RETURN"
						 +" WHERE PAY_STATUS IS NULL"
						 +" ORDER BY TO_NUMBER(BANK_TRANS_SN)";
		
		this.queryForResult(handler, sql);
	}

	public List<BankReturnDto> queryUnExecBankRetrun() throws JDBCException {
		String sql = "select * from( SELECT a.*,b.file_no, b.cust_id, b.cust_name,  b.acct_id, b.acctitem_id, b.bill_sn, b.start_date, b.end_date,  b.bank_fee_name, b.fee, b.county_id, b.area_id, b.user_id, b.prod_sn"
				+ " FROM C_BANK_RETURN a ,c_Bank_Gotodisk b WHERE a.trans_sn=b.trans_sn and PAY_STATUS IS NULL union all SELECT a.*,b.file_no, b.cust_id, b.cust_name,  b.acct_id, b.acctitem_id, b.bill_sn, b.start_date, b.end_date,  b.bank_fee_name, b.fee, b.county_id, b.area_id, b.user_id, ''"
				+ " FROM C_BANK_RETURN a ,c_Bank_Refundtodisk b WHERE a.trans_sn=b.trans_sn and PAY_STATUS IS NULL) ORDER BY TO_NUMBER(BANK_TRANS_SN)";
		return createQuery(BankReturnDto.class, sql).list();
	}
	
	public void updateFailure() throws JDBCException{
		String sql = "UPDATE C_BANK_RETURN t SET pay_status = 'INVALID'  WHERE is_success<>'0000' and t.pay_status is null";
		executeUpdate(sql);
	}

}

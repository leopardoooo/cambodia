package com.ycsoft.business.dao.core.acct;

/**
 * CGeneralContractDao.java	2011/01/24
 */
 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CGeneralContract;
import com.ycsoft.business.dto.core.acct.GeneralContractDto;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * CGeneralContractDao -> C_GENERAL_CONTRACT table's operator
 */
@Component
public class CGeneralContractDao extends BaseEntityDao<CGeneralContract> {
	

	/**
	 * default empty constructor
	 */
	public CGeneralContractDao() {}

	/**
	 * 根据地区查询预收款或工程款
	 * @param limit 
	 * @param start 
	 * @param countyId
	 * @param string 
	 * @return
	 * @throws JDBCException
	 */
	public Pager<GeneralContractDto> queryGeneralContracts(Integer start, Integer limit, String query, String countyId) throws JDBCException {
		String sql = StringHelper.append("SELECT C.*, a.left_amount, (A.INTI_AMOUNT - a.left_amount) used_amount,",
				" f.invoice_id,f.invoice_code,f.invoice_mode,f.invoice_book_id,f.acct_date,f.real_pay,r.finance_status," +
				" case when c.nominal_amount>0 then d.refund_amount else 0 end refund_amount",
				" FROM C_GENERAL_CONTRACT C, (select sum(c.Payed_Amount) refund_amount,c.contract_no from c_general_contract c  group by c.contract_no) d ," +
				" (SELECT B.CONTRACT_ID, SUM(B.AMOUNT) INTI_AMOUNT, SUM(B.BALANCE) LEFT_AMOUNT",
				" FROM C_GENERAL_CREDENTIAL B GROUP BY B.CONTRACT_ID) A ,c_fee f,r_invoice r" +
				" WHERE c.contract_id = a.contract_id(+)  and c.fee_sn=f.fee_sn(+) and c.county_id = ?",
				" and f.invoice_id=r.invoice_id(+) and f.invoice_book_id=r.invoice_book_id(+) and c.contract_no = d.contract_no ");
		if(StringHelper.isNotEmpty(query)){
			sql = StringHelper.append(sql,"and (c.contract_no like '%"+query+"%'"," or c.cust_name like '%"+query+"%')");
		}
		sql = StringHelper.append(sql," order by c.create_time desc");
		
		return createQuery(GeneralContractDto.class, sql, countyId).setStart(start).setLimit(limit).page();
	}

	/**
	 * 根据合同号查询
	 * @param contractNo
	 * @return
	 * @throws JDBCException 
	 */
	public CGeneralContract queryByContractNo(String contractNo) throws JDBCException {
		String sql = StringHelper.append("select c.* from c_general_contract c",
		" where c.contract_no =?");
		return createQuery(CGeneralContract.class, sql, contractNo).first();
	}

}

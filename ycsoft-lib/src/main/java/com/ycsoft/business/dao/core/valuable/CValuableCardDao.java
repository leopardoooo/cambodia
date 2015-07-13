package com.ycsoft.business.dao.core.valuable;

/**
 * CVoucherDao.java	2011/01/25
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.valuable.CValuableCard;
import com.ycsoft.business.dto.device.ValuableCardDto;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;



/**
 * CVoucherDao -> C_VOUCHER table's operator
 */
@Component
public class CValuableCardDao extends BaseEntityDao<CValuableCard> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4843572868667995579L;

	/**
	 * default empty constructor
	 */
	public CValuableCardDao() {}

	public List<CValuableCard> queryByValuableId(String[] cards) throws JDBCException {
	String sql = StringHelper.append(
			"SELECT valuable_id from c_valuable_card where 1=1 and("+getSqlGenerator().setWhereInArray("valuable_id",cards)+") ");
	return createQuery(CValuableCard.class, sql).list();
	}	
	
	public Pager<ValuableCardDto> queryValuableAllCard(Integer start, Integer limit, String query, String countyId) throws JDBCException {
		String sql = StringHelper.append("select t.*,t1.fee_sn,t1.invoice_id,t1.invoice_code,t1.invoice_mode,t1.invoice_book_id," +
				" t1.acct_date,t1.real_pay, t2.finance_status " +
				" from c_valuable_card t, c_fee t1,r_invoice t2 " +
				" where t.done_code = t1.create_done_code and t1.invoice_id = t2.invoice_id(+) " +
				" and t1.invoice_book_id = t2.invoice_book_id(+) and t1.county_id =t.county_id and t.county_id = ? ");
		if(StringHelper.isNotEmpty(query)){
			sql = StringHelper.append(sql,"and (t.valuable_id like '%"+query+"%' or t1.invoice_id like '%"+query+"%' or t.done_code like '%"+query+"%')");
		}
		sql = StringHelper.append(sql," order by t.done_code desc");
		
		return createQuery(ValuableCardDto.class, sql, countyId).setStart(start).setLimit(limit).page();
	}
	
	public Pager<ValuableCardDto> queryValuableHisAllCard(Integer start, Integer limit, String query, String countyId) throws JDBCException {
		String sql = StringHelper.append("select t.amount,t.valuable_id,t.busi_done_code done_code,t1.fee_sn,t1.invoice_id," +
				" t1.invoice_code,t1.invoice_mode,t1.invoice_book_id," +
				" t1.acct_date,t1.real_pay, t2.finance_status,t.cust_name " +
				" from c_valuable_card_his t, c_fee t1,r_invoice t2 " +
				" where t.busi_done_code = t1.create_done_code and t1.invoice_id = t2.invoice_id(+) " +
				" and t1.invoice_book_id = t2.invoice_book_id(+) and t1.county_id =t.county_id and t.county_id = ? ");
		if(StringHelper.isNotEmpty(query)){
			sql = StringHelper.append(sql,"and (t.valuable_id like '%"+query+"%' or t1.invoice_id like '%"+query+"%' " +
					"or t.busi_done_code like '%"+query+"%' or t.done_code like '%"+query+"%')");
		}
		sql = StringHelper.append(sql," order by done_code desc");
		
		return createQuery(ValuableCardDto.class, sql, countyId).setStart(start).setLimit(limit).page();
	}
	
	public void editValuableCard(String doneCode,String custName) throws JDBCException {
		String sql = StringHelper.append(
				"update c_valuable_card set cust_name =? where done_code =? ");
		executeUpdate(sql, custName,doneCode);
	}
	public List<CValuableCard> queryBydoneCode(Integer doneCode) throws JDBCException {
		String sql = StringHelper.append("SELECT * from c_valuable_card where done_code =? ");
		return createQuery(CValuableCard.class, sql,doneCode).list();
	}	
	public List<ValuableCardDto> queryById(String[] valuableId,String countyId) throws JDBCException {
		String sql = "select * from c_valuable_card  where   county_id =?  and ("+getSqlGenerator().setWhereInArray("valuable_id",valuableId)+")";
		return createQuery(ValuableCardDto.class, sql,countyId).list();
	}	
	public void deleteById(String[] valuableId) throws JDBCException {
		String sql = "delete c_valuable_card where valuable_id = ?  ";
		executeBatch(sql,valuableId);
	}	
}

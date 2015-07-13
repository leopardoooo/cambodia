package com.ycsoft.business.dao.core.promotion;

/**
 * CPromFeeDao.java	2012/07/11
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.promotion.CPromFee;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CPromFeeDao -> C_PROM_FEE table's operator
 */
@Component
public class CPromFeeDao extends BaseEntityDao<CPromFee> {
	
	/**
	 * default empty constructor
	 */
	public CPromFeeDao() {}

	public List<CAcctAcctitemInactive> queryInactiveByDoneCode(
			Integer doneCode) throws JDBCException {
		String sql = "select ca.* from c_prom_fee cp,c_acct_acctitem_inactive ca where cp.prom_fee_sn=ca.promotion_sn and cp.done_code=?";
		return this.createQuery(CAcctAcctitemInactive.class,sql,doneCode).list();
	}
	
	public CPromFee queryPromFeeByDoneCode(Integer doneCode,String countId) throws Exception{
		String sql = "select * from c_prom_fee c where c.done_code=? and county_id=?";
		return this.createQuery(sql, doneCode,countId).first();
	}

	public void removeWithHis(Integer doneCode, Integer feeDoneCode) throws JDBCException {
		//记录历史
		String sql = " insert into c_prom_fee_his (select DONE_CODE create_done_code,PROM_FEE_SN,prom_fee_id, "
			+ " CUST_ID,STATUS,AREA_ID,COUNTY_ID,CREATE_TIME,? DONE_CODE from c_prom_fee c where c.done_code=?)";
		this.executeUpdate(sql, doneCode,feeDoneCode);
		//记录历史
		sql = "insert into c_prom_fee_prod_his (select * from c_prom_fee_prod c where c.prom_fee_sn in (select prom_fee_sn from c_prom_fee where done_code=?))";
		this.executeUpdate(sql, feeDoneCode);
		
		//删除记录
		sql = "delete from c_prom_fee_prod c where c.prom_fee_sn in (select prom_fee_sn from c_prom_fee where done_code=?)";
		this.executeUpdate(sql, feeDoneCode);
		//删除记录
		sql = "delete from c_prom_fee c where c.done_code=?";
		this.executeUpdate(sql, feeDoneCode);
	}
	public List<CPromFee> queryPromFee(String[] promFeeIds,String custId,String countyID) throws JDBCException {
		String sql = "select * from c_prom_fee where  cust_id = ? and county_id = ? and prom_fee_id in("
				+ getSqlGenerator().in(promFeeIds) + ") ";
		return createQuery(sql,custId,countyID).list();
	}
	
	public Pager<CPromFee> queryPromFeeByCust(String custId,String countyId, Integer start, Integer limit) throws Exception {
		String sql = "select cp.*,pp.prom_fee_name||'_'||pp.prom_fee/100||'元_'||to_char(cp.create_time,'yyyymmdd hh24:mi:ss') prom_fee_name from c_prom_fee cp,p_prom_fee pp where cp.prom_fee_id=pp.prom_fee_id " +
				" and cp.cust_id = ? and cp.county_id=? order by cp.create_time desc ";
		return this.createQuery(CPromFee.class, sql, custId,countyId).setStart(start).setLimit(limit).page();
	}
	
	public List<CPromFee>  queryCustPromFee(String custId,String countyId)throws Exception{
		String sql = " select cp.*,pp.prom_fee_name,pp.prom_fee ,t.optr_id,t.busi_code from c_prom_fee cp,p_prom_fee pp,c_done_code t " +
				" where  cp.cust_id = ? and cp.county_id = ?  and  cp.prom_fee_id=pp.prom_fee_id and t.done_code =cp.done_code " +
				" and t.county_id = ?  order by cp.create_time desc  ";
		return this.createQuery(CPromFee.class , sql,custId, countyId,countyId).list();

	}
	
}

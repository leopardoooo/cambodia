package com.ycsoft.business.dao.prod;

/**
 * PPromFeeDao.java	2012/06/28
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * PPromFeeDao -> P_PROM_FEE table's operator
 */
@Component
public class PPromFeeDao extends BaseEntityDao<PPromFee> {
	
	/**
	 * default empty constructor
	 */
	public PPromFeeDao() {}

	public List<PPromFee> querySelectablePromPay(String countyId) throws JDBCException {
		String sql = StringHelper.append("select * from p_prom_fee p ,p_prom_fee_county pt where p.prom_fee_id=pt.prom_fee_id ",
				" and sysdate>trunc(p.eff_date) and (p.exp_date is null or sysdate < trunc(p.exp_date)) and pt.county_id=? and p.status = ? ");
		return createQuery(sql,countyId,StatusConstants.ACTIVE).list();
	}

	public Pager<PPromFee> queryPromFee(String keyword ,String countyId,Integer start,Integer limit ) throws JDBCException {
		String sql = "select distinct t.*,t2.printitem_name from p_prom_fee t,t_printitem t2,p_prom_fee_county t3"
			+ " where t.printitem_id=t2.printitem_id  and t.prom_fee_id=t3.prom_fee_id and t3.county_id='"+countyId+"'";
		if(countyId.equals(SystemConstants.COUNTY_ALL)){
			sql = "select distinct t.*,t2.printitem_name from p_prom_fee t,t_printitem t2"
				+ " where t.printitem_id=t2.printitem_id";
		}
		if(StringHelper.isNotEmpty(keyword)){
			sql = StringHelper.append(sql," and t.prom_fee_name like '%"+keyword+"%'");
		}
		return createQuery(sql).setStart(start).setLimit(limit).page();
	}

	public List<PPromFee> queryIsPromFee(String userId, String prodId) throws JDBCException {
		String sql = StringHelper.append("select distinct pf.prom_fee_name from c_prom_fee_prod p,c_prom_fee c,busi.p_prom_fee pf ",
		" where p.prom_fee_sn=c.prom_fee_sn and c.status=? and pf.prom_fee_id=c.prom_fee_id ",
		" and c.create_time> add_months(last_day(trunc(sysdate))+1,-2) and c.create_time<last_day(trunc(sysdate))+1 ",
		" and p.user_id=? and p.prod_id= ? ");
		return createQuery(sql,StatusConstants.ACTIVE,userId,prodId).list();
	}

}

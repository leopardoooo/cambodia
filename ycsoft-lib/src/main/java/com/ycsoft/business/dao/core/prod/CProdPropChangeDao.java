/**
 * CProdPropChangeDao.java	2010/07/13
 */

package com.ycsoft.business.dao.core.prod;


import java.util.List;

import org.springframework.stereotype.Component;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CProdPropChangeDao -> C_PROD_PROP_CHANGE table's operator
 */
@Component
public class CProdPropChangeDao extends BaseEntityDao<CProdPropChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4710329294236197489L;

	/**
	 * default empty constructor
	 */
	public CProdPropChangeDao() {}
	
	/**
	 * @param prodSn
	 * @param county_id
	 * @return
	 */
	public CProdPropChange queryLastStatus(String prodSn, String countyId) throws Exception{
		String sql = "select * from C_PROD_PROP_CHANGE where prod_sn=? and county_id=? and COLUMN_NAME='status' order by change_time desc";
		return this.createQuery(sql, prodSn,countyId).first();
	}

	public CProdPropChange queryOldDate(int doneCode, String countyId) throws Exception{
		String sql = "select * from C_PROD_PROP_CHANGE where  done_code = ? and county_id=? and COLUMN_NAME='invalid_date' order by change_time desc ";
		return this.createQuery(sql, doneCode,countyId).first();
	}
	
	public void removeByDoneCode(String userId, Integer doneCode,String countyId) throws Exception{
		String sql = "delete C_PROD_PROP_CHANGE " +
				" where prod_sn =? " +
				" and done_code=? " +
				" and county_id=? ";
		executeUpdate(sql,  userId,doneCode,countyId);
	}

	/**
	 * 查找套餐缴费生成的零资费产品异动
	 * @param doneCode
	 * @return
	 */
	public List<CProdPropChange> queryPromPayChange(Integer doneCode,String countyId) throws JDBCException {
//		String sql = StringHelper.append("select distinct t1.* from c_prod_prop_change t1,c_prom_fee t2,c_prom_fee_prod t3,c_prod t4,p_prod_tariff t5",
//				" where t1.prod_sn=t4.prod_sn and t2.prom_fee_sn=t3.prom_fee_sn and t3.user_id=t4.user_id ",
//				" and t3.prod_id=t4.prod_id and t2.county_id=t4.county_id and t2.cust_id=t4.cust_id  ",
//				" and t4.tariff_id=t5.tariff_id and t5.rent=0 and t1.column_name='invalid_date' and t1.done_code=?");
		String sql=StringHelper.append("  select ch.prod_sn,min(ch.old_value) old_value ",
				"from busi.c_prod_prop_change ch,c_prom_fee pf,c_prom_fee_prod pfp,p_prod_tariff ppt ",
         " where ch.column_name='invalid_date' and ch.prod_sn=pfp.prod_sn ",
        " and ppt.tariff_id=pfp.tariff_id and (ppt.rent=0 or ppt.billing_cycle>1) ",
        " and pfp.prom_fee_sn=pf.prom_fee_sn and pf.done_code=? and ch.done_code=? and ch.county_id=? group by ch.prod_sn");
		return createQuery(sql, doneCode,doneCode,countyId).list();
	}
}

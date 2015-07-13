/**
 * PProdTariffDisctDao.java	2010/07/15
 */

package com.ycsoft.business.dao.prod;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * PProdTariffDisctDao -> P_PROD_TARIFF_DISCT table's operator
 */
@Component
public class PProdTariffDisctDao extends BaseEntityDao<PProdTariffDisct> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5904130966380676349L;

	/**
	 * default empty constructor
	 */
	public PProdTariffDisctDao() {
	}

	/**
	 * 查询多个资费的折扣信息
	 *
	 * @param tariffId
	 * @return
	 * @throws Exception
	 */
	public List<PProdTariffDisct> queryDisctByTariffIds(String[] tariffIds, String countyId)
			throws Exception {
		List<PProdTariffDisct> disctList = new ArrayList<PProdTariffDisct>();
		String sql = "";
		if (tariffIds.length>0){
			sql = "select t.*,r.rule_str rule_id_text,t.disct_name||'(缴费满'||t.min_pay/100||'元)' disct_name_all" +
					" from p_prod_tariff_disct t,p_prod_tariff_disct_county ct,t_rule_define r" +
					" where trunc(t.eff_date) <sysdate and (t.exp_date is null or t.exp_date > trunc(sysdate))" +
					" and t.status=? and t.rule_id=r.rule_id(+) and t.disct_id=ct.disct_id" +
					" and(" + getSqlGenerator().setWhereInArray("t.tariff_id",tariffIds) + ") ";
			if(!countyId.equals(SystemConstants.COUNTY_ALL)){
				sql += " and ct.county_id='" + countyId + "'";
			}
			disctList.addAll(this.createQuery(PProdTariffDisct.class, sql,
					StatusConstants.ACTIVE).list());
		}
		return disctList;
	}

	/**
	 * 查询多个资费的折扣信息
	 * @param tariffId
	 * @return
	 * @throws Exception
	 */
	public List<PProdTariffDisct> queryDisctByTariffId(String tariffId, String countyId) throws Exception{
		String sql = "select distinct t.* from p_prod_tariff_disct t,p_prod_tariff_disct_county ct" +
				" where t.disct_id=ct.disct_id and tariff_id = ?";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and ct.county_id='" + countyId + "'";
		}
		sql += " order by create_time desc ";
		return this.createQuery(PProdTariffDisct.class, sql,tariffId).list();
	}

	public List<PProdTariffDisct> queryDisctByPordId(String prodId) throws Exception{
		String sql = "select * from p_prod_tariff_disct t1,t_rule_define t2,p_prod_tariff t3 " +
				"where t1.rule_id=t2.rule_id and t1.tariff_id = t3.tariff_id and t3.prod_id = ? ";
		return this.createQuery(PProdTariffDisct.class, sql,prodId).list();
	}
	
	public void deleteDisctByDisctId(String disctId) throws Exception {
		String sql ="update p_prod_tariff_disct set status=? where disct_id=?";
		executeUpdate(sql, StatusConstants.INVALID,disctId);
	}
}

/**
 * PSpkgDao.java	2015/09/05
 */
 
package com.ycsoft.business.dao.prod; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PSpkg;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * PSpkgDao -> P_SPKG table's operator
 */
@Component
public class PSpkgDao extends BaseEntityDao<PSpkg> {
	
	/**
	 * default empty constructor
	 */
	public PSpkgDao() {}
	
	public PSpkg querySpkgBySn(String spkgSn) throws Exception {
		return this.createQuery("select * from p_spkg where spkg_sn=? and eff_date<sysdate and (exp_date is null or exp_date>sysdate)", spkgSn).first();
	}
	
	public Pager<PSpkg> querySpkg(String query, Integer start, Integer limit) throws Exception {
		String sql = "select distinct c.cust_no,c.cust_name, wm_concat(distinct p.prod_name) prod_name,s.*"
				+ " from p_spkg s, c_cust c, p_prod p, p_prod_tariff ppt"
				+ " where s.spkg_sn=c.spkg_sn(+) and s.spkg_sn=ppt.spkg_sn(+) and ppt.prod_id=p.prod_id(+)";
		if(StringHelper.isNotEmpty(query)){
			sql += " and s.spkg_title like '%"+query+"%' or spkg_text like '%"+query+"%'";
		}
		sql += "group by c.cust_no,c.cust_name,s.sp_id, s.spkg_sn, s.spkg_title, s.spkg_text, s.eff_date, s.exp_date, "
				+ "s.remark, s.optr_id, s.create_time, s.status,s.confirm_optr_id, s.confirm_date, s.apply_optr_id, s.apply_date";
		return this.createQuery(sql).setStart(start).setLimit(limit).page();
	}
	
	public int countBySpkgSn(String spId, String spkgSn) throws Exception {
		String sql = "select count(1) from p_spkg where spkg_sn=?";
		if(StringHelper.isNotEmpty(spId)){
			sql += " and sp_id<>'"+spId+"'";
		}
		return this.count(sql, spkgSn);
	}

}

/**
 * PSpkgDao.java	2015/09/05
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PSpkg;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
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
		String sql = "select  c.cust_no,c.cust_name,s.*"
				+ " from p_spkg s, c_cust c "
				+ " where s.spkg_sn=c.spkg_sn(+) ";
		if(StringHelper.isNotEmpty(query)){
			sql += " and s.spkg_title like '%"+query+"%' or s.spkg_text like '%"+query+"%'";
		}
		sql += " order by s.create_time desc";
		Pager<PSpkg> page= this.createQuery(sql).setStart(start).setLimit(limit).page();
		
		return page;
	}
	
	public List<String> queryProdName(String spkg_sn) throws JDBCException{
		String sql="select distinct p.prod_name "
				+ " from p_prod p ,p_prod_tariff ppt where p.prod_id=ppt.prod_id and ppt.spkg_sn=? ";
		return this.findUniques(sql, spkg_sn);
	}
	
	public int countBySpkgSn(String spId, String spkgSn) throws Exception {
		String sql = "select count(1) from p_spkg where spkg_sn=?";
		if(StringHelper.isNotEmpty(spId)){
			sql += " and sp_id<>'"+spId+"'";
		}
		return this.count(sql, spkgSn);
	}

}

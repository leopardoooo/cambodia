/**
 * TServerResDao.java	2010/11/18
 */
 
package com.ycsoft.business.dao.prod;


import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.prod.ResDto;



/**
 * TServerResDao -> T_SERVER_RES table's operator
 */
@Component
public class TServerResDao extends BaseEntityDao<TServerRes> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1371751819784808866L;

	/**
	 * default empty constructor
	 */
	public TServerResDao() {}
	
	public List<TServerRes> queryBandRes(String serverId) throws Exception {
		String sql = "select * from t_server_res where server_id=?";
		return this.createQuery(sql, serverId).list();
	}
	
	public void deleteServerRes(TServerRes sres) throws JDBCException {
		String sql = "delete from t_server_res t" +
				" where t.boss_res_id=? and t.external_res_id=? and t.server_id=?";
		this.executeUpdate(sql, sres.getBoss_res_id(), sres.getExternal_res_id(), sres.getServer_id());
	}
	
	public TServerRes queryServerRes(String bossResId,String serverId) throws JDBCException {
		String sql = "select * from t_server_res t" +
		" where t.boss_res_id=? and t.server_id=?";
		return createQuery(sql,bossResId, serverId).first();
	}
	
	public List<TServer> queryServerByServType(String servType) throws JDBCException {
		String sql = "select * from t_server where serv_type=?";
		return this.createQuery(TServer.class, sql, servType).list();
	}
	
	public List<TServer> queryServerByCountyId(String countyId) throws JDBCException {
		String sql = "";
		if (countyId.equals(SystemConstants.COUNTY_ALL)) {
			sql = "select * from t_server";
			return this.createQuery(TServer.class, sql).list();
		} else {
			sql = "select * from t_server s,t_server_county sc where s.server_id=sc.server_id and sc.county_id=?";
			return this.createQuery(TServer.class, sql, countyId).list();
		}
		
	}
	
	public List<TServerRes> getServerRes(String[] county,Boolean key) throws JDBCException {
		String sql = "";
		if(key){
			sql +=" select distinct t.external_res_id,t.boss_res_id,'"+SystemConstants.COUNTY_ALL+"' county_id from t_server_res t  ";
		}
		if(county.length>0){
			if(key){
				sql +=" union all ";
			}
			sql += "select distinct t1.external_res_id,t1.boss_res_id,t2.county_id from t_server_res t1,t_server_county t2 " +
					" where t1.server_id=t2.server_id and("+getSqlGenerator().setWhereInArray("t2.county_id",county)+") ";
		}
		return this.createQuery(sql).list();
	}
	
	
	public List<TServerRes> getServerRes(String countyId) throws JDBCException {
		String sql = "";
		if(SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append("select distinct t.external_res_id,t.boss_res_id from t_server_res t");
		}else{
			sql = StringHelper.append("select distinct t1.external_res_id,t1.boss_res_id from t_server_res t1,t_server_county t2 where t1.server_id=t2.server_id",
				" and t2.county_id='",countyId,"'");
		}
		
		return this.createQuery(sql).list();
	}

	public ResDto validRes(String countyId,String resId) throws Exception{
		 String sql = "select count(1) cnt  from t_server_res t ,t_server_county c where t.boss_res_id= ?  and t.server_id=c.server_id and c.county_id= ? ";
		 return createQuery(ResDto.class,sql,resId,countyId).first();
	}
	
	public Pager<TServerRes> queryServerRes(String resId, String keyword, String countyId,
			Integer start, Integer limit) throws JDBCException {
		String sql = "";
		if(SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = "select t.*,r.res_name boss_res_name,s.server_name,s.serv_type from t_server_res t,p_res r,t_server s" +
					" where t.boss_res_id=r.res_id and t.server_id=s.server_id and r.res_id=?";
		}else{
			sql = "select t.*,r.res_name boss_res_name,s.server_name,s.serv_type" +
				" from t_server_res t,t_server_county t2,p_res r,t_server s" +
				" where t.server_id=t2.server_id and t.boss_res_id=r.res_id"+
				" and t.server_id=s.server_id and r.res_id=? and t2.county_id='"+countyId+"'";
		}
		if(StringHelper.isNotEmpty(keyword)){
			sql += " and t.res_name like '%"+keyword+"%'";
		}
		sql += " order by r.create_time desc";
		return this.createQuery(sql, resId).setStart(start).setLimit(limit).page();
	}
}

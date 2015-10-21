/**
 * PDictProdDao.java	2010/07/06
 */

package com.ycsoft.business.dao.prod;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PRes;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.prod.ResDto;


/**
 * PRESDao -> P_RES table's operator
 */
@Component
public class PResDao extends BaseEntityDao<PRes> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6031215381207969236L;

	/**
	 * default empty constructor
	 */
	public PResDao() {}
	
	public Pager<ResDto> queryRes(String servId, String keyword, String countyId,
			Integer start, Integer limit) throws JDBCException {
		String cond = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			cond = " and (p.county_id='"+SystemConstants.COUNTY_ALL+"' or p.county_id='"+countyId+"') ";
		}
		String sql = "select p.res_id,p.res_name,p.serv_id,p.res_desc,p.status,p.create_time,p.res_type,p.currency,p.optr_id,p.area_id,p.county_id,p.band_width,"
			+ " case when count(t.id)>0 then 'T' else 'F' end isRecommend"
			+ " from p_res p,("
				+ " select a.prod_id id,a.res_id from p_prod_static_res a"
					+ " union"
				+ " select b.prod_id id,b.res_id from p_prod_county_res b"
					+ " union"
				+ " select c.group_id id,c.res_id from p_resgroup_res c"
			+ " ) t where p.res_id=t.res_id(+)" + cond;
		if(StringUtils.isNotEmpty(keyword)){
			sql += " and p.res_name like '%"+keyword+"%'";
	    }
		if(StringHelper.isNotEmpty(servId)){
			sql += " and p.serv_id='"+servId+"'";
		}
	    sql += " group by p.res_id,p.res_name,p.serv_id,p.res_desc,p.status,p.create_time,p.res_type,p.currency,p.optr_id,p.area_id,p.county_id,p.band_width order by p.create_time desc";
		return createQuery(ResDto.class, sql).setStart(start).setLimit(limit).page();
	}
	
	
	public void updateResStatus(String resId, String status) throws JDBCException {
		String sql = "update p_res set status=? where res_id=?";
		this.executeUpdate(sql, status, resId);
	}
	public List<PRes> queryResById(String groupId) throws JDBCException {
		String sql = "select pr.res_name from p_resgroup_res prr,p_res pr where prr.group_id= ? and prr.res_id=pr.res_id";
		return this.createQuery(sql,groupId).list();
	}
	/**
	 * 查询用户产品的的动态资源信息
	 * @param prodSn
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryByUserProdSn(String prodSn,String countyId) throws JDBCException {
		String sql = "select p.* from c_prod_rsc pr,p_res p where pr.res_id=p.res_id and pr.prod_sn=? and pr.county_id=?";
		return this.createQuery(sql, prodSn,countyId).list();
	}
	
	
	
	/**
	 * 查询数字用户产品的的的动态资源和控制字信息
	 * @param prodSn
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryControlIdByUserProdSn(String prodSn,String countyId) throws JDBCException {
		String sql = "select distinct p.*,tr.external_res_id "+
		"from c_prod_rsc pr, p_res p ,c_prod c,c_user u,r_card r,r_card_model m,t_server t,t_server_res tr,t_server_county tc "+
		"where pr.res_id = p.res_id "+
		"and c.user_id = u.user_id   and p.res_id = tr.boss_res_id "+
		"and u.card_id = r.card_id and r.device_model = m.device_model and t.supplier_id = m.ca_type "+
		"and t.server_id = tr.server_id  and tc.server_id = t.server_id "+
		"and tc.county_id = ? "+
		"and c.prod_sn = ? "+
		"and pr.prod_sn = ? "+
		"and pr.county_id = ? ";
		return this.createQuery(sql, countyId,prodSn,prodSn,countyId).list();
	}
	
	
	/**
	 * 查询数字VOD,宽带产品的的的动态资源和控制字信息
	 * @param prodSn
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryITVBANDControlIdByUserProdSn(String prodSn,String countyId) throws JDBCException {
		String sql = "select distinct p.*,tr.external_res_id "+
		  "from c_prod_rsc pr, p_res p,t_server_res tr,t_server_county tc "+
		  "where pr.res_id = p.res_id "+
		  "and p.res_id = tr.boss_res_id "+
		  "and tr.server_id = tc.server_id "+
		  "and tc.county_id = ? "+
		  "and pr.prod_sn = ? "+
		  "and pr.county_id = ?";
		return this.createQuery(sql, countyId,prodSn,countyId).list();
	}

	/**
	 * 查询用户产品的的静态资源信息
	 * @param prodId
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryByProdId(String prodId,String countyId) throws JDBCException{
		String sql = "select pr.* from " +
				"(select res_id from p_prod_static_res  where  prod_id=? " +
				" union all select res_id from p_prod_county_res where prod_id=? and county_id=?) p,p_res pr " +
				" where p.res_id=pr.res_id";
		return this.createQuery(PRes.class, sql, prodId, prodId, countyId).list();
	}
	
	
	/**
	 * 查询数字用户产品的静态资源和控制字信息
	 * @param prodId
	 * @param prodSn
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryDTVControlIdByProdSn(String prodId,String prodSn,String countyId) throws JDBCException{
		String sql = "select distinct pr.* from " + 
			"(select res_id from p_prod_static_res where prod_id = ? "+
			"union all select res_id from p_prod_county_res where prod_id = ? and county_id = ?) ps, " +
			"(select p.*,tr.external_res_id from " + 
			"c_prod c,c_user u,r_card r,r_card_model m,t_server t,t_server_res tr,t_server_county tc ,p_res p " +
			"where c.user_id = u.user_id   and p.res_id = tr.boss_res_id " +
			"and u.card_id = r.card_id and r.device_model = m.device_model and t.supplier_id = m.ca_type " +
			"and t.server_id = tr.server_id  and tc.server_id = t.server_id and tc.county_id = ? " + 
			"and c.prod_id = ? and c.prod_sn = ?) pr where ps.res_id = pr.res_id";
		return this.createQuery(PRes.class, sql, prodId, prodId, countyId,countyId,prodId,prodSn).list();
	}
	
	
	/**
	 * 查询VOD,宽带用户产品的的静态资源和控制字信息
	 * @param prodId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryITVBANDControlIdByProdId(String prodId,String countyId) throws JDBCException{
		String sql = "select distinct pr.* from "+   
				"(select res_id from p_prod_static_res  where  prod_id=? "+   
				"union all select res_id from p_prod_county_res where prod_id=? and county_id=?) p,"+
				"(select r.*,tr.external_res_id from p_res r,t_server_res tr,t_server_county tc where r.res_id = tr.boss_res_id "+ 
				"and tr.server_id = tc.server_id and tc.county_id = ?)pr  where p.res_id=pr.res_id" ;
		return this.createQuery(PRes.class, sql, prodId, prodId, countyId,countyId).list();
	}
	
	/**
	 * 查询资源对应的控制字 
	 * @param servId
	 * @return
	 * @throws JDBCException
	 */
	public List<ResDto> queryStaticByServId(String servId,String countyId) throws JDBCException {
		String sql = "select distinct pr.res_id,pr.currency,pr.res_name from t_server_res t, t_server_county c, p_res pr " +
				" where c.server_id = t.server_id and t.boss_res_id = pr.res_id and pr.status='"+StatusConstants.ACTIVE+"'" ;
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql = StringHelper.append(sql," and c.county_id='" + countyId + "'");
		}
		if(StringHelper.isNotEmpty(servId)){
			String[] att = servId.split(",");
			sql = StringHelper.append(sql," and pr.serv_id in ("+sqlGenerator.in(att)+")");
		}
		sql = StringHelper.append(sql," order by currency desc");
		return this.createQuery(ResDto.class, sql).list();
	}

	

	public List<PRes> queryResByprodId(String prodId) throws JDBCException {
		String sql = "select * from   p_prod_static_res t1,p_res t2 where t1.res_id = t2.res_id and t1.prod_id = ? ";
		return this.createQuery(PRes.class, sql,prodId).list();
	}

	/**
	 * 根据服务类型查询资源
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryResByServId(String servId,String countyId) throws JDBCException {
		String sql = null;
		if(StringHelper.isEmpty(countyId)){
			sql = "select * from p_res p where p.serv_id= ? and p.status='"+StatusConstants.ACTIVE+"'";
		}else if(SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = "select * from p_res p where p.serv_id= ? and p.currency='"+SystemConstants.BOOLEAN_TRUE+"' and p.status='"+StatusConstants.ACTIVE+"'";
		}else{
			sql = StringHelper.append("select * from p_res p where p.status='",StatusConstants.ACTIVE, "' and p.serv_id=? and p.res_id in",
					"(select r.boss_res_id from t_server_res r where r.server_id in",
					"(select t.server_id from t_server_county t where t.county_id='"+countyId+"'))");
		}
		return createQuery(PRes.class, sql,servId).list();
	}
	
	/**
	 * 按是否通用排序的所有资源
	 * @return
	 * @throws JDBCException
	 */
	public List<ResDto> getServerRes() throws JDBCException {
		String sql = "select * from p_res order by currency desc ";
		return createQuery(ResDto.class,sql).list();
	}
	
	public List<ResDto> queryResByResIds(String[]  ResIds) throws JDBCException {
		String codes = getSqlGenerator().in(ResIds);
		String sql = StringHelper.append("select * from p_res where res_id in ("+codes+")");
		return createQuery(ResDto.class, sql).list();
	}
	
	public PRes getResByResName(String resName) throws Exception {
		String sql = "select * from p_res t where t.res_name like '%"+resName+"%'";
		return this.createQuery(sql).first();
	}

}

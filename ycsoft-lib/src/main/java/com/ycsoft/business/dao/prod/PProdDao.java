/**
 * PProdDao.java	2010/06/07
 */

package com.ycsoft.business.dao.prod;

import static com.ycsoft.commons.constants.SystemConstants.PROD_TYPE_BASE;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdCounty;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ResGroupDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.ProdDto;

/**
 * PProdDao -> P_PROD table's operator
 */
@Component
public class PProdDao extends BaseEntityDao<PProd> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5742534538074858229L;

	/**
	 * default empty constructor
	 */
	public PProdDao() {
	}

	/**
	 * 根据产品id获取产品基本信息
	 *
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public ProdDto queryProdById(String prodId) throws Exception {
		String sql = StringHelper.append("select p.*, case when p.exp_date < sysdate then 'T' ELSE 'F' end is_exp   from p_prod p where prod_id=?");
		return createQuery(ProdDto.class,sql, prodId).first();
	}

	public boolean validName(String prodName,String[] prodCountyIds,String prodId) throws Exception {
		String sql = "";
		if(StringHelper.isNotEmpty(prodId)){
			sql = StringHelper.append("select count(1) from p_prod p,p_prod_county pc  where p.prod_id=pc.prod_id and p.prod_name=?",
					"  and p.prod_id <> ?  and pc.county_id in (",sqlGenerator.in(prodCountyIds),")");
			return  count(sql, prodName,prodId)>0?true:false ;
		}else{
			sql = StringHelper.append("select count(1) from p_prod p,p_prod_county pc  where p.prod_id=pc.prod_id and p.prod_name=?",
					"  and pc.county_id in (",sqlGenerator.in(prodCountyIds),")");
			return  count(sql, prodName)>0?true:false ;
		}
	}
	/**
	 * 根据产品id，获取产品的静态资源
	 *
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryProdStaticRes(String countyId,String prodId) throws Exception {
		String sql = "select pr.* from p_res pr,p_prod_static_res ppst "
				+ " where pr.res_id= ppst.res_id and ppst.prod_id=? " +
						" UNION ALL select pr.* from p_res pr,p_prod_county_res ppcr" +
						" where pr.res_id= ppcr.res_id and ppcr.prod_id=? and ppcr.county_id=? ";
		List<PRes> resList = this.createQuery(PRes.class, sql, prodId,prodId,countyId).list();
		return resList;
	}

	/**
	 * 获取产品对应的动态资源组信息
	 *
	 * @param prodId
	 * @return
	 */
	public List<ResGroupDto> queryResGroup(String prodId) throws Exception {
		String sql = "select prg.*,ppdr.res_number from p_resgroup prg,p_prod_dyn_res ppdr "
				+ " where prg.group_id= ppdr.group_id and ppdr.prod_id=?";
		List<ResGroupDto> groupList = this.createQuery(ResGroupDto.class, sql,
				prodId).list();
		return groupList;
	}

	/**
	 * 获取资源组对应的资源信息
	 *
	 * @param resGroupId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryResByGroupId(String resGroupId) throws Exception {
		String sql = "select pr.* from p_res pr,p_resgroup_res prgr "
				+ " where pr.res_id= prgr.res_id and prgr.group_id=?";
		List<PRes> resList = this.createQuery(PRes.class, sql, resGroupId)
				.list();
		return resList;
	}
	
	/**
	 * 获取资源组对应的资源信息
	 *
	 * @param resGroupIds
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryResByGroupIds(String[] resGroupIds) throws Exception {
		String sql = "select pr.* from p_res pr,p_resgroup_res prgr "
				+ " where pr.res_id= prgr.res_id and prgr.group_id in("
				+ getSqlGenerator().in(resGroupIds) + ")";
		List<PRes> resList = this.createQuery(PRes.class, sql).list();
		return resList;
	}

	/**
	 * 根据用户类型和所属地区获取可以订购的产品 可以订购的产品状态必须是空闲
	 *
	 * @param servId
	 *            服务类型
	 * @param areaId
	 *            操作员所属地区
	 * @param countyId 
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryCanOrderUserProd(String servId,
			String areaId,String countyId,String dataRight) throws Exception {
		List<PProd> prodList = null;
		
		if(!SystemConstants.DEFAULT_DATA_RIGHT.equals(dataRight)){
			dataRight = StringHelper.append("t2.",dataRight.trim());
		}
		
		String sql = StringHelper.append("select distinct t.* from p_prod t,p_prod_county t2,P_PROD_TARIFF t3,P_PROD_TARIFF_COUNTY t4 ",
				" where t.eff_date < SYSDATE AND (t.exp_date IS NULL OR t.exp_date > SYSDATE) and t.prod_id=t2.prod_id and t.prod_id=t3.prod_id and t3.tariff_id=t4.tariff_id",
				" and t.status = ? and prod_type =? and t2.county_id=? and t4.county_id=? and serv_id =? and ",dataRight," order by t.is_base desc ,t.prod_name desc ");
		
		
		prodList = this.createQuery(sql,StatusConstants.ACTIVE, PROD_TYPE_BASE,
				countyId ,countyId,servId).list();
		return prodList;
	}
	
	public List<PProd> queryFeeOrderUserProd(String servId) throws Exception {
		String sql = StringHelper.append("select distinct t.prod_id, t.prod_name||'_'||ppt.tariff_name prod_name, ppt.tariff_id from p_prod t,P_PROD_TARIFF ppt",
				" where t.eff_date < SYSDATE AND (t.exp_date IS NULL OR t.exp_date > SYSDATE) and t.prod_id=ppt.prod_id ",
				" and t.status = ? and t.prod_type =? and t.serv_id =?",
				" and ppt.eff_date < SYSDATE AND (ppt.exp_date IS NULL OR ppt.exp_date > SYSDATE)"
				+ " and ppt.status=? and ppt.rent=0 order by t.prod_name desc");
		return this.createQuery(sql, StatusConstants.ACTIVE, PROD_TYPE_BASE, servId, StatusConstants.ACTIVE ).list();
		
	}

	/**
	 * @param areaId
	 * @return
	 */
	public List<PProd> queryCanOrderPkg(String countyId,String dataRight) throws Exception {
		if(!SystemConstants.DEFAULT_DATA_RIGHT.equals(dataRight)){
			dataRight = StringHelper.append("pc.",dataRight.trim());
		}
		String sql = StringHelper.append("select p.* from p_prod p,p_prod_county pc where p.prod_id=pc.prod_id",
				" and p.eff_date<SYSDATE AND (p.exp_date IS NULL OR p.exp_date>SYSDATE) and ",dataRight,
				" and status=? and (prod_type=? or prod_type=?) and pc.county_id=?");
		List<PProd> prodList = this.createQuery(sql,
				StatusConstants.ACTIVE, SystemConstants.PROD_TYPE_CUSTPKG,SystemConstants.PROD_TYPE_SPKG,countyId).list();
		return prodList;
	}

	/**
	 * 查询子产品信息
	 *
	 * @param prodId
	 * @return
	 */
	public List<PProd> querySubProds(String prodId) throws JDBCException {
		String sql = "SELECT  distinct * FROM p_prod where eff_date<SYSDATE AND (exp_date IS NULL OR exp_date>SYSDATE) and"
				+ " prod_id in (select distinct prod_id from p_package_prod where package_id =? and type=?)";
		return createQuery(sql, prodId, SystemConstants.PACKAGE_MARKET_TYPE).list();
	}

	/**
	 * 根据账目id查询产品信息
	 *
	 * @param acctItemId
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryProdByAcctItemId(String acctItemId)
			throws Exception {
		String sql = "select p.* from t_acctitem_to_prod ap,p_prod p,t_public_acctitem a"
				+ " where ap.acctitem_id = a.acctitem_id and ap.prod_id=p.prod_id and a.acctitem_id=?";
		return this.createQuery(sql, acctItemId).list();
	}

	/**
	 * 查询指定地区和全省公用的产品
	 * @param areaId
	 * @return
	 * @throws Exception
	 */
	public List<PProd> getProdByAreaId(String countyDataRight,String showAll,String startEffDate,
			String endEffDate, String startExpDate, String endExpDate) throws Exception {
		String sql = " select distinct  p.prod_name||'_'||p.prod_id prod_name, p.prod_id, p.printitem_id, p.prod_desc, p.serv_id, p.prod_type, p.is_base, p.is_bind_base, p.status, p.for_area_id, p.area_id,p.county_id, p.optr_id, p.create_time, p.just_for_once, p.invalid_date, p.refund, p.trans, p.eff_date, p.exp_date, " +
				" p.str1, p.str2, p.str3, p.str4,p.str5, p.str6, p.str7, p.str8, p.str9, p.str10," +
				" case when (p.eff_date<sysdate and (p.exp_date is null or p.exp_date > sysdate)) then 'F' else 'T' end is_invalid" +
				" from p_prod p,p_prod_county pc where p.prod_id=pc.prod_id";
		
		if(!SystemConstants.DEFAULT_DATA_RIGHT.equals(countyDataRight)){
			sql = StringHelper.append(sql," and pc.",countyDataRight.trim());
		}
		
		if(StringHelper.isNotEmpty(startEffDate)){
			sql += " and to_char(p.eff_date,'yyyy-MM-dd') >= '"+startEffDate+"'";
		}
		if(StringHelper.isNotEmpty(endEffDate)){
			sql += " and to_char(p.eff_date,'yyyy-MM-dd') <= '"+endEffDate+"'";
		}
		if(StringHelper.isNotEmpty(startExpDate)){
			sql += " and to_char(p.exp_date,'yyyy-MM-dd') >= '"+startExpDate+"'";
		}
		if(StringHelper.isNotEmpty(endExpDate)){
			sql += " and to_char(p.exp_date,'yyyy-MM-dd') <= '"+endExpDate+"'";
		}
		
		//过滤失效
		if(SystemConstants.BOOLEAN_FALSE.equals(showAll)){
			sql = StringHelper.append(sql," and (p.eff_date<sysdate and (p.exp_date is null or p.exp_date > sysdate)) ORDER BY PROD_NAME ");
		}else{
			sql = StringHelper.append(sql," ORDER BY PROD_NAME ");
		}
		return createQuery(sql).list();
	}

	public List<PProd> getBaseProd(String countyId) throws Exception {
//		String sql = "  select p.prod_id from busi.p_prod p where p.serv_id = ? and p.is_base = ? and p.status = ? and (p.for_area_id = ? or p.for_area_id = ? ) ";
//		return createQuery(sql,SystemConstants.PROD_SERV_ID_DTV,SystemConstants.BOOLEAN_TRUE,StatusConstants.ACTIVE,SystemConstants.AREA_ALL,areaId).list();
		String sql = StringHelper.append("select p.prod_id from p_prod p,p_prod_county pc where p.prod_id=pc.prod_id",
				" and p.serv_id = ? and p.is_base = ? and p.status = ? and pc.county_id= ?");
		return createQuery(sql,SystemConstants.PROD_SERV_ID_DTV,SystemConstants.BOOLEAN_TRUE,StatusConstants.ACTIVE,countyId).list();
	}
	
//	public ProdDto queryProdByIdArea(String pordId, String areaId)
//			throws Exception {
//		String sql = " select * from p_prod  where prod_id = ? and for_area_id = ?  ";
//		return createQuery(ProdDto.class, sql, pordId, areaId).first();
//	}

	public List<ProdDto> queryProdByServIdArea(String servId, String dataRight,String prodType)
			throws Exception {
		String sql = StringHelper.append(" select distinct t.prod_id,t.prod_name from p_prod t,p_prod_county t2 where t.prod_id=t2.prod_id ",
				" and (t.exp_date is null or t.exp_date > sysdate) and t.status=? and t.prod_type = ? and t2.",dataRight.trim());
		if(prodType.equals(SystemConstants.PROD_TYPE_CUSTPKG)){
			sql += " and serv_id in ('"+SystemConstants.PROD_SERV_ID_ITV+"','"+SystemConstants.PROD_SERV_ID_DTV+"','"+SystemConstants.PROD_SERV_ID_BAND+"')";
		}else{
			if(servId.equals(SystemConstants.PROD_SERV_ID_ITV)){
				sql += " and serv_id in ('"+SystemConstants.PROD_SERV_ID_ITV+"','"+SystemConstants.PROD_SERV_ID_DTV+"')";
			}else{
				sql += " and serv_id = '"+servId+"' ";
			}
		}
		sql += " order by t.prod_id";
		return createQuery(ProdDto.class,sql,StatusConstants.ACTIVE,SystemConstants.PROD_TYPE_BASE).list();
	}
	
	public List<PProdCounty> queryProdCountyByServIdArea(String servId, String dataRight,String prodType)
		throws Exception {
		String sql = StringHelper.append(" select distinct t.prod_id,t2.county_id from p_prod t,p_prod_county t2 where t.prod_id=t2.prod_id ",
				" and (t.exp_date is null or t.exp_date > sysdate) and t.status=? and t.prod_type = ? and t2.",dataRight.trim());
		if(prodType.equals(SystemConstants.PROD_TYPE_CUSTPKG)){
			sql += " and serv_id in ('"+SystemConstants.PROD_SERV_ID_ITV+"','"+SystemConstants.PROD_SERV_ID_DTV+"','"+SystemConstants.PROD_SERV_ID_BAND+"')";
		}else{
			if(servId.equals(SystemConstants.PROD_SERV_ID_ITV)){
				sql += " and serv_id in ('"+SystemConstants.PROD_SERV_ID_ITV+"','"+SystemConstants.PROD_SERV_ID_DTV+"')";
			}else{
				sql += " and serv_id = '"+servId+"' ";
			}
		}
		sql += " order by t.prod_id";
		return createQuery(PProdCounty.class,sql,StatusConstants.ACTIVE,SystemConstants.PROD_TYPE_BASE).list();
	}
	
	public void deleteProdByProdId(String prodId) throws Exception {
		String sql ="update p_prod set status=? where prod_id=?";
		executeUpdate(sql, StatusConstants.INVALID,prodId);
	}

	public PProd queryProdByProdSn(String prodSn) throws JDBCException {
		String sql = "select * from p_prod p,c_prod c where p.prod_id=c.prod_id and prod_sn=?";
		return createQuery(sql, prodSn).first();
	}
	/**
	 * 根据数组产品编号，查询多个产品的基本信息
	 * @param prodIds
	 * @return
	 * @throws JDBCException
	 */
	public List<PProd> findByProdIds(String[] prodIds) throws JDBCException {
		String sql = "select * from p_prod where "+getSqlGenerator().setWhereInArray("prod_id",prodIds)+" order by prod_id";
		return createQuery(sql).list();
	}
	
	public List<PProdDto> queryProdByCountyId(String countyId,
			String prodStatus, String tariffStatus, String ruleId,String tariffType)
			throws JDBCException {
		String sql = "select p.*,pt.tariff_id,pt.tariff_name,pt.tariff_desc,pt.billing_cycle,"+
			" pt.billing_type,pt.rent,pt.month_rent_cal_type,pt.day_rent_cal_type,pt.use_fee_rule,"+
			" pt.bill_rule,pt.status pt_status,pt.rule_id,pt.tariff_type"+
			" from p_prod p,p_prod_tariff pt,p_prod_tariff_county ptc"+
			" where p.prod_id=pt.prod_id and pt.tariff_id=ptc.tariff_id and sysdate > trunc(p.eff_date) and (trunc(p.exp_date) < sysdate or p.exp_date is null)"+
			" and ptc.county_id=?";
		if(StringHelper.isNotEmpty(prodStatus)){
			sql += " and p.status='"+prodStatus+"'";
		}
		if(StringHelper.isNotEmpty(tariffStatus)){
			sql += " and pt.status='"+tariffStatus+"'";
		}
		if(StringHelper.isNotEmpty(ruleId)){
			sql += " and pt.rule_id='"+ruleId+"'";
		}else{
			sql += " and pt.rule_id is null ";
		}
		if(StringHelper.isNotEmpty(tariffType)){
			sql += " and p.tariff_type='"+tariffType+"'";
		}
		
		sql += " order by p.prod_name";
		
		return this.createQuery(PProdDto.class,sql, countyId).list();
	}

	public List<PProdDto> queryProdByCountyId(String prodId, String countyId) throws JDBCException {
		String sql = StringHelper.append("select distinct p.*,pt.tariff_id,pt.tariff_name,pt.tariff_desc,pt.billing_cycle,",
			" pt.billing_type,pt.rent,pt.month_rent_cal_type,pt.day_rent_cal_type,pt.use_fee_rule,",
			" pt.bill_rule,pt.status pt_status,pt.rule_id,pt.tariff_type",
			" from p_prod p,p_prod_tariff pt,p_prod_tariff_county ptc",
			" where p.prod_id=pt.prod_id and pt.tariff_id=ptc.tariff_id and p.serv_id <> 'ATV' AND P.PROD_TYPE <> 'CPKG' ",
			" and sysdate > trunc(p.eff_date) and (trunc(p.exp_date) < sysdate or p.exp_date is null)",
			" AND PT.RULE_ID IS NULL and pt.status='ACTIVE'");
		if(StringHelper.isNotEmpty(prodId)){
			sql = StringHelper.append(sql," and p.prod_id='",prodId,"'");
		}
		
		if(StringHelper.isNotEmpty(countyId)){
			sql = StringHelper.append(sql," and ptc.county_id='",countyId,"'");
		}
		
		sql = StringHelper.append(sql," order by p.prod_name");
		
		return this.createQuery(PProdDto.class,sql).list();
	}
	
	public Map<String,Integer> queryUserCountGroupByType(String pkgId) throws Exception {
		Map<String,Integer> userCountMap = new HashMap<>();
		final String sql = "select user_type,sum(MAX_USER_CNT) count from p_package_prod "
				+ "where PACKAGE_ID=? "
				+ "group by user_type";
		List<Object[]> userTypeList= this.createSQLQuery(sql, pkgId).list();
		if (CollectionHelper.isNotEmpty(userTypeList)){
			for (Object[] obj:userTypeList){
				userCountMap.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
			}
		}
		return userCountMap;
	}
	
	/**
	 * 查找所有包含宽带的产品或者套餐的实际带宽
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> queryProdBandWidth() throws Exception{
		Map<String,Integer> prodBandWidthMap = new HashMap<>();
		String sql = "select a.prod_id,c.band_width from p_prod a,p_prod_static_res b,p_res c where "
				+ "	a.prod_id = b.prod_id and b.res_id=c.res_id and a.prod_type='BASE' AND A.SERV_ID='BAND' "
				+ "	union  "
				+ "	select d.package_id,c.band_width from p_prod a,p_prod_static_res b,p_res c ,p_package_prod d where "
				+ "	d.user_type='BAND' AND d.prod_list=a.prod_id and "
				+ "	a.prod_id = b.prod_id and b.res_id=c.res_id and a.prod_type='BASE' AND A.SERV_ID='BAND'";
		List<Object[]> bandWidthList= this.createSQLQuery(sql).list();
		if (CollectionHelper.isNotEmpty(bandWidthList)){
			for (Object[] obj:bandWidthList){
				BigDecimal bd = (BigDecimal)obj[1];
				prodBandWidthMap.put(obj[0].toString(), bd.intValue());
			}
		}
		return prodBandWidthMap;
	}

	public List<PProd> queryPackProdList(String[] prodIds) throws JDBCException{
		String sql = "select * from p_prod t where "+getSqlGenerator().setWhereInArray("t.prod_id",prodIds)+" and t.status = ? order by t.prod_id ";
		return this.createQuery(sql,StatusConstants.ACTIVE).list();
	}

	
}

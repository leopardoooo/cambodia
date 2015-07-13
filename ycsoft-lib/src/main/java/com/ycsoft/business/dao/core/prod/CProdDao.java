/**
 * CUserProdDao.java	2010/05/12
 */

package com.ycsoft.business.dao.core.prod;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdInvalidTariff;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.business.dto.core.prod.CProdBacthDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.CustProdDto;
import com.ycsoft.business.dto.core.prod.UserProdDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.core.Query;


/**
 * CProdDao -> C_PROD table's operator
 */
@Component
public class CProdDao extends BaseEntityDao<CProd> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3466872043417464144L;

	/**
	 * default empty constructor
	 */
	public CProdDao() {}
	
	/**
	 * 查询一个客户的需要计费模式判断的产品（不含子产品）
	 * 只提取资费包月且按月计费的产品
	 * @param custId
	 * @param countyId
	 * @return
	 */
	public List<CProdDto> queryAllAcctModeProd(String custId,String countyId)throws JDBCException{
		String sql=StringHelper.append("select cp.*,pp.serv_id,pp.is_base,ppt.rent tariff_rent,ppt.billing_cycle,ppt.billing_type",
				",(caa.active_balance+caa.inactive_balance) all_balance,caa.owe_fee,caa.real_bill ",
				" from c_prod cp,p_prod_tariff ppt,p_prod pp,c_acct_acctitem caa ",
				" where  ppt.tariff_id=cp.tariff_id and cp.prod_id=pp.prod_id and cp.package_sn is null ",
				" and cp.acct_id=caa.acct_id and cp.prod_id=caa.acctitem_id and cp.county_id=caa.county_id ",
				" and cp.cust_id=? and cp.county_id=? ",
				" and ppt.rent>0 and ppt.billing_cycle=1 and ppt.billing_type=? " );
		return this.createQuery(CProdDto.class, sql, custId,countyId,SystemConstants.BILLING_TYPE_MONTH).list();
	}
	/**
	 * 查找一个客户名下所有存在账目的有效产品
	 * @param cust_id
	 * @return 
	 */
	public List<CProdDto> queryAllProdAcct(String cust_id,String county_id) throws JDBCException{
		String sql=StringHelper.append("select cp.*,ppt.rent tariff_rent,ppt.billing_cycle,pp.serv_id from c_prod cp,c_acct_acctitem caa,p_prod_tariff ppt,p_prod pp",
			" where cp.acct_id=caa.acct_id and cp.prod_id=caa.acctitem_id and cp.county_id=caa.county_id",
			" and cp.cust_id=? and cp.county_id=? and caa.county_id=? and ppt.tariff_id=cp.tariff_id and cp.prod_id=pp.prod_id");
		
		return this.createQuery(CProdDto.class, sql, cust_id,county_id,county_id).list();
	}
	//修改产品资费
	public void updateProdTariff(Integer doneCode, String prodSn,String tariffId,String stopByInvalidDate, String publicType) throws JDBCException{

		String sql="insert into c_prod_invalid_tariff "+
				" (prod_sn, tariff_id,new_tariff_id, eff_date, exp_date, county_id, area_id)" +
				" select prod_sn, tariff_id,?, null, sysdate, county_id, area_id from c_prod " +
				" where prod_sn=? ";
		executeUpdate(sql,tariffId ,prodSn);

		CProd prod = this.findByKey(prodSn);
		sql ="update c_prod set tariff_id=?,stop_by_invalid_date=?,next_tariff_id=?,public_acctitem_type=? " +
				" where prod_sn=?";
		executeUpdate(sql, tariffId,stopByInvalidDate,null,publicType,prodSn);
		
		if(!prod.getStop_by_invalid_date().equals(stopByInvalidDate)){
			sql = "insert into c_prod_prop_change(prod_sn,column_name,old_value,new_value,done_code,change_time,county_id,area_id)" +
				"values(?,?,?,?,?,sysdate,?,?)";
			this.executeUpdate(sql, 
					prodSn, "stop_by_invalid_date", prod.getStop_by_invalid_date(), stopByInvalidDate, doneCode, prod.getCounty_id(),prod.getArea_id());
		}
		if(!prod.getPublic_acctitem_type().equals(publicType)){
			sql = "insert into c_prod_prop_change(prod_sn,column_name,old_value,new_value,done_code,change_time,county_id,area_id)" +
				"values(?,?,?,?,?,sysdate,?,?)";
			this.executeUpdate(sql, 
					prodSn, "public_acctitem_type", prod.getPublic_acctitem_type(), publicType, doneCode, prod.getCounty_id(),prod.getArea_id());
		}
	}
	
	public void updateNextBillDate(String prodSn) throws Exception {
		String sql = "update c_prod set next_bill_date=sysdate where prod_sn=?";
		this.executeUpdate(sql, prodSn);
	}
	
	public void updatePreOpenTime(String prodSn) throws Exception {
		String sql = "update c_prod set PRE_OPEN_TIME=null,status=?,status_date=sysdate where prod_sn=?";
		this.executeUpdate(sql, StatusConstants.ACTIVE, prodSn);
	}
	
	/**
	 * 修改产品未生效资费
	 * @param prodSn
	 * @param nextTariffId
	 * @param countyId
	 * @throws Exception
	 */
	public void updateNextTariff(String prodSn,String nextTariffId,String countyId) throws Exception{
		String sql ="update c_prod set next_tariff_id = ? " +
				" where prod_sn=? " +
				" and county_id=?";
		executeUpdate(sql, nextTariffId,prodSn,countyId);

	}
	/**
	 * 修改失效日期
	 * @param prodSn
	 * @param expDate
	 * @param countyId
	 * @throws Exception
	 */
	public void updateExpDate(String prodSn,String expDate,String countyId) throws Exception{
		String sql ="update c_prod set exp_date = to_date(?,'yyyy-mm-dd') " +
				" where prod_sn=? " +
				" and county_id=?";
		executeUpdate(sql, expDate,prodSn,countyId);

	}
	
	/**
	 * 根据用户编号修改产品的下一出帐日期
	 * @param userId 	用户id
	 * @param stopDate 下一出帐日期
	 * @param countyId
	 * @throws Exception
	 */
	public void updateNextBillDate(String userId,String stopDate,String countyId) throws Exception {
		String sql = "update c_prod set next_bill_date = next_bill_date+ (sysdate - to_date(?,'yyyy-mm-dd'))" +
				" where user_id =? " +
				" and county_id =? " +
				" and next_bill_date is not null";
		executeUpdate(sql,stopDate,userId,countyId);
	}

	/**
	 * 根据用户ID 获取用户产品
	 * @param userId
	 */
	public List<CProdDto> queryUserProd(String userId, String countyId) throws Exception {
		StringBuilder sqlBul = new StringBuilder(
				"select cp.*,p.prod_name,p.prod_desc,pt.tariff_name,p.serv_id,p.is_base");
		sqlBul.append(" from c_prod cp,p_prod p,p_prod_tariff pt");
		sqlBul.append(" where cp.prod_id=p.prod_id and cp.tariff_id=pt.tariff_id");
		sqlBul.append(" and cp.user_id=? and cp.county_id=?  and ( cp.package_sn is null or " +
				" cp.package_sn not in (select prod_sn from c_prod c where c.user_id= ? and c.prod_type=?))");
		return createQuery(CProdDto.class,sqlBul.toString(), userId,countyId,userId,SystemConstants.PROD_TYPE_USERPKG).list();

	}
	
	/**
	 * 根据用户ID 获取用户产品,包括套餐子产品
	 * @param userId
	 */
	public List<CProdDto> queryUserAllProds(String userId, String countyId) throws Exception {
		StringBuilder sqlBul = new StringBuilder(
				"select cp.*,p.prod_name,p.prod_desc,pt.tariff_name,p.serv_id,p.is_base,pt.rent tariff_rent");
		sqlBul.append(" from c_prod cp,p_prod p,p_prod_tariff pt");
		sqlBul.append(" where cp.prod_id=p.prod_id and cp.tariff_id=pt.tariff_id");
		sqlBul.append(" and cp.user_id=? and cp.county_id=?");
		return createQuery(CProdDto.class,sqlBul.toString(), userId,countyId).list();
	}

	/**
	 * 查找数字电视用户基本包产品
	 * @param userId
	 * @param county_id
	 * @return
	 */
	public List<CProdDto> queryUserDtvBaseProd(String userId, String countyId) throws Exception {
		StringBuilder sqlBul = new StringBuilder("select cp.*,p.prod_name,p.prod_desc,pt.billing_cycle ");
		sqlBul.append(" from c_prod cp,p_prod p ,p_prod_tariff pt");
		sqlBul.append(" where cp.prod_id=p.prod_id ");
		sqlBul.append(" and cp.package_sn is null and cp.user_id=? and cp.county_id=?");
		sqlBul.append(" and (p.serv_id=? or p.serv_id = ?) and p.is_base=?  and pt.tariff_id=cp.tariff_id ");

		return createQuery(CProdDto.class,sqlBul.toString(), userId,countyId,SystemConstants.PROD_SERV_ID_DTV,SystemConstants.PROD_SERV_ID_BAND,SystemConstants.BOOLEAN_TRUE).list();
	}


	/**
	 * 根据客户id获取客户订购的产品
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryProdByCustId(String custId, String countyId) throws Exception {
		String sqlBul = StringHelper.append(
			" select cp.*,p.is_bank_pay p_bank_pay,pt1.month_rent_cal_type,pt1.billing_cycle,p.is_base,p.prod_name,p.serv_id,p1.prod_name package_name,p.prod_desc,p.just_for_once,p.refund ,p.trans  ,",
			" caa.owe_fee,caa.real_balance,caa.active_balance,caa.inactive_balance,caa.real_fee,",
			" pt1.tariff_name tariff_name,pt1.rent tariff_rent,pt2.tariff_name next_tariff_name,pt1.billing_type billing_type,",
			" case when (p.eff_date>sysdate and (p.exp_date is null or p.exp_date>sysdate)) or (p.just_for_once='T' and to_char(cp.order_date,'yyyymmdd')<to_char(sysdate,'yyyymmdd')) " ,
			" or cp.package_sn is not null  then 'F' else 'T' end  allow_pay ,",
			" case when pt1.billing_type = 'BY' and pt1.rent = 0 and pt1.status='"+StatusConstants.ACTIVE+"' then  'T' ELSE 'F' end is_zero_tariff ,",
			" case when (pt1.status = 'INVALID' or not exists (select 1 from p_prod_tariff_county ptc " ,
			" where ptc.tariff_id=pt1.tariff_id and ptc.county_id=?)) then  'T' ELSE 'F' end is_invalid_tariff,",
			" case when (select count(1) from c_prod_rsc pr, p_resgroup_res prg, p_prod_dyn_res ppdr",
                " where  prg.res_id = pr.res_id and ppdr.group_id = prg.group_id",
                " and pr.prod_sn = cp.prod_sn and ppdr.prod_id=cp.prod_id and pr.county_id = ?) > 0",
			" then 'T' else 'F' end has_dyn",
			" from c_prod cp,p_prod p,p_prod p1,p_prod_tariff pt1,p_prod_tariff pt2,c_acct_acctitem caa ",
			" where cp.prod_id=p.prod_id and cp.tariff_id=pt1.tariff_id ",
			" and cp.next_tariff_id=pt2.tariff_id(+)",
			" and caa.acct_id = cp.acct_id and caa.acctitem_id = cp.prod_id ",
			" and cp.package_id = p1.prod_id(+)",
			" and (cp.package_sn is null or p1.prod_type=?) and cp.cust_id=? and cp.county_id=? order by cp.prod_type");
		return createQuery(CProdDto.class,sqlBul,countyId,countyId,SystemConstants.PROD_TYPE_CUSTPKG, custId,countyId).list();

	}
	
	/**
	 * 根据客户id获取客户订购的产品,
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryProdBalanceByCustId(String custId, String countyId) throws Exception {
		String sqlBul = StringHelper.append("SELECT cp.*,(caa.active_balance+caa.order_balance-caa.owe_fee-caa.real_fee) all_balance, pp.prod_name , ppt.tariff_name",
					" FROM busi.c_prod cp,busi.c_acct_acctitem caa , p_prod pp, p_prod_tariff ppt", 
					" WHERE cp.acct_id=caa.acct_id AND cp.prod_id=caa.acctitem_id", 
					" AND cp.prod_id= pp.prod_id AND cp.tariff_id=ppt.tariff_id",
					" AND cp.county_id=? and cp.cust_id=?");
		return createQuery(CProdDto.class,sqlBul,countyId, custId).list();

	}
	
	/**
	 * 
	 * 根据数组编号查询产品
	 * @param ids 	编号数组
	 * @param key	判断是客户编号数组"CUST"还是用户编号数组"USER"
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CProdBacthDto> queryProdByIds(String[] ids, String countyId,String key,String prodId) throws Exception {
		String str = "cp.cust_id";
		if(key.equals("USER")){
			str = "cp.user_id"; 
		}
		String sqlBul = " select cp.prod_sn,cp.prod_id,cp.cust_id,cp.user_id,cp.tariff_id,cp.order_date,cp.next_tariff_id,cp.next_bill_date,cp.invalid_date,cp.county_id,cp.area_id," +
			" cc.cust_name,cu.card_id,p.prod_name ,p.serv_id, pt1.tariff_name tariff_name,pt1.billing_cycle,pt1.billing_type," +
			" pt1.rent tariff_rent,pt2.tariff_name next_tariff_name, case when pt1.billing_type = 'BY' and pt1.rent = 0 " +
			" and pt1.status='"+StatusConstants.ACTIVE+"' then  'T' ELSE 'F' end is_zero_tariff, " +
			" case when (pt1.status = 'INVALID' or not exists (select 1 from p_prod_tariff_county ptc " +
			" where ptc.tariff_id=pt1.tariff_id and ptc.county_id=?)) then  'T' ELSE 'F' end is_invalid_tariff , " +
			" case when cua.terminal_type is null and cud.terminal_type is not null then  cud.terminal_type" +
			" else cua.terminal_type end terminal_type , cc.cust_class,cc.cust_colony " +
			" from c_prod cp,p_prod p,p_prod p1,p_prod_tariff pt1,p_prod_tariff pt2,c_cust cc,c_user cu , c_user_atv cua, c_user_dtv cud" +
			" where cp.prod_id=p.prod_id and cp.tariff_id=pt1.tariff_id and cp.cust_id = cc.cust_id " +
			" and cp.user_id = cu.user_id  AND cu.user_id = cua.user_id(+) and cu.user_id = cud.user_id(+)" +
			" and cp.next_tariff_id=pt2.tariff_id(+) and cp.package_id = p1.prod_id(+)"+
			" and (cp.package_sn is null or p1.prod_type=?)  and cp.county_id=? and cp.prod_id = ?" +
			" and ( "+ getSqlGenerator().setWhereInArray(str, ids) + ") ";
		return createQuery(CProdBacthDto.class,sqlBul,countyId,SystemConstants.PROD_TYPE_CUSTPKG, countyId,prodId).list();
	}
	
	public List<CProd> queryBaseProdByIds(String[] ids,String key) throws Exception {
		List<CProd> prodList = new ArrayList<CProd>();
		String sql = "";
		String str = "cp.cust_id";
		if(key.equals("USER")){
			str = "cp.user_id"; 
		}
		if (ids.length > 0) {
			sql = StringHelper.append(
				" select distinct p.prod_id,p.prod_name from c_prod cp, p_prod p, p_prod p1 " +
				"where cp.prod_id = p.prod_id  and cp.package_id = p1.prod_id(+) and (cp.package_sn is null or p1.prod_type = 'CPKG') and  ("
					+ getSqlGenerator().setWhereInArray(str, ids) + ") ");
		}
		prodList.addAll(this.createQuery(CProd.class, sql).list());
		return prodList;
	}
	
	/**
	 * 根据客户id获取客户订购的产品历史
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryProdHisByCustId(String custId, String countyId) throws Exception {
		StringBuilder sqlBul = new StringBuilder(
			"select cp.*,p.is_base,p.prod_name,p.prod_desc,p.just_for_once,p.refund allow_refund,p.trans allow_trans ," +
			"pt1.tariff_name tariff_name,pt2.tariff_name next_tariff_name");
		sqlBul.append(" from c_prod_his cp,p_prod p,p_prod p1,p_prod_tariff pt1,p_prod_tariff pt2");
		sqlBul.append(" where cp.prod_id=p.prod_id and cp.tariff_id=pt1.tariff_id ");
		sqlBul.append(" and cp.next_tariff_id=pt2.tariff_id(+)");
		sqlBul.append(" and cp.package_id = p1.prod_id(+)");
		sqlBul.append(" and (cp.package_sn is null or p1.prod_type=?) and cp.cust_id=? and cp.county_id=?");
		return createQuery(CProdDto.class,sqlBul.toString(),SystemConstants.PROD_TYPE_CUSTPKG, custId,countyId).list();

	}

	public List<CProdDto> queryChildProdByPkgsn(String pkgSn, String countyId) throws Exception {
		StringBuilder sqlBul = new StringBuilder(
				"select cp.*,p.prod_name,p.prod_desc,pt.tariff_name");
		sqlBul.append(" from c_prod cp,p_prod p,p_prod_tariff pt");
		sqlBul.append(" where cp.prod_id=p.prod_id and cp.tariff_id=pt.tariff_id(+)");
		sqlBul.append("  and cp.PACKAGE_SN=? and cp.county_id=?");
		return createQuery(CProdDto.class,sqlBul.toString(), pkgSn,countyId).list();
	}
	
	public CProdDto queryByProdSn(String prodSn) throws Exception {
		StringBuilder sqlBul = new StringBuilder(
				"select cp.*,p.is_base,pt.billing_cycle");
		sqlBul.append(" from c_prod cp,p_prod p ,p_prod_tariff pt");
		sqlBul.append(" where cp.prod_id=p.prod_id and cp.tariff_id=pt.tariff_id");
		sqlBul.append("  and cp.prod_sn=? ");
		return createQuery(CProdDto.class,sqlBul.toString(), prodSn).first();
	}
	/**
	 * 资费可以为空
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public CProdDto queryByProdSnCommon(String prodSn) throws Exception {
		StringBuilder sqlBul = new StringBuilder(
				"select cp.*,p.is_base,pt.billing_cycle");
		sqlBul.append(" from c_prod cp,p_prod p ,p_prod_tariff pt");
		sqlBul.append(" where cp.prod_id=p.prod_id and cp.tariff_id=pt.tariff_id(+)");
		sqlBul.append("  and cp.prod_sn=? ");
		return createQuery(CProdDto.class,sqlBul.toString(), prodSn).first();
	}
	
	/**
	 * 查询当前产品的变更信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public Pager<CProdInvalidTariff> queryTariffChange(String prodSn,Integer start,Integer limit) throws Exception {
		StringBuilder sqlBul = new StringBuilder("select pit.prod_sn,pit.tariff_id,pit.new_tariff_id,pit.eff_date,pit.exp_date,pit.area_id,pit.county_id");//--用户产品失效
		sqlBul.append(" ,(select pt.tariff_name from p_prod_tariff pt where pt.tariff_id = pit.tariff_id) old_tariff_name");
		sqlBul.append(" ,(select pt.tariff_name from p_prod_tariff pt where pt.tariff_id = pit.new_tariff_id) new_tariff_name");
		sqlBul.append(" from c_prod_invalid_tariff pit");
		sqlBul.append(" where pit.prod_sn=?");
		sqlBul.append(" union select jnt.prod_sn ,jnt.tariff_id new_tariff_id,jnt.old_tariff_id tariff_id,jnt.eff_date ,null exp_date,jnt.area_id ,jnt.county_id ");
		sqlBul.append(" ,(select pt.tariff_name from p_prod_tariff pt where pt.tariff_id = jnt.old_tariff_id) old_tariff_name");
		sqlBul.append(" ,(select pt.tariff_name from p_prod_tariff pt where pt.tariff_id = jnt.tariff_id) new_tariff_name");
		sqlBul.append(" from  j_prod_next_tariff jnt where jnt.prod_sn = ?");
		Pager<CProdInvalidTariff> prodChangeList = this.createQuery(CProdInvalidTariff.class, sqlBul.toString(), prodSn,prodSn).setStart(start).setLimit(limit).page();
		return prodChangeList;
	}

	/**
	 * 查询当前产品的异动信息
	 * @param prodId
	 * @param countyId
	 * @param limit 
	 * @param start 
	 * @return
	 * @throws Exception
	 */
	/**
	 * @param prodSn
	 * @param countyId
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	/**
	 * @param prodSn
	 * @param countyId
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<CProdPropChange> queryChangeByProd(String prodSn,String countyId, Integer start, Integer limit) throws Exception{
		StringBuilder sqlBul = new StringBuilder("select p.*,t.comments column_name_text,t.param_name,c.busi_code,c.optr_id");
		sqlBul.append(" from c_prod_prop_change p,t_tab_define t,c_done_code c");
		sqlBul.append(" where upper(p.column_name) = upper(t.column_name) and p.done_code=c.done_code ");
		sqlBul.append(" and t.table_name='CUSERPROD' and t.status=? and p.prod_sn=? and p.county_id=? ");
		sqlBul.append(" order by change_time desc");

		Query<CProdPropChange> query = this.createQuery(CProdPropChange.class, sqlBul.toString(),StatusConstants.ACTIVE, prodSn,countyId);
		return query.setStart(start).setLimit(limit).page();
	}
	
	public List<CProdPropChange> queryChangeByDoneCode(int doneCode,String countyId) throws Exception{
		StringBuilder sqlBul = new StringBuilder("select * from c_prod_prop_change p ");
		sqlBul.append(" where done_code=? and p.county_id=?");

		List<CProdPropChange> prodPropList = this.createQuery(CProdPropChange.class, sqlBul.toString(), doneCode,countyId).list();
		return prodPropList;
	}


	/**
	 * 根据用户id获取客户订购的产品
	 * @param userIds
	 * @param county_id
	 * @return
	 */
	public List<CProdDto> queryUserProdByUserIds(String[] userIds, String countyId) throws JDBCException {
		if (userIds == null || userIds.length == 0)
			return new ArrayList<CProdDto>();
		String sql = StringHelper
				.append(
						"select cp.*,p.prod_name,p.prod_desc,pt.tariff_name,p.is_base",
						" from c_prod cp,p_prod p,p_prod p1,p_prod_tariff pt",
						" where cp.prod_id=p.prod_id and cp.tariff_id=pt.tariff_id",
						" and cp.package_id = p1.prod_id(+)",
						" and (cp.package_sn is null or p1.prod_type=?) and cp.county_id=?",
						" and cp.user_id in("+getSqlGenerator().in(userIds)+")");

		return createQuery(CProdDto.class,sql, SystemConstants.PROD_TYPE_CUSTPKG, countyId
				).list();
	}

	/**
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public List<CProdDto> queryPkgByCustId(String custId, String countyId)throws JDBCException {
		String sql ="select cp.*,pp.prod_name,ppt.tariff_name,ppt.billing_type, " +
				" case when PPT.billing_type = 'BY' and PPT.rent = 0 then  'T' ELSE 'F' end is_zero_tariff "+
				" from c_prod cp,p_prod pp,p_prod_tariff ppt " +
				" where cp.prod_id=pp.prod_id and cp.tariff_id=ppt.tariff_id " +
				" and cp.prod_type=?  and cp.cust_id=?  and cp.county_id=?";
		return createQuery(CProdDto.class,sql, SystemConstants.PROD_TYPE_CUSTPKG,custId,countyId).list();
	}
	/**
	 * @param doneCode
	 * @param county_id
	 * @return
	 */
	public List<CProd> queryByDoneCode(Integer doneCode, String countyId) throws JDBCException{
		String sql ="select * from c_prod where done_code=? and county_id=?";
		return this.createQuery(sql, doneCode,countyId).list();
	}
	/**
	 * @param prodSn
	 * @param pkgSn
	 * @param pkgId
	 * @param county_id
	 */
	public void updateProdPkg(String prodSn, String pkgSn, String pkgId,
			String countyId) throws JDBCException{
		String sql="update c_prod set PACKAGE_SN=?,PACKAGE_ID=?,billinfo_eff_date=sysdate where prod_sn=? and county_id=?";
		executeUpdate(sql, pkgSn,pkgId,prodSn,countyId);
	}
	/**
	 * @param pkgSn
	 * @param county_id
	 * @return
	 */
	public List<CProd> queryByPkgSn(String pkgSn, String countyId)  throws JDBCException{
		String sql ="select * from c_prod where package_sn=? and county_id=?";

		return this.createQuery(sql, pkgSn,countyId).list();
	}
	/**
	 * @param custId
	 * @param pkgId
	 * @param countyId 
	 * @param county_id
	 * @return 
	 * @throws Exception 
	 */
	public List<CustProdDto> queryCustProdForPkg(String custId, String pkgId,String pkgTarrifId,
			String countyId) throws Exception{
		String sql = StringHelper.append(
				"select a.cust_id,b.user_id,a.cust_name,c.prod_sn,c.prod_id,e.prod_name,e.prod_id,b.stb_id,b.card_id,b.user_type,f.max_prod_count",
			"  from c_cust a, c_user b, c_prod c,p_prod e, p_package_prod f,C_ACCT_ACCTITEM CA",
			" where a.cust_id = b.cust_id",
			"   and b.user_id = c.user_id",
			"   and c.prod_id = f.prod_id",
			"   and c.prod_id = e.prod_id",
			"   AND C.ACCT_ID=CA.ACCT_ID AND C.PROD_ID=CA.ACCTITEM_ID AND (CA.OWE_FEE = 0 or (c.county_id= '",SystemConstants.COUNTY_0101,"' and e.is_base='",SystemConstants.BOOLEAN_TRUE,"'))",
			"   and c.package_sn is null ",
			"   and a.cust_id = ?",
			"   and f.package_id= ?",
			"   and f.PACKAGE_TARIFF_ID= ?",
			"	and f.type=?",
			"   and a.county_id= ?");

		return this.createQuery(CustProdDto.class,sql, custId,pkgId,pkgTarrifId,SystemConstants.PACKAGE_MARKET_TYPE,countyId).list();
	}
	
	/**
	 * @param custId
	 * @param pkgId
	 * @param county_id
	 */
	public List<CustProdDto> queryUnitProdForPkg(String custId, String pkgId,String pkgTarrifId,
			String countyId) throws Exception{
		StringBuilder sqlBul = new StringBuilder(
				"select a.cust_id,b.user_id,a.cust_name,c.prod_sn,c.prod_id,e.prod_name,b.stb_id,b.card_id,b.user_type");
		sqlBul.append("  from c_cust a,c_cust_unit_to_resident u, c_user b, c_prod c,p_prod e, p_package_prod f");
		sqlBul.append(" where a.cust_id = b.cust_id");
		sqlBul.append("   and a.cust_id = u.resident_cust_id");
		sqlBul.append("   and b.user_id = c.user_id");
		sqlBul.append("   and c.prod_id = f.prod_id");
		sqlBul.append("   and c.prod_id = e.prod_id");
		sqlBul.append("   and c.package_sn is null ");
		sqlBul.append("   and u.unit_CUST_ID =? ");
		sqlBul.append("   and c.status = ?");
		sqlBul.append("   and f.package_id= ?");
		sqlBul.append("   and f.type= ?");
		sqlBul.append("   and a.county_id= ?");

		return this.createQuery(CustProdDto.class,sqlBul.toString(), custId,StatusConstants.ACTIVE,pkgId, SystemConstants.PACKAGE_MARKET_TYPE,countyId).list();
	}
	/**
	 * @param custId
	 * @param pkgId
	 * @param county_id
	 * @return
	 */
	public List<CustProdDto> queryProdsOfPkg(String custId, String pkgId,
			String countyId) throws JDBCException {
		StringBuilder sqlBul = new StringBuilder(
			"select distinct a.cust_id,b.user_id,a.cust_name,c.prod_sn,c.prod_id,e.prod_name,b.stb_id,b.card_id,b.user_type, c.package_sn,c.package_id");
		sqlBul.append("  from c_cust a, c_user b, c_prod c,p_prod e, p_package_prod f");
		sqlBul.append(" where a.cust_id = b.cust_id");
		sqlBul.append("   and b.user_id = c.user_id");
		sqlBul.append("   and c.prod_id = f.prod_id");
		sqlBul.append("   and c.prod_id = e.prod_id");
		sqlBul.append("   and c.package_sn = (select prod_sn from c_prod cp where cp.cust_id=? and cp.prod_id=?)");
//		sqlBul.append("   and c.status = ?");
		sqlBul.append("   and a.county_id= ?");
		sqlBul.append("   and f.type= ?");
		sqlBul.append("   and a.cust_id  IN " +
				"(SELECT u.resident_cust_id FROM c_cust_unit_to_resident u " +
				" WHERE u.unit_cust_id=? UNION ALL SELECT ? FROM dual)");
		

		return this.createQuery(CustProdDto.class, sqlBul.toString(), custId,
				pkgId,  countyId, SystemConstants.PACKAGE_MARKET_TYPE, custId,custId).list();
	}

	public List<CProd> queryByAcctItem(String acctId, String acctItemId,
			String countyId) throws JDBCException{
		String sql ="select * from c_prod where acct_id=? and prod_id=? and county_id=?";

		return createQuery(sql,acctId,acctItemId,countyId).list();
	}
	
	public List<CProd> queryByProdId(String userId, String acctItemId) throws JDBCException{
		String sql ="select * from c_prod where user_id=? and prod_id=?";

		return createQuery(sql,userId,acctItemId).list();
	}
	
	public List<CProd> queryByUserId(String userId) throws JDBCException{
		String sql ="select * from c_prod where user_id=?";

		return createQuery(sql,userId).list();
	}


	public Date getDate(Date date,int addMonths,int addDays) throws JDBCException{
		String sql = "select add_months(to_date(?,'yyyy-mm-dd'),?) + ? from dual";
		return DateHelper.strToDate(findUnique(sql, DateHelper.dateToStr(date),addMonths,addDays));
	}
	
	/**
	 *  查询属于单位的客户产品
	 * @param custId
	 * @param unitIds
	 * @return
	 * @throws JDBCException
	 */
	public List<CProd> queryUnitProdByUnitIds(String custId, String[] unitIds)
			throws JDBCException {
		String sql = "SELECT * FROM c_prod b WHERE b.cust_id =?  AND "
				+ "b.package_sn IN (SELECT prod_sn FROM c_prod a WHERE a.cust_id in ("
				+ getSqlGenerator().in(unitIds) + "))";
		return createQuery(sql, custId).list();
	}


	public List<CProd> queryAllowRefundProd(String dataRight) throws JDBCException{
		String sql = "select * from p_prod where "+dataRight;
		return createQuery(sql).list();
	}

	/**
	 * 查询指定业务订购的基本包
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public PProd queryBaseProdByDoneCode(long doneCode)throws Exception  {
		String sql ="SELECT p.* FROM p_prod p,c_prod c WHERE p.prod_id=c.prod_id" +
				" AND p.is_base=? and done_code =? and c.package_sn is null";
		return this.createQuery(PProd.class,sql, SystemConstants.BOOLEAN_TRUE,doneCode).first();
	}

	public CProd queryProdsByUserProd(String userId, String prodId) throws JDBCException {
		String sql = "select * from c_prod c,p_prod p where c.prod_id=p.prod_id and c.user_id=? and c.prod_id=?";
		return createQuery(sql,userId,prodId).first();
	}

	
	public List<CProd> queryProdsByCustProd(String custId, String prodId) throws JDBCException {
		String sql = "select * from c_prod where cust_id=? and prod_id=?";
		return createQuery(sql,custId,prodId).list();
	}
	
	public CProd queryLaterOrderDateByProdType(String userId, String prodType) throws JDBCException {
		String sql = "select * from c_prod cp,p_prod p " +
				" where cp.user_id=? and cp.prod_id=p.prod_id" +
				" AND cp.order_type='ORDER' AND p.str1=?  ORDER BY cp.order_date DESC";
		return createQuery(sql,userId,prodType).first();
	}

	public void updateStopType(String userId, String newValue) throws JDBCException {
		executeUpdate("UPDATE c_prod t SET t.stop_type=? WHERE t.user_id=? and prod_id in (select prod_id from p_prod where is_base=?)"
				, newValue ,userId,SystemConstants.BOOLEAN_TRUE);
	}

	public List<CProd> queryUpkgProd() throws JDBCException {
		String sql = StringHelper.append("select distinct c.cust_id,c.user_id,c.county_id from c_prod c,c_prod_include d ",
				" where c.user_id =d.user_id");
		return createQuery(CProd.class,sql).list();
	}
	
	public int queryBillFeeByProdSn(String prodSn) throws JDBCException {
		String sql = "select SUM(B.OWE_FEE) from b_bill b where b.prod_sn=? and b.status='0'";
		return count(sql, prodSn);
	}

	public List<String> queryResByUserId(String userId, String prodId,String countyId) throws Exception {
		String sql = "select t.res_id,p.res_name from c_prod cp,c_prod_rsc t,p_res p"
			+" where cp.prod_sn=t.prod_sn and t.res_id=p.res_id"
			+" and cp.user_id=? and cp.prod_id<>?"
			+" union"
			+" select r.res_id,p.res_name from c_prod cp,p_prod_static_res r,p_res p"
			+" where cp.prod_id=r.prod_id and r.res_id=p.res_id "
			+" and cp.user_id=? and cp.prod_id<>?"
			+" union"
			+" select distinct r.res_id,p.res_name from c_prod cp,p_prod_county_res r,p_res p"
			+" where cp.prod_id=r.prod_id and r.res_id=p.res_id and cp.user_id=? and cp.prod_id<>? and r.county_id=?";
		return this.findUniques(sql, userId, prodId,userId, prodId,userId, prodId,countyId);
	}

	public List<UserProdDto> queryUserProdToCallCenter(Map<String,Object> p,String countyId) throws Exception {
		String cond = "", brandCond = "";
		Iterator<String> it = p.keySet().iterator();
		boolean flag = false;
		while(it.hasNext()){
			String key = it.next();
			String value = p.get(key).toString();
			
			if(key.equals("USER_ID")){
				cond += " and u.user_id='"+value+"'";
			}
			if(key.equals("CARD_ID")){
				cond += " and u.card_id='"+value+"'";
			}
			if(key.equals("STB_ID")){
				cond += " and u.stb_id='"+value+"'";
			}
			if(key.equals("MODEM_MAC")){
				cond += " and u.modem_mac='"+value+"'";
			}
			if(key.equals("BROAD_NAME")){
				flag = true;
				brandCond = " and b.login_name='" + value + "'";
			}
		}
		String sql = "";
		if(flag){
			sql = "select p.*,pp.prod_name,u.user_type,u.stb_id,u.card_id,u.modem_mac," +
			"u.status user_status,'' terminal_type,'' serv_type, b.login_name" +
			" from c_user u,c_user_broadband b,c_prod p,p_prod pp" +
			" where u.user_id=b.user_id and u.user_id=p.user_id and p.prod_id=pp.prod_id" +
			" and u.county_id=? and p.county_id=?" + cond + brandCond;
		}else{
			sql = "select p.*,pp.prod_name,u.user_type,u.stb_id,u.card_id,u.modem_mac," +
				"u.status user_status,a.terminal_type,a.serv_type,'' login_name" +
				" from c_user u,c_user_atv a,c_prod p,p_prod pp" +
				" where u.user_id=a.user_id and u.user_id=p.user_id and p.prod_id=pp.prod_id" +
				" and u.county_id=?" + cond +
				" union all " +
				"select p.*,pp.prod_name,u.user_type,u.stb_id,u.card_id,u.modem_mac," +
				"u.status user_status,d.terminal_type,d.serv_type,'' login_name" +
				" from c_user u,c_user_dtv d,c_prod p,p_prod pp" +
				" where u.user_id=d.user_id and u.user_id=p.user_id and p.prod_id=pp.prod_id" +
				" and u.county_id=?" + cond ;
		}
		return this.createQuery(UserProdDto.class, sql, countyId, countyId).list();
	}

	/**
	 * 统计指定产品缴费月份数，当天
	 * @param userId
	 * @param acctitemId
	 * @return
	 * @throws Exception
	 */
	public int payMonthsOfToday(String userId, String acctitemId)
			throws Exception {
		String sql = "SELECT round(sum(months_between(fa.prod_invalid_date,fa.begin_date))) "
				+ " FROM c_fee f,c_fee_acct fa WHERE f.user_id=? "
				+ " AND fa.fee_sn=f.fee_sn AND f.acctitem_id=?"
				+ " AND f.CREATE_TIME BETWEEN to_date(to_char(SYSDATE,'yyyymmdd'),'yyyymmdd')  "
				+ " AND to_date(to_char(SYSDATE,'yyyymmdd')||'23:59:59','yyyymmdd hh24:mi:ss') " 
				+ " AND f.status='PAY'";
		return Integer.parseInt(findUnique(sql, userId, acctitemId));
	}
	
	public List<CProd> queryProdByPackageProd(String custId, String packageId) throws Exception {
		String sql = "select * from c_prod t where t.package_id=? and t.cust_id=?";
		return this.createQuery(sql, packageId, custId).list();
	}
	
	public boolean isProdOpen(String status) throws Exception {
		String sql = "select count(1) from t_prod_status_openstop where status_id=? and open_or_stop='1'";
		return this.count(sql, status)>0;
	}

	public CProd queryBaseProdByCustId(String cust_id, String county_id) throws JDBCException{
		String sql = "SELECT t2.* "
					  +" FROM C_USER T, C_USER_DTV T1, C_PROD T2, P_PROD T3 "
					  +" WHERE T.USER_ID = T1.USER_ID "
					  +" AND T.USER_ID = T2.USER_ID "
					  +" AND T1.TERMINAL_TYPE = 'ZZD' "
					  +" AND T2.PROD_ID = T3.PROD_ID "
					  +" AND T3.IS_BASE = 'T' "
					  +" AND t.cust_id= ? "
					  +" AND t.county_id= ?";
		return createQuery(sql, cust_id, county_id).first();
	}

}

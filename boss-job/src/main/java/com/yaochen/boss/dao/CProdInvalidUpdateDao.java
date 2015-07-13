package com.yaochen.boss.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.yaochen.boss.model.CProdCycleDto;
import com.yaochen.boss.model.CProdUpdate;
import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * 使用公用后处理产品到期日
 */
@Component
public class CProdInvalidUpdateDao extends BaseEntityDao<CProd> {
	
	public void truncateCProdFeeFromPublic() throws JDBCException{
		String sql="truncate table c_prod_feefrompublic";
		this.executeUpdate(sql);
	}

	/**
	 * 按优先顺序取公用和专项公用账目
	 * @return
	 * @throws JDBCException
	 */
	public List<TPublicAcctitem> queryPublicAcctitems() throws JDBCException {
		String sql = "select * from t_public_acctitem t where t.acctitem_type in ('PUBLIC','SPEC')"
				+ " order by t.acctitem_type desc";
		return this.createQuery(TPublicAcctitem.class, sql).list();
	}
	/**
	 * 查询数据库时间
	 * @return
	 * @throws JDBCException
	 */
	public String queryDbDate() throws JDBCException{
		String sql="select to_char(sysdate,'yyyy-mm-dd') from dual";
		return this.findUnique(sql);
	}
	/**
	 * 更新一个客户使用公用产品的到期日
	 * @param cu
	 * @param doneCode 
	 * @throws JDBCException
	 */
	public void updateCprod(Map<String,CProdCycleDto> cprodmp, Integer doneCode) throws JDBCException{
		for(CProdCycleDto prod: cprodmp.values()){
			String sql = "insert into c_prod_prop_change (select c.prod_sn,? ,to_char(c.invalid_date,'yyyy-mm-dd'),to_char((trunc(sysdate)+?+0.5),'yyyy-mm-dd') ,?,"
				+ " sysdate,c.county_id,c.area_id  from c_prod c where c.prod_sn = ? and c.county_id=? and abs(c.invalid_date-trunc(sysdate)-?)>2 )";
			this.executeUpdate(sql, "invalid_date",prod.getInvalid_date_num(),doneCode,prod.getProd_sn(),prod.getCounty_id(),prod.getInvalid_date_num());
			sql=" update c_prod set invalid_date=trunc(trunc(sysdate)+?+0.5) where (prod_sn=? or package_sn=?) and county_id=?";
			this.executeUpdate(sql, prod.getInvalid_date_num(),prod.getProd_sn(),prod.getProd_sn(),prod.getCounty_id());
		}
	}
	/**
	 * 更新一个产品的到期日
	 * @param prod
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void updateOneProdInavlid(CProd prod,Integer doneCode)throws JDBCException {
		String sql = "insert into c_prod_prop_change (select c.prod_sn,? ,to_char(c.invalid_date,'yyyy-mm-dd'),to_char(?,'yyyy-mm-dd') ,?,"
			+ " sysdate,c.county_id,c.area_id  from c_prod c where c.prod_sn = ? and c.county_id=?  and abs(c.invalid_date- to_date(to_char(?,'yyyy-mm-dd'),'yyyy-mm-dd') )>2 )";
		this.executeUpdate(sql, "invalid_date",prod.getInvalid_date(),doneCode,prod.getProd_sn(),prod.getCounty_id(),prod.getInvalid_date());
		sql=" update c_prod set invalid_date=? where (prod_sn=? or package_sn=?) and county_id=? ";
		this.executeUpdate(sql, prod.getInvalid_date(),prod.getProd_sn(),prod.getProd_sn(),prod.getCounty_id());
	}
	/**
	 * 获取一个客户的使用公用的产品
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdUpdate> queryCProdUpdates(String cust_id,String county_id) throws JDBCException{
		List<CProdUpdate> culist=new ArrayList<CProdUpdate>();
		List<TPublicAcctitem> pitems= queryPublicAcctitems();
		for(TPublicAcctitem pitem:pitems){
			List<CProdCycleDto> cdlist=this.createQuery(CProdCycleDto.class,queryCProdByUsePublicAcctitemSql(pitem,true), cust_id,county_id).list();
			if(cdlist.size()>0){
				culist.add(new CProdUpdate(cdlist));
			}
		}
		return culist;
	}
	/**
	 * 生成使用指定公用账目后产品查询SQL,
	 * onecustsign=true 查指定一个客户
	 * @param pitem
	 * @return
	 */
	public String queryCProdByUsePublicAcctitemSql(TPublicAcctitem pitem,boolean onecustsign){
		String sql=StringHelper.append("select cp.prod_sn,cp.county_id,cp.cust_id,"
			," f_account_invalid((caa1.active_balance+caa1.inactive_balance+caa1.order_balance-caa1.real_bill-caa1.owe_fee),ppt.rent,case when cp.billinfo_eff_date>trunc(sysdate) then trunc(cp.billinfo_eff_date) else trunc(sysdate) end)-trunc(sysdate) invalid_date_num,"
			," ppt.rent*12/365 tariff_rent_365,"
			,"(caa2.active_balance+caa2.order_balance+caa2.inactive_balance) public_balance,"
			," trunc(cp.exp_date)-trunc(sysdate) exp_date_num"
			," from  c_prod cp, t_acctitem_to_prod atop,busi.p_prod_tariff ppt,"
			," busi.c_acct_acctitem caa1,busi.c_acct_acctitem caa2,busi.c_acct ca"
			," where cp.prod_id=atop.prod_id and atop.acctitem_id='",pitem.getAcctitem_id(),"'"
			," and cp.public_acctitem_type in (",(pitem.getAcctitem_type().equals("SPEC")?"'ALL','SPEC_ONLY'":"'ALL','PUBLIC_ONLY'"),")"
			," and cp.tariff_id=ppt.tariff_id and ppt.rent>0 and cp.package_sn is null"
			," and ppt.billing_cycle=1 and cp.stop_by_invalid_date='F'"
			," and cp.status in('" + StatusConstants.ACTIVE +"','" +StatusConstants.PREAUTHOR + "') and caa1.acct_id=cp.acct_id"
			," and caa1.acctitem_id=cp.prod_id and caa1.county_id=cp.county_id"
			," and ca.cust_id=cp.cust_id and ca.county_id=cp.county_id"
			," and ca.acct_type='PUBLIC' and ca.acct_id=caa2.acct_id"
			," and ca.county_id=caa2.county_id and caa2.acctitem_id='",pitem.getAcctitem_id(),"'"
			,(!onecustsign?"":" and cp.cust_id=? and cp.county_id=?")
			," and (caa2.active_balance+caa2.order_balance+caa2.inactive_balance)>0 order by cp.cust_id,invalid_date_num");
		return sql;
	}
	/**
	 * 查询一个客户可用于计算到期日的产品清单
	 * @return
	 */
	public List<CProdCycleDto> queryCProdCanManthInvalid(String cust_id,String county_id)throws JDBCException {
		String sql=StringHelper.append(
	    " select cp.prod_sn,cp.county_id,cp.cust_id, "
       ," trunc(f_account_invalid((caa1.active_balance+caa1.inactive_balance+caa1.order_balance-caa1.real_bill-caa1.owe_fee),ppt.rent,"
       ," case when cp.billinfo_eff_date>trunc(sysdate) then trunc(cp.billinfo_eff_date) else trunc(sysdate) end)+0.9) invalid_date"
       ," from  c_prod cp, busi.p_prod_tariff ppt,busi.c_acct_acctitem caa1 "
       ," where  cp.tariff_id=ppt.tariff_id and ppt.rent>0 and cp.package_sn is null "
       ," and ppt.billing_cycle=1 and cp.stop_by_invalid_date='F' "
       ," and caa1.acct_id=cp.acct_id and caa1.acctitem_id=cp.prod_id and caa1.county_id=cp.county_id "
       ," and cp.cust_id=? and cp.county_id=? and cp.status in ('" + StatusConstants.ACTIVE + "','" + StatusConstants.PREAUTHOR + "') "
       ," and (caa1.active_balance+caa1.inactive_balance+caa1.order_balance-caa1.real_bill)>=0 ");	
		return this.createQuery(CProdCycleDto.class,sql,cust_id,county_id).list();
	}
	/**
	 * 被包含的不计费状态产品计算到期日
	 * 只计费产品状态是 不计费 且 计费周期=1 资费>0的产品
	 * @return
	 */
	public List<CProdCycleDto> queryCProdPaushInvalid(String cust_id,String county_id)throws JDBCException{
		String sql=StringHelper.append(
		" select a.prod_sn,a.county_id,a.cust_id, ",
		" trunc(f_account_invalid((caa1.active_balance+caa1.inactive_balance+caa1.order_balance-caa1.real_bill-caa1.owe_fee) ",
		"   ,a.rent,a.invalid_date)+0.9) invalid_date ",
		" from (select t.prod_sn,t.county_id,t.cust_id,t.acct_id,t.prod_id,ppt.rent,nvl(max(cp.invalid_date),t.invalid_date) invalid_date ",
		" from c_prod t,c_prod cp,c_prod_include ci, busi.p_prod_tariff ppt ",
		" where ci.user_id=t.user_id and ci.include_prod_sn=t.prod_sn and  cp.prod_sn=ci.prod_sn ",
		" and cp.county_id=? and t.county_id=? and t.cust_id=?  ",
		" and t.tariff_id=ppt.tariff_id and ppt.billing_cycle=1 and ppt.rent>0 ",
		" and t.stop_by_invalid_date='F' and t.status=? and t.package_sn is null ",
		" group by t.prod_sn,t.county_id,t.cust_id,t.acct_id,t.prod_id,ppt.rent,t.invalid_date) a, busi.c_acct_acctitem caa1 ",
		" where a.acct_id=caa1.acct_id and a.prod_id=caa1.acctitem_id and a.county_id=caa1.county_id ");
		return this.createQuery(CProdCycleDto.class,sql,county_id,county_id,cust_id,StatusConstants.PAUSE).list();
	}
}

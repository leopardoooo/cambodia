package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFollowPay;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CProdOrderDao extends BaseEntityDao<CProdOrder> {
	/**
	 * 保存订单修改
	 * @param order
	 * @throws JDBCException
	 */
	public void updateOrderEdit(CProdOrder order) throws JDBCException{
		String sql="update c_prod_order set prod_id=?,tariff_id=?,disct_id=?,exp_date=?,order_months=?,order_fee=?,remark=? where order_sn=?";
		this.executeUpdate(sql, order.getProd_id(),order.getTariff_id(),order.getDisct_id(),order.getExp_date(),order.getOrder_months(),order.getOrder_fee(),order.getRemark(),order.getOrder_sn());
	}
	/**
	 * 查询可以续费的订单记录
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrderFollowPay> queryFollowPayOrderDto(String custId) throws JDBCException{
		String sql=StringHelper.append(
				" select b.prod_type,cu.user_type,cu.terminal_type,b.prod_name,b.serv_id,b.is_base,nvl(d.disct_rent, c.rent) rent,",
				"  nvl(d.billing_cycle,c.billing_cycle) billing_cycle,c.billing_type,nvl(d.disct_name,c.tariff_name) tariff_name ,",
				"  case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name,",
				"   a.* ",
		        " from c_prod_order a,p_prod b,p_prod_tariff c,p_prod_tariff_disct d,p_prod e ,c_user cu",
		        " where a.cust_id=? and a.prod_id=b.prod_id and a.package_id=e.prod_id(+) ",
		        " and a.tariff_id=c.tariff_id(+) and a.disct_id= d.disct_id(+)  ",
//				"		 and a.exp_date>=trunc(sysdate) ",
		        " and a.user_id=cu.user_id(+) ",
				"		 order by b.prod_type desc,cu.user_type,cu.user_id,b.is_base desc,",
		        "  (case when b.is_base='BASE' and b.serv_id<>'BAND' then b.prod_id end),",
		        " a.exp_date desc ");
		return this.createQuery(CProdOrderFollowPay.class, sql, custId).list();
		
	}
	/**
	 * 查询一个客户有效的订单
	 */
	public List<CProdOrderDto> queryCustEffOrderDto(String custId) throws JDBCException{
		String sql = "select c.billing_type, b.prod_name,b.prod_type,b.serv_id,b.is_base,e.prod_name package_name,nvl(d.disct_name,c.tariff_name) tariff_name,d.disct_name, a.* "
				+ " from c_prod_order a,p_prod b,p_prod_tariff c,p_prod_tariff_disct d,p_prod e "
				+ " where a.cust_id=? and a.prod_id=b.prod_id and a.package_id=e.prod_id(+) "
				+ " and a.tariff_id=c.tariff_id(+) and a.disct_id= d.disct_id(+) "
				+ " and (a.exp_date>=trunc(sysdate) or a.status in (?,?,?) ) "
				+ " order by a.cust_id,a.user_id,b.is_base desc,a.exp_date  ";
		
		return this.createQuery(CProdOrderDto.class, sql, custId,StatusConstants.REQSTOP,
				StatusConstants.LINKSTOP,StatusConstants.INSTALL).list();
	}
	
	/**
	 * 查询一个客户所有的订单
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrderDto> queryCustAllOrderDto(String custId) throws JDBCException{
		String sql = "select c.billing_type, b.prod_name,b.prod_type,b.serv_id,b.is_base,e.prod_name package_name,nvl(d.disct_name,c.tariff_name) tariff_name,d.disct_name, a.* "
				+ " from c_prod_order a,p_prod b,p_prod_tariff c,p_prod_tariff_disct d,p_prod e "
				+ " where a.cust_id=? and a.prod_id=b.prod_id and a.package_id=e.prod_id(+) "
				+ " and a.tariff_id=c.tariff_id(+) and a.disct_id= d.disct_id(+) "
				+ " order by a.cust_id,a.user_id,b.is_base desc,a.exp_date ";
		return this.createQuery(CProdOrderDto.class, sql, custId).list();
	}
	
	public List<CProdOrderDto> queryCustOrderALLAndHisDto(String custId) throws JDBCException{
		String sql = "select c.billing_type, b.prod_name,b.prod_type,b.serv_id,b.is_base,e.prod_name package_name,nvl(d.disct_name,c.tariff_name) tariff_name,d.disct_name, "
				+ " a.order_sn, a.done_code, a.package_sn, a.package_id, a.cust_id, a.user_id, a.prod_id, a.tariff_id, a.disct_id, a.status, a.status_date, a.eff_date, a.exp_date, a.active_fee, a.bill_fee, a.order_months, a.order_fee, order_time, a.order_type, a.package_group_id, a.area_id, a.county_id, a.optr_id, a.remark, a.public_acctitem_type, a.is_pay  "
				+ " from c_prod_order a,p_prod b,p_prod_tariff c,p_prod_tariff_disct d,p_prod e "
				+ " where a.cust_id=? and a.prod_id=b.prod_id and a.package_id=e.prod_id(+) "
				+ " and a.tariff_id=c.tariff_id(+) and a.disct_id= d.disct_id(+) "
				+ " union all "
				+ " select c.billing_type, b.prod_name,b.prod_type,b.serv_id,b.is_base,e.prod_name package_name,nvl(d.disct_name,c.tariff_name) tariff_name,d.disct_name, "
				+ " a.order_sn, a.done_code, a.package_sn, a.package_id, a.cust_id, a.user_id, a.prod_id, a.tariff_id, a.disct_id, 'INVALID' status, a.status_date, a.eff_date, a.exp_date, a.active_fee, a.bill_fee, a.order_months, a.order_fee, order_time, a.order_type, a.package_group_id, a.area_id, a.county_id, a.optr_id, a.remark, a.public_acctitem_type, a.is_pay  "
						+ " from c_prod_order_his a,p_prod b,p_prod_tariff c,p_prod_tariff_disct d,p_prod e "
						+ " where a.cust_id=? and a.prod_id=b.prod_id and a.package_id=e.prod_id(+) "
						+ " and a.tariff_id=c.tariff_id(+) and a.disct_id= d.disct_id(+) "
						
				+ " order by cust_id,user_id,is_base desc,exp_date ";
		return this.createQuery(CProdOrderDto.class, sql, custId, custId).list();
	}
	
	public List<CProdOrder> queryCustEffOrder(String custId) throws JDBCException{
		String sql = "select * from c_prod_order where cust_id=? "
				+ " and (exp_date>=trunc(sysdate) or status in (?,?,?))"
				+ "order by cust_id,user_id,exp_date ";
		
		return this.createQuery(CProdOrder.class, sql, custId,StatusConstants.REQSTOP,
				StatusConstants.LINKSTOP,StatusConstants.INSTALL).list();
	}
	/**
	 * 按主键获得一个订单的信息信息
	 * @param order_sn
	 * @return
	 * @throws JDBCException 
	 */
	public CProdOrderDto queryCProdOrderDtoByKey(String order_sn) throws JDBCException{
		String sql=StringHelper.append(" select p.prod_type,p.serv_id,p.is_base,nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle,ppt.billing_type,cu.protocol_date,case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d,c_user cu ",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id and cu.user_id(+)=o.user_id ",
			    " and  o.order_sn=? "); 
		return this.createQuery(CProdOrderDto.class, sql,order_sn).first();
	}
	
	/**
	 * 查询待支付的订单(含套餐和套餐子产品)
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryUnPayOrder(String cust_id) throws JDBCException{
		String sql="select  o.* from c_prod_order o,c_done_code_unpay u where is_pay='F' and o.cust_id=u.cust_id and o.done_code=u.done_code and u.cust_id=? ";
		return this.createQuery( sql,cust_id).list();
	}
	/**
	 * 更新未支付订单支付属性
	 * @param done_code
	 * @param cust_id
	 * @throws JDBCException
	 */
    public void updateOrderToPay(String order_sn,String cust_id,String is_pay) throws JDBCException{
    	String sql="update c_prod_order set is_pay=? where order_sn=? and cust_id=? ";
    	this.executeUpdate(sql, is_pay,order_sn,cust_id);
    }

	/**
	 * 查询一个套餐的子订单明细
	 * @param package_sn
	 * @return
	 * @throws JDBCException 
	 */
	public List<CProdOrder> queryPakDetailOrder(String package_sn) throws JDBCException{
		String sql= "select * from c_prod_order where package_sn=? ";
		return this.createQuery(sql, package_sn).list();
	}
	/**
	 * 查询在套餐之后续订的单产品(跟套餐子产品重叠)
	 * @param package_sn
	 * @return
	 * @throws JDBCException 
	 */
	public List<CProdOrder> querySingleProdOrderAfterPak(String package_sn) throws JDBCException{
		String sql=StringHelper.append(" select t.* from c_prod_order t,c_prod_order pak,p_prod p ",
				" where pak.package_sn=? and  pak.prod_id=p.prod_id and p.prod_type=? ",
				" and pak.user_id=t.user_id and t.package_sn is null ",
				" and t.exp_date>pak.exp_date and( p.serv_id =? or pak.prod_id=t.prod_id) order by t.exp_date desc ");
		return this.createQuery(sql, package_sn,SystemConstants.PROD_TYPE_BASE,SystemConstants.PROD_SERV_ID_BAND).list();
	}
	/**
	 * 查询一个用户一个产品的有效的所有订购记录(不含套餐子产品)
	 * @param user_id
	 * @param prod_id
	 * @return
	 */
	public List<CProdOrder> queryNotExpOrderByProd(String user_id,String prod_id)throws JDBCException{	
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=? and prod_id=? and package_sn is null ",
				" and exp_date >=trunc(sysdate)  ");
		return this.createQuery(sql, user_id,prod_id).list();
	}

	/**
	 * 查询一个用户一个单产品有效的所有订购记录(含套餐子产品)
	 * @param user_id
	 * @param prod_id
	 * @return
	 */
	public List<CProdOrder> queryNotExpAllOrderByProd(String user_id,String prod_id)throws JDBCException{
		
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=? and prod_id=?  and exp_date >=trunc(sysdate)  order by exp_date ");
		return this.createQuery(sql, user_id,prod_id).list();
	}
	
	/**
	 * 查询一个用户有效的所有产品订购记录(含套餐子产品)
	 * @param user_id
	 * @param prod_id
	 * @return
	 */
	public List<CProdOrder> queryNotExpAllOrderByUser(String user_id)throws JDBCException{
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=?  and exp_date >=trunc(sysdate)  order by exp_date ");
		return this.createQuery(sql, user_id).list();
	}	
	
	/**
	 * 查询一个宽带用户的所有单产品订购记录(不含套餐子产品)
	 * @param user_id
	 * @return
	 */
	public List<CProdOrder> queryNotExpOrderByBand(String user_id)throws JDBCException{
		
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=? and prod_id in (select a.prod_id from p_prod a where a.serv_id='BAND') ",
				" and package_sn is null ",
				" and exp_date >=trunc(sysdate) ");
		return this.createQuery(sql, user_id).list();
	}
	
	/**
	 * 查询一个客户有效的套餐所有订购记录
	 * @param user_id
	 * @return
	 */
	public List<CProdOrder> queryNotExpPackageOrder(String cust_id)throws JDBCException{
		String sql=StringHelper.append("select * from c_prod_order ",
				"where cust_id=? and prod_id in (select a.prod_id from p_prod a where a.prod_type<>'BASE') ",
				"and package_sn is null ",
				" and exp_date >=trunc(sysdate) order by exp_date");
		return this.createQuery(sql, cust_id).list();
	}
	
	/**
	 * 查询一个客户所有套餐订单详细清单
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrderDto> queryPackageOrderDtoByCustId(String cust_id)throws JDBCException{
		
		String sql=StringHelper.append(" select p.prod_type,p.serv_id,p.is_base,nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle,ppt.billing_type, nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id  ",
			    " and  o.cust_id=? and o.prod_id in (select a.prod_id from p_prod a where a.prod_type<>'BASE')",
			    " and o.package_sn is null ",
				"  order by o.exp_date ");
		
		return this.createQuery(CProdOrderDto.class,sql, cust_id).list();
	}
	/**
	 * 查询用户的产品订购记录清单(含套餐子产品)
	 * @param user_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrderDto> queryProdOrderDtoByUserId(String user_id) throws JDBCException{

		String sql=StringHelper.append(
				" select p.prod_type,p.serv_id, p.is_base, cu.protocol_date, nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle,ppt.billing_type,"
				," case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d,c_user cu ",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id and cu.user_id(+)=o.user_id ",
			    " and  o.user_id=? order by o.exp_date "); 
		
		return this.createQuery(CProdOrderDto.class,sql, user_id).list();
	}
	
	public List<CProdOrderDto> queryProdOrderDtoByUserIdList(String[] userIds) throws JDBCException{

		String sql=StringHelper.append(
				" select p.prod_name,p.prod_type,p.serv_id, p.is_base, cu.protocol_date, nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle,ppt.billing_type,"
				," case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d,c_user cu ",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id and cu.user_id(+)=o.user_id ",
			    " and o.user_id in ("+sqlGenerator.in(userIds)+") ",
			    " and trunc(o.exp_date)>=trunc(sysdate)",
			    " order by o.exp_date "); 
		
		return this.createQuery(CProdOrderDto.class,sql).list();
	}
	
	/**
	 * 查询一个套餐的子产品详细信息
	 * @param package_sn
	 * @return
	 */
	public List<CProdOrderDto> queryProdOrderDtoByPackageSn(String package_sn)throws JDBCException{
		String sql=StringHelper.append(
				" select p.prod_type,p.serv_id, p.is_base,cu.protocol_date, nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle,ppt.billing_type,"
				," case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d,c_user cu ",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id and cu.user_id(+)=o.user_id ",
			    " and  o.package_sn=? order by o.exp_date "); 
		return this.createQuery(CProdOrderDto.class,sql, package_sn).list();
		
	}
	
	public List<CProdOrder> queryOrderProdByUserId(String user_id) throws JDBCException{
		String sql = "select * from c_prod_order where user_id=?";
		return this.createQuery(sql, user_id).list();
	}
	/**
	 * 更新订单的授权检查时间
	 * @param orderSn
	 * @throws Exception
	 */
	public void updateCheckTime(String orderSn) throws Exception{
		String sql="update c_prod_order set check_time=sysdate where order_sn=? and check_time is null ";
		this.executeUpdate(sql, orderSn);
	}
	
	/**
	 * 查询需要宽带修正带宽的用户
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryUserNeedChanageBandWidth() throws JDBCException{
		String sql=" select t.* "
		+" from busi.c_prod_order t,busi.p_prod p "
		+"  where  t.prod_id=p.prod_id and p.serv_id=? "
		+"  and t.status in (?,?) "
		+" AND t.EXP_DATE>=TRUNC(SYSDATE) AND t.check_time is null " 
		+" and t.eff_date <=trunc(sysdate)+1 and t.is_pay='T' ";
		return this.createQuery(CProdOrder.class,sql, SystemConstants.PROD_SERV_ID_BAND,StatusConstants.ACTIVE,StatusConstants.INSTALL).list();
	}
	/**
	 * 更新失效的订单状态未到期停
	 * @throws JDBCException 
	 */
	public void updateExpOrderStatusToForStop() throws JDBCException{
		String sql="update   c_prod_order t set t.status=? ,t.status_date=sysdate where t.status=? and t.exp_date<trunc(sysdate)";
		this.executeUpdate(sql, StatusConstants.FORSTOP,StatusConstants.ACTIVE);
	}
	
	/**
	 * 查询一个用户一个单产品有效的所有订购记录(含套餐子产品)
	 * 先套餐再按生效时间排序
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryNotExpAllOrderByProdOrderByEff(String user_id,String prod_id)throws JDBCException{
		
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=? and prod_id=?  and exp_date >=trunc(sysdate)  order by (case when package_sn is not null then 1 else 2 end), eff_date ");
		return this.createQuery(sql, user_id,prod_id).list();
	}
	/** 
	 * 查询一个用户有效的所有产品订购记录(含套餐子产品)
	 * 先套餐再按生效时间排序
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryNotExpAllOrderByUserOrderEff(String user_id)throws JDBCException{
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=?  and exp_date >=trunc(sysdate)  order by (case when package_sn is not null then 1 else 2 end),eff_date ");
		return this.createQuery(sql, user_id).list();
	}
	/**
	 * 查询一个客户有效的套餐所有订购记录
	 * 先套餐再按生效时间排序
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryNotExpPackageOrderByEff(String cust_id)throws JDBCException{
		String sql=StringHelper.append("select * from c_prod_order ",
				"where cust_id=? and prod_id in (select a.prod_id from p_prod a where a.prod_type<>'BASE') ",
				"and package_sn is null ",
				" and exp_date >=trunc(sysdate) order by eff_date");
		return this.createQuery(sql, cust_id).list();
	}
	/**
	 * 查询订单回退相关的所有订单
	 * @param custId
	 * @param userIds
	 * @param taskDoneCode
	 * @return
	 * @throws Exception
	 */
	public List<CProdOrder> queryTaskCancelOrder(String custId,String[] userIds,Integer taskDoneCode) throws Exception{
		String sql ="select a.* from c_prod_order a "+
				  " where  a.order_sn in ( "+
				  "	select order_sn "+
				  "	  from c_prod_order "+
				  "	 where cust_id = ?  "+
				  "	   and user_id in ("+sqlGenerator.in(userIds)+") "+
				  "	  "+
				  "	union "+
				  "	select pak.order_sn "+
				  "	  from c_prod_order a,c_prod_order pak "+
				  "	 where a.cust_id = ? "+
				  "	   and a.user_id in ("+sqlGenerator.in(userIds)+") "+
				  "	   and a.package_sn=pak.order_sn and pak.done_code>? )";
		return this.createQuery(sql, custId,custId,taskDoneCode).list();
	}
	/**
	 * 查询正常宽带所有订单失效的订单清单
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryBandAllOrderExp() throws JDBCException{
		String sql="select t.* "
				+" from c_prod_order t ,c_user cu "
				+" where t.status='ACTIVE' and t.exp_date<trunc(sysdate) "
				+" and cu.user_id=t.user_id and cu.user_type='BAND' and cu.status='ACTIVE' "
				+" and not exists(select 1 from c_prod_order a "
						+ " where a.user_id=t.user_id and a.exp_date>=trunc(sysdate))";
		return this.createQuery(sql).list();				
	}
	/**
	 * 查询产品的boss资源定义pres
	 * @param prod_id
	 * @return
	 * @throws JDBCException 
	 */
	public List<PRes> queryPRes(String prod_id) throws JDBCException{
		String sql="select p.* from p_prod_static_res a,p_res p  where a.res_id=p.res_id and a.prod_id=? ";
		return this.createQuery(PRes.class, sql, prod_id).list();
	}
}

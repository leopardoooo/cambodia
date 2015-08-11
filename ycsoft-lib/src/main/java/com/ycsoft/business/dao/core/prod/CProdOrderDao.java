package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CProdOrderDao extends BaseEntityDao<CProdOrder> {
	public List<CProdOrderDto> queryCustEffOrderDto(String custId) throws JDBCException{
		String sql = "select b.prod_name,b.prod_type,b.serv_id,b.is_base,e.prod_name package_name,c.tariff_name,d.disct_name, a.* "
				+ " from c_prod_order a,p_prod b,p_prod_tariff c,p_prod_tariff_disct d,p_prod e "
				+ " where a.cust_id=? and a.prod_id=b.prod_id and a.package_id=e.prod_id(+) "
				+ " and a.tariff_id=c.tariff_id(+) and a.disct_id= d.disct_id(+) "
				+ " and (a.exp_date>=sysdate or a.status in (?,?,?)) "
				+ " order by a.cust_id,a.user_id,a.exp_date  ";
		
		return this.createQuery(CProdOrderDto.class, sql, custId,StatusConstants.REQSTOP,
				StatusConstants.LINKSTOP,StatusConstants.INSTALL).list();
	}
	
	
	public List<CProdOrder> queryCustEffOrder(String custId) throws JDBCException{
		String sql = "select * from c_prod_order where cust_id=? "
				+ " and (exp_date>=sysdate or status in (?,?,?))"
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
		String sql=StringHelper.append(" select p.prod_type,p.serv_id,p.is_base,cu.protocol_date,case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
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
    public void updateOrderToPay(Integer done_code,String cust_id) throws JDBCException{
    	String sql="update c_prod_order set is_pay='T' where is_pay='F' and done_code=? and cust_id=? ";
    	this.executeUpdate(sql, done_code,cust_id);
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
		
		String sql=StringHelper.append(" select p.prod_type,p.serv_id,p.is_base,nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle, nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
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
				" select p.prod_type,p.serv_id, p.is_base, cu.protocol_date, nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle,"
				," case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d,c_user cu ",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id and cu.user_id(+)=o.user_id ",
			    " and  o.user_id=? order by o.exp_date "); 
		
		return this.createQuery(CProdOrderDto.class,sql, user_id).list();
	}
	/**
	 * 查询一个套餐的子产品详细信息
	 * @param package_sn
	 * @return
	 */
	public List<CProdOrderDto> queryProdOrderDtoByPackageSn(String package_sn)throws JDBCException{
		String sql=StringHelper.append(
				" select p.prod_type,p.serv_id, p.is_base,cu.protocol_date, nvl(d.billing_cycle,ppt.billing_cycle) billing_cycle,"
				," case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d,c_user cu ",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id and cu.user_id(+)=o.user_id ",
			    " and  o.package_sn=? order by o.exp_date "); 
		return this.createQuery(CProdOrderDto.class,sql, package_sn).list();
		
	}

}

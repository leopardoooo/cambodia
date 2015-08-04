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
				+ " order by a.cust_id,a.user_id,a.exp_date desc ";
		
		return this.createQuery(CProdOrderDto.class, sql, custId,StatusConstants.REQSTOP,
				StatusConstants.LINKSTOP,StatusConstants.INSTALL).list();
	}
	
	
	public List<CProdOrder> queryCustEffOrder(String custId) throws JDBCException{
		String sql = "select * from c_prod_order where cust_id=? "
				+ " and (exp_date>=sysdate or status in (?,?,?))"
				+ "order by cust_id,user_id,exp_date desc";
		
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
		String sql=StringHelper.append(" select case when cu.user_id is null then null when   cu.user_type in ('OTT_MOBILE','BAND') then cu.login_name else nvl(cu.stb_id,'INSTALL') end user_name ,nvl(d.disct_name,ppt.tariff_name) tariff_name,p.prod_name, o.* ",
				" from c_prod_order o,p_prod p,p_prod_tariff ppt ,p_prod_tariff_disct d,c_user cu ",
			    " where p.prod_id=o.prod_id and ppt.tariff_id=o.tariff_id and d.disct_id(+)=o.disct_id and cu.user_id(+)=o.user_id ",
			    " and  o.order_sn=? "); 
		return this.createQuery(CProdOrderDto.class, sql,order_sn).first();
	}
	
	/**
	 * 查询待支付的订单
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryUnPayOrder(String cust_id) throws JDBCException{
		String sql="select  o.* from c_prod_order o,c_done_code_unpay u where o.package_sn is null and  o.cust_id=u.cust_id and o.done_code=u.done_code and u.cust_id=? ";
		return this.createQuery( sql,cust_id).list();
	}
	/**
	 * 更新未支付订单支付属性
	 * @param done_code
	 * @param cust_id
	 * @throws JDBCException
	 */
    public void updateOrderToPay(Integer done_code,String cust_id) throws JDBCException{
    	String sql="update c_prod_order set is_pay='T' where done_code=? and cust_id=? ";
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
	 * 查询转移支付被覆盖退订的单产品订购记录
	 * @param user_id
	 * @param prod_id
	 * @return
	 */
	public List<CProdOrder> queryTransOrderByProd(String user_id,String prod_id)throws JDBCException{
		
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=? and prod_id=? and package_sn is null ",
				" and exp_date >=trunc(sysdate) ");
		return this.createQuery(sql, user_id,prod_id).list();
	}
	/**
	 * 查询转移支付被覆盖退订的宽带单产品订购记录
	 * @param user_id
	 * @return
	 */
	public List<CProdOrder> queryTransOrderByBand(String user_id)throws JDBCException{
		
		String sql=StringHelper.append("select * from c_prod_order ",
				"where user_id=? and prod_id in (select a.prod_id from p_prod a where a.serv_id='BAND') ",
				" and package_sn is null ",
				" and exp_date >=trunc(sysdate) ");
		return this.createQuery(sql, user_id).list();
	}
	/**
	 * 查询转移支付被覆盖退订的套餐订购记录
	 * @param user_id
	 * @return
	 */
	public List<CProdOrder> queryTransOrderByPackage(String cust_id)throws JDBCException{
		String sql=StringHelper.append("select * from c_prod_order ",
				"where cust_id=? and prod_id in (select a.prod_id from p_prod a where a.prod_type<>'BASE') ",
				"and package_sn is null ",
				" and exp_date >=trunc(sysdate) ");
		return this.createQuery(sql, cust_id).list();
	} 
	
	/**
	 * 查询用户的产品订购记录清单
	 * @param user_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryProdOrderByUserId(String user_id) throws JDBCException{
		String sql="select * from c_prod_order where user_id=? ";
		return this.createQuery(sql, user_id).list();
	}
}

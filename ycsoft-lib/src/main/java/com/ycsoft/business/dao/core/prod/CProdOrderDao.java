package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CProdOrderDao extends BaseEntityDao<CProdOrder> {
	public List<CProdOrder> queryCustEffOrder(String custId) throws JDBCException{
		String sql = "select * from c_prod_order where cust_id=? "
				+ " and eff_date<=sysdate and exp_date>=sysdate "
				+ "order by cust_id,user_id,exp_date desc";
		
		return this.createQuery(CProdOrder.class, sql, custId).list();
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
}

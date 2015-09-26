package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

@Component
public class CProdOrderFeeDao extends BaseEntityDao<CProdOrderFee>  {
	//TODO 要去掉这个方法相关的所有代码
	@Override
	public int[] update(CProdOrderFee ...entitys) throws JDBCException{
		return super.update(entitys);
	}
	/**
	 * 更新订单金额记录的余额
	 * @param order_fee_sn
	 * @param output_fee
	 * @throws JDBCException 
	 */
	public void updateOrderFee(String order_fee_sn,Integer output_fee) throws JDBCException{
		String sql="update c_prod_order_fee set fee=fee-? where order_fee_sn=? ";
		this.executeUpdate(sql, output_fee,order_fee_sn);
	}
	public List<CProdOrderFee> queryByOrderSn(String order_sn) throws JDBCException{
		String sql="select * from c_prod_order_fee where order_sn=?";
		return this.createQuery(sql, order_sn).list();
	}
	
	/**
	 * 根据转出信息查询订单金额明细
	 * @param order_sn
	 * @param output_type
	 * @param output_sn
	 * @return
	 */
	public List<CProdOrderFee> queryByOutPutInfo(String order_sn,String output_type,String output_sn)throws JDBCException{
		String sql="select * from c_prod_order_fee where order_sn=? and output_type=? and output_sn=? ";
		return this.createQuery(sql, order_sn,output_type,output_sn).list();
	}
    /**
     * 清除转出信息
     * @param order_sn
     * @param output_type
     * @param output_sn
     * @throws JDBCException
     
	public void clearOutPutInfo(String order_sn,String output_type) throws JDBCException{
		String sql="update c_prod_order_fee set output_type=null ,output_sn=null,output_fee=0"
				+ " where order_sn=? and output_type=? ";
		this.executeUpdate(sql, order_sn,output_type);
	}*/
	
	public void deleteOrderFeeByOrderSn(String order_sn)throws JDBCException{
		String sql="delete from c_prod_order_fee where order_sn=? ";
		this.executeUpdate(sql, order_sn);
	}

	public void updateFeeType(String order_sn,Integer done_code,String fee_type) throws JDBCException{
		String sql="update c_prod_order_fee set fee_type=? where done_code=?  and order_sn=?";
		this.executeUpdate(sql, fee_type,done_code,order_sn);
	}
	
	public Pager<CProdOrderFee> queryOrderFeeDetail(String orderSn, Integer start, Integer limit) throws Exception {
		String sql = "select t.done_code,t.order_fee_sn,t.input_type,t.fee_type,t.fee input_fee,t.create_time, t.remark input_prod_name "
				+ " from (select * from c_prod_order_fee where order_sn=?) t"
				+ " order by t.create_time desc";
		
		return this.createQuery(sql, orderSn).setStart(start).setLimit(limit).page();
	}
	
	public List<CProdOrderFee> queryPayedOrderFeeByUser(String custId,String[] userIds) throws Exception{
		String sql ="select b.* from c_prod_order a,c_prod_order_Fee b where a.order_sn in ( "+
					"	select order_sn "+
					"	  from c_prod_order "+
					"	 where cust_id = ? "+
					"	   and user_id in ("+sqlGenerator.in(userIds)+") "+
					"	   and package_sn is null "+
					"	union "+
					"	select package_sn order_sn "+
					"	  from c_prod_order "+
					"	 where cust_id = ? "+
					"	   and user_id in ("+sqlGenerator.in(userIds)+") "+
					"	   and package_sn is not null) and a.is_pay='T' and a.order_sn = b.order_sn";
		return this.createQuery(sql, custId,custId).list();
	}
	/**
	 * 查询工单回退相关的所有费用
	 * @param custId
	 * @param userIds
	 * @param taskDoneCode
	 * @return
	 * @throws Exception
	 */
	public List<CProdOrderFee> queryTaskPayedOrderFeeByUser(String custId,String[] userIds,Integer taskDoneCode) throws Exception{
		String sql ="select b.* from c_prod_order a,c_prod_order_Fee b "+
				  " where  a.is_pay='T' and a.order_sn = b.order_sn and a.order_sn in ( "+
				  "	select order_sn "+
				  "	  from c_prod_order "+
				  "	 where cust_id = ?  "+
				  "	   and user_id in ("+sqlGenerator.in(userIds)+") "+
				  "	   and package_sn is null "+
				  "	union "+
				  "	select pak.order_sn "+
				  "	  from c_prod_order a,c_prod_order pak "+
				  "	 where a.cust_id = ? "+
				  "	   and a.user_id in ("+sqlGenerator.in(userIds)+") "+
				  "	   and a.package_sn=pak.order_sn and pak.done_code>? )";
		return this.createQuery(sql, custId,custId,taskDoneCode).list();
	}
	
}

package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CProdOrderFeeDao extends BaseEntityDao<CProdOrderFee>  {
	
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
     */
	public void clearOutPutInfo(String order_sn,String output_type) throws JDBCException{
		String sql="update c_prod_order_fee set output_type=null ,output_sn=null,output_fee=null"
				+ " where order_sn=? and output_type=? ";
		this.executeUpdate(sql, order_sn,output_type);
	}
	
	public void deleteOrderFeeByOrderSn(String order_sn)throws JDBCException{
		String sql="delete from c_prod_order_fee where order_sn=? ";
		this.executeUpdate(sql, order_sn);
	}

	public void updateFeeType(Integer done_code,String fee_type) throws JDBCException{
		String sql="update c_prod_order_fee set fee_type=? where done_code=? and fee_type=?";
		this.executeUpdate(sql, fee_type,done_code,StatusConstants.UNPAY);
	}
}

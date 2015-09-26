package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrderFeeOut;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CProdOrderFeeOutDao extends  BaseEntityDao<CProdOrderFeeOut> {
	
	public List<CProdOrderFeeOut> queryByDoneCode(Integer doneCode) throws JDBCException{
		String sql="select o.*,f.fee_type,f.order_sn from c_prod_order_fee_out o,c_prod_order_fee f where o.done_code=? and o.order_fee_sn=f.order_fee_sn ";
		return this.createQuery(sql, doneCode).list();
	}
	
	public List<CProdOrderFeeOut> queryByDoneCodeTransFee(Integer doneCode) throws JDBCException{
		String sql="select o.*,f.fee_type,f.order_sn "
				+ "from c_prod_order_fee_out o,c_prod_order_fee f"
				+ " where o.done_code=? and o.order_fee_sn=f.order_fee_sn and o.output_type=? ";
		return this.createQuery(sql, doneCode,SystemConstants.ORDER_FEE_TYPE_TRANSFEE).list();
	}

	public List<CProdOrderFeeOut> queryByDoneCodeAndSn(Integer doneCode,String outputSn)throws JDBCException{
		String sql="select o.*,f.fee_type,f.order_sn from c_prod_order_fee_out o,c_prod_order_fee f where  o.order_fee_sn=f.order_fee_sn and o.done_code=? and o.output_sn=? ";
		return this.createQuery(sql, doneCode,outputSn).list();
	}
	
}

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

	public void updateFeeType(Integer done_code,String fee_type) throws JDBCException{
		String sql="update c_prod_order_fee set fee_type=? where done_code=? and fee_type=?";
		this.executeUpdate(sql, fee_type,done_code,StatusConstants.UNPAY);
	}
}

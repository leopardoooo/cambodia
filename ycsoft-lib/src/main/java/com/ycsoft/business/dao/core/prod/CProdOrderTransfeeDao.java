package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrderTransfee;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CProdOrderTransfeeDao extends BaseEntityDao<CProdOrderTransfee> {
	
	/**
	 * 删除转移支付异动
	 * @param doneCode
	 * @param cust_id
	 * @throws JDBCException 
	 */
	public void deleteTransfeeChange(Integer doneCode,String cust_id) throws JDBCException{
		String sql="delete c_prod_order_transfee where done_code=? and cust_id=? ";
		this.executeUpdate(sql, doneCode,cust_id);
	}

}

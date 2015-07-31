package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
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

}

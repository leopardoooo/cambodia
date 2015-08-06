package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderHis;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CProdOrderHisDao extends BaseEntityDao<CProdOrderHis> {
	
	/**
	 * 使用删除doneCode查询被移入历史表的已支付订单记录
	 * @param deleteDoneCode
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdOrder> queryCProdOrderByDelete(Integer deleteDoneCode,String cust_id) throws JDBCException{
		String sql="select * from c_prod_order_his where delete_done_code=? and cust_id=? and is_pay='T' ";
		return this.createQuery(CProdOrder.class, sql, deleteDoneCode,cust_id).list();
	}

}
